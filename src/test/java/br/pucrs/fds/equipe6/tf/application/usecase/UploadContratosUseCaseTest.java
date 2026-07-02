package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.*;
import br.pucrs.fds.equipe6.tf.domain.repository.IClienteRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IFormaPagamentoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
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

class UploadContratosUseCaseTest {

    // repositórios de mentirinha (guardam tudo em memória) pra não precisar de banco
    static class ContratoRepositorioFalso implements IContratoRepository {
        private final List<Contrato> contratos = new ArrayList<>();

        @Override public Contrato save(Contrato contrato) { contratos.add(contrato); return contrato; }
        @Override public Contrato findById(Integer id) { return null; }
        @Override public List<Contrato> findAll() { return contratos; }
        @Override public List<Contrato> findByJogo(Jogo jogo) { return new ArrayList<>(); }
        @Override public List<Contrato> findByClienteCPF(String cpf) { return new ArrayList<>(); }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int id) {
            return contratos.stream().anyMatch(c -> c.getId() == id);
        }
    }

    static class ClienteRepositorioFalso implements IClienteRepository {
        private final List<Cliente> clientes = new ArrayList<>();

        void adicionar(Cliente c) { clientes.add(c); }

        @Override public Cliente save(Cliente cliente) { clientes.add(cliente); return cliente; }
        @Override public Cliente findById(Long id) {
            return clientes.stream().filter(c -> c.getCod() == id).findFirst().orElse(null);
        }
        @Override public Optional<Cliente> findByCPF(String cpf) { return Optional.empty(); }
        @Override public List<Cliente> findAll() { return clientes; }
        @Override public void deleteById(Long id) { }
    }

    static class JogoRepositorioFalso implements IJogoRepository {
        private final List<Jogo> jogos = new ArrayList<>();

        void adicionar(Jogo j) { jogos.add(j); }

        @Override public Jogo save(Jogo jogo) { jogos.add(jogo); return jogo; }
        @Override public List<Jogo> saveAll(List<Jogo> lista) { return lista; }
        @Override public Jogo findById(Integer id) {
            return jogos.stream().filter(j -> j.getCod() == id).findFirst().orElse(null);
        }
        @Override public List<Jogo> findAll() { return jogos; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int cod) { return false; }
    }

    static class FormaPagamentoRepositorioFalso implements IFormaPagamentoRepository {
        private final List<FormaPagamento> formas = new ArrayList<>();

        void adicionar(FormaPagamento f) { formas.add(f); }

        @Override public FormaPagamento save(FormaPagamento formaPagamento) { formas.add(formaPagamento); return formaPagamento; }
        @Override public FormaPagamento findById(Integer id) {
            return formas.stream().filter(f -> f.getNum() == id).findFirst().orElse(null);
        }
        @Override public List<FormaPagamento> findAll() { return formas; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int num) { return false; }
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

    private ContratoRepositorioFalso contratoRepository;
    private ClienteRepositorioFalso clienteRepository;
    private JogoRepositorioFalso jogoRepository;
    private FormaPagamentoRepositorioFalso formaPagamentoRepository;
    private UploadContratosUseCase useCase;

    @BeforeEach
    void setUp() {
        contratoRepository = new ContratoRepositorioFalso();
        clienteRepository = new ClienteRepositorioFalso();
        jogoRepository = new JogoRepositorioFalso();
        formaPagamentoRepository = new FormaPagamentoRepositorioFalso();
        useCase = new UploadContratosUseCase(contratoRepository, clienteRepository, jogoRepository, formaPagamentoRepository);
    }

    private MultipartFile arquivo(String csv) {
        return new MockMultipartFile("file", "contratos.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void deveCadastrarContratoQuandoTudoValido() {
        String csv = "id;data;periodo;cliente;jogo;forma\n1;01/01/2026;30;10;20;30\n";

        clienteRepository.adicionar(new Cliente(10L, "L", "111", "l@t.com", null, "s"));
        jogoRepository.adicionar(new Jogo(20, "Jogo A", 2024, 0.5, null, null));
        formaPagamentoRepository.adicionar(new FormaPagamento(30, 5));

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(contratoRepository.findAll()).hasSize(1);
    }

    @Test
    void deveIgnorarQuandoContratoJaExiste() {
        String csv = "id;data;periodo;cliente;jogo;forma\n1;01/01/2026;30;10;20;30\n";

        Cliente cliente = new Cliente(10L, "L", "111", "l@t.com", null, "s");
        clienteRepository.adicionar(cliente);
        Jogo jogo = new Jogo(20, "Jogo A", 2024, 0.5, null, null);
        jogoRepository.adicionar(jogo);
        FormaPagamento forma = new FormaPagamento(30, 5);
        formaPagamentoRepository.adicionar(forma);
        contratoRepository.save(new Contrato(1, new java.util.Date(), 10, cliente, jogo));

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(contratoRepository.findAll()).hasSize(1);
    }

    @Test
    void deveIgnorarQuandoClienteNaoEncontrado() {
        String csv = "id;data;periodo;cliente;jogo;forma\n1;01/01/2026;30;10;20;30\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(contratoRepository.findAll()).isEmpty();
    }

    @Test
    void deveIgnorarQuandoJogoNaoEncontrado() {
        String csv = "id;data;periodo;cliente;jogo;forma\n1;01/01/2026;30;10;20;30\n";
        clienteRepository.adicionar(new Cliente(10L, "L", "111", "l@t.com", null, "s"));

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(contratoRepository.findAll()).isEmpty();
    }

    @Test
    void deveIgnorarQuandoFormaPagamentoNaoEncontrada() {
        String csv = "id;data;periodo;cliente;jogo;forma\n1;01/01/2026;30;10;20;30\n";
        clienteRepository.adicionar(new Cliente(10L, "L", "111", "l@t.com", null, "s"));
        jogoRepository.adicionar(new Jogo(20, "Jogo A", 2024, 0.5, null, null));

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(contratoRepository.findAll()).isEmpty();
    }

    @Test
    void deveRetornarFalseQuandoLinhaEstaMalFormatada() {
        String csv = "id;data;periodo;cliente;jogo;forma\nabc;01/01/2026;30;10;20;30\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isFalse();
    }

    @Test
    void deveRetornarFalseQuandoOcorreIOException() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
