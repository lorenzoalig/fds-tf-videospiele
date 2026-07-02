package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class JogosTest {

    private Jogos jogos;

    @BeforeEach
    void setUp() {
        jogos = new Jogos();
    }

    private int anoAtual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Test
    void deveIniciarListaVazia() {
        assertThat(jogos.getJogos()).isEmpty();
    }

    @Test
    void deveAdicionarJogo() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogos.addJogo(j);

        assertThat(jogos.getJogos()).containsExactly(j);
    }

    @Test
    void deveBuscarJogoPorCodigoExistente() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogos.addJogo(j);

        assertThat(jogos.buscaJogoCod(1)).isEqualTo(j);
    }

    @Test
    void deveRetornarNullAoBuscarJogoPorCodigoInexistente() {
        assertThat(jogos.buscaJogoCod(99)).isNull();
    }

    @Test
    void naoDeveAlterarSituacaoQuandoManual() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        j.setSituacao(Situacao.BLOQUEADO);
        j.setSituacaoManual(true);
        jogos.addJogo(j);

        Contratos contratos = new Contratos();
        jogos.atualizarSituacaoJogos(contratos);

        assertThat(j.getSituacao()).isEqualTo(Situacao.BLOQUEADO);
    }

    @Test
    void deveMarcarDisponivelQuandoSemContratos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogos.addJogo(j);

        Contratos contratos = new Contratos();
        jogos.atualizarSituacaoJogos(contratos);

        assertThat(j.getSituacao()).isEqualTo(Situacao.DISPONIVEL);
    }

    @Test
    void deveMarcarContratadoQuandoHaContratoAtivo() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        jogos.addJogo(j);

        Cliente cliente = new Cliente(1L, "Lorenzo", "111", "l@t.com", null, "s");
        Contrato c = new Contrato(1, new Date(), 10, cliente, j);

        Contratos contratos = new Contratos();
        contratos.addContrato(c);

        jogos.atualizarSituacaoJogos(contratos);

        assertThat(j.getSituacao()).isEqualTo(Situacao.CONTRATADO);
    }

    @Test
    void deveMarcarObsoletoQuandoCadastradoHaMaisDe3AnosSemContrato() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual() - 3, 0.5, null, null);
        jogos.addJogo(j);

        Contratos contratos = new Contratos();
        jogos.atualizarSituacaoJogos(contratos);

        assertThat(j.getSituacao()).isEqualTo(Situacao.OBSOLETO);
    }

    @Test
    void deveMarcarRemovidoQuandoCadastradoHaMaisDe4AnosSemContrato() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual() - 5, 0.5, null, null);
        jogos.addJogo(j);

        Contratos contratos = new Contratos();
        jogos.atualizarSituacaoJogos(contratos);

        assertThat(j.getSituacao()).isEqualTo(Situacao.REMOVIDO);
    }

    @Test
    void deveFiltrarJogosPorSituacao() {
        Jogo disponivel = new Jogo(1, "A", anoAtual(), 0.5, null, null);
        Jogo bloqueado = new Jogo(2, "B", anoAtual(), 0.5, null, null);
        bloqueado.setSituacao(Situacao.BLOQUEADO);

        jogos.addJogo(disponivel);
        jogos.addJogo(bloqueado);

        assertThat(jogos.consultaJogos("disponivel")).containsExactly(disponivel);
        assertThat(jogos.consultaJogos("bloqueado")).containsExactly(bloqueado);
    }

    @Test
    void deveRetornarListaVaziaParaSituacaoDesconhecida() {
        Jogo disponivel = new Jogo(1, "A", anoAtual(), 0.5, null, null);
        jogos.addJogo(disponivel);

        assertThat(jogos.consultaJogos("inexistente")).isEmpty();
    }
}
