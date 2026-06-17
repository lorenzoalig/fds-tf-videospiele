package br.pucrs.fds.equipe6.trab1;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Jogo {
    private int cod;
    private String nome;
    private int ano;
    private double valorMinuto;
    private Categoria categoria;
    private Situacao situacao;

    public Jogo(int cod, String nome, int ano, double valorMinuto, Categoria categoria) {
        this.cod = cod;
        this.nome = nome;
        this.ano = ano;
        this.valorMinuto = valorMinuto;
        this.situacao = Situacao.DISPONIVEL;
        this.categoria = categoria;
    }

    public void setSituacao(Situacao situacao){
        this.situacao = situacao;
    }

    public Situacao getSituacao(){
        return this.situacao;
    }

    public int getCod() {
        return this.cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return this.ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getValorMinuto() {
        return this.valorMinuto;
    }

    public void setValorMinuto(double valorMinuto) {
        this.valorMinuto = valorMinuto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    public boolean estaContratado(Contratos contratos){
        List<Contrato> contratosDoJogo = contratos.getContratos()
                                                    .stream()
                                                    .filter(c -> c.getJogo().equals(this))
                                                    .toList();
        return  !contratosDoJogo.isEmpty();
    }

    public boolean estaObsoleto(Contratos contratos){

        Calendar limite = Calendar.getInstance();
        limite.add(Calendar.YEAR, -2);
        Date dataLimite = limite.getTime();

        List<Contrato> contratosDoJogo = contratos.getContratos()
                                                    .stream()
                                                    .filter(c -> c.getJogo().equals(this))
                                                    .toList();

    // Caso 1:nunca teve contrato e foi lançado há mais de 2 anos
    if(contratosDoJogo.isEmpty()) {

        Calendar lancamento = Calendar.getInstance();

        lancamento.set(this.getAno(), Calendar.JANUARY,1,0,0,0);
        lancamento.set(Calendar.MILLISECOND, 0);

        if (lancamento.getTime().before(dataLimite)){
            return true;
        }
    } else{

    // Caso 2:último contrato expirou há mais de 2 anos

            Date ultimaDataFim = contratosDoJogo.stream()
                                                .flatMap(c -> c.getUsos().stream())
                                                .map(Uso::getDataFim)
                                                .filter(Objects::nonNull)
                                                .max(Date::compareTo)
                                                .orElse(null);

            if(ultimaDataFim != null && ultimaDataFim.before(dataLimite)){
                return true;
            } else return false;
         }
        return false;
    }

    public boolean estaRemovido(Contratos contratos){

        List<Contrato> contratosDoJogo = contratos.getContratos()
                                                    .stream()
                                                    .filter(c -> c.getJogo().equals(this))
                                                    .toList();

        Calendar limite = Calendar.getInstance();
        limite.add(Calendar.YEAR, -3);
        Date dataLimite = limite.getTime();


        Date ultimaDataFim = contratosDoJogo.stream()
                                                .flatMap(c -> c.getUsos().stream())
                                                .map(Uso::getDataFim)
                                                .filter(Objects::nonNull)
                                                .max(Date::compareTo)
                                                .orElse(null);

        return ultimaDataFim != null && ultimaDataFim.before(dataLimite);
    }
}
