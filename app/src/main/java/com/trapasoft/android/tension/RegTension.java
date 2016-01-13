package com.trapasoft.android.tension;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alejandro on 15/12/15.
 * Modelo de un registro de tensión
 * Contiene los campos necesarios, incluido el id que proporciona la b.d.
 */
public class RegTension {
    int id;
    String fechahora;
    String fecha;
    String hora;
    long sist;
    long diast;
    int pulso;

    /**
     * constructor vacío. Rellena la fecha/hora por defecto
     */
    public RegTension() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        this.fechahora = sdf.format(date);
        this.sist = 0L;
        this.diast = 0L;
        this.pulso = 0;
        setFechayHora(this.fechahora);

    }

    public RegTension(String fechahora, long sist, long diast, int pulso) {
        this.fechahora = fechahora;
        this.sist = sist;
        this.diast = diast;
        this.pulso = pulso;
        setFechayHora(fechahora);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechahora() {
        return fechahora;
    }

    public void setFechahora(String fechahora) {
        this.fechahora = fechahora;
        setFechayHora(fechahora);
    }

    public void setFechahora(String fecha, String hora) {
        this.fechahora = fecha + " " + hora;
        this.fecha = fecha;
        this.hora = hora;
    }

    public long getSist() {
        return sist;
    }

    public void setSist(long sist) {
        this.sist = sist;
    }

    public double getDiast() {
        return diast;
    }

    public void setDiast(long diast) {
        this.diast = diast;
    }

    public int getPulso() {
        return pulso;
    }

    public void setPulso(int pulso) {
        this.pulso = pulso;
    }
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setFechayHora(String fechahora) {
        if (fechahora.length() > 0) {
            String[] campos = fechahora.split(" ");
            this.fecha = campos[0];
            this.hora = campos[1];
        }
    }

    public String toString() {
        return this.id + "; " + this.fechahora + "; " + this.sist + "; " + this.diast + "; " + this.pulso;
    }

}
