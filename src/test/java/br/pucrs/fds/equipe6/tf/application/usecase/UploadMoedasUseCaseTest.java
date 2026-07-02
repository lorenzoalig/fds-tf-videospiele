package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Moeda;
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

class UploadMoedasUseCaseTest {

    // repositório falso
    static class MoedaRepositorioFalso implements IMoedaRepository {
        private final List<Moeda> moedas = new ArrayList<>();

        @Override public Moeda save(Moeda moeda) { moedas.add(moeda); return moeda; }
        @Override public Moeda findById(Integer id) { return null; }
        @Override public List<Moeda> findAll() { return moedas; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int cod) {
            return moedas.stream().anyMatch(m -> m.getCod() == cod);
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

    private MoedaRepositorioFalso moedaRepository;
    private UploadMoedasUseCase useCase;

    @BeforeEach
    void setUp() {
        moedaRepository = new MoedaRepositorioFalso();
        useCase = new UploadMoedasUseCase(moedaRepository);
    }

    private MultipartFile arquivo(String csv) {
        return new MockMultipartFile("file", "moedas.csv", "text/csv", csv.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void deveCadastrarMoedaComTaxaInformada() {
        String csv = "cod;nome;simbolo;taxa\n1;Dólar;$;5.2\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(moedaRepository.findAll()).hasSize(1);
        assertThat(moedaRepository.findAll().get(0).getTaxaParaReal()).isEqualTo(5.2);
    }

    @Test
    void deveUsarTaxaPadraoQuandoNaoInformada() {
        String csv = "cod;nome;simbolo\n1;Real;R$\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(moedaRepository.findAll()).hasSize(1);
        assertThat(moedaRepository.findAll().get(0).getTaxaParaReal()).isEqualTo(1.0);
    }

    @Test
    void deveIgnorarQuandoJaExiste() {
        moedaRepository.save(new Moeda(1, "Antiga", "R$", 2.0));

        String csv = "cod;nome;simbolo;taxa\n1;Dólar;$;5.2\n";

        boolean resultado = useCase.executar(arquivo(csv));

        assertThat(resultado).isTrue();
        assertThat(moedaRepository.findAll()).hasSize(1);
        assertThat(moedaRepository.findAll().get(0).getNome()).isEqualTo("Antiga");
    }

    @Test
    void deveRetornarFalseQuandoOcorreExcecao() {
        boolean resultado = useCase.executar(new ArquivoComErro());

        assertThat(resultado).isFalse();
    }
}
