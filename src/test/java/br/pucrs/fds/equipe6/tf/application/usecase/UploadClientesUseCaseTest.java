package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Cliente;
import br.pucrs.fds.equipe6.tf.domain.repository.IClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UploadClientesUseCaseTest {

    // repo falso
    static class ClienteRepositorioFalso implements IClienteRepository {
        private final List<Cliente> clientes = new ArrayList<>();

        @Override public Cliente save(Cliente cliente) { clientes.add(cliente); return cliente; }
        @Override public Cliente findById(Long id) { return null; }
        @Override public Optional<Cliente> findByCPF(String cpf) { return Optional.empty(); }
        @Override public List<Cliente> findAll() { return clientes; }
        @Override public void deleteById(Long id) { }
    }

    // arquivo que dá erro na hora de ler (simula IOException)
    static class ArquivoComErro implements MultipartFile {
        @Override public String getName() { return "file"; }
        @Override public String getOriginalFilename() { return "erro.csv"; }
        @Override public String getContentType() { return "text/csv"; }
        @Override public boolean isEmpty() { return false; }
        @Override public long getSize() { return 0; }
        @Override public byte[] getBytes() throws IOException { throw new IOException("erro de leitura"); }
        @Override public InputStream getInputStream() throws IOException { throw new IOException("erro de leitura"); }
        @Override public void transferTo(File dest) throws IOException { throw new IOException("erro de leitura"); }
    }

    private ClienteRepositorioFalso clienteRepository;
    private UploadClientesUseCase useCase;

    @BeforeEach
    void setUp() {
        clienteRepository = new ClienteRepositorioFalso();
        useCase = new UploadClientesUseCase(clienteRepository);
    }

    @Test
    void deveCadastrarClientesIgnorandoHeader() {
        String csv = "cod;nome;cpf\n1;Lorenzo;111.111.111-11\n2;Ana;222.222.222-22\n";
        MultipartFile file = new MockMultipartFile("file", "clientes.csv", "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));

        boolean resultado = useCase.executar(file);

        assertThat(resultado).isTrue();
        assertThat(clienteRepository.findAll()).hasSize(2);
        assertThat(clienteRepository.findAll().get(0).getCod()).isEqualTo(1L);
        assertThat(clienteRepository.findAll().get(0).getNome()).isEqualTo("Lorenzo");
        assertThat(clienteRepository.findAll().get(0).getCPF()).isEqualTo("111.111.111-11");
        assertThat(clienteRepository.findAll().get(1).getNome()).isEqualTo("Ana");
    }

    @Test
    void deveRetornarFalseQuandoOcorreIOException() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
