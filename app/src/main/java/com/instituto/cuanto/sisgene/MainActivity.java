package com.instituto.cuanto.sisgene;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.instituto.cuanto.sisgene.bean.Encuestador;
import com.instituto.cuanto.sisgene.bean.Usuarios;
import com.instituto.cuanto.sisgene.dao.CargaEncuestaDAO;
import com.instituto.cuanto.sisgene.dao.UsuarioDAO;
import com.instituto.cuanto.sisgene.entidad.Acceso;
import com.instituto.cuanto.sisgene.entidad.Campoopcion;
import com.instituto.cuanto.sisgene.entidad.Campoportada;
import com.instituto.cuanto.sisgene.entidad.CaratulaEncuesta;
import com.instituto.cuanto.sisgene.entidad.Catalogo;
import com.instituto.cuanto.sisgene.entidad.Ccpp;
import com.instituto.cuanto.sisgene.entidad.Celdamatriz;
import com.instituto.cuanto.sisgene.entidad.DetalleEncuesta;
import com.instituto.cuanto.sisgene.entidad.Dispositivo;
import com.instituto.cuanto.sisgene.entidad.EstructuraEncuesta;
import com.instituto.cuanto.sisgene.entidad.Funcionalidad;
import com.instituto.cuanto.sisgene.entidad.Item;
import com.instituto.cuanto.sisgene.entidad.Opcion;
import com.instituto.cuanto.sisgene.entidad.Patron;
import com.instituto.cuanto.sisgene.entidad.Persona;
import com.instituto.cuanto.sisgene.entidad.Pregunta;
import com.instituto.cuanto.sisgene.entidad.PreguntaItem;
import com.instituto.cuanto.sisgene.entidad.PreguntaOpcion;
import com.instituto.cuanto.sisgene.entidad.Rol;
import com.instituto.cuanto.sisgene.entidad.Seccion;
import com.instituto.cuanto.sisgene.entidad.SubSeccion;
import com.instituto.cuanto.sisgene.entidad.TipoDocumento;
import com.instituto.cuanto.sisgene.entidad.Upc;
import com.instituto.cuanto.sisgene.entidad.Usuario;
import com.instituto.cuanto.sisgene.entidad.UsuarioPersona;
import com.instituto.cuanto.sisgene.forms.ValidarAdministradorRequest;
import com.instituto.cuanto.sisgene.forms.ValidarAdministradorResponse;
import com.instituto.cuanto.sisgene.util.Criptografo;
import com.instituto.cuanto.sisgene.util.LeerProperties;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.io.File;
import android.os.Environment;

import retrofit.Callback;


import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    Button btnAceptar, btnCerrar;
    EditText etNombreUsuario, etClave, etCodigoEncuesta;
    TextView tvNombreUsuarioError, tvClaveError, tvErroLogin;
    Spinner listaRol;
    LinearLayout lyCodigo;
    Gson gson = new Gson();
    String ip="",puerto="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAceptar = (Button) findViewById(R.id.btnAceptar);
        btnCerrar = (Button) findViewById(R.id.btnCerrar);
        etNombreUsuario = (EditText) findViewById(R.id.etNombreUsuario);
        etClave = (EditText) findViewById(R.id.etClave);
        etCodigoEncuesta = (EditText) findViewById(R.id.etCodigoEncuesta);
        tvNombreUsuarioError = (TextView) findViewById(R.id.tvNombreUsuarioError);
        tvClaveError = (TextView) findViewById(R.id.tvClaveError);
        listaRol = (Spinner) findViewById(R.id.spRol);
        lyCodigo = (LinearLayout) findViewById(R.id.lyNombreEncuesta);

        listaRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 2) { // Si es el usuario se le pedira el codigo de la Encuesta
                    lyCodigo.setVisibility(View.VISIBLE);
                } else {
                    lyCodigo.setVisibility(View.INVISIBLE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }

        });

        btnAceptar.setOnClickListener(btnAceptarsetOnClickListener);
        btnCerrar.setOnClickListener(btnCerrarsetOnClickListener);

        //leerRUTA();
    }

    public void leerRUTA(){

        File ruta_sd = Environment.getExternalStorageDirectory();

        System.out.println("ruta sd : --->>> "+ruta_sd.toString());
        System.out.println("ruta sd : --->>> "+ruta_sd.getAbsolutePath());
        System.out.println("ruta sd : --->>> "+ruta_sd.getPath());
    }

    View.OnClickListener btnAceptarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean camposOK = true;
            boolean loginOK = false;

            tvNombreUsuarioError.setText("");
            tvClaveError.setText("");

            if (etNombreUsuario.getText().toString().trim().length() == 0) {
                tvNombreUsuarioError.setText(getResources().getString(R.string.login_user_error));
                camposOK = false;
            }
            if (etClave.getText().toString().trim().length() == 0) {
                tvClaveError.setText(getResources().getString(R.string.login_pass_error));
                camposOK = false;
            }

            String rolAcceso = listaRol.getSelectedItem().toString();
            UsuarioDAO usuarioDAO = new UsuarioDAO();

            if (camposOK) {
                int cantidadData = usuarioDAO.obtenerCantidadUsuarios(MainActivity.this);

                if (cantidadData == 0) {
                    if (rolAcceso.equals("ADMINISTRADOR")) {

                        LeerProperties leerProperties = new LeerProperties();
                        String ipWS = leerProperties.leerIPWS();
                        String puertoWS = leerProperties.leerPUERTOWS();

                        System.out.println("IP : "+ipWS + " -- PUERTO : "+ puertoWS);
                        //ip = ipWS;
                        //puerto = puertoWS;


                        //if(ipWS != null && puertoWS != null){
                            new RestCosumeAsyncTask().execute();
                        //}else{
                        //    Toast.makeText(MainActivity.this, "No se encuentra el archivo de configuracion", Toast.LENGTH_LONG).show();
                        //}

                    }else{
                        Toast.makeText(MainActivity.this, "Tablet no cargada, porfavor utilizar un usuario ADMINISTRADOR", Toast.LENGTH_LONG).show();
                    }

                }else{
                    System.out.println("YA HAY DATA");
                    String nomUsu = etNombreUsuario.getText().toString().trim();
                    String clvUsu = etClave.getText().toString().trim();

                    if (!rolAcceso.equals("ADMINISTRADOR")) {

                        Criptografo criptografo = new Criptografo();

                        Usuarios usuario = usuarioDAO.obtenerUsuario(MainActivity.this, nomUsu, rolAcceso);

                        if (usuario != null) {
                            String claveUsu = criptografo.desencripta(usuario.getClave());

                            if (clvUsu.equals(claveUsu)) {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("user", nomUsu);
                                editor.putString("nombres", usuario.getNombre() + " " + usuario.getAp_paterno() + " " + usuario.getAp_materno());
                                editor.putString("rol", rolAcceso);
                                editor.commit();

                                Toast.makeText(MainActivity.this, "BIENVENIDO " + usuario.getNombre() + " " + usuario.getAp_paterno() + " " + usuario.getAp_materno(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                                startActivity(intent);

                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Usuario y/o contraseña incorrectos", Toast.LENGTH_LONG).show();
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "Usuario Administrador sin acceso", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    View.OnClickListener btnCerrarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //finish();
            finishAffinity();

        }
    };


    private class RestCosumeAsyncTask extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        String nomUsu = etNombreUsuario.getText().toString().trim();
        String clvUsu = etClave.getText().toString().trim();
        String codEncuesta = etCodigoEncuesta.getText().toString().trim();

        @Override
        protected Void doInBackground(String... params) {

            ValidarAdministradorRequest validarRequest = new ValidarAdministradorRequest();
            validarRequest.setUsuario(nomUsu);
            validarRequest.setClave(clvUsu);
            validarRequest.setCodigo_encuesta(codEncuesta);

            String jsonEnviar = gson.toJson(validarRequest);

            try {
                String a = URLEncoder.encode(jsonEnviar, "UTF-8");
                System.out.println("\n\nJSON CODIFICADO : "+a);
                jsonEnviar = a;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            //Servidor cuanto, producción
            //String ip="190.40.162.59";
            //String puerto="8085";

            //Servidor local, desarollo
            String ip="192.168.1.107";
            String puerto="8080";

            RestAdapter restAdapter = new RestAdapter.Builder()
                    //Endpoint anterior
                    //.setEndpoint("http://"+ip+":"+puerto+"/WSSisgene/WebServiceSISGENE")
                    .setEndpoint("http://"+ip+":"+puerto+"/sisgene/api/auth")

            .build();

            ClienteService service = restAdapter.create(ClienteService.class);

            service.repositorySync(jsonEnviar, new Callback<ValidarAdministradorResponse>() {
                @Override
                public void success(ValidarAdministradorResponse validarAdministradorResponse, Response response) {
                    String jsonInput = gson.toJson(validarAdministradorResponse);
                    System.out.println("JSON LLEGADA : "+jsonInput);

                    if(validarAdministradorResponse.getCodigo_respuesta().equals("01")) {
                        progressDialog.hide();
                        Toast.makeText(MainActivity.this, validarAdministradorResponse.getMensaje(), Toast.LENGTH_LONG).show();
                    }else {
                        cargarEnBD(validarAdministradorResponse);
                        limpiarCampos();
                        progressDialog.hide();
                        Toast.makeText(MainActivity.this, "Carga de información con éxito", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(MainActivity.this, "Ocurrio un error en la carga de Información, verifique su conexión a Internet", Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            });

            System.out.println("TERMINANDO DE EJECUTAR");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("PREEXECUTE");
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Cargando información en tablet");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            System.out.println("POSTEXCECUTE");
            //progressDialog.hide();
        }
    }

    public void limpiarCampos() {
        etNombreUsuario.setText("");
        etClave.setText("");
        etCodigoEncuesta.setText("");
    }

    public void cargarEnBD(ValidarAdministradorResponse validarResponse) {
/*
        System.out.println("LISTA ACCESO : "+validarResponse.getLista_acceso().size());
        System.out.println("LISTA TIPO DOC: "+validarResponse.getLista_tipodocumento().size());
        System.out.println("LIST USUARIO :  : "+validarResponse.getLista_usuario().size());
*/
        try {

            CargaEncuestaDAO cargaDAO = new CargaEncuestaDAO();

            List<Catalogo> listaCatalogos = validarResponse.getLista_Catalogo();
            for (Catalogo catalogo : listaCatalogos) {
                cargaDAO.cargarCatalogo(MainActivity.this, catalogo.getCat_grupo(), catalogo.getCat_sub_grupo(),
                        catalogo.getCat_nombre(), catalogo.getCat_descripcion(), catalogo.getCat_codigo());
            }

            List<CaratulaEncuesta> listCaratulaEnc = validarResponse.getLista_caratula_encuesta();
            for (CaratulaEncuesta caraenc : listCaratulaEnc) {
                cargaDAO.cargarCaratulaEncuesta(MainActivity.this, caraenc.getCae_codigo(), caraenc.getCae_nombre(), caraenc.getCae_descripcion(),
                        caraenc.getCae_finicio(), caraenc.getCae_ffin(), caraenc.getCae_estado(), caraenc.getCae_tipo_dispositivo(),
                        caraenc.getCae_numero_encuestas_usu(), caraenc.getCae_logo_empresa(), caraenc.getCae_tot_supervisores(), caraenc.getCae_id(),
                        caraenc.getCae_b_nencuesta(), caraenc.getCae_b_departamento(), caraenc.getCae_b_provincia(), caraenc.getCae_b_distrito(), caraenc.getCae_b_ccppnombre(),
                        caraenc.getCae_b_ccppcategoria(), caraenc.getCae_b_nconglomerado(), caraenc.getCae_b_nzona(), caraenc.getCae_b_nmanzana(), caraenc.getCae_b_nvivienda(),
                        caraenc.getCae_b_nhogar(), caraenc.getCae_b_area(), caraenc.getCae_b_condicion(), caraenc.getCae_b_avecaljirpas(), caraenc.getCae_b_npuerta(),
                        caraenc.getCae_b_interior(), caraenc.getCae_b_piso(), caraenc.getCae_b_etasecgru(), caraenc.getCae_b_manzana(), caraenc.getCae_b_lote(),
                        caraenc.getCae_b_km(), caraenc.getCae_b_nomapeinformante(), caraenc.getCae_b_codinformante(), caraenc.getCae_b_telcel(), caraenc.getCae_b_referencia(),
                        caraenc.getCae_b_latitud(), caraenc.getCae_b_longitud(), caraenc.getCae_b_nomencuestador(), caraenc.getCae_b_fecvisita1(), caraenc.getCae_b_fecvisita2(),
                        caraenc.getCae_b_fecvisita3(), caraenc.getCae_b_coddigitador(), caraenc.getCae_b_maquina(), caraenc.getCae_b_fecdigitacion(),
                        caraenc.getCae_b_codsupervisor(), caraenc.getCae_b_fecsupervision1(), caraenc.getCae_b_fecsupervision2(), caraenc.getCae_b_tipsupervision1(),
                        caraenc.getCae_b_tipsupervision2(), caraenc.getCae_b_tipencuesta(), caraenc.getCae_b_observaciones());
            }

            List<Pregunta> listPregunta = validarResponse.getLista_pregunta();
            for (Pregunta pregunta : listPregunta) {
                cargaDAO.cargarPregunta(MainActivity.this, pregunta.getPre_id(), pregunta.getPre_numero(), pregunta.getPre_enunciado(),
                        pregunta.getPre_explicativo(), pregunta.getPre_comentario(), pregunta.getPre_guia_rpta(), pregunta.getPre_tipo_rpta(),
                        pregunta.getPre_unico_patron(), pregunta.getPre_cant_maxima_items(), pregunta.getPre_maxNumRptas(), pregunta.getPre_importaOrdenRptas(),
                        pregunta.getPre_subtipo(), pregunta.getPre_tiponumerico(), pregunta.getPre_desde(), pregunta.getPre_hasta(), pregunta.getPat_id(), pregunta.getPre_importa_orden_rptaABMU(), pregunta.getPre_excluye_ABMU());
                System.out.println("REVIREST-- " + pregunta.getPre_enunciado() + " - " + pregunta.getPre_subtipo() + " - " + pregunta.getPre_tiponumerico() + " - " +
                        pregunta.getPre_desde() + " - " + pregunta.getPre_hasta());
            }

            List<Seccion> listaSeccion = validarResponse.getLista_seccion_cuerpo();
            for (Seccion seccion : listaSeccion) {
                cargaDAO.cargarSeccion(MainActivity.this, seccion.getSec_nombre(), seccion.getSec_nota(),
                        seccion.getSec_numero_seccion(), seccion.getSec_id(), seccion.getSec_categoria(), seccion.getCae_id(), seccion.getSec_flag_portada());
            }

            List<SubSeccion> lisSubseccion = validarResponse.getLista_sub_seccion_cuerpo();
            System.out.println("SUBSECCION TAMAÑO lisSubseccion: "+lisSubseccion.size());
            for (SubSeccion subseccion : lisSubseccion) {
                cargaDAO.cargarSubseccion(MainActivity.this, subseccion.getSus_nombre(), subseccion.getSus_nota(), subseccion.getSus_numero_subseccion(), subseccion.getSus_id(), subseccion.getCae_id(), subseccion.getSeccion(), subseccion.getSus_flag_portada());
                System.out.println("SUBSECCION PARA BD: " + subseccion.getSus_nombre() + " " + subseccion.getSus_numero_subseccion());
            }

            List<Seccion> lisSeccionPortada = validarResponse.getLista_seccion_portada();
            System.out.println("SUBSECCION PORTADA lisSeccionPortada: "+ lisSeccionPortada.size());
            for(Seccion seccionPortada : lisSeccionPortada){
                cargaDAO.cargarSeccion(MainActivity.this, seccionPortada.getSec_nombre(), seccionPortada.getSec_nota(),
                        seccionPortada.getSec_numero_seccion(), seccionPortada.getSec_id(), seccionPortada.getSec_categoria(), seccionPortada.getCae_id(), seccionPortada.getSec_flag_portada());
            }

            List<SubSeccion> lisSubseccionPortada = validarResponse.getLista_sub_seccion_portada();
            System.out.println("SUBSECCION TAMAÑO lisSubseccionPortada: "+lisSubseccionPortada.size());
            for (SubSeccion subseccion : lisSubseccionPortada) {
                cargaDAO.cargarSubseccion(MainActivity.this, subseccion.getSus_nombre(), subseccion.getSus_nota(), subseccion.getSus_numero_subseccion(), subseccion.getSus_id(), subseccion.getCae_id(), subseccion.getSeccion(), subseccion.getSus_flag_portada());
                System.out.println("SUBSECCION PARA BD: " + subseccion.getSus_nombre() + " " + subseccion.getSus_numero_subseccion());
            }


            List<EstructuraEncuesta> listaEstEnc = validarResponse.getLista_estructura_encuesta();
            for (EstructuraEncuesta estEnc : listaEstEnc) {
                System.out.println("PRE_ID MAIN : "+ estEnc.getPre_id());
                cargaDAO.cargarEstructura_encuesta(MainActivity.this, estEnc.getSec_id(), estEnc.getSus_id_nivel1(),
                        estEnc.getSus_id_nivel2(), estEnc.getPre_id(), estEnc.getEse_id());
            }


            List<DetalleEncuesta> listDetalleEnc = validarResponse.getLista_det_encuesta();
            for (DetalleEncuesta detEnc : listDetalleEnc) {
                cargaDAO.cargarDet_encuesta(MainActivity.this, detEnc.getDee_id(), detEnc.getCae_id(), detEnc.getEse_id());
            }

            List<Opcion> listOpcion = validarResponse.getLista_opcion();
            for (Opcion opcion : listOpcion) {

                cargaDAO.cargarOpcion(MainActivity.this, opcion.getOpc_id(), opcion.getOpc_nombre());
            }

            List<Item> listItem = validarResponse.getLista_item();
            for (Item item : listItem) {
                cargaDAO.cargarItem(MainActivity.this, item.getIte_id(), item.getIte_nombre());
            }


            List<PreguntaOpcion> listPReguntaOpc = validarResponse.getLista_pregunta_opcion();
            for (PreguntaOpcion preguntaOpcion : listPReguntaOpc) {
                cargaDAO.cargarPreguntaOpcion(MainActivity.this, preguntaOpcion.getPro_id(), preguntaOpcion.getPre_id(),
                        preguntaOpcion.getOpc_id(), preguntaOpcion.getPro_numeralOpcion(), preguntaOpcion.getPro_numeroPreguntaSiguiente(), preguntaOpcion.getPro_valor(),
                        preguntaOpcion.getCae_id(), preguntaOpcion.getPro_tipo_dato(), preguntaOpcion.getPro_desde(), preguntaOpcion.getPro_hasta(), preguntaOpcion.getPro_valida_fila_MA(),
                        preguntaOpcion.getPro_parte_rpta(),preguntaOpcion.getPro_nombre_variable());
            }

            List<PreguntaItem> listPreguntaItem = validarResponse.getLista_pregunta_item();
            for (PreguntaItem preguntaItem : listPreguntaItem) {
                cargaDAO.cargarPreguntaItem(MainActivity.this, preguntaItem.getPri_id(), preguntaItem.getPre_id(), preguntaItem.getIte_id(),
                        preguntaItem.getPri_numeralItem(),  preguntaItem.getPri_valor(), preguntaItem.getPri_tipo_dato(), preguntaItem.getPri_desde(),
                        preguntaItem.getPri_hasta(),preguntaItem.getPri_sin_encabezado());
            }

            List<Funcionalidad> listFuncionalidad = validarResponse.getLista_funcionalidad();
            for (Funcionalidad funcionalidad : listFuncionalidad) {
                cargaDAO.cargarFuncionalidad(MainActivity.this, funcionalidad.getFun_id(), funcionalidad.getFun_nombre(), funcionalidad.getFun_estado());
            }

            List<Rol> listaRoles = validarResponse.getLista_rol();
            for (Rol rol : listaRoles) {
                cargaDAO.cargarRol(MainActivity.this, rol.getRol_id(), rol.getRol_codigo(), rol.getRol_descripcion());
            }

            List<Acceso> listAcceso = validarResponse.getLista_acceso();
            for (Acceso acceso : listAcceso) {
                cargaDAO.cargarAcceso(MainActivity.this, acceso.getAcc_id(), acceso.getFun_id(), acceso.getRol_id(), acceso.getAcc_estado());
            }

            List<TipoDocumento> listTipoDoc = validarResponse.getLista_tipodocumento();
            for (TipoDocumento tipodoc : listTipoDoc) {
                cargaDAO.cargarTipoDocumento(MainActivity.this, tipodoc.getTip_id(), tipodoc.getTip_nombre(), tipodoc.getTip_descripcion(),
                        tipodoc.getTip_longitud(), tipodoc.getTip_estado(), tipodoc.getTip_tipo_caracter());
            }

            List<Persona> listPersona = validarResponse.getLista_persona();
            for (Persona persona : listPersona) {
                cargaDAO.cargarPersona(MainActivity.this, persona.getPer_id(), persona.getId_tipo_doc(), persona.getNum_documento(),
                        persona.getNombres(), persona.getApellido_paterno(), persona.getApellido_materno(), persona.getTelefono(),
                        persona.getCelular(), persona.getCorreo(), persona.getFecha_nacimiento(), persona.getEdad(), persona.getGrado_instruccion(),
                        persona.getEstado_civil(), persona.getGenero());
            }

            List<Usuario> listUsuarios = validarResponse.getLista_usuario();
            for (Usuario usuario : listUsuarios) {
                cargaDAO.cargarUsuario(MainActivity.this, usuario.getUsu_id(), usuario.getUsu_usuario(), usuario.getUsu_clave(),
                        usuario.getRol_id());
            }

            List<Dispositivo> listaDispositivo = validarResponse.getLista_dispositivo();
            for (Dispositivo dispositivo : listaDispositivo) {
                cargaDAO.cargarDispositivo(MainActivity.this, dispositivo.getDis_id(), dispositivo.getDis_nombre(), dispositivo.getDis_descripcion(),
                        dispositivo.getDis_marca(), dispositivo.getDis_modelo(), dispositivo.getDis_serie());
            }

            List<UsuarioPersona> listUsuarioPersonas = validarResponse.getLista_usuariopersona();
            for (UsuarioPersona usuper : listUsuarioPersonas) {
                cargaDAO.cargarUsuarioPersona(MainActivity.this, usuper.getUsp_id(), usuper.getPer_id(), usuper.getUsu_id(),
                        usuper.getCae_id(),
                        usuper.getUsp_tot_encRealizadas(), usuper.getUsp_tot_encAsignadas());
            }

            List<Ccpp> lisCcpp = validarResponse.getLista_ccpp();
            for(Ccpp ccpp : lisCcpp){
                cargaDAO.cargarCcpp(MainActivity.this, ccpp.getCcpp_id(), ccpp.getUbi_id(), ccpp.getCcpp_codigo(),
                        ccpp.getCcpp_centro_poblado(), ccpp.getCcpp_area(), ccpp.getCcpp_cod_area(), ccpp.getCcpp_categoria(),
                        ccpp.getCcpp_cod_categoria());
            }

            List<Upc> lisUpc = validarResponse.getLista_upc();
            for(Upc upc : lisUpc){
                cargaDAO.cargarUpc(MainActivity.this, upc.getUpc_id(), upc.getCcpp_id(), upc.getDis_id(), upc.getUsp_id_enc(),
                        upc.getUsp_id_sup(), upc.getCae_id(), upc.getUpc_conglomerado(), upc.getUpc_tot_enc_congl(),
                        upc.getUpc_desde_numenc(), upc.getUpc_hasta_numenc(), upc.getUpc_categoria(),upc.getUpc_cod_categoria());
            }

            List<Campoopcion> lisCampoopcion = validarResponse.getLista_campoopcion();
            for(Campoopcion campoopcion : lisCampoopcion){
                cargaDAO.cargarCampoopcion(MainActivity.this, campoopcion.getCao_id(), campoopcion.getCap_id(),
                        campoopcion.getCao_codigo(), campoopcion.getCao_opcion());
            }

            List<Campoportada> lisCampoportada = validarResponse.getLista_campoportada();
            for(Campoportada campoportada : lisCampoportada){
                cargaDAO.cargarCampoportada(MainActivity.this, campoportada.getCap_id(), campoportada.getSec_id(),
                        campoportada.getSus_id(), campoportada.getCae_id(), campoportada.getCap_numero(), campoportada.getCap_descripcion());
            }

            List<Celdamatriz> lisCeldamatriz = validarResponse.getLista_celdamatriz();
            for(Celdamatriz celdamatriz : lisCeldamatriz){
                cargaDAO.cargarCeldamatriz(MainActivity.this, celdamatriz.getCem_id(), celdamatriz.getPro_id(), celdamatriz.getPri_id(),
                        celdamatriz.getCem_fila(), celdamatriz.getCem_columna(), celdamatriz.getCem_tipo_dato(), celdamatriz.getCem_desde(), celdamatriz.getCem_hasta());
            }

            List<Patron> lisPatron = validarResponse.getLista_patron();
            for(Patron patron : lisPatron){
                cargaDAO.cargarPatron(MainActivity.this, patron.getPat_id(), patron.getPat_descripcion(), patron.getPat_max_items());
            }

        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
        }

    }

    public String juntar(List<Encuestador> encuestador) {
        String response = "";

        for (Encuestador enc : encuestador) {
            response = response + enc.getUserEncuestador() + ",";
        }
        return response;
    }
}
