package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MoedaTest {

    @Test
    void deveCriarComConstrutorPadrao() {
        Moeda m = new Moeda();
        assertThat(m.getCod()).isZero();
        assertThat(m.getNome()).isNull();
        assertThat(m.getSimbolo()).isNull();
        assertThat(m.getTaxaParaReal()).isZero();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        Moeda m = new Moeda(1, "Dólar", "$", 5.2);

        assertThat(m.getCod()).isEqualTo(1);
        assertThat(m.getNome()).isEqualTo("Dólar");
        assertThat(m.getSimbolo()).isEqualTo("$");
        assertThat(m.getTaxaParaReal()).isEqualTo(5.2);
    }

    @Test
    void deveAlterarValoresComSetters() {
        Moeda m = new Moeda();
        m.setCod(2);
        m.setNome("Euro");
        m.setSimbolo("€");
        m.setTaxaParaReal(6.1);

        assertThat(m.getCod()).isEqualTo(2);
        assertThat(m.getNome()).isEqualTo("Euro");
        assertThat(m.getSimbolo()).isEqualTo("€");
        assertThat(m.getTaxaParaReal()).isEqualTo(6.1);
    }
}
