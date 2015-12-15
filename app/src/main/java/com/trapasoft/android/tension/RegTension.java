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

    }

    public RegTension(String fechahora, long sist, long diast, int pulso) {
        this.fechahora = fechahora;
        this.sist = sist;
        this.diast = diast;
        this.pulso = pulso;
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
}
