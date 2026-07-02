package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ContratoTest {

    private Date dataDe(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes, dia, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    void deveCriarComConstrutorPadrao() {
        Contrato c = new Contrato();
        assertThat(c.getId()).isZero();
        assertThat(c.isCancelado()).isFalse();
    }

    @Test
    void deveCriarComConstrutorSemFormaPagamento() {
        Cliente cliente = new Cliente(1L, "Lorenzo", "111", "l@t.com", null, "s");
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 1.0, null, null);
        Date data = dataDe(2026, Calendar.JANUARY, 1);

        Contrato c = new Contrato(1, data, 30, cliente, jogo);

        assertThat(c.getId()).isEqualTo(1);
        assertThat(c.getData()).isEqualTo(data);
        assertThat(c.getPeriodo()).isEqualTo(30);
        assertThat(c.getCliente()).isEqualTo(cliente);
        assertThat(c.getJogo()).isEqualTo(jogo);
        assertThat(c.getUsos()).isEmpty();
        assertThat(c.isCancelado()).isFalse();
    }

    @Test
    void deveCriarComConstrutorComFormaPagamento() {
        Cliente cliente = new Cliente(1L, "Lorenzo", "111", "l@t.com", null, "s");
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 1.0, null, null);
        FormaPagamento fp = new PIX(1, 10, "chave");
        Date data = dataDe(2026, Calendar.JANUARY, 1);

        Contrato c = new Contrato(1, data, 30, cliente, jogo, fp);

        assertThat(c.getFormaPagamento()).isEqualTo(fp);
        assertThat(c.getUsos()).isEmpty();
        assertThat(c.isCancelado()).isFalse();
    }

    @Test
    void deveAlterarValoresComSetters() {
        Contrato c = new Contrato();
        Date data = dataDe(2026, Calendar.FEBRUARY, 1);
        FormaPagamento fp = new PIX(2, 5, "chave2");

        c.setId(9);
        c.setData(data);
        c.setPeriodo(15);
        c.setFormaPagamento(fp);
        c.setCancelado(true);

        assertThat(c.getId()).isEqualTo(9);
        assertThat(c.getData()).isEqualTo(data);
        assertThat(c.getPeriodo()).isEqualTo(15);
        assertThat(c.getFormaPagamento()).isEqualTo(fp);
        assertThat(c.isCancelado()).isTrue();
    }

    @Test
    void deveAdicionarUsoEVincularContrato() {
        Contrato c = new Contrato();
        Uso uso = new Uso(1, dataDe(2026, Calendar.JANUARY, 1), dataDe(2026, Calendar.JANUARY, 1), 10, 12);

        c.addUso(uso);

        assertThat(c.getUsos()).containsExactly(uso);
        assertThat(uso.getContrato()).isEqualTo(c);
    }

    @Test
    void deveCalcularDataFimSomandoPeriodoADataInicial() {
        Date data = dataDe(2026, Calendar.JANUARY, 1);
        Contrato c = new Contrato();
        c.setData(data);
        c.setPeriodo(10);

        Calendar esperado = Calendar.getInstance();
        esperado.setTime(data);
        esperado.add(Calendar.DAY_OF_MONTH, 10);

        assertThat(c.getDataFim()).isEqualTo(esperado.getTime());
    }

    @Test
    void deveCancelarContratoLogicamente() {
        Contrato c = new Contrato();
        assertThat(c.isCancelado()).isFalse();

        c.cancelar();

        assertThat(c.isCancelado()).isTrue();
    }
}
