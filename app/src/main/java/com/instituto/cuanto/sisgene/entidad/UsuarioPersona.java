
package com.instituto.cuanto.sisgene.entidad;

/**
 *
 * @author JMonzalve
 */
public class UsuarioPersona {
    private String usp_id;
    private String per_id;
    private String usu_id;
    private String cae_id;
    private String usp_tot_encRealizadas;
    private String usp_tot_encAsignadas;
    
    public UsuarioPersona(){
        //Constructor de la clase UsuarioPersona
    }

    public String getUsp_id() {
        return usp_id;
    }

    public void setUsp_id(String usp_id) {
        this.usp_id = usp_id;
    }

    public String getPer_id() {
        return per_id;
    }

    public void setPer_id(String per_id) {
        this.per_id = per_id;
    }

    public String getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(String usu_id) {
        this.usu_id = usu_id;
    }

    public String getCae_id() {
        return cae_id;
    }

    public void setCae_id(String cae_id) {
        this.cae_id = cae_id;
    }

    public String getUsp_tot_encRealizadas() {
        return usp_tot_encRealizadas;
    }

    public void setUsp_tot_encRealizadas(String usp_tot_encRealizadas) {
        this.usp_tot_encRealizadas = usp_tot_encRealizadas;
    }

    public String getUsp_tot_encAsignadas() {
        return usp_tot_encAsignadas;
    }

    public void setUsp_tot_encAsignadas(String usp_tot_encAsignadas) {
        this.usp_tot_encAsignadas = usp_tot_encAsignadas;
    }
}
