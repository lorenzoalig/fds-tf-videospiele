package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class CategoriasTest {

    private Categorias categorias;

    @BeforeEach
    void setUp() {
        categorias = new Categorias();
    }

    @Test
    void deveIniciarListaVazia() {
        assertThat(categorias.getCategorias()).isEmpty();
    }

    @Test
    void deveAdicionarCategoria() {
        Categoria c = new Categoria(1, "Ação", 10.0);
        categorias.addCategoria(c);

        assertThat(categorias.getCategorias()).containsExactly(c);
    }

    @Test
    void deveBuscarCategoriaPorNomeExistente() {
        Categoria c = new Categoria(1, "Ação", 10.0);
        categorias.addCategoria(c);

        assertThat(categorias.getCategoriaPorNome("Ação")).isEqualTo(c);
    }

    @Test
    void deveRetornarNullAoBuscarCategoriaPorNomeInexistente() {
        assertThat(categorias.getCategoriaPorNome("Inexistente")).isNull();
    }

    @Test
    void deveBuscarCategoriaPorNomeComStreamExistente() {
        Categoria c = new Categoria(2, "RPG", 15.0);
        categorias.addCategoria(c);

        assertThat(categorias.buscaCategoriaPorNome("RPG")).isEqualTo(c);
    }

    @Test
    void deveRetornarNullAoBuscarCategoriaPorNomeComStreamInexistente() {
        assertThat(categorias.buscaCategoriaPorNome("Inexistente")).isNull();
    }

    @Test
    void deveDefinirListaDeCategoriasComSetter() {
        ArrayList<Categoria> lista = new ArrayList<>();
        Categoria c = new Categoria(3, "Estratégia", 12.0);
        lista.add(c);

        categorias.setCategorias(lista);

        assertThat(categorias.getCategorias()).containsExactly(c);
    }
}
