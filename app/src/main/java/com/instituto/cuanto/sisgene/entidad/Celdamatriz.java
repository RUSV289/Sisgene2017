package com.instituto.cuanto.sisgene.entidad;

/**
 * Created by RUSVEL on 7/02/2017.
 */
public class Celdamatriz {

    private String cem_id;
    private String pro_id;
    private String pri_id;
    private String cem_fila;
    private String cem_columna;
    private String cem_tipo_dato;
    private String cem_desde;
    private String cem_hasta;

    public Celdamatriz() {
    }

    public String getCem_id() {
        return cem_id;
    }

    public void setCem_id(String cem_id) {
        this.cem_id = cem_id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPri_id() {
        return pri_id;
    }

    public void setPri_id(String pri_id) {
        this.pri_id = pri_id;
    }

    public String getCem_fila() {
        return cem_fila;
    }

    public void setCem_fila(String cem_fila) {
        this.cem_fila = cem_fila;
    }

    public String getCem_columna() {
        return cem_columna;
    }

    public void setCem_columna(String cem_columna) {
        this.cem_columna = cem_columna;
    }

    public String getCem_tipo_dato() {
        return cem_tipo_dato;
    }

    public void setCem_tipo_dato(String cem_tipo_dato) {
        this.cem_tipo_dato = cem_tipo_dato;
    }

    public String getCem_desde() {
        return cem_desde;
    }

    public void setCem_desde(String cem_desde) {
        this.cem_desde = cem_desde;
    }

    public String getCem_hasta() {
        return cem_hasta;
    }

    public void setCem_hasta(String cem_hasta) {
        this.cem_hasta = cem_hasta;
    }
}
