
package com.instituto.cuanto.sisgene.entidad;

/**
 *
 * @author JMonzalve
 */
public class Seccion {
    private String sec_id;
    private String sec_nombre;
    private String sec_nota;
    private String sec_numero_seccion;

    //nuevos campos campos version 2 sisgene 2017
    private String sec_categoria;
    private String cae_id;
    private String sec_flag_portada; //'Flag para diferenciar si SECCION es de la Portada o es del Cuerpo del Cuestionario.            Valores:            0 = No            1 = Si',

    public String getSec_categoria() {
        return sec_categoria;
    }

    public void setSec_categoria(String sec_categoria) {
        this.sec_categoria = sec_categoria;
    }

    public String getCae_id() {
        return cae_id;
    }

    public void setCae_id(String cae_id) {
        this.cae_id = cae_id;
    }

    public String getSec_flag_portada() {
        return sec_flag_portada;
    }

    public void setSec_flag_portada(String sec_flag_portada) {
        this.sec_flag_portada = sec_flag_portada;
    }

    public Seccion(){
        //Constructor de la clase Seccion
    }

    public String getSec_id() {
        return sec_id;
    }

    public void setSec_id(String sec_id) {
        this.sec_id = sec_id;
    }

    public String getSec_nombre() {
        return sec_nombre;
    }

    public void setSec_nombre(String sec_nombre) {
        this.sec_nombre = sec_nombre;
    }

    public String getSec_nota() {
        return sec_nota;
    }

    public void setSec_nota(String sec_nota) {
        this.sec_nota = sec_nota;
    }

    public String getSec_numero_seccion() {
        return sec_numero_seccion;
    }

    public void setSec_numero_seccion(String sec_numero_seccion) {
        this.sec_numero_seccion = sec_numero_seccion;
    }
}
