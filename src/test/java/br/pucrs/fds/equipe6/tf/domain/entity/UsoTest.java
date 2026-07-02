package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class UsoTest {

    private Date dataDe(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes, dia, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    void deveCriarComConstrutorPadrao() {
        Uso uso = new Uso();
        assertThat(uso.getNumero()).isZero();
        assertThat(uso.getDataInicio()).isNull();
        assertThat(uso.getDataFim()).isNull();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        Date inicio = dataDe(2026, Calendar.JANUARY, 1);
        Date fim = dataDe(2026, Calendar.JANUARY, 1);

        Uso uso = new Uso(1, inicio, fim, 10, 12);

        assertThat(uso.getNumero()).isEqualTo(1);
        assertThat(uso.getDataInicio()).isEqualTo(inicio);
        assertThat(uso.getDataFim()).isEqualTo(fim);
        assertThat(uso.getHorarioInicio()).isEqualTo(10);
        assertThat(uso.getHorarioFim()).isEqualTo(12);
    }

    @Test
    void deveAlterarValoresComSetters() {
        Uso uso = new Uso();
        Date inicio = dataDe(2026, Calendar.MARCH, 5);
        Date fim = dataDe(2026, Calendar.MARCH, 5);
        Contrato contrato = new Contrato();

        uso.setNumero(2);
        uso.setDataInicio(inicio);
        uso.setDataFim(fim);
        uso.setHorarioInicio(9);
        uso.setHorarioFim(11);
        uso.setContrato(contrato);

        assertThat(uso.getNumero()).isEqualTo(2);
        assertThat(uso.getDataInicio()).isEqualTo(inicio);
        assertThat(uso.getDataFim()).isEqualTo(fim);
        assertThat(uso.getHorarioInicio()).isEqualTo(9);
        assertThat(uso.getHorarioFim()).isEqualTo(11);
        assertThat(uso.getContrato()).isEqualTo(contrato);
    }

    @Test
    void deveCalcularDuracaoNoMesmoDia() {
        Date data = dataDe(2026, Calendar.JANUARY, 1);
        Uso uso = new Uso(1, data, data, 10, 12);

        assertThat(uso.getDuracaoMinutos()).isEqualTo(120);
    }

    @Test
    void deveCalcularDuracaoEntreDiasDiferentes() {
        Date inicio = dataDe(2026, Calendar.JANUARY, 1);
        Date fim = dataDe(2026, Calendar.JANUARY, 2);

        Uso uso = new Uso(1, inicio, fim, 8, 10);

        // 1 dia inteiro (1440 min) + 2 horas (120 min)
        assertThat(uso.getDuracaoMinutos()).isEqualTo(1560);
    }
}
