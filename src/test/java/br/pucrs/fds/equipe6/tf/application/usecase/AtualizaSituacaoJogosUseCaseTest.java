package br.pucrs.fds.equipe6.tf.application.usecase;

import br.pucrs.fds.equipe6.tf.domain.entity.*;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AtualizaSituacaoJogosUseCaseTest {

    // repo falso
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

    static class ContratoRepositorioFalso implements IContratoRepository {
        private final List<Contrato> contratos = new ArrayList<>();

        void adicionar(Contrato c) { contratos.add(c); }

        @Override public Contrato save(Contrato contrato) { contratos.add(contrato); return contrato; }
        @Override public Contrato findById(Integer id) { return null; }
        @Override public List<Contrato> findAll() { return contratos; }
        @Override public List<Contrato> findByJogo(Jogo jogo) {
            return contratos.stream().filter(c -> c.getJogo() != null && c.getJogo().equals(jogo)).toList();
        }
        @Override public List<Contrato> findByClienteCPF(String cpf) { return new ArrayList<>(); }
        @Override public void deleteById(Integer id) { }
        @Override public boolean existsById(int id) { return false; }
    }

    private JogoRepositorioFalso jogoRepository;
    private ContratoRepositorioFalso contratoRepository;
    private AtualizaSituacaoJogosUseCase useCase;

    @BeforeEach
    void setUp() {
        jogoRepository = new JogoRepositorioFalso();
        contratoRepository = new ContratoRepositorioFalso();
        useCase = new AtualizaSituacaoJogosUseCase(jogoRepository, contratoRepository);
    }

    private int anoAtual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Test
    void naoDeveAlterarSituacaoDeJogoManual() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogo.setSituacao(Situacao.BLOQUEADO);
        jogo.setSituacaoManual(true);
        jogoRepository.adicionar(jogo);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.BLOQUEADO);
    }

    @Test
    void deveMarcarDisponivelQuandoSemContratos() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.DISPONIVEL);
    }

    @Test
    void deveMarcarContratadoQuandoHaContratoAtivo() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Contrato contrato = new Contrato(1, new Date(), 30, cliente, jogo);
        contratoRepository.adicionar(contrato);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.CONTRATADO);
    }

    @Test
    void deveMarcarObsoletoQuandoCadastradoHaMaisDe3AnosSemContrato() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual() - 3, 0.5, null, null);
        jogoRepository.adicionar(jogo);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.OBSOLETO);
    }

    @Test
    void deveMarcarRemovidoQuandoCadastradoHaMaisDe4AnosSemContrato() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual() - 6, 0.5, null, null);
        jogoRepository.adicionar(jogo);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.REMOVIDO);
    }

    @Test
    void deveVoltarParaDisponivelQuandoContratoExpirouNaturalmente() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.add(Calendar.DAY_OF_MONTH, -60);
        Contrato contratoExpirado = new Contrato(1, dataInicio.getTime(), 30, cliente, jogo);
        contratoRepository.adicionar(contratoExpirado);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.DISPONIVEL);
    }

    @Test
    void deveVoltarParaDisponivelQuandoContratoAtivoFoiCancelado() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Contrato contratoCancelado = new Contrato(1, new Date(), 30, cliente, jogo);
        contratoCancelado.cancelar();
        contratoRepository.adicionar(contratoCancelado);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.DISPONIVEL);
    }

    @Test
    void deveMarcarObsoletoQuandoUltimoUsoHaMaisDe2Anos() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.add(Calendar.YEAR, -2);
        dataInicio.add(Calendar.DAY_OF_MONTH, -40);
        Contrato contrato = new Contrato(1, dataInicio.getTime(), 10, cliente, jogo);

        Calendar dataUltimoUso = Calendar.getInstance();
        dataUltimoUso.add(Calendar.YEAR, -2);
        dataUltimoUso.add(Calendar.DAY_OF_MONTH, -30);
        Uso uso = new Uso(1, dataUltimoUso.getTime(), dataUltimoUso.getTime(), 0, 1);
        contrato.addUso(uso);
        contratoRepository.adicionar(contrato);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.OBSOLETO);
    }

    @Test
    void deveMarcarRemovidoQuandoUltimoUsoHaMaisDe3Anos() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.add(Calendar.YEAR, -4);
        Contrato contrato = new Contrato(1, dataInicio.getTime(), 30, cliente, jogo);

        Calendar dataUltimoUso = Calendar.getInstance();
        dataUltimoUso.add(Calendar.YEAR, -4);
        Uso uso = new Uso(1, dataUltimoUso.getTime(), dataUltimoUso.getTime(), 0, 1);
        contrato.addUso(uso);
        contratoRepository.adicionar(contrato);

        useCase.executar();

        assertThat(jogo.getSituacao()).isEqualTo(Situacao.REMOVIDO);
    }

    @Test
    void jogoDesbloqueadoDeveVoltarASerRecalculadoAutomaticamente() {
        Jogo jogo = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogoRepository.adicionar(jogo);

        Cliente cliente = new Cliente(1L, "L", "111", "l@t.com", null, "s");
        Contrato contrato = new Contrato(1, new Date(), 30, cliente, jogo);
        contratoRepository.adicionar(contrato);

        AtualizaSituacaoJogoUseCase manualUseCase = new AtualizaSituacaoJogoUseCase(jogoRepository);
        manualUseCase.executar(1, "bloqueado");
        assertThat(jogo.getSituacao()).isEqualTo(Situacao.BLOQUEADO);
        assertThat(jogo.isSituacaoManual()).isTrue();

        useCase.executar();
        assertThat(jogo.getSituacao()).isEqualTo(Situacao.BLOQUEADO);

        manualUseCase.executar(1, "disponivel");
        assertThat(jogo.isSituacaoManual()).isFalse();

        useCase.executar();
        assertThat(jogo.getSituacao()).isEqualTo(Situacao.CONTRATADO);
    }
}
