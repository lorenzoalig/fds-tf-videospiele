package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class CartaoCreditoTest {

    @Test
    void deveCriarComConstrutorPadrao() {
        CartaoCredito cc = new CartaoCredito();
        assertThat(cc.getNum()).isZero();
        assertThat(cc.getDiaVencimento()).isZero();
        assertThat(cc.getNumero()).isNull();
        assertThat(cc.getValidade()).isNull();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        Date validade = new Date();
        CartaoCredito cc = new CartaoCredito(1, 10, "1234-5678", validade);

        assertThat(cc.getNum()).isEqualTo(1);
        assertThat(cc.getDiaVencimento()).isEqualTo(10);
        assertThat(cc.getNumero()).isEqualTo("1234-5678");
        assertThat(cc.getValidade()).isEqualTo(validade);
    }

    @Test
    void deveAlterarValoresComSetters() {
        CartaoCredito cc = new CartaoCredito();
        Date validade = new Date();

        cc.setNumero("9999-0000");
        cc.setValidade(validade);

        assertThat(cc.getNumero()).isEqualTo("9999-0000");
        assertThat(cc.getValidade()).isEqualTo(validade);
    }
}
