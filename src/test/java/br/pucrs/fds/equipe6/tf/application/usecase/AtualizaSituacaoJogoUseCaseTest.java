package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Situacao;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AtualizaSituacaoJogoUseCaseTest {

    // repo falso
    static class JogoRepositorioFalso implements IJogoRepository {
        private final List<Jogo> jogos = new ArrayList<>();

        void adicionar(Jogo j) { jogos.add(j); }

        @Override public Jogo save(Jogo jogo) { return jogo; }
        @Override public List<Jogo> saveAll(List<Jogo> lista) { return lista; }
        @Override public Jogo findById(Integer id) {
            return jogos.stream().filter(j -> j.getCod() == id).findFirst().orElse(null);
        }
        @Override public List<Jogo> findAll() { return jogos; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int cod) { return false; }
    }

    private JogoRepositorioFalso jogoRepository;
    private AtualizaSituacaoJogoUseCase useCase;

    @BeforeEach
    void setUp() {
        jogoRepository = new JogoRepositorioFalso();
        useCase = new AtualizaSituacaoJogoUseCase(jogoRepository);
    }

    @Test
    void deveRetornarVazioQuandoJogoNaoExiste() {
        Optional<Jogo> resultado = useCase.executar(1, "bloqueado");

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveRetornarVazioQuandoStatusInvalido() {
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Optional<Jogo> resultado = useCase.executar(1, "status-invalido");

        assertThat(resultado).isEmpty();
    }

    @Test
    void deveAtualizarSituacaoEMarcarComoManual() {
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Optional<Jogo> resultado = useCase.executar(1, "bloqueado");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getSituacao()).isEqualTo(Situacao.BLOQUEADO);
        assertThat(resultado.get().isSituacaoManual()).isTrue();
    }
}
