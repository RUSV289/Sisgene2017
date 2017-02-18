package com.instituto.cuanto.sisgene.entidad;

/**
 * Created by RUSVEL on 7/02/2017.
 */
public class Ccpp {
    private String ccpp_id;
    private String ubi_id;
    private String ccpp_codigo;
    private String ccpp_centro_poblado;
    private String ccpp_area;
    private String ccpp_cod_area;
    private String ccpp_categoria;
    private String ccpp_cod_categoria;

    public Ccpp() {
    }

    public String getCcpp_id() {
        return ccpp_id;
    }

    public String getUbi_id() {
        return ubi_id;
    }

    public void setUbi_id(String ubi_id) {
        this.ubi_id = ubi_id;
    }

    public void setCcpp_id(String ccpp_id) {
        this.ccpp_id = ccpp_id;
    }

    public String getCcpp_codigo() {
        return ccpp_codigo;
    }

    public void setCcpp_codigo(String ccpp_codigo) {
        this.ccpp_codigo = ccpp_codigo;
    }

    public String getCcpp_centro_poblado() {
        return ccpp_centro_poblado;
    }

    public void setCcpp_centro_poblado(String ccpp_centro_poblado) {
        this.ccpp_centro_poblado = ccpp_centro_poblado;
    }

    public String getCcpp_area() {
        return ccpp_area;
    }

    public void setCcpp_area(String ccpp_area) {
        this.ccpp_area = ccpp_area;
    }

    public String getCcpp_cod_area() {
        return ccpp_cod_area;
    }

    public void setCcpp_cod_area(String ccpp_cod_area) {
        this.ccpp_cod_area = ccpp_cod_area;
    }

    public String getCcpp_categoria() {
        return ccpp_categoria;
    }

    public void setCcpp_categoria(String ccpp_categoria) {
        this.ccpp_categoria = ccpp_categoria;
    }

    public String getCcpp_cod_categoria() {
        return ccpp_cod_categoria;
    }

    public void setCcpp_cod_categoria(String ccpp_cod_categoria) {
        this.ccpp_cod_categoria = ccpp_cod_categoria;
    }
}
