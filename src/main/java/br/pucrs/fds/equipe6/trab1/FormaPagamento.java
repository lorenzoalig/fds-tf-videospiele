package br.pucrs.fds.equipe6.trab1;

// versão mínima/temporária — Alice vai expandir com herança (CartaoCredito, PIX)
public class FormaPagamento {
    private int num;
    private int diaVencimento;

    public FormaPagamento(int num, int diaVencimento) {
        this.num = num;
        this.diaVencimento = diaVencimento;
    }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public int getDiaVencimento() { return diaVencimento; }
    public void setDiaVencimento(int diaVencimento) { this.diaVencimento = diaVencimento; }
}
