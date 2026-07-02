package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JogoTest {

    private int anoAtual() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    private Date menosAnos(int anos) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -anos);
        return cal.getTime();
    }

    private Date menosAnosEDias(int anos, int diasExtra) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -anos);
        cal.add(Calendar.DAY_OF_MONTH, -diasExtra);
        return cal.getTime();
    }

    private Contrato contratoComDataFim(Date dataFim, boolean cancelado) {
        Contrato c = new Contrato();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataFim);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        c.setData(cal.getTime());
        c.setPeriodo(1);
        c.setCancelado(cancelado);
        return c;
    }

    private Uso usoComDataFim(int numero, Date dataFim) {
        Uso u = new Uso();
        u.setNumero(numero);
        u.setDataInicio(dataFim);
        u.setDataFim(dataFim);
        u.setHorarioInicio(0);
        u.setHorarioFim(1);
        return u;
    }

    @Test
    void deveCriarComConstrutorPadrao() {
        Jogo j = new Jogo();
        assertThat(j.getCod()).isZero();
        assertThat(j.getSituacao()).isNull();
        assertThat(j.isSituacaoManual()).isFalse();
    }

    @Test
    void deveCriarComConstrutorComArgumentosESituacaoDisponivel() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);

        Jogo j = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);

        assertThat(j.getCod()).isEqualTo(1);
        assertThat(j.getNome()).isEqualTo("Jogo A");
        assertThat(j.getAno()).isEqualTo(2024);
        assertThat(j.getValorMinuto()).isEqualTo(0.5);
        assertThat(j.getCategoria()).isEqualTo(categoria);
        assertThat(j.getMoeda()).isEqualTo(moeda);
        assertThat(j.getSituacao()).isEqualTo(Situacao.DISPONIVEL);
    }

    @Test
    void deveAlterarValoresComSetters() {
        Jogo j = new Jogo();
        Categoria categoria = new Categoria(2, "RPG", 15.0);
        Moeda moeda = new Moeda(2, "Dólar", "$", 5.0);

        j.setCod(9);
        j.setNome("Outro");
        j.setAno(2020);
        j.setValorMinuto(1.2);
        j.setCategoria(categoria);
        j.setMoeda(moeda);
        j.setSituacao(Situacao.BLOQUEADO);
        j.setSituacaoManual(true);

        assertThat(j.getCod()).isEqualTo(9);
        assertThat(j.getNome()).isEqualTo("Outro");
        assertThat(j.getAno()).isEqualTo(2020);
        assertThat(j.getValorMinuto()).isEqualTo(1.2);
        assertThat(j.getCategoria()).isEqualTo(categoria);
        assertThat(j.getMoeda()).isEqualTo(moeda);
        assertThat(j.getSituacao()).isEqualTo(Situacao.BLOQUEADO);
        assertThat(j.isSituacaoManual()).isTrue();
    }

    // ---------- estaContratado ----------

    @Test
    void estaContratadoDeveSerFalsoQuandoListaVazia() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        assertThat(j.estaContratado(Collections.emptyList())).isFalse();
    }

    @Test
    void estaContratadoDeveSerVerdadeiroComContratoAtivoNaoCancelado() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato ativo = contratoComDataFim(menosAnosEDias(0, -10), false); // dataFim daqui a 10 dias

        assertThat(j.estaContratado(List.of(ativo))).isTrue();
    }

    @Test
    void estaContratadoDeveSerFalsoComContratoCancelado() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato cancelado = contratoComDataFim(menosAnosEDias(0, -10), true);

        assertThat(j.estaContratado(List.of(cancelado))).isFalse();
    }

    @Test
    void estaContratadoDeveSerFalsoComContratoExpirado() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato expirado = contratoComDataFim(menosAnosEDias(0, 10), false); // dataFim ha 10 dias

        assertThat(j.estaContratado(List.of(expirado))).isFalse();
    }

    // ---------- estaObsoleto ----------

    @Test
    void estaObsoletoDeveSerFalsoQuandoSemContratosECadastroRecente() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        assertThat(j.estaObsoleto(Collections.emptyList())).isFalse();
    }

    @Test
    void estaObsoletoDeveSerVerdadeiroQuandoSemContratosECadastradoHaMaisDe3Anos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual() - 5, 0.5, null, null);
        assertThat(j.estaObsoleto(Collections.emptyList())).isTrue();
    }

    @Test
    void estaObsoletoDeveSerFalsoQuandoContratosSemUsos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual() - 5, 0.5, null, null);
        Contrato c = contratoComDataFim(menosAnosEDias(0, 10), false);

        assertThat(j.estaObsoleto(List.of(c))).isFalse();
    }

    @Test
    void estaObsoletoDeveSerVerdadeiroQuandoUltimoUsoHaMaisDe2Anos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato c = contratoComDataFim(menosAnosEDias(0, 10), false);
        c.addUso(usoComDataFim(1, menosAnosEDias(3, 0)));

        assertThat(j.estaObsoleto(List.of(c))).isTrue();
    }

    @Test
    void estaObsoletoDeveSerFalsoQuandoUltimoUsoRecente() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato c = contratoComDataFim(menosAnosEDias(0, 10), false);
        c.addUso(usoComDataFim(1, new Date()));

        assertThat(j.estaObsoleto(List.of(c))).isFalse();
    }

    // ---------- estaRemovido ----------

    @Test
    void estaRemovidoDeveSerFalsoQuandoNaoObsoleto() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        assertThat(j.estaRemovido(Collections.emptyList())).isFalse();
    }

    @Test
    void estaRemovidoDeveSerVerdadeiroQuandoSemContratosECadastroMuitoAntigo() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual() - 6, 0.5, null, null);
        assertThat(j.estaRemovido(Collections.emptyList())).isTrue();
    }

    @Test
    void estaRemovidoDeveSerFalsoQuandoSemContratosObsoletoMasNaoAtinge4Anos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual() - 3, 0.5, null, null);
        assertThat(j.estaObsoleto(Collections.emptyList())).isTrue();
        assertThat(j.estaRemovido(Collections.emptyList())).isFalse();
    }

    @Test
    void estaRemovidoDeveSerVerdadeiroQuandoUltimoUsoHaMaisDe3Anos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato c = contratoComDataFim(menosAnosEDias(0, 10), false);
        c.addUso(usoComDataFim(1, menosAnosEDias(4, 0)));

        assertThat(j.estaObsoleto(List.of(c))).isTrue();
        assertThat(j.estaRemovido(List.of(c))).isTrue();
    }

    @Test
    void estaRemovidoDeveSerFalsoQuandoUltimoUsoObsoletoMasNaoAtinge3Anos() {
        Jogo j = new Jogo(1, "Jogo A", anoAtual(), 0.5, null, null);
        Contrato c = contratoComDataFim(menosAnosEDias(0, 10), false);
        // pouco mais de 2 anos: obsoleto = true, removido = false
        c.addUso(usoComDataFim(1, menosAnosEDias(2, 30)));

        assertThat(j.estaObsoleto(List.of(c))).isTrue();
        assertThat(j.estaRemovido(List.of(c))).isFalse();
    }
}
