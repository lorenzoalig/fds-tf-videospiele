package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PIXTest {

    @Test
    void deveCriarComConstrutorPadrao() {
        PIX pix = new PIX();
        assertThat(pix.getNum()).isZero();
        assertThat(pix.getDiaVencimento()).isZero();
        assertThat(pix.getChave()).isNull();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        PIX pix = new PIX(2, 5, "chave-pix@exemplo.com");

        assertThat(pix.getNum()).isEqualTo(2);
        assertThat(pix.getDiaVencimento()).isEqualTo(5);
        assertThat(pix.getChave()).isEqualTo("chave-pix@exemplo.com");
    }

    @Test
    void deveAlterarChaveComSetter() {
        PIX pix = new PIX();
        pix.setChave("nova-chave");
        assertThat(pix.getChave()).isEqualTo("nova-chave");
    }
}
