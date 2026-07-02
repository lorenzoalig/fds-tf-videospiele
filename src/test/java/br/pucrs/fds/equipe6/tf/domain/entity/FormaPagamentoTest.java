package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FormaPagamentoTest {

    @Test
    void deveCriarComConstrutorPadrao() {
        FormaPagamento fp = new FormaPagamento();
        assertThat(fp.getNum()).isZero();
        assertThat(fp.getDiaVencimento()).isZero();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        FormaPagamento fp = new FormaPagamento(1, 10);
        assertThat(fp.getNum()).isEqualTo(1);
        assertThat(fp.getDiaVencimento()).isEqualTo(10);
    }

    @Test
    void deveAlterarValoresComSetters() {
        FormaPagamento fp = new FormaPagamento();
        fp.setNum(5);
        fp.setDiaVencimento(15);
        assertThat(fp.getNum()).isEqualTo(5);
        assertThat(fp.getDiaVencimento()).isEqualTo(15);
    }
}
