package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ClientelaTest {

    private Clientela clientela;

    @BeforeEach
    void setUp() {
        clientela = new Clientela();
    }

    @Test
    void deveIniciarListaVazia() {
        assertThat(clientela.getClientes()).isEmpty();
    }

    @Test
    void deveAdicionarCliente() {
        Cliente c = new Cliente(1L, "Lorenzo", "111.111.111-11", "l@t.com", null, "s");
        clientela.addCliente(c);

        assertThat(clientela.getClientes()).containsExactly(c);
    }

    @Test
    void deveBuscarClientePorCpfExistente() {
        Cliente c = new Cliente(1L, "Lorenzo", "111.111.111-11", "l@t.com", null, "s");
        clientela.addCliente(c);

        assertThat(clientela.buscarClienteCPF("111.111.111-11")).isEqualTo(c);
    }

    @Test
    void deveRetornarNullAoBuscarClientePorCpfInexistente() {
        Cliente c = new Cliente(1L, "Lorenzo", "111.111.111-11", "l@t.com", null, "s");
        clientela.addCliente(c);

        assertThat(clientela.buscarClienteCPF("000.000.000-00")).isNull();
    }

    @Test
    void deveBuscarClientePorCodigoExistente() {
        Cliente c = new Cliente(5L, "Ana", "222.222.222-22", "a@t.com", null, "s");
        clientela.addCliente(c);

        assertThat(clientela.buscarClientePorCod(5L)).isEqualTo(c);
    }

    @Test
    void deveRetornarNullAoBuscarClientePorCodigoInexistente() {
        Cliente c = new Cliente(5L, "Ana", "222.222.222-22", "a@t.com", null, "s");
        clientela.addCliente(c);

        assertThat(clientela.buscarClientePorCod(99L)).isNull();
    }

    @Test
    void deveDefinirListaDeClientesComSetter() {
        ArrayList<Cliente> lista = new ArrayList<>();
        Cliente c = new Cliente(7L, "Beto", "333.333.333-33", "b@t.com", null, "s");
        lista.add(c);

        clientela.setClientes(lista);

        assertThat(clientela.getClientes()).containsExactly(c);
    }
}
