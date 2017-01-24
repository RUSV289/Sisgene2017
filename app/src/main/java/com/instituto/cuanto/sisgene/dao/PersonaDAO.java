package com.instituto.cuanto.sisgene.dao;

import android.content.Context;
import android.database.Cursor;

import com.instituto.cuanto.sisgene.entidad.Allegado;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesus on 08/11/2015.
 */
public class PersonaDAO {

    public PersonaDAO() {
    }

    public boolean actualizarPersona(Context context, String nombre, String appaterno, String apmaterno,
                                     String numdoc, String telefono, String celular, String correo, int per_id) {
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String idPers = per_id + "";
        String arg[] = {nombre, appaterno, apmaterno, numdoc, telefono, celular, correo, idPers};
        boolean response = false;

        System.out.println("IDM PERSONA : " + per_id);

        try {
            String sql = " UPDATE persona " +
                    " SET per_nombres = ?, " +
                    " per_appaterno = ?, " +
                    " per_apmaterno = ?, " +
                    " per_num_documento = ?, " +
                    " per_telefono = ?, " +
                    " per_celular = ?, " +
                    " per_correo = ? " +
                    " WHERE per_id = ?";

            dataBaseHelper.db.execSQL(sql, arg);

            response = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return response;

    }

    public boolean insertarPersona(Context context, String nombre, String appaterno, String apmaterno,
                                   String numdoc, String telefono, String celular, String correo) {
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {nombre, appaterno, apmaterno, numdoc, telefono, celular, correo, "1"};
        boolean response = false;

        try {
            String sql = " INSERT INTO persona (per_nombres,per_appaterno,per_apmaterno,per_num_documento,per_telefono,per_celular,per_correo,tip_id)" +
                    " VALUES(?,?,?,?,?,?,?,?)";
            System.out.println("insertarPersona: sql: " + sql);
            dataBaseHelper.db.execSQL(sql, arg);
            response = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        System.out.println("insertarPersona: response:" + response);
        return response;
    }

    public int obtenerUltIdPersona(Context context) {

        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        int response = 0;

        try {

            cursor = dataBaseHelper.db.rawQuery(" select per.per_id" +
                    " from persona per" +
                    " order by per.per_id desc", null);

            if (cursor.moveToFirst()) {

                response = cursor.getInt(0);

            }
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        System.out.println("obtenerUltIdPersona: id:" + response);
        return 0;

    }

    public int obtenerIdPersonabyNombres(Context context, String nombre, String appaterno, String apmaterno) {

        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        int response = 0;
        String arg[] = {nombre, appaterno, apmaterno};

        String sql = " SELECT alle.all_id " +
                " from allegado alle " +
                " where alle.all_nombres = ? " +
                " and alle.all_appaterno = ? " +
                " and alle.all_apmaterno = ?";

        try {
            cursor = dataBaseHelper.db.rawQuery(sql, arg);

            if (cursor.moveToFirst()) {
                response = cursor.getInt(1);
            }

            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return -1;

    }

    public boolean insertarAllegado(Context context, String nombre, String appaterno, String apmaterno, String caer_id) {
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        String arg[] = {nombre, appaterno, apmaterno, caer_id};
        boolean response = false;

        try {
            String sql = " INSERT INTO allegado (all_nombre,all_appaterno,all_apmaterno,caer_id)" +
                    " VALUES(?,?,?,?)";

            dataBaseHelper.db.execSQL(sql, arg);

            response = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        System.out.println("insertarAllegado: response: " + response+"  nombre"+ nombre);
        return response;
    }

    public int obtenerUltIdAlle(Context context) {
        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);

        int response = 0;

        try {
            cursor = dataBaseHelper.db.rawQuery("select alle.all_id from allegado alle order by alle.all_id desc", null);
            if (cursor.moveToFirst()) {
                response = cursor.getInt(0);
            }
            System.out.println("obtenerUltIdAlle: OK, id: " + response);
            return response;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        System.out.println("obtenerUltIdAlle: ERROR ");
        return 0;


    }



    public boolean modificarCodigoIdentificacioAllegado(Context context, String nombre, String apellidoPat, String apellidoMat, String newValorcodigoIdentificacion) {

        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        boolean response = false;


        String sql = "UPDATE allegado SET all_codigo_identificacion = '"+newValorcodigoIdentificacion+"'"+
                      " where all_nombre = '"+nombre+"' "+
                      " and all_appaterno = '"+apellidoPat+"' "+
                      " and  all_apmaterno = '"+apellidoMat+"'";

        try {

            dataBaseHelper.db.execSQL(sql);

            response = true;
            System.out.println("UPDATE CODIGO IDENTIFICACION OK EXITOSO2");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return response;
    }

    public List<Allegado> obtenerAllegadoEncuestada(Context context,String idCabEnc) {

        Cursor cursor = null;
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<Allegado> listAllegado = new ArrayList<Allegado>();
        Allegado allegado = null;

        String sql = " select alle.all_codigo_identificacion,alle.all_nombre,alle.all_appaterno, " +
                " alle.all_apmaterno,alle.all_gradofamiliaridad " +
                " from cab_enc_rpta caer " +
                " inner join allegado alle on caer.caer_id = alle.caer_id " +
                " where caer.caer_id = "+idCabEnc;

        try {

            cursor = dataBaseHelper.db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    allegado = new Allegado();
                    allegado.setCodigo_identificacion((cursor.getString(0) != null) ? cursor.getString(0) : "");
                    allegado.setNombres((cursor.getString(1) != null) ? cursor.getString(1) : "");
                    allegado.setApellido_paterno((cursor.getString(2) != null) ? cursor.getString(2) : "");
                    allegado.setApellido_materno((cursor.getString(3) != null) ? cursor.getString(3) : "");
                    allegado.setGrado_familiaridad((cursor.getString(4) != null) ? cursor.getString(4) : "");

                    listAllegado.add(allegado);
                    System.out.println("lista allegados: " + allegado.toString());
                } while (cursor.moveToNext()) ;

            }
            System.out.println("OKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK");
            return listAllegado;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        System.out.println("nullllllllllllllllllllllllllllllllllll");
        return null;
    }

}
