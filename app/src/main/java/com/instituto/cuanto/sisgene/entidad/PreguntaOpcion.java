
package com.instituto.cuanto.sisgene.entidad;

/**
 *
 * @author JMonzalve
 */
public class PreguntaOpcion {
    private String pro_id;
    private String pre_id;
    private String opc_id;
    private String pro_numeralOpcion;
    private String pro_numeroPreguntaSiguiente;
    private String pro_valor;
    //Nuevos campos sisgene 2 version 2017
    private String cae_id;
    private String pro_tipo_dato;
    private String pro_desde;
    private String pro_hasta;
    private String pro_valida_fila_MA;
    private String pro_parte_rpta;
    private String pro_nombre_variable;

    public String getCae_id() {
        return cae_id;
    }

    public void setCae_id(String cae_id) {
        this.cae_id = cae_id;
    }

    public String getPro_tipo_dato() {
        return pro_tipo_dato;
    }

    public void setPro_tipo_dato(String pro_tipo_dato) {
        this.pro_tipo_dato = pro_tipo_dato;
    }

    public String getPro_desde() {
        return pro_desde;
    }

    public void setPro_desde(String pro_desde) {
        this.pro_desde = pro_desde;
    }

    public String getPro_hasta() {
        return pro_hasta;
    }

    public void setPro_hasta(String pro_hasta) {
        this.pro_hasta = pro_hasta;
    }

    public String getPro_valida_fila_MA() {
        return pro_valida_fila_MA;
    }

    public void setPro_valida_fila_MA(String pro_valida_fila_MA) {
        this.pro_valida_fila_MA = pro_valida_fila_MA;
    }

    public String getPro_parte_rpta() {
        return pro_parte_rpta;
    }

    public void setPro_parte_rpta(String pro_parte_rpta) {
        this.pro_parte_rpta = pro_parte_rpta;
    }

    public String getPro_nombre_variable() {
        return pro_nombre_variable;
    }

    public void setPro_nombre_variable(String pro_nombre_variable) {
        this.pro_nombre_variable = pro_nombre_variable;
    }

    public PreguntaOpcion(){
        //Constructor de la clase PreguntaOpcion
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPre_id() {
        return pre_id;
    }

    public void setPre_id(String pre_id) {
        this.pre_id = pre_id;
    }

    public String getOpc_id() {
        return opc_id;
    }

    public void setOpc_id(String opc_id) {
        this.opc_id = opc_id;
    }

    public String getPro_numeralOpcion() {
        return pro_numeralOpcion;
    }

    public void setPro_numeralOpcion(String pro_numeralOpcion) {
        this.pro_numeralOpcion = pro_numeralOpcion;
    }

    public String getPro_numeroPreguntaSiguiente() {
        return pro_numeroPreguntaSiguiente;
    }

    public void setPro_numeroPreguntaSiguiente(String pro_numeroPreguntaSiguiente) {
        this.pro_numeroPreguntaSiguiente = pro_numeroPreguntaSiguiente;
    }



    public String getPro_valor() {
        return pro_valor;
    }

    public void setPro_valor(String pro_valor) {
        this.pro_valor = pro_valor;
    }


}
