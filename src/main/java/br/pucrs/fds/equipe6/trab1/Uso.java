package br.pucrs.fds.equipe6.trab1;

import java.time.*;
import java.util.Date;

public class Uso {
    private int numero;
    private Date dataInicio;
    private Date dataFim;
    private int horarioInicio;
    private int horarioFim;


    public Uso(int numero, Date dataInicio, Date dataFim, int horarioInicio, int horarioFim) {
        this.numero = numero;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDataInicio() {
        return this.dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return this.dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public int getHorarioInicio() {
        return this.horarioInicio;
    }

    public void setHorarioInicio(int horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public int getHorarioFim() {
        return this.horarioFim;
    }

    public void setHorarioFim(int horarioFim) {
        this.horarioFim = horarioFim;
    }

    public long getDuracaoMinutos() {
        LocalDate dataIni = dataInicio.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalTime horaIni = LocalTime.of(horarioInicio % 24, 0);

        LocalDate dataF = dataFim.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalTime horaFim = LocalTime.of(horarioFim % 24, 0);

        LocalDateTime dataHoraIni = LocalDateTime.of(dataIni, horaIni);
        LocalDateTime dataHoraFim = LocalDateTime.of(dataF, horaFim);

        return Duration.between(dataHoraIni, dataHoraFim).toMinutes();
    }

}