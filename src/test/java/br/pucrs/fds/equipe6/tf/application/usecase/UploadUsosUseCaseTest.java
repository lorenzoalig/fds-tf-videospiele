package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Cliente;
import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Uso;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IUsoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UploadUsosUseCaseTest {

    // repositório falso
    static class UsoRepositorioFalso implements IUsoRepository {
        private final List<Uso> usos = new ArrayList<>();

        @Override public Uso save(Uso uso) { usos.add(uso); return uso; }
        @Override public Uso findById(Integer id) { return null; }
        @Override public List<Uso> findAll() { return usos; }
        @Override public List<Uso> findByContratoId(int contratoId) { return new ArrayList<>(); }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int numero) {
            return usos.stream().anyMatch(u -> u.getNumero() == numero);
        }
    }

    static class ContratoRepositorioFalso implements IContratoRepository {
        private final List<Contrato> contratos = new ArrayList<>();

        void adicionar(Contrato c) { contratos.add(c); }

        @Override public Contrato save(Contrato contrato) { contratos.add(contrato); return contrato; }
        @Override public Contrato findById(Integer id) {
            return contratos.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        }
        @Override public List<Contrato> findAll() { return contratos; }
        @Override public List<Contrato> findByJogo(Jogo jogo) { return new ArrayList<>(); }
        @Override public List<Contrato> findByClienteCPF(String cpf) { return new ArrayList<>(); }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int id) { return false; }
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

    private UsoRepositorioFalso usoRepository;
    private ContratoRepositorioFalso contratoRepository;
    private UploadUsosUseCase useCase;

    @BeforeEach
    void setUp() {
        usoRepository = new UsoRepositorioFalso();
        contratoRepository = new ContratoRepositorioFalso();
        useCase = new UploadUsosUseCase(usoRepository, contratoRepository);
    }

    private MultipartFile arquivo(String csv) {
        return new MockMultipartFile("file", "usos.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void deveCadastrarUsoQuandoContratoExiste() {
        String csv = "numero;dataInicio;dataFim;horaIni;horaFim;contrato\n1;01/01/2026;01/01/2026;10;12;5\n";

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, null, null);
        Contrato contrato = new Contrato(5, new Date(), 30, cliente, jogo);
        contratoRepository.adicionar(contrato);

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(contrato.getUsos()).hasSize(1);
        assertThat(usoRepository.findAll()).hasSize(1);
        assertThat(usoRepository.findAll().get(0).getNumero()).isEqualTo(1);
        assertThat(usoRepository.findAll().get(0).getContrato()).isEqualTo(contrato);
    }

    @Test
    void deveIgnorarQuandoUsoJaExiste() {
        usoRepository.save(new Uso(1, new Date(), new Date(), 1, 2));

        String csv = "numero;dataInicio;dataFim;horaIni;horaFim;contrato\n1;01/01/2026;01/01/2026;10;12;5\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(usoRepository.findAll()).hasSize(1);
    }

    @Test
    void deveIgnorarQuandoContratoNaoEncontrado() {
        String csv = "numero;dataInicio;dataFim;horaIni;horaFim;contrato\n1;01/01/2026;01/01/2026;10;12;5\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(usoRepository.findAll()).isEmpty();
    }

    @Test
    void deveRetornarFalseQuandoLinhaMalFormatada() {
        String csv = "numero;dataInicio;dataFim;horaIni;horaFim;contrato\nabc;01/01/2026;01/01/2026;10;12;5\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isFalse();
    }

    @Test
    void deveRetornarFalseQuandoOcorreIOException() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
