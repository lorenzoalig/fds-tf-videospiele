package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Categoria;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Moeda;
import br.pucrs.fds.equipe6.tf.domain.repository.ICategoriaRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IMoedaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

class UploadJogosUseCaseTest {

    // repo falso
    static class JogoRepositorioFalso implements IJogoRepository {
        private final List<Jogo> jogos = new ArrayList<>();

        @Override public Jogo save(Jogo jogo) { jogos.add(jogo); return jogo; }
        @Override public List<Jogo> saveAll(List<Jogo> lista) { return lista; }
        @Override public Jogo findById(Integer id) { return null; }
        @Override public List<Jogo> findAll() { return jogos; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int cod) {
            return jogos.stream().anyMatch(j -> j.getCod() == cod);
        }
    }

    static class CategoriaRepositorioFalso implements ICategoriaRepository {
        private final List<Categoria> categorias = new ArrayList<>();

        void adicionar(Categoria c) { categorias.add(c); }

        @Override public Categoria save(Categoria categoria) { categorias.add(categoria); return categoria; }
        @Override public Categoria findById(Integer id) { return null; }
        @Override public List<Categoria> findAll() { return categorias; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int num) { return false; }
    }

    static class MoedaRepositorioFalso implements IMoedaRepository {
        private final List<Moeda> moedas = new ArrayList<>();

        void adicionar(Moeda m) { moedas.add(m); }

        @Override public Moeda save(Moeda moeda) { moedas.add(moeda); return moeda; }
        @Override public Moeda findById(Integer id) {
            return moedas.stream().filter(m -> m.getCod() == id).findFirst().orElse(null);
        }
        @Override public List<Moeda> findAll() { return moedas; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int cod) { return false; }
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

    private JogoRepositorioFalso jogoRepository;
    private CategoriaRepositorioFalso categoriaRepository;
    private MoedaRepositorioFalso moedaRepository;
    private UploadJogosUseCase useCase;

    @BeforeEach
    void setUp() {
        jogoRepository = new JogoRepositorioFalso();
        categoriaRepository = new CategoriaRepositorioFalso();
        moedaRepository = new MoedaRepositorioFalso();
        useCase = new UploadJogosUseCase(jogoRepository, categoriaRepository, moedaRepository);
    }

    private MultipartFile arquivo(String csv) {
        return new MockMultipartFile("file", "jogos.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void deveCadastrarJogoQuandoTudoValido() {
        String csv = "cod;nome;ano;valorMinuto;categoria;moeda\n1;Jogo A;2024;0.5;Ação;10\n";
        categoriaRepository.adicionar(new Categoria(1, "AÇÃO", 10.0));
        moedaRepository.adicionar(new Moeda(10, "Real", "R$", 1.0));

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(jogoRepository.findAll()).hasSize(1);
        assertThat(jogoRepository.findAll().get(0).getNome()).isEqualTo("Jogo A");
        assertThat(jogoRepository.findAll().get(0).getCategoria().getNum()).isEqualTo(1);
        assertThat(jogoRepository.findAll().get(0).getMoeda().getCod()).isEqualTo(10);
    }

    @Test
    void deveIgnorarQuandoJogoJaExiste() {
        jogoRepository.save(new Jogo(1, "Existente", 2020, 0.1, null, null));

        String csv = "cod;nome;ano;valorMinuto;categoria;moeda\n1;Jogo A;2024;0.5;Ação;10\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(jogoRepository.findAll()).hasSize(1);
        assertThat(jogoRepository.findAll().get(0).getNome()).isEqualTo("Existente");
    }

    @Test
    void deveIgnorarQuandoCategoriaNaoEncontrada() {
        String csv = "cod;nome;ano;valorMinuto;categoria;moeda\n1;Jogo A;2024;0.5;Inexistente;10\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(jogoRepository.findAll()).isEmpty();
    }

    @Test
    void deveIgnorarQuandoMoedaNaoEncontrada() {
        String csv = "cod;nome;ano;valorMinuto;categoria;moeda\n1;Jogo A;2024;0.5;Ação;10\n";
        categoriaRepository.adicionar(new Categoria(1, "Ação", 10.0));

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(jogoRepository.findAll()).isEmpty();
    }

    @Test
    void deveRetornarFalseQuandoOcorreIOException() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
