package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SituacaoTest {

    @Test
    void deveRetornarNomeCorretoParaCadaValor() {
        assertThat(Situacao.DISPONIVEL.getNome()).isEqualTo("disponivel");
        assertThat(Situacao.CONTRATADO.getNome()).isEqualTo("contratado");
        assertThat(Situacao.OBSOLETO.getNome()).isEqualTo("obsoleto");
        assertThat(Situacao.REMOVIDO.getNome()).isEqualTo("removido");
        assertThat(Situacao.BLOQUEADO.getNome()).isEqualTo("bloqueado");
    }

    @Test
    void deveAlterarNomeComSetter() {
        Situacao situacao = Situacao.DISPONIVEL;
        situacao.setNome("novoNome");
        assertThat(situacao.getNome()).isEqualTo("novoNome");
        // restaura para não afetar outros testes que compartilham a instância do enum
        situacao.setNome("disponivel");
    }

    @Test
    void deveBuscarPorNomeExistente() {
        assertThat(Situacao.buscaPorNome("contratado")).isEqualTo(Situacao.CONTRATADO);
        assertThat(Situacao.buscaPorNome("bloqueado")).isEqualTo(Situacao.BLOQUEADO);
    }

    @Test
    void deveRetornarNullAoBuscarPorNomeInexistente() {
        assertThat(Situacao.buscaPorNome("inexistente")).isNull();
    }
}
