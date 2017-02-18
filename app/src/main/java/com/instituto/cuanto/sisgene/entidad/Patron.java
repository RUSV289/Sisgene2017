package com.instituto.cuanto.sisgene.entidad;

/**
 * Created by RUSVEL on 7/02/2017.
 */
public class Patron {

    private String pat_id;
    private String pat_descripcion;
    private String pat_max_items;

    public Patron() {
    }

    public String getPat_id() {
        return pat_id;
    }

    public void setPat_id(String pat_id) {
        this.pat_id = pat_id;
    }

    public String getPat_descripcion() {
        return pat_descripcion;
    }

    public void setPat_descripcion(String pat_descripcion) {
        this.pat_descripcion = pat_descripcion;
    }

    public String getPat_max_items() {
        return pat_max_items;
    }

    public void setPat_max_items(String pat_max_items) {
        this.pat_max_items = pat_max_items;
    }
}
