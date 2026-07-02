package br.pucrs.fds.equipe6.tf.domain.entity;

import br.pucrs.fds.equipe6.tf.application.dto.CriaContratoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ContratosTest {

    private Contratos contratos;
    private Clientela clientela;
    private Jogos jogos;
    private FormasPagamento formasPagamento;

    private Cliente cliente;
    private Jogo jogo;
    private FormaPagamento formaPagamento;

    @BeforeEach
    void setUp() {
        contratos = new Contratos();
        clientela = new Clientela();
        jogos = new Jogos();
        formasPagamento = new FormasPagamento();

        cliente = new Cliente(1L, "Lorenzo", "111.111.111-11", "l@t.com", null, "s");
        jogo = new Jogo(1, "Jogo A", Calendar.getInstance().get(Calendar.YEAR), 0.5, null, null);
        formaPagamento = new PIX(1, 10, "chave");

        clientela.addCliente(cliente);
        jogos.addJogo(jogo);
        formasPagamento.addFormaPagamento(formaPagamento);
    }

    private CriaContratoDTO dtoValido() {
        CriaContratoDTO dto = new CriaContratoDTO();
        dto.setId(1);
        dto.setPeriodo(30);
        dto.setCpf("111.111.111-11");
        dto.setCodigoJogo(1);
        dto.setNum(1);
        dto.setData(new Date());
        return dto;
    }

    @Test
    void deveIniciarListaVazia() {
        assertThat(contratos.getContratos()).isEmpty();
    }

    @Test
    void deveAdicionarContrato() {
        Contrato c = new Contrato(1, new Date(), 10, cliente, jogo);
        contratos.addContrato(c);

        assertThat(contratos.getContratos()).containsExactly(c);
    }

    @Test
    void deveConsultarContratosCompletos() {
        Contrato c = new Contrato(1, new Date(), 10, cliente, jogo);
        contratos.addContrato(c);

        assertThat(contratos.consultarContratosCompletos()).containsExactly(c);
    }

    @Test
    void deveBuscarContratosPorCpf() {
        Contrato c = new Contrato(1, new Date(), 10, cliente, jogo);
        contratos.addContrato(c);

        assertThat(contratos.getContratosPorCpf("111.111.111-11")).containsExactly(c);
        assertThat(contratos.getContratosPorCpf("000.000.000-00")).isEmpty();
    }

    @Test
    void deveBuscarContratoPorIdExistente() {
        Contrato c = new Contrato(1, new Date(), 10, cliente, jogo);
        contratos.addContrato(c);

        assertThat(contratos.buscarContratoPorId(1)).isEqualTo(c);
    }

    @Test
    void deveRetornarNullAoBuscarContratoPorIdInexistente() {
        assertThat(contratos.buscarContratoPorId(99)).isNull();
    }

    @Test
    void deveDefinirListaDeContratosComSetter() {
        ArrayList<Contrato> lista = new ArrayList<>();
        Contrato c = new Contrato(1, new Date(), 10, cliente, jogo);
        lista.add(c);

        contratos.setContratos(lista);

        assertThat(contratos.getContratos()).containsExactly(c);
    }

    @Test
    void addContratoValidadoDeveFalharQuandoIdJaExiste() {
        Contrato existente = new Contrato(1, new Date(), 10, cliente, jogo);
        contratos.addContrato(existente);

        boolean resultado = contratos.addContratoValidado(dtoValido(), clientela, jogos, formasPagamento);

        assertThat(resultado).isFalse();
        assertThat(contratos.getContratos()).hasSize(1);
    }

    @Test
    void addContratoValidadoDeveFalharQuandoClienteNaoExiste() {
        CriaContratoDTO dto = dtoValido();
        dto.setCpf("999.999.999-99");

        assertThat(contratos.addContratoValidado(dto, clientela, jogos, formasPagamento)).isFalse();
    }

    @Test
    void addContratoValidadoDeveFalharQuandoJogoNaoExiste() {
        CriaContratoDTO dto = dtoValido();
        dto.setCodigoJogo(999);

        assertThat(contratos.addContratoValidado(dto, clientela, jogos, formasPagamento)).isFalse();
    }

    @Test
    void addContratoValidadoDeveFalharQuandoFormaPagamentoNaoExiste() {
        CriaContratoDTO dto = dtoValido();
        dto.setNum(999);

        assertThat(contratos.addContratoValidado(dto, clientela, jogos, formasPagamento)).isFalse();
    }

    @Test
    void addContratoValidadoDeveFalharQuandoJogoJaContratado() {
        Contrato ativo = new Contrato(50, new Date(), 30, cliente, jogo);
        contratos.addContrato(ativo);

        CriaContratoDTO dto = dtoValido();
        dto.setId(2); // id diferente do existente

        assertThat(contratos.addContratoValidado(dto, clientela, jogos, formasPagamento)).isFalse();
    }

    @Test
    void addContratoValidadoDeveTerSucessoComDadosValidos() {
        boolean resultado = contratos.addContratoValidado(dtoValido(), clientela, jogos, formasPagamento);

        assertThat(resultado).isTrue();
        assertThat(contratos.getContratos()).hasSize(1);
        assertThat(contratos.buscarContratoPorId(1)).isNotNull();
    }
}
