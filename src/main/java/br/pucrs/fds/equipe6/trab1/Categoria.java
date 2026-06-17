package br.pucrs.fds.equipe6.trab1;

import java.util.List;

public class Categoria {

    private  int num;
    private String nome;
    private double valorMinimo;
    private List<Jogo> jogos;


    public Categoria(int num, String nome, double valorMinimo) {
        this.num = num;
        this.nome = nome;
        this.valorMinimo = valorMinimo;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValorMinimo() {
        return this.valorMinimo;
    }

    public void setValorMinimo(double valorMinimo) {
        this.valorMinimo = valorMinimo;
    }


}
