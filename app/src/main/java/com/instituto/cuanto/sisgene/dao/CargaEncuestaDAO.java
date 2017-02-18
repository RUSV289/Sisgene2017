package com.instituto.cuanto.sisgene.dao;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

/**
 * Created by Jesus on 10/11/2015.
 */
public class CargaEncuestaDAO {

    public CargaEncuestaDAO(){}

    public boolean cargarCatalogo(Context context, String cat_grupo, String cat_sub_grupo, String cat_nombre,
                                  String cat_descripcion, String cat_codigo){
            Cursor cursor   = null;
            DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
            String arg[] = {cat_grupo,cat_sub_grupo,cat_nombre,cat_descripcion,cat_codigo};
            boolean response = false;


            try {
                String sql = " INSERT INTO catalogo(cat_grupo,cat_sub_grupo,cat_nombre,cat_descripcion,cat_codigo)" +
                        " VALUES(?,?,?,?,?)";

                dataBaseHelper.db.execSQL(sql,arg);

                System.out.println("CARGO CATALOGO OK");
                response = true;

            } catch (Exception ex) {
                System.out.println("ERROR AL GUARDAR CATALOGOS: "+ex.getMessage());
                ex.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }

            return response;

    }

    public boolean cargarCaratulaEncuesta(Context context, String cae_codigo, String cae_nombre, String cae_descripcion,
                                          String cae_finicio, String cae_ffin, String cae_estado, String cae_tipo_dispositivo,
                                          String cae_numero_encuestas_usu, String cae_logo_empresa, String cae_tot_supervisores,
                                          String cae_id, String cae_b_nencuesta, String cae_b_departamento, String cae_b_provincia,
                                          String cae_b_distrito, String cae_b_ccppnombre, String cae_b_ccppcategoria, String cae_b_nconglomerado,
                                          String cae_b_nzona, String cae_b_nmanzana, String cae_b_nvivienda, String cae_b_nhogar,
                                          String cae_b_area, String cae_b_condicion, String cae_b_avecaljirpas, String cae_b_npuerta,
                                          String cae_b_interior, String cae_b_piso, String cae_b_etasecgru, String cae_b_manzana,
                                          String cae_b_lote, String cae_b_km, String cae_b_nomapeinformante, String cae_b_codinformante,
                                          String cae_b_telcel, String cae_b_referencia, String cae_b_latitud, String cae_b_longitud,
                                          String cae_b_nomencuestador, String cae_b_fecvisita1, String cae_b_fecvisita2, String cae_b_fecvisita3,
                                          String cae_b_coddigitador, String cae_b_maquina,String cae_b_fecdigitacion, String cae_b_codsupervisor,
                                          String cae_b_fecsupervision1, String cae_b_fecsupervision2, String cae_b_tipsupervision1, String cae_b_tipsupervision2,
                                          String cae_b_tipencuesta, String cae_b_observaciones){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {cae_id,cae_codigo,cae_nombre,cae_descripcion,cae_finicio,cae_ffin,cae_estado,
                cae_tipo_dispositivo,cae_numero_encuestas_usu,cae_logo_empresa,cae_tot_supervisores, cae_b_nencuesta, cae_b_departamento, cae_b_provincia, cae_b_distrito, cae_b_ccppnombre, cae_b_ccppcategoria, cae_b_nconglomerado,
                cae_b_nzona, cae_b_nmanzana, cae_b_nvivienda, cae_b_nhogar, cae_b_area, cae_b_condicion, cae_b_avecaljirpas, cae_b_npuerta, cae_b_interior,
                cae_b_piso, cae_b_etasecgru, cae_b_manzana, cae_b_lote, cae_b_km, cae_b_nomapeinformante, cae_b_codinformante, cae_b_telcel, cae_b_referencia,
                cae_b_latitud, cae_b_longitud, cae_b_nomencuestador, cae_b_fecvisita1, cae_b_fecvisita2, cae_b_fecvisita3, cae_b_coddigitador, cae_b_maquina,
                cae_b_fecdigitacion, cae_b_codsupervisor, cae_b_fecsupervision1, cae_b_fecsupervision2, cae_b_tipsupervision1, cae_b_tipsupervision2,
                cae_b_tipencuesta, cae_b_observaciones};
        boolean response = false;


        try {
            String sql = " INSERT INTO caratula_encuesta(cae_id,cae_codigo,cae_nombre,cae_descripcion,cae_finicio,cae_ffin,cae_estado," +
                    " cae_tipo_dispositivo,cae_numero_encuestas_usu,cae_logo_empresa,cae_tot_supervisores, cae_b_nencuesta, cae_b_departamento, cae_b_provincia, " +
                    "cae_b_distrito, cae_b_ccppnombre, cae_b_ccppcategoria, cae_b_nconglomerado, cae_b_nzona, cae_b_nmanzana, cae_b_nvivienda, cae_b_nhogar, cae_b_area, cae_b_condicion, cae_b_avecaljirpas, cae_b_npuerta, cae_b_interior, " +
                    "cae_b_piso, cae_b_etasecgru, cae_b_manzana, cae_b_lote, cae_b_km, cae_b_nomapeinformante, cae_b_codinformante, cae_b_telcel, cae_b_referencia, " +
                    "cae_b_latitud, cae_b_longitud, cae_b_nomencuestador, cae_b_fecvisita1, cae_b_fecvisita2, cae_b_fecvisita3, cae_b_coddigitador, cae_b_maquina, " +
                    "cae_b_fecdigitacion, cae_b_codsupervisor, cae_b_fecsupervision1, cae_b_fecsupervision2, cae_b_tipsupervision1, cae_b_tipsupervision2, " +
                    "cae_b_tipencuesta, cae_b_observaciones) "+
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("CARGO OK CARATULA_ENCUESTA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGAR CARATULA ENCUESTA: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarDet_encuesta(Context context, String dee_id, String cae_id, String ese_id){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {dee_id,cae_id,ese_id};
        boolean response = false;


        try {
            String sql = " INSERT INTO det_encuesta(dee_id,cae_id,ese_id)"+
                    " VALUES(?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("CAERGO OK DET_ENCUESTA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA DET_ENCUESTA : "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarPregunta(Context context, String pre_id, String pre_numero, String pre_enunciado, String pre_explicativo,
                                          String pre_comentario, String pre_guia_rpta, String pre_tipo_rpta, String pre_unico_patron,
                                          String pre_cant_maxima_items, String pre_maxNumRptas, String pre_importaOrdenRptas,
                                          String pre_subtipo, String pre_tipoNumerico, String pre_desde, String pre_hasta, String pat_id, String pre_importa_orden_rptaabmu, String pre_excluye_ABMU){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {pre_id,pre_numero,pre_enunciado,pre_explicativo,pre_comentario,pre_guia_rpta,pre_tipo_rpta,
                pre_unico_patron,pre_cant_maxima_items,pre_maxNumRptas,pre_importaOrdenRptas, pre_subtipo, pre_tipoNumerico, pre_desde,pre_hasta, pat_id, pre_importa_orden_rptaabmu, pre_excluye_ABMU};
        boolean response = false;


        try {
            String sql = " INSERT INTO pregunta(pre_id,pre_numero,pre_enunciado,pre_explicativo,pre_comentario,pre_guia_rpta,pre_tipo_rpta," +
                    " pre_unica_persona,pre_cant_maxima_items,pre_nummaxrptamu,pre_importarordenrptamu, pre_subtipo, pre_tiponumerico, pre_desde, pre_hasta, pat_id, pre_importarordenrptamu, pre_excluye_ABMU)"+
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA PREGUNTA ");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA PREGUNTA: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarSeccion(Context context, String sec_nombre, String sec_nota, String sec_numero_seccion, String sec_id, String sec_categoria, String cae_id, String sec_flag_portada){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {sec_id,sec_nombre,sec_nota,sec_numero_seccion, sec_categoria, cae_id, sec_flag_portada};
        boolean response = false;



        try {
            String sql = " INSERT INTO seccion(sec_id,sec_nombre,sec_nota,sec_numero_seccion, sec_categoria, cae_id, sec_flag_portada)"+
                    " VALUES(?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA PREGUNTA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA SECCION: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarSubseccion(Context context, String sus_nombre, String sus_nota, String sus_numero_seccion, String sus_id, String cae_id, String seccion, String sus_flag_portada){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {sus_id,sus_nombre,sus_nota,sus_numero_seccion, cae_id, seccion, sus_flag_portada};
        boolean response = false;

        System.out.println("ANTES DE GUARDAR SUBSECCION: "+sus_nombre+" - "+sus_nota+" - "+sus_numero_seccion+" - "+sus_id);
        try {
            String sql = " INSERT INTO sub_seccion(sus_id,sus_nombre,sus_nota,sus_numero_subseccion, cae_id, seccion, sus_flag_portada)"+
                    " VALUES(?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA SUBSECCION");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA SUBSECCION: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

   // public boolean cargarSeccionPortada(Context context, String sus_nombre, String sus_nota, String sus_numero_seccion, String sus_id, String cae_id, String seccion, String sus_flag_portada){

    //}

    public boolean cargarEstructura_encuesta(Context context, String sec_id, String sus_id_nivel1, String sus_id_nivel2,
                                             String pre_id, String ese_id){
        System.out.println("PRE_ID _ --> "+pre_id);

        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {ese_id,sec_id,sus_id_nivel1,sus_id_nivel2,pre_id};
        boolean response = false;


        try {
            String sql = " INSERT INTO estructura_encuesta(ese_id,sec_id,sus_id_nivel1,sus_id_nivel2,pre_id)"+
                    " VALUES(?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

           //System.out.println("ERROR AL CARGA ESTRUCTURA ENCUESTA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA ESTRUCTURA_ENCUESTA: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarOpcion(Context context, String opc_id, String opc_nombre){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {opc_id,opc_nombre};
        boolean response = false;

        System.out.println(":::____::::>>>> "+opc_id);
        System.out.println(":::____::::>>>> "+opc_nombre);
        try {
            String sql = " INSERT INTO opcion(opc_id,opc_nombre)"+
                    " VALUES(?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("ok AL CARGA opcion");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA OPCION: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarItem(Context context, String ite_id, String ite_nombre){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {ite_id,ite_nombre};
        boolean response = false;

        System.out.println("ITEEEEM ID : "+ite_id+" -- NOMBRE : "+ite_nombre);


        try {
            String sql = " INSERT INTO item(ite_id,ite_nombre)"+
                    " VALUES(?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA ITEM");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA ITEM: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarPreguntaOpcion(Context context, String pro_id, String pre_id, String opc_id,
                                             String pro_numeralOpcion, String pro_numeroPreguntaSiguiente, String pro_valor, String cae_id, String pro_tipo_dato, String pro_desde,
                                             String pro_hasta, String pro_valida_fila_ma, String pro_parte_rpta, String pro_nombre_variable){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {pro_id,pre_id,opc_id,pro_numeralOpcion,pro_numeroPreguntaSiguiente, pro_valor, cae_id, pro_tipo_dato, pro_desde, pro_hasta, pro_valida_fila_ma, pro_parte_rpta, pro_nombre_variable};
        boolean response = false;
        System.out.println("--##pro_valor  "+ pro_valor);

        try {
            String sql = " INSERT INTO pregunta_opcion(pro_id,pre_id,opc_id,pro_numeralopcion,pro_numeropreguntasiguiente, pro_valor, cae_id, pro_tipo_dato, pro_desde, pro_hasta, pro_valida_fila_ma, pro_parte_rpta, pro_nombre_variable)"+
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA PREGUNTA_OPCION");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA RPEGUNTA_OPCION: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarPreguntaItem(Context context, String pri_id, String pre_id, String ite_id,
                                        String pri_numeralItem, String pri_valor, String pri_tipo_dato, String pri_desde,
                                        String pri_hasta, String pri_sin_encabezado){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {pri_id,pre_id,ite_id,pri_numeralItem, pri_valor, pri_tipo_dato, pri_desde, pri_hasta, pri_sin_encabezado};
        boolean response = false;

        System.out.println("PRI_ID : "+pri_id + " -- PRE_ID : "+pre_id + " -- ITEM_ID : "+ite_id+ " -- PRI_NUMERALITEM : "+pri_numeralItem+"--##pri_valor--  "+pri_valor);

        try {
            String sql = " INSERT INTO pregunta_item(pri_id,pre_id,ite_id,pri_numeralitem, pri_valor, pri_tipo_dato, pri_desde, pri_hasta, pri_sin_encabezado)"+
                    " VALUES(?,?,?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA PREGUNTA_ITEM");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA PREGUNTA_ITEM: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarFuncionalidad(Context context, String fun_id, String fun_nombre, String fun_estado){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {fun_id,fun_nombre,fun_estado};
        boolean response = false;

        try {
            String sql = " INSERT INTO funcionalidad(fun_id,fun_nombre,fun_estado)"+
                    " VALUES(?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA FUNCIONALIDAD");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA FUNCIONALIDAD: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarRol(Context context, String rol_id, String rol_codigo, String rol_descripcion){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {rol_id,rol_codigo,rol_descripcion};
        boolean response = false;


        try {
            String sql = " INSERT INTO rol(rol_id,rol_codigo,rol_descripcion)"+
                    " VALUES(?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL CARGA rROL");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGA ROL: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarAcceso(Context context, String acc_id, String fun_id, String rol_id, String acc_estado){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {acc_id,fun_id,rol_id,acc_estado};
        boolean response = false;


        try {
            String sql = " INSERT INTO acceso(acc_id,fun_id,rol_id,acc_estado)"+
                    " VALUES(?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL ACCESO");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL ACCESO: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarTipoDocumento(Context context, String tip_id, String tip_nombre, String tip_descripcion,
                                        String tip_longitud, String tip_estado, String tip_tipo_caracter){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {tip_id,tip_nombre,tip_descripcion,tip_longitud,tip_estado,tip_tipo_caracter};
        boolean response = false;


        try {
            String sql = " INSERT INTO tipo_documento(tip_id,tip_nombre,tip_descripcion,tip_longitud,tip_estado,tip_tipo_caracter)"+
                    " VALUES(?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL TIPO_DOCU");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL TIPO_DOCU: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarPersona(Context context, String per_id, String id_tipo_doc, String num_documento, String nombres,
                                 String apellido_paterno, String apellido_materno, String telefono, String celular,
                                 String correo, String fecha_nacimiento, String edad, String grado_instruccion,
                                 String estado_civil, String genero){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {per_id,num_documento,nombres,apellido_paterno,apellido_materno,telefono,celular,
                correo,fecha_nacimiento,edad,grado_instruccion,estado_civil,genero,id_tipo_doc};
        boolean response = false;

        try {
            String sql = " INSERT INTO persona(per_id,per_num_documento,per_nombres,per_appaterno,per_apmaterno,per_telefono," +
                    " per_celular,per_correo,per_fnacimiento,per_edad,per_gradinstruccion,per_estadocivil,per_genero,tip_id)"+
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL PERSONA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL PERSONA: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarUsuario(Context context, String usu_id, String usu_usuario, String usu_clave,
                                       String rol_id){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {usu_id,usu_usuario,usu_clave,rol_id};
        boolean response = false;

        System.out.println("CARGADO USUARIO usuid :: "+usu_id+" usu_usuario:: "+usu_usuario);
        try {
            String sql = " INSERT INTO usuario(usu_id,usu_usuario,usu_clave,rol_id)"+
                    " VALUES(?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL USUARIO");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL USUARIO: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    /*public boolean cargarGrupo(Context context, String gru_id, String usu_id_supervisor, String gru_numero,
                                 String gru_tot_encuestadores){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {gru_id,usu_id_supervisor,gru_numero,gru_tot_encuestadores};
        boolean response = false;

        System.out.println("cargando grupo id_grupo:: "+gru_id+ " idsupoervisor:: "+usu_id_supervisor+" grunumero:: "+gru_numero);

        try {
            String sql = " INSERT INTO grupo(gru_id,usu_idsupervisor,gru_numero,gru_tot_encuestadores)"+
                    " VALUES(?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("ok AL GRUPO");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL CARGAR GRUPO: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }
    */

    public boolean cargarDispositivo(Context context, String dis_id, String dis_nombre, String dis_descripcion,
                               String dis_marca, String dis_modelo, String dis_serie){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {dis_id,dis_nombre,dis_descripcion,dis_marca,dis_modelo,dis_serie};
        boolean response = false;


        try {
            String sql = " INSERT INTO dispositivo(dis_id,dis_nombre,dis_descripcion,dis_marca,dis_modelo,dis_serie)"+
                    " VALUES(?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL DISPOSITIVO");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL DISPOSITIVO: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean cargarUsuarioPersona(Context context, String usp_id, String per_id, String usu_id,
                                 String cae_id,
                                 String usp_tot_encRealizadas, String usp_tot_encAsignadas){
        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {usp_id,per_id,usu_id,cae_id,
                usp_tot_encRealizadas,usp_tot_encAsignadas};
        boolean response = false;
        //System.out.println("ID GRUPO DE USUARIOPERSONA :: " + gru_id + "usuid:: "+usu_id);

        try {
            String sql = " INSERT INTO usuario_persona(usp_id,per_id,usu_id, " +
                    " cae_id ,usp_tot_encrealizadas,usp_tot_encasignadas)"+
                    " VALUES(?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL USUARIO_PERONA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL USUARIO_PERSONA: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

    public boolean cargarCcpp(Context context, String ccpp_id, String ubi_id, String ccpp_codigo, String ccpp_centro_poblado, String ccpp_area, String ccpp_cod_area, String ccpp_categoria, String ccpp_cod_categoria){

        Cursor cursor   = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {ccpp_id, ubi_id, ccpp_codigo, ccpp_centro_poblado, ccpp_area, ccpp_cod_area, ccpp_categoria, ccpp_cod_categoria};
        boolean response = false;
        System.out.println("Cargando un CCPP");

        try {
            String sql = " INSERT INTO ccpp(ccpp_id, ubi_id, ccpp_codigo, ccpp_centro_poblado, ccpp_area, ccpp_cod_area, ccpp_categoria, ccpp_cod_categoria) " +
                "VALUES (?,?,?,?,?,?,?,?)";
            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL USUARIO_PERONA");
            response = true;
        } catch (Exception ex) {
            System.out.println("ERROR AL USUARIO_PERSONA: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

    public boolean cargarUpc(Context context, String upc_id, String ccpp_id, String dis_id, String usp_id_enc, String usp_id_sup, String cae_id, String upc_conglomerado, String upc_tot_enc_congl, String upc_desde_numenc, String upc_hasta_numenc, String upc_categoria, String upc_cod_categoria){

        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {upc_id, ccpp_id, dis_id, usp_id_enc, usp_id_sup, cae_id, upc_conglomerado, upc_tot_enc_congl, upc_desde_numenc, upc_hasta_numenc, upc_categoria, upc_cod_categoria};
        boolean response = false;
        System.out.println("Cargando un UPC");
        try {
            String sql = " INSERT INTO usu_per_ccpp(upc_id, ccpp_id, dis_id, usp_id_enc, usp_id_sup, cae_id, upc_conglomerado, upc_tot_enc_congl, upc_desde_numenc, upc_hasta_numenc, upc_categoria, upc_cod_categoria) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            dataBaseHelper.db.execSQL(sql,arg);

            System.out.println("OK AL UPC");
            response = true;
        }catch (Exception ex) {
            System.out.println("ERROR AL cargarUpc "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

    public  boolean cargarCampoopcion(Context context, String cao_id, String cap_id, String cao_codigo, String cao_opcion){
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {cao_id, cap_id, cao_codigo, cao_opcion};
        boolean response = false;
        System.out.println("Cargando un Campoopcion");
        try{
            String sql = " INSERT INTO campo_opcion(cao_id, cap_id, cao_codigo, cao_opcion) "+
                    " VALUES (?,?,?,?)";
            dataBaseHelper.db.execSQL(sql, arg);
            System.out.println("OK AL CAMPOOPCION");
            response = true;
        }catch (Exception ex){
            System.out.println("ERROR AL cargarCampoopcion "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

    public boolean cargarCampoportada(Context context, String cap_id, String sec_id, String sus_id, String cae_id, String cap_numero, String cap_descripcion){
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {cap_id, sec_id, sus_id, cae_id, cap_numero, cap_descripcion};
        boolean response = false;
        System.out.println("Cargando un campoPortada");
        try{
            String sql = " INSERT INTO campo_portada(cap_id, sec_id, sus_id, cae_id, cap_numero, cap_descripcion) "+
                    " VALUES (?,?,?,?,?,?)";
            dataBaseHelper.db.execSQL(sql, arg);
            System.out.println("OK AL CARGAR CAMPOPORTADA");
            response = true;
        }catch(Exception ex){
            System.out.println("ERROR AL campoPortada "+ex.getMessage());
            ex.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

    public boolean cargarCeldamatriz(Context context, String cem_id, String pro_id, String pri_id, String cem_fila, String cem_columna, String cem_tipo_dato, String cem_desde, String cem_hasta){
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {cem_id, pro_id, pri_id, cem_fila, cem_columna, cem_tipo_dato, cem_desde, cem_hasta};
        boolean response = false;
        System.out.println("Cargando un cargarCeldamatriz");
        try{
            String sql = " INSERT INTO celda_matriz(cem_id, pro_id, pri_id, cem_fila, cem_columna, cem_tipo_dato, cem_desde, cem_hasta) "+
                    " VALUES (?,?,?,?,?,?,?,?)";

            dataBaseHelper.db.execSQL(sql, arg);
            System.out.println("OK AL CARGAR CELDAMATRIZ");
            response = true;
        }catch(Exception ex){
            System.out.println("ERROR AL CELDAMATRIZ "+ex.getMessage());
            ex.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

    public boolean cargarPatron(Context context, String pat_id, String pat_descripcion, String pat_max_items){
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {pat_id, pat_descripcion, pat_max_items};
        boolean response = false;
        System.out.println("Cargando un cargarPatron");
        try{
            String sql = " INSERT INTO celda_matriz(pat_id, pat_descripcion, pat_max_items) "+
                    " VALUES (?,?,?)";

            dataBaseHelper.db.execSQL(sql, arg);
            System.out.println("OK AL CARGAR PATRON");
            response = true;
        }catch(Exception ex){
            System.out.println("ERROR AL PATRON "+ex.getMessage());
            ex.printStackTrace();
        }finally {
            if (cursor != null)
                cursor.close();
        }

        return response;
    }

}
