package br.pucrs.fds.equipe6.tf.application.dto;

import br.pucrs.fds.equipe6.tf.domain.entity.Categoria;
import br.pucrs.fds.equipe6.tf.domain.entity.Cliente;
import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Moeda;
import br.pucrs.fds.equipe6.tf.domain.entity.Uso;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContratoRespostaDTOTest {

    private Contrato criarContrato() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);
        Cliente cliente = new Cliente(1L, "Lorenzo", "11122233344", "l@t.com", null, "senha");
        return new Contrato(1, new Date(), 30, cliente, jogo);
    }

    @Test
    void deveMapearCamposBasicosDoContrato() {
        Contrato contrato = criarContrato();

        ContratoRespostaDTO dto = new ContratoRespostaDTO(contrato);

        assertThat(dto.getId()).isEqualTo(contrato.getId());
        assertThat(dto.getPeriodo()).isEqualTo(contrato.getPeriodo());
        assertThat(dto.getData()).isEqualTo(contrato.getData());
        assertThat(dto.getCpf()).isEqualTo(contrato.getCliente().getCPF());
        assertThat(dto.getNomeCliente()).isEqualTo(contrato.getCliente().getNome());
        assertThat(dto.isCancelado()).isEqualTo(contrato.isCancelado());
        assertThat(dto.getCodigoJogo()).isEqualTo(contrato.getJogo().getCod());
        assertThat(dto.getNomeJogo()).isEqualTo(contrato.getJogo().getNome());
        assertThat(dto.getCategoria()).isEqualTo(contrato.getJogo().getCategoria().getNome());
    }

    @Test
    void deveMapearContratoCanceladoCorretamente() {
        Contrato contrato = criarContrato();
        contrato.cancelar();

        ContratoRespostaDTO dto = new ContratoRespostaDTO(contrato);

        assertThat(dto.isCancelado()).isTrue();
    }

    @Test
    void deveRetornarListaVaziaDeUsosQuandoContratoNaoTemUsos() {
        Contrato contrato = criarContrato();

        ContratoRespostaDTO dto = new ContratoRespostaDTO(contrato);

        assertThat(dto.getUsos()).isEmpty();
    }

    @Test
    void deveMapearUsosDoContratoParaUsoSimples() {
        Contrato contrato = criarContrato();
        Date inicio = new Date(1000L);
        Date fim = new Date(2000L);
        Uso uso = new Uso(1, inicio, fim, 8, 18);
        contrato.addUso(uso);

        ContratoRespostaDTO dto = new ContratoRespostaDTO(contrato);

        assertThat(dto.getUsos()).hasSize(1);
        ContratoRespostaDTO.UsoSimples usoSimples = dto.getUsos().get(0);
        assertThat(usoSimples.numero).isEqualTo(uso.getNumero());
        assertThat(usoSimples.dataInicio).isEqualTo(uso.getDataInicio());
        assertThat(usoSimples.dataFim).isEqualTo(uso.getDataFim());
        assertThat(usoSimples.horarioInicio).isEqualTo(uso.getHorarioInicio());
        assertThat(usoSimples.horarioFim).isEqualTo(uso.getHorarioFim());
        assertThat(usoSimples.duracaoMinutos).isEqualTo(uso.getDuracaoMinutos());
    }

    @Test
    void deveMapearMultiplosUsosNaOrdemDoContrato() {
        Contrato contrato = criarContrato();
        contrato.addUso(new Uso(1, new Date(1000L), new Date(2000L), 8, 12));
        contrato.addUso(new Uso(2, new Date(3000L), new Date(4000L), 14, 18));

        ContratoRespostaDTO dto = new ContratoRespostaDTO(contrato);

        assertThat(dto.getUsos()).hasSize(2);
        assertThat(dto.getUsos().get(0).numero).isEqualTo(1);
        assertThat(dto.getUsos().get(1).numero).isEqualTo(2);
    }

    @Test
    void deveAplicarSettersEGetters() {
        Contrato contrato = criarContrato();
        ContratoRespostaDTO dto = new ContratoRespostaDTO(contrato);
        Date novaData = new Date(5000L);
        List<ContratoRespostaDTO.UsoSimples> novosUsos = new ArrayList<>();
        novosUsos.add(new ContratoRespostaDTO.UsoSimples(9, novaData, novaData, 1, 2, 60L));

        dto.setId(99);
        dto.setPeriodo(60);
        dto.setCpf("99988877766");
        dto.setNomeCliente("Outro Cliente");
        dto.setCodigoJogo(42);
        dto.setNomeJogo("Outro Jogo");
        dto.setCategoria("Outra Categoria");
        dto.setData(novaData);
        dto.setCancelado(true);
        dto.setUsos(novosUsos);

        assertThat(dto.getId()).isEqualTo(99);
        assertThat(dto.getPeriodo()).isEqualTo(60);
        assertThat(dto.getCpf()).isEqualTo("99988877766");
        assertThat(dto.getNomeCliente()).isEqualTo("Outro Cliente");
        assertThat(dto.getCodigoJogo()).isEqualTo(42);
        assertThat(dto.getNomeJogo()).isEqualTo("Outro Jogo");
        assertThat(dto.getCategoria()).isEqualTo("Outra Categoria");
        assertThat(dto.getData()).isEqualTo(novaData);
        assertThat(dto.isCancelado()).isTrue();
        assertThat(dto.getUsos()).isEqualTo(novosUsos);
    }

    @Test
    void usoSimplesDeveArmazenarTodosOsCampos() {
        Date inicio = new Date(1000L);
        Date fim = new Date(2000L);

        ContratoRespostaDTO.UsoSimples usoSimples =
                new ContratoRespostaDTO.UsoSimples(7, inicio, fim, 9, 21, 120L);

        assertThat(usoSimples.numero).isEqualTo(7);
        assertThat(usoSimples.dataInicio).isEqualTo(inicio);
        assertThat(usoSimples.dataFim).isEqualTo(fim);
        assertThat(usoSimples.horarioInicio).isEqualTo(9);
        assertThat(usoSimples.horarioFim).isEqualTo(21);
        assertThat(usoSimples.duracaoMinutos).isEqualTo(120L);
    }
}
