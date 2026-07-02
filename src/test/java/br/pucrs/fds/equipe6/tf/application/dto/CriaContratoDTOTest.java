package br.pucrs.fds.equipe6.tf.application.dto;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class CriaContratoDTOTest {

    @Test
    void deveArmazenarERetornarTodosOsCampos() {
        CriaContratoDTO dto = new CriaContratoDTO();
        Date data = new Date();

        dto.setId(1);
        dto.setPeriodo(30);
        dto.setCpf("11122233344");
        dto.setCodigoJogo(5);
        dto.setNum(2);
        dto.setData(data);

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getPeriodo()).isEqualTo(30);
        assertThat(dto.getCpf()).isEqualTo("11122233344");
        assertThat(dto.getCodigoJogo()).isEqualTo(5);
        assertThat(dto.getNum()).isEqualTo(2);
        assertThat(dto.getData()).isEqualTo(data);
    }

    @Test
    void deveIniciarComValoresPadrao() {
        CriaContratoDTO dto = new CriaContratoDTO();

        assertThat(dto.getId()).isZero();
        assertThat(dto.getPeriodo()).isZero();
        assertThat(dto.getCpf()).isNull();
        assertThat(dto.getCodigoJogo()).isZero();
        assertThat(dto.getNum()).isZero();
        assertThat(dto.getData()).isNull();
    }
}
