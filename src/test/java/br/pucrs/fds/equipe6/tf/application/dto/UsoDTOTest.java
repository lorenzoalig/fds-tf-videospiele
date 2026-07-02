package br.pucrs.fds.equipe6.tf.application.dto;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class UsoDTOTest {

    @Test
    void deveArmazenarERetornarTodosOsCampos() {
        UsoDTO dto = new UsoDTO();
        Date inicio = new Date(1000L);
        Date fim = new Date(2000L);

        dto.setIdContrato(1);
        dto.setNumero(3);
        dto.setDataInicio(inicio);
        dto.setDataFim(fim);
        dto.setHorarioInicio(8);
        dto.setHorarioFim(18);

        assertThat(dto.getIdContrato()).isEqualTo(1);
        assertThat(dto.getNumero()).isEqualTo(3);
        assertThat(dto.getDataInicio()).isEqualTo(inicio);
        assertThat(dto.getDataFim()).isEqualTo(fim);
        assertThat(dto.getHorarioInicio()).isEqualTo(8);
        assertThat(dto.getHorarioFim()).isEqualTo(18);
    }

    @Test
    void deveIniciarComValoresPadrao() {
        UsoDTO dto = new UsoDTO();

        assertThat(dto.getIdContrato()).isZero();
        assertThat(dto.getNumero()).isZero();
        assertThat(dto.getDataInicio()).isNull();
        assertThat(dto.getDataFim()).isNull();
        assertThat(dto.getHorarioInicio()).isZero();
        assertThat(dto.getHorarioFim()).isZero();
    }
}
