package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Categoria;
import br.pucrs.fds.equipe6.tf.domain.repository.ICategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UploadCategoriasUseCaseTest {

    // repo falso
    static class CategoriaRepositorioFalso implements ICategoriaRepository {
        private final List<Categoria> categorias = new ArrayList<>();

        @Override public Categoria save(Categoria categoria) { categorias.add(categoria); return categoria; }
        @Override public Categoria findById(Integer id) { return null; }
        @Override public List<Categoria> findAll() { return categorias; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int num) {
            return categorias.stream().anyMatch(c -> c.getNum() == num);
        }
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

    private CategoriaRepositorioFalso categoriaRepository;
    private UploadCategoriasUseCase useCase;

    @BeforeEach
    void setUp() {
        categoriaRepository = new CategoriaRepositorioFalso();
        useCase = new UploadCategoriasUseCase(categoriaRepository);
    }

    @Test
    void deveCadastrarCategoriasNovasEIgnorarHeader() {
        String csv = "num;nome;valorMinimo\n1;Ação;10.0\n2;RPG;15.0\n";
        MultipartFile file = new MockMultipartFile("file", "categorias.csv", "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));

        boolean resultado = useCase.executar(file);

        assertThat(resultado).isTrue();
        assertThat(categoriaRepository.findAll()).hasSize(2);
        assertThat(categoriaRepository.findAll().get(0).getNome()).isEqualTo("Ação");
        assertThat(categoriaRepository.findAll().get(1).getNum()).isEqualTo(2);
    }

    @Test
    void deveIgnorarCategoriaJaExistente() {
        categoriaRepository.save(new Categoria(1, "Já cadastrada", 5.0));

        String csv = "num;nome;valorMinimo\n1;Ação;10.0\n";
        MultipartFile file = new MockMultipartFile("file", "categorias.csv", "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));

        boolean resultado = useCase.executar(file);

        assertThat(resultado).isTrue();
        assertThat(categoriaRepository.findAll()).hasSize(1);
        assertThat(categoriaRepository.findAll().get(0).getNome()).isEqualTo("Já cadastrada");
    }

    @Test
    void deveRetornarFalseQuandoOcorreIOException() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
