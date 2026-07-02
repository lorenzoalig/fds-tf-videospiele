package br.pucrs.fds.equipe6.tf.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    void deveCriarComConstrutorPadrao() {
        Cliente c = new Cliente();
        assertThat(c.getCod()).isZero();
        assertThat(c.getNome()).isNull();
    }

    @Test
    void deveCriarComConstrutorComArgumentos() {
        Date nascimento = new Date();
        Cliente c = new Cliente(1L, "Lorenzo", "111.111.111-11", "lorenzo@teste.com", nascimento, "senha123");

        assertThat(c.getCod()).isEqualTo(1L);
        assertThat(c.getNome()).isEqualTo("Lorenzo");
        assertThat(c.getCPF()).isEqualTo("111.111.111-11");
        assertThat(c.getEmail()).isEqualTo("lorenzo@teste.com");
        assertThat(c.getNascimento()).isEqualTo(nascimento);
        assertThat(c.getPassword()).isEqualTo("senha123");
    }

    @Test
    void deveAlterarValoresComSetters() {
        Cliente c = new Cliente();
        Date nascimento = new Date();

        c.setCod(2L);
        c.setNome("Ana");
        c.setCPF("222.222.222-22");
        c.setEmail("ana@teste.com");
        c.setNascimento(nascimento);
        c.setPassword("outraSenha");

        assertThat(c.getCod()).isEqualTo(2L);
        assertThat(c.getNome()).isEqualTo("Ana");
        assertThat(c.getCPF()).isEqualTo("222.222.222-22");
        assertThat(c.getEmail()).isEqualTo("ana@teste.com");
        assertThat(c.getNascimento()).isEqualTo(nascimento);
        assertThat(c.getPassword()).isEqualTo("outraSenha");
    }
}
