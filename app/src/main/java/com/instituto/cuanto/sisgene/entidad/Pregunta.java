
package com.instituto.cuanto.sisgene.entidad;

/**
 *
 * @author JMonzalve
 */
public class Pregunta {
    private String pre_id;
    private String pre_numero;
    private String pre_enunciado;
    private String pre_explicativo;
    private String pre_comentario;
    private String pre_guia_rpta;
    private String pre_tipo_rpta;
    private String pre_unico_patron;
    private String pre_cant_maxima_items;
    private String pre_maxNumRptas;
    private String pre_importaOrdenRptas;

    //nuevos campos agregados para las validaciones de las respuestas
    private String pre_subtipo;
    private String pre_tiponumerico;
    private String pre_desde;
    private String pre_hasta;


    //nuevos campos sisgene version 2 2017
    private String pat_id;
    private String pre_importa_orden_rptaABMU;
    private String pre_excluye_ABMU;



    public String getPre_unico_patron() {
        return pre_unico_patron;
    }

    public void setPre_unico_patron(String pre_unico_patron) {
        this.pre_unico_patron = pre_unico_patron;
    }

    public String getPat_id() {
        return pat_id;
    }

    public void setPat_id(String pat_id) {
        this.pat_id = pat_id;
    }

    public String getPre_importa_orden_rptaABMU() {
        return pre_importa_orden_rptaABMU;
    }

    public void setPre_importa_orden_rptaABMU(String pre_importa_orden_rptaABMU) {
        this.pre_importa_orden_rptaABMU = pre_importa_orden_rptaABMU;
    }

    public String getPre_excluye_ABMU() {
        return pre_excluye_ABMU;
    }

    public void setPre_excluye_ABMU(String pre_excluye_ABMU) {
        this.pre_excluye_ABMU = pre_excluye_ABMU;
    }

    public Pregunta(){
        //Constructor de la clase Pregunta
    }

    public String getPre_id() {
        return pre_id;
    }

    public void setPre_id(String pre_id) {
        this.pre_id = pre_id;
    }

    public String getPre_numero() {
        return pre_numero;
    }

    public void setPre_numero(String pre_numero) {
        this.pre_numero = pre_numero;
    }

    public String getPre_enunciado() {
        return pre_enunciado;
    }

    public void setPre_enunciado(String pre_enunciado) {
        this.pre_enunciado = pre_enunciado;
    }

    public String getPre_explicativo() {
        return pre_explicativo;
    }

    public void setPre_explicativo(String pre_explicativo) {
        this.pre_explicativo = pre_explicativo;
    }

    public String getPre_comentario() {
        return pre_comentario;
    }

    public void setPre_comentario(String pre_comentario) {
        this.pre_comentario = pre_comentario;
    }

    public String getPre_guia_rpta() {
        return pre_guia_rpta;
    }

    public void setPre_guia_rpta(String pre_guia_rpta) {
        this.pre_guia_rpta = pre_guia_rpta;
    }

    public String getPre_tipo_rpta() {
        return pre_tipo_rpta;
    }

    public void setPre_tipo_rpta(String pre_tipo_rpta) {
        this.pre_tipo_rpta = pre_tipo_rpta;
    }


    public String getPre_cant_maxima_items() {
        return pre_cant_maxima_items;
    }

    public void setPre_cant_maxima_items(String pre_cant_maxima_items) {
        this.pre_cant_maxima_items = pre_cant_maxima_items;
    }

    public String getPre_maxNumRptas() {
        return pre_maxNumRptas;
    }

    public void setPre_maxNumRptas(String pre_maxNumRptas) {
        this.pre_maxNumRptas = pre_maxNumRptas;
    }

    public String getPre_importaOrdenRptas() {
        return pre_importaOrdenRptas;
    }

    public void setPre_importaOrdenRptas(String pre_importaOrdenRptas) {
        this.pre_importaOrdenRptas = pre_importaOrdenRptas;
    }

    public String getPre_subtipo() {
        return pre_subtipo;
    }

    public void setPre_subtipo(String pre_subtipo) {
        this.pre_subtipo = pre_subtipo;
    }

    public String getPre_tiponumerico() {
        return pre_tiponumerico;
    }

    public void setPre_tiponumerico(String pre_tipoNumerico) {
        this.pre_tiponumerico = pre_tipoNumerico;
    }

    public String getPre_desde() {
        return pre_desde;
    }

    public void setPre_desde(String pre_desde) {
        this.pre_desde = pre_desde;
    }

    public String getPre_hasta() {
        return pre_hasta;
    }

    public void setPre_hasta(String pre_hasta) {
        this.pre_hasta = pre_hasta;
    }
}
