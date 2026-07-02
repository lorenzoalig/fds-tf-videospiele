package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoriaTest {

    @Test
    void deveCriarComConstrutorPadrao() {
        Categoria c = new Categoria();
        assertThat(c.getNum()).isZero();
        assertThat(c.getNome()).isNull();
        assertThat(c.getValorMinimo()).isZero();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        Categoria c = new Categoria(1, "Ação", 10.5);

        assertThat(c.getNum()).isEqualTo(1);
        assertThat(c.getNome()).isEqualTo("Ação");
        assertThat(c.getValorMinimo()).isEqualTo(10.5);
    }

    @Test
    void deveAlterarValoresComSetters() {
        Categoria c = new Categoria();
        c.setNum(2);
        c.setNome("RPG");
        c.setValorMinimo(20.0);

        assertThat(c.getNum()).isEqualTo(2);
        assertThat(c.getNome()).isEqualTo("RPG");
        assertThat(c.getValorMinimo()).isEqualTo(20.0);
    }
}
