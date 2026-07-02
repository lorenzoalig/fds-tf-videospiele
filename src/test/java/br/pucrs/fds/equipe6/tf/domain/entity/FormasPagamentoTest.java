package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FormasPagamentoTest {

    private FormasPagamento formasPagamento;

    @BeforeEach
    void setUp() {
        formasPagamento = new FormasPagamento();
    }

    @Test
    void deveIniciarListaVazia() {
        assertThat(formasPagamento.getFormasPagamento()).isEmpty();
    }

    @Test
    void deveAdicionarFormaPagamento() {
        PIX pix = new PIX(1, 10, "chave");
        formasPagamento.addFormaPagamento(pix);

        assertThat(formasPagamento.getFormasPagamento()).containsExactly(pix);
    }

    @Test
    void deveBuscarPorNumExistente() {
        PIX pix = new PIX(1, 10, "chave");
        formasPagamento.addFormaPagamento(pix);

        assertThat(formasPagamento.buscaPorNum(1)).isEqualTo(pix);
    }

    @Test
    void deveRetornarNullAoBuscarPorNumInexistente() {
        PIX pix = new PIX(1, 10, "chave");
        formasPagamento.addFormaPagamento(pix);

        assertThat(formasPagamento.buscaPorNum(99)).isNull();
    }
}
