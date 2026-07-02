package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.FormaPagamento;
import br.pucrs.fds.equipe6.tf.domain.entity.PIX;
import br.pucrs.fds.equipe6.tf.domain.repository.IFormaPagamentoRepository;
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

class UploadFormasPagamentoUseCaseTest {

    // repo falso
    static class FormaPagamentoRepositorioFalso implements IFormaPagamentoRepository {
        private final List<FormaPagamento> formas = new ArrayList<>();

        @Override public FormaPagamento save(FormaPagamento formaPagamento) { formas.add(formaPagamento); return formaPagamento; }
        @Override public FormaPagamento findById(Integer id) { return null; }
        @Override public List<FormaPagamento> findAll() { return formas; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int num) {
            return formas.stream().anyMatch(f -> f.getNum() == num);
        }
    }

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

    private FormaPagamentoRepositorioFalso formaPagamentoRepository;
    private UploadFormasPagamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        formaPagamentoRepository = new FormaPagamentoRepositorioFalso();
        useCase = new UploadFormasPagamentoUseCase(formaPagamentoRepository);
    }

    private MultipartFile arquivo(String csv) {
        return new MockMultipartFile("file", "formas.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void deveCadastrarPixQuandoTipoUm() {
        String csv = "num;dia;tipo;chave\n1;10;1;chave-pix\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(formaPagamentoRepository.findAll()).hasSize(1);
        assertThat(formaPagamentoRepository.findAll().get(0)).isInstanceOf(PIX.class);
        assertThat(((PIX) formaPagamentoRepository.findAll().get(0)).getChave()).isEqualTo("chave-pix");
    }

    @Test
    void deveIgnorarTipoDiferenteDeUm() {
        String csv = "num;dia;tipo;chave\n1;10;2;\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(formaPagamentoRepository.findAll()).isEmpty();
    }

    @Test
    void deveIgnorarQuandoJaExiste() {
        formaPagamentoRepository.save(new PIX(1, 10, "chave-antiga"));

        String csv = "num;dia;tipo;chave\n1;10;1;chave-pix\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(formaPagamentoRepository.findAll()).hasSize(1);
        assertThat(((PIX) formaPagamentoRepository.findAll().get(0)).getChave()).isEqualTo("chave-antiga");
    }

    @Test
    void deveRetornarFalseQuandoOcorreExcecao() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
