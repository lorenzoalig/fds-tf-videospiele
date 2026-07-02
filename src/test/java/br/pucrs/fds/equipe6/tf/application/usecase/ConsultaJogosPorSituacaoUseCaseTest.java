package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.*;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConsultaJogosPorSituacaoUseCaseTest {

    // repo falso
    static class JogoRepositorioFalso implements IJogoRepository {
        private final List<Jogo> jogos = new ArrayList<>();

        void adicionar(Jogo j) { jogos.add(j); }

        @Override public Jogo save(Jogo jogo) { jogos.add(jogo); return jogo; }
        @Override public List<Jogo> saveAll(List<Jogo> lista) { return lista; }
        @Override public Jogo findById(Integer id) { return null; }
        @Override public List<Jogo> findAll() { return jogos; }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int cod) { return false; }
    }

    static class ContratoRepositorioFalso implements IContratoRepository {
        @Override public Contrato save(Contrato contrato) { return contrato; }
        @Override public Contrato findById(Integer id) { return null; }
        @Override public List<Contrato> findAll() { return new ArrayList<>(); }
        @Override public List<Contrato> findByJogo(Jogo jogo) { return new ArrayList<>(); }
        @Override public List<Contrato> findByClienteCPF(String cpf) { return new ArrayList<>(); }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int id) { return false; }
    }

    private JogoRepositorioFalso jogoRepository;
    private ConsultaJogosPorSituacaoUseCase useCase;

    @BeforeEach
    void setUp() {
        jogoRepository = new JogoRepositorioFalso();
        ContratoRepositorioFalso contratoRepository = new ContratoRepositorioFalso();
        AtualizaSituacaoJogosUseCase atualizaSituacaoJogosUseCase =
                new AtualizaSituacaoJogosUseCase(jogoRepository, contratoRepository);
        useCase = new ConsultaJogosPorSituacaoUseCase(jogoRepository, atualizaSituacaoJogosUseCase);
    }

    private int anoAtual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Test
    void deveRetornarJogosDisponiveisAposAtualizarSituacao() {
        Jogo disponivel = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Jogo bloqueado = new Jogo(2, "Jogo B", anoAtual(), 0.5, null, null);
        bloqueado.setSituacao(Situacao.BLOQUEADO);
        bloqueado.setSituacaoManual(true);

        jogoRepository.adicionar(disponivel);
        jogoRepository.adicionar(bloqueado);

        List<Jogo> resultado = useCase.executar("disponivel");

        assertThat(resultado).containsExactly(disponivel);
    }

    @Test
    void deveRetornarListaVaziaParaSituacaoInexistente() {
        Jogo disponivel = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(disponivel);

        List<Jogo> resultado = useCase.executar("situacao-invalida");

        assertThat(resultado).isEmpty();
    }
}
