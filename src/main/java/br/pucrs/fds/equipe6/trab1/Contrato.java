package br.pucrs.fds.equipe6.trab1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Contrato {
    private int id;
    private Date data;
    private int periodo;
    private Cliente cliente;
    private Jogo jogo;
    private List<Uso> usos;
    private boolean cancelado = false; // novo atributo ! para cancelar contratos (endpoint 10) 


    public Contrato(int id, Date data, int periodo, Cliente cliente, Jogo jogo, Uso uso) {
        this.id = id;
        this.data = data;
        this.periodo = periodo;
        this.cliente = cliente;
        this.jogo = jogo;

        usos = new ArrayList<Uso>();
        usos.add(uso);
    }
    
    //construtor pro metodo da classe contratos 
    public Contrato(int id, Date data, int periodo, Cliente cliente, Jogo jogo) {
    this.id = id;
    this.data = data;
    this.periodo = periodo;
    this.cliente = cliente;
    this.jogo = jogo;
    this.usos = new ArrayList<>();
    this.cancelado = false;
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public void addUso(Uso uso){
        usos.add(uso);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public Jogo getJogo(){
        return this.jogo;
    }

    public List<Uso> getUsos(){
        return this.usos;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    // Consulta a data final do contrato data + periodo
    public Date getDataFim() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(data);

        cal.add(Calendar.DAY_OF_MONTH, periodo);

        return cal.getTime();
    }

    // metodo que cancela logicamente nao fisicamente
    public void cancelar() { this.cancelado = true; }
}
