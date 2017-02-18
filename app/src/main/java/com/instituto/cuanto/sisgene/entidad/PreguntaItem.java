
package com.instituto.cuanto.sisgene.entidad;

/**
 *
 * @author JMonzalve
 */
public class PreguntaItem {
    private String pri_id;
    private String pre_id;
    private String ite_id;
    private String pri_numeralItem;
    private String pri_valor;

    //Nuevos campo version 2 sisgene 2017
    private String pri_tipo_dato;
    private String pri_desde;
    private String pri_hasta;
    private String pri_sin_encabezado;

    public String getPri_tipo_dato() {
        return pri_tipo_dato;
    }

    public void setPri_tipo_dato(String pri_tipo_dato) {
        this.pri_tipo_dato = pri_tipo_dato;
    }

    public String getPri_desde() {
        return pri_desde;
    }

    public void setPri_desde(String pri_desde) {
        this.pri_desde = pri_desde;
    }

    public String getPri_hasta() {
        return pri_hasta;
    }

    public void setPri_hasta(String pri_hasta) {
        this.pri_hasta = pri_hasta;
    }

    public String getPri_sin_encabezado() {
        return pri_sin_encabezado;
    }

    public void setPri_sin_encabezado(String pri_sin_encabezado) {
        this.pri_sin_encabezado = pri_sin_encabezado;
    }

    public PreguntaItem(){
        //Constructor de la clase PreguntaItem
    }

    public String getPri_id() {
        return pri_id;
    }

    public void setPri_id(String pri_id) {
        this.pri_id = pri_id;
    }

    public String getPre_id() {
        return pre_id;
    }

    public void setPre_id(String pre_id) {
        this.pre_id = pre_id;
    }

    public String getIte_id() {
        return ite_id;
    }

    public void setIte_id(String ite_id) {
        this.ite_id = ite_id;
    }

    public String getPri_numeralItem() {
        return pri_numeralItem;
    }

    public String getPri_valor() {
        return pri_valor;
    }

    public void setPri_valor(String pri_valor) {
        this.pri_valor = pri_valor;
    }

    public void setPri_numeralItem(String pri_numeralItem) {
        this.pri_numeralItem = pri_numeralItem;
    }
}