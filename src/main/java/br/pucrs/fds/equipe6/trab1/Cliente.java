package br.pucrs.fds.equipe6.trab1;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Cliente {
    private String nome;
    private String CPF;
    private String email;
    private Date data;

    private String password;

    private List<Contrato> contratos;

    public Cliente(String nome, String CPF, String email, Date data, String password) {
        this.nome = nome;
        this.CPF = CPF;
        this.email = email;
        this.data = data;
        this.password = password;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return this.CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}