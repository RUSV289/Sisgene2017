package com.instituto.cuanto.sisgene;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.instituto.cuanto.sisgene.adapter.TipoPreguntaAbiertaAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMatrizMultipleAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMatrizSimpleAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMixtaAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaMultipleAdapter;
import com.instituto.cuanto.sisgene.adapter.TipoPreguntaUnicaAdapter;
import com.instituto.cuanto.sisgene.bean.CabeceraRespuesta;
import com.instituto.cuanto.sisgene.bean.EncuestaPregunta;
import com.instituto.cuanto.sisgene.bean.PreguntaAlternativa;
import com.instituto.cuanto.sisgene.bean.PreguntaItem;
import com.instituto.cuanto.sisgene.dao.CabeceraRespuestaDAO;
import com.instituto.cuanto.sisgene.dao.DetalleEncRptaDAO;
import com.instituto.cuanto.sisgene.dao.EncuestaDAO;
import com.instituto.cuanto.sisgene.dao.PersonaDAO;
import com.instituto.cuanto.sisgene.dao.TipoPreguntaDAO;
import com.instituto.cuanto.sisgene.entities.Pregunta;
import com.instituto.cuanto.sisgene.entities.RespuestaItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaAbiertaItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMatrizMultipleItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMatrizSimpleItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMixtaItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaMultipleItem;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaUnicaItem;
import com.instituto.cuanto.sisgene.util.EnvioServiceUtil;
import com.instituto.cuanto.sisgene.util.TiempoDiferencia;
import com.instituto.cuanto.sisgene.util.Util;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
//comentario que deberia guardarse
/**
 * Created by Gustavo on 09/10/2015.
 */
public class PreguntasActivity extends AppCompatActivity {

    static ArrayList<String> nombresEncuestados;
    static ArrayList<Integer> codigosIdentEncuestados;

    static ArrayList<String> nomEncuestados;
    static ArrayList<String> apePatEncuestados;
    static ArrayList<String> apeMatEncuestados;

    List<PreguntaAlternativa> listPreguntaAlterntiva;
    List<PreguntaItem> listPreguntaItems;

    Button btnSiguiente;
    Button btnGuardarEncuesta;
    Button btnBuscarPregunta;
    ListView lvRespuestas_tipoGeneral;
    Context context = PreguntasActivity.this;

    ScrollView scrollView;
    TextView tvEnunciadoPregunta;
    TextView tvSeccion;
    TextView tvSubSeccion;
    EditText etnumPregunta;

    EditText etnumSeccion;

    //variables que seran cambiadas de pregunta en pregunta
    //Pregunta
    String enunciadoPregunta;
    String numeroPreguntaBD;
    int numPregunta = -1;
    String idPregunta;
    int mumMaxChequeados;
    String tipoPreguntaActual;
    int encuestarTodos;
    int ordenImportancia;
    //nuevos para restriccion de las pregunta abiertas
    String subtipo;
    String tiponumerico;
    String desde="";
    String hasta="";


    //Seccion
    String nombreSecccion;
    String numeroSecccion;
    String nombreSubSeccion;
    String numeroSubSeccion;

    String comentario;

    //datos para validar encuesta
    int ultimoIdPregunta; //ultimo id de pregunta de la tabla Pregunta. Para validar si ya se ha repondido todas las preguntas
    boolean ultimaPregunta = false;
    EditText editTextObservacionRechazar;
    EditText editTextObservacionFinalizar;
    EditText editTextObservacion;

    //Retomar encuesta
    int idRespuesta;
    String valorRespuesta;
    String idPreguntaDeRpta;
    //caer_id de la cual se esta retomando la encuesta
    String caer_id_retoma;

    EditText campoRespuestaAB;

    //para saber si se viene por que se retomo una encuesta
    String retomarEncuesta;

    Button btnFinalizarEncuesta;

    //Prara saber si esa pregunta ya ha sido respondida
    boolean preguntaRespondida = false;

    //Para saber si para una pregunta para varias personas ¿quienes respondieron y quines no? 0 = no, 1 = si (respondio)
    ArrayList<Integer> respondioAllegadosPregunta = new ArrayList<>();

    //para obtener el mapa <codigoAllegado, respuesta>
    HashMap<String, String> mapAllegadoRespuesta;

   //ArrayList<Integer> respondioAllegadosPreguntaTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        retomarEncuesta = getIntent().getStringExtra("RETORNAR");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitypreguntas);

        nombresEncuestados = new ArrayList<>();
        codigosIdentEncuestados = new ArrayList<>();

        nomEncuestados = new ArrayList<>();
        apePatEncuestados = new ArrayList<>();
        apeMatEncuestados = new ArrayList<>();

        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        btnGuardarEncuesta = (Button) findViewById(R.id.btnGuardarEncuesta);
        //btnRechazarEncuesta = (Button) findViewById(R.id.btnRechazarEncuesta);
        btnFinalizarEncuesta = (Button) findViewById(R.id.btnFinalizarEncuesta);
        btnBuscarPregunta = (Button) findViewById(R.id.btnBuscarPregunta);
        etnumPregunta = (EditText) findViewById(R.id.etnumPregunta);
        etnumSeccion = (EditText)findViewById(R.id.etnumSeccion);

        editTextObservacion = new EditText(context);
        editTextObservacionRechazar = new EditText(context);
        editTextObservacionFinalizar = new EditText(context);

        //lyFragmentoListaPreguntas = (LinearLayout) findViewById(R.id.lyFragmentoListaPreguntas);
        lvRespuestas_tipoGeneral = (ListView) findViewById(R.id.lvRespuestas_tipoGeneral);
        tvEnunciadoPregunta = (TextView) findViewById(R.id.tvEnunciadoPregunta);
        tvSeccion = (TextView) findViewById(R.id.tvSeccion);

        //para poner restricciones al edittext de la pregunta abierta
        //campoRespuestaAB = (EditText)findViewById(R.id.etRespuestaPreguntaEncuestado);
        //campoRespuestaAB.setInputType(InputType.TYPE_CLASS_NUMBER);

        tvSubSeccion = (TextView) findViewById(R.id.tvSubSeccion);
        TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter = new TipoPreguntaAbiertaAdapter();
        TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter = new TipoPreguntaUnicaAdapter();
        //miListaTipoPreguntaAbierta = new ArrayList<>();
        scrollView = new ScrollView(PreguntasActivity.this);
        listPreguntaItems = new ArrayList<>();

        btnSiguiente.setOnClickListener(btnSiguientesetOnClickListener);
        btnGuardarEncuesta.setOnClickListener(btnGuardarEncuestasetOnClickListener);
        //btnRechazarEncuesta.setOnClickListener(btnRechazarEncuestasetOnClickListener);
        btnFinalizarEncuesta.setOnClickListener(btnFinalizarEncuestasetOnClickListener);
        btnBuscarPregunta.setOnClickListener(btnBuscarPreguntasetOnClickListener);

        if(retomarEncuesta==null) {
            if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(NombresPersonasEncuestadasActivity.KEY_ARG_NOMBRES_ENCUESTADOS)) {
                System.out.println("se obtienen datos - nombres y codigos de indentificacion");
                nombresEncuestados = getIntent().getStringArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_NOMBRES_ENCUESTADOS);
                codigosIdentEncuestados = getIntent().getIntegerArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_ID_ENCUESTADOS);

                nomEncuestados = getIntent().getStringArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_NOM_ENCUESTADOS);
                apePatEncuestados = getIntent().getStringArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_APEPAT_ENCUESTADOS);
                apeMatEncuestados = getIntent().getStringArrayListExtra(NombresPersonasEncuestadasActivity.KEY_ARG_APEMAT_ENCUESTADOS);

                System.out.println("PreguntasActivity DIM codigosIdentEncuestados: " + codigosIdentEncuestados.size());
            }

            leerPrimeraPregunta();        //leer todos los datos de la primera pregunta
            leerTipoPreguntaxPregunta();
            obtenerNumeroPreguntas();
        }
        else{
            //es por que se esta retomando la encuesta
            System.out.println("Aca se estaria retomando la encuesta");
            System.out.println("se obtienen datos - nombres y codigos de indentificacion");
            nombresEncuestados = getIntent().getStringArrayListExtra("nombresCompletosAllegados");
            codigosIdentEncuestados = getIntent().getIntegerArrayListExtra("codigoAllegados");

            nomEncuestados = getIntent().getStringArrayListExtra("nombresAllegados");
            apePatEncuestados = getIntent().getStringArrayListExtra("apellidoPaternoAllegados");
            apeMatEncuestados = getIntent().getStringArrayListExtra("apellidoMaternollegados");

            idPregunta = getIntent().getStringExtra("id_preguntaContinuar");
            caer_id_retoma = getIntent().getStringExtra("caer_id_retoma");

            TipoPreguntaDAO tipoPreguntaDAO = new TipoPreguntaDAO();
            int ultimoIdPreguntaTempo = tipoPreguntaDAO.obtenerUltiIdPregunta(PreguntasActivity.this);

            System.out.println("VERIFICAMOS VALORES ID RESPESTA "+idPregunta+"  "+ultimoIdPreguntaTempo);
            if(Integer.parseInt(idPregunta) == ultimoIdPreguntaTempo){// en caso que se llego a revisar todas las preguntas y se retome la encuesta se hara desde la primera pregunta
                System.out.println("Se retoma la encuesta desde el princio "+idPregunta+"  "+ultimoIdPreguntaTempo);
                leerPrimeraPregunta();        //leer todos los datos de la primera pregunta
                leerTipoPreguntaxPregunta();
                obtenerNumeroPreguntas();
            }
            else{
                leerSiguientePregunta();
                leerTipoPreguntaxPregunta();
                obtenerNumeroPreguntas();
            }


        }


    }

    private void obtenerNumeroPreguntas() {
        //Validar que aun no se hayan culminado el numero de encuestas a realizar por el usuario
        TipoPreguntaDAO tipoPreguntaDAO = new TipoPreguntaDAO();
        ultimoIdPregunta = tipoPreguntaDAO.obtenerUltiIdPregunta(PreguntasActivity.this);
        System.out.println("ultimoIdPregunta: " + ultimoIdPregunta);
    }

    private void leerPrimeraPregunta() {


        EncuestaDAO encuestaDAO = new EncuestaDAO();
        EncuestaPregunta encuestaPregunta;
        //ir a BD para sacar la primera preguna de dicha ecuesta
        encuestaPregunta = encuestaDAO.obtenerPreguntaEncuesta(PreguntasActivity.this);
        // Setear datos para la primera pregunta
        System.out.println("encuestaPregunta.getSec_nombre" + encuestaPregunta.getSec_nombre());

        nombreSecccion = encuestaPregunta.getSec_nombre();
        numeroSecccion = encuestaPregunta.getSec_numero_seccion();
        numeroSecccion = encuestaPregunta.getSec_numero_seccion();
        numeroSubSeccion = encuestaPregunta.getSus_numero_subseccion();
        nombreSubSeccion = encuestaPregunta.getSus_nombre();
        idPregunta = encuestaPregunta.getPre_id();
        //pregutna
        tipoPreguntaActual = encuestaPregunta.getPre_tipo_rpta();
        enunciadoPregunta = encuestaPregunta.getPre_enunciado();
        numeroPreguntaBD = encuestaPregunta.getPre_numero();
        encuestarTodos = Integer.parseInt(encuestaPregunta.getPre_unico_patron()); // 0:todos   -  1: una persona
        comentario = encuestaPregunta.getPre_comentario();

        //para las restricciones en las respuesta de tipo abierta
        subtipo = encuestaPregunta.getPre_subtipo();  //alfanumerico, numerico, cadena
        tiponumerico = encuestaPregunta.getPre_tiponumerico(); // decimal, entero
        desde = encuestaPregunta.getPre_desde();
        hasta = encuestaPregunta.getPre_hasta();



        //vemos si esa pregunta fue respondida
        //obtener el id de la ultima cabecera
        CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();
        CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);
        String respuestaP = encuestaDAO.obtenerRespuestaDeUnaPregunta(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc()+"", idPregunta);
        if(!respuestaP.equals("")){
            preguntaRespondida = true;
            Toast.makeText(PreguntasActivity.this, "Pregunta ya contestada:: "+respuestaP, Toast.LENGTH_LONG).show();
            StringTokenizer tokens = new StringTokenizer(respuestaP, "&");
            mapAllegadoRespuesta = new HashMap();
            while(tokens.hasMoreTokens()){
                String token = tokens.nextToken();
                System.out.println("MAP primeraPregunta:: "+token.substring(0,4) + "  "+token.substring(4,token.length()));
                mapAllegadoRespuesta.put(token.substring(0, 4), token.substring(4, token.length()));
            }
            //verificar quienes respondieron la pregunta y quienes no
            verificaQuienesRespondieron();
        }
        else{
            preguntaRespondida = false;
            Toast.makeText(PreguntasActivity.this, "Pregunta no respondida", Toast.LENGTH_LONG).show();
        }




        try {
            ordenImportancia = Integer.parseInt(encuestaPregunta.getPre_importarordenrptamu()); //Que va a retornar
        } catch (Exception ex) {
            ex.printStackTrace();
            ordenImportancia = 0;
        }

        //obtener alternativas
        listPreguntaAlterntiva = encuestaDAO.obtenerAlternativas(PreguntasActivity.this, idPregunta);

        if (tipoPreguntaActual.equals("MI") || tipoPreguntaActual.equals("MM"))
            listPreguntaItems = encuestaDAO.obtenerItems(PreguntasActivity.this, idPregunta);

    }

    private void verificaQuienesRespondieron() {
        respondioAllegadosPregunta = new ArrayList<>();
        switch (tipoPreguntaActual){
            case "UN":  //tipo de pregunta unica

                for(int i =0 ; i<codigosIdentEncuestados.size();i++){
                    //agregar el codigo de identificacion
                    Formatter codIdent = new Formatter();
                    String codIdEncTemp = "[" +  codIdent.format("%02d", codigosIdentEncuestados.get(i)) + "]";
                    if(mapAllegadoRespuesta.get(codIdEncTemp)!=null){
                        if(mapAllegadoRespuesta.get(codIdEncTemp).equals("null")||mapAllegadoRespuesta.get(codIdEncTemp).equals("")){
                            respondioAllegadosPregunta.add(0); // no respondio
                        }
                        else{
                            respondioAllegadosPregunta.add(1); //si respondio
                        }
                    }

                }
                for(int j= 0;j<respondioAllegadosPregunta.size();j++){
                    System.out.println("respuestas:: "+respondioAllegadosPregunta.get(j));
                }
                break;
            case "AB": //tipo de pregunta abierta
                for(int i =0 ; i<codigosIdentEncuestados.size();i++){
                    Formatter codIdent = new Formatter();
                    String codIdEncTemp = "[" +  codIdent.format("%02d", codigosIdentEncuestados.get(i)) + "]";
                    if(mapAllegadoRespuesta.get(codIdEncTemp)!=null){
                        if(mapAllegadoRespuesta.get(codIdEncTemp).equals("null")||mapAllegadoRespuesta.get(codIdEncTemp).equals("")){
                            respondioAllegadosPregunta.add(0); // no respondio
                        }
                        else{
                            respondioAllegadosPregunta.add(1); //si respondio
                        }
                     }
                }
                break;
            case "MI": //tipo de pregunta abierta
                for(int i =0 ; i<codigosIdentEncuestados.size();i++){
                    Formatter codIdent = new Formatter();
                    String codIdEncTemp = "[" +  codIdent.format("%02d", codigosIdentEncuestados.get(i)) + "]";
                    if(mapAllegadoRespuesta.get(codIdEncTemp)!=null){
                        if(mapAllegadoRespuesta.get(codIdEncTemp).equals("null")||mapAllegadoRespuesta.get(codIdEncTemp).equals("")){
                            respondioAllegadosPregunta.add(0); // no respondio
                        }
                        else{
                            respondioAllegadosPregunta.add(1); //si respondio
                        }
                    }
                }
                break;
            case "MU": //tipo de pregunta abierta
                for(int i =0 ; i<codigosIdentEncuestados.size();i++){
                    Formatter codIdent = new Formatter();
                    String codIdEncTemp = "[" +  codIdent.format("%02d", codigosIdentEncuestados.get(i)) + "]";
                    if(mapAllegadoRespuesta.get(codIdEncTemp)!=null){
                        if(mapAllegadoRespuesta.get(codIdEncTemp).equals("null")||mapAllegadoRespuesta.get(codIdEncTemp).equals("")){
                            respondioAllegadosPregunta.add(0); // no respondio
                        }
                        else{
                            respondioAllegadosPregunta.add(1); //si respondio
                        }
                    }
                }
                break;
            case "MS": //tipo de pregunta matriz simple
                for(int i =0 ; i<codigosIdentEncuestados.size();i++){
                    Formatter codIdent = new Formatter();
                    String codIdEncTemp = "[" +  codIdent.format("%02d", codigosIdentEncuestados.get(i)) + "]";
                    if(mapAllegadoRespuesta.get(codIdEncTemp)!=null){

                        StringTokenizer tokens2 = new StringTokenizer(mapAllegadoRespuesta.get(codIdEncTemp), "$");
                        boolean respondioEstaPersona = false;
                        while(tokens2.hasMoreTokens()){
                            String token2 = tokens2.nextToken();
                            System.out.println(token2);
                            StringTokenizer tokens3 = new StringTokenizer(token2, "%");
                            int it = 0;
                            while(tokens3.hasMoreTokens()){
                                it++;
                                String token3 = tokens3.nextToken();
                                if(it>1){
                                    if(!(token3.endsWith("null")||token3.equals(""))){
                                        respondioEstaPersona = true;
                                        //break;
                                    }
                                    System.out.println(token3);
                                }
                                //String token3 = tokens3.nextToken();
                                //System.out.println(token3);
                            }
                        }

                        if(respondioEstaPersona){
                            System.out.println("Repondio la pregunta");
                            respondioAllegadosPregunta.add(1); //si respondio
                        }
                        else{
                            System.out.println("No respondio la pregunta");
                            respondioAllegadosPregunta.add(0); // no respondio
                        }



                        /*if(mapAllegadoRespuesta.get(codIdEncTemp).equals("null")||mapAllegadoRespuesta.get(codIdEncTemp).equals("")){
                            respondioAllegadosPregunta.add(0); // no respondio
                        }
                        else{
                            respondioAllegadosPregunta.add(1); //si respondio
                        }*/
                    }
                }
                break;
            case "MM": //tipo de pregunta matriz simple
                for(int i =0 ; i<codigosIdentEncuestados.size();i++){
                    Formatter codIdent = new Formatter();
                    String codIdEncTemp = "[" +  codIdent.format("%02d", codigosIdentEncuestados.get(i)) + "]";
                    if(mapAllegadoRespuesta.get(codIdEncTemp)!=null){

                        StringTokenizer tokens2 = new StringTokenizer(mapAllegadoRespuesta.get(codIdEncTemp), "$");
                        boolean respondioEstaPersona = false;
                        while(tokens2.hasMoreTokens()){
                            String token2 = tokens2.nextToken();
                            System.out.println(token2);
                            StringTokenizer tokens3 = new StringTokenizer(token2, "%");
                            int it = 0;
                            while(tokens3.hasMoreTokens()){
                                it++;
                                String token3 = tokens3.nextToken();
                                if(it>1){
                                    if(!(token3.endsWith("null")||token3.equals(""))){
                                        respondioEstaPersona = true;
                                        //break;
                                    }
                                    System.out.println(token3);
                                }
                                //String token3 = tokens3.nextToken();
                                //System.out.println(token3);
                            }
                        }

                        if(respondioEstaPersona){
                            System.out.println("Repondio la pregunta");
                            respondioAllegadosPregunta.add(1); //si respondio
                        }
                        else{
                            System.out.println("No respondio la pregunta");
                            respondioAllegadosPregunta.add(0); // no respondio
                        }



                        /*if(mapAllegadoRespuesta.get(codIdEncTemp).equals("null")||mapAllegadoRespuesta.get(codIdEncTemp).equals("")){
                            respondioAllegadosPregunta.add(0); // no respondio
                        }
                        else{
                            respondioAllegadosPregunta.add(1); //si respondio
                        }*/
                    }
                }
                break;
        }

    }

    private void mostrarDatosSeccion() {
        tvSeccion.setText("Secci\u00f3n Nro. "+ numeroSecccion + ": " + nombreSecccion);
        if(!nombreSubSeccion.equals("")){
            tvSubSeccion.setText(" - Subsecci\u00f3n Nro. "+numeroSubSeccion + " " + nombreSubSeccion);
        }
        if(!comentario.equals(""))
            tvEnunciadoPregunta.setText("Pregunta Nro. "+numeroPreguntaBD + ": " + enunciadoPregunta + " (" + comentario + ")");
        else
            tvEnunciadoPregunta.setText("Pregunta Nro. "+numeroPreguntaBD + ": " + enunciadoPregunta + "");

        String opciones = "";
        System.out.println("\n\n*************MOSTRAR DATOS SECCION***************** SI NO ES UNA PREGUNTA ABIERTA");

        if(!tipoPreguntaActual.equals("AB")){
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                opciones = opciones + listPreguntaAlterntiva.get(i).getOpc_nombre().trim() + "\t";
                System.out.println("opcion " + i + ": " + listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }
            opciones = opciones + "\n";

            if (tipoPreguntaActual.equals("MS") || tipoPreguntaActual.equals("MM"))
                System.out.println("Cantidad de Items: " + listPreguntaItems.size());
            for (int i = 0; i < listPreguntaItems.size(); i++) {
                opciones = opciones + listPreguntaItems.get(i).getIte_nombre().trim() + "\t";
                System.out.println("item " + i + ": " + listPreguntaItems.get(i).getIte_nombre().trim());
            }

            //mostrar datos -- eliminar
            System.out.println("secccion: " + numeroSecccion + ": " + nombreSecccion);
            System.out.println("subsecccion: " + numeroSubSeccion + ": " + nombreSubSeccion);
            System.out.println("Enunciado: " + enunciadoPregunta);
            System.out.println("Opciones: " + opciones);
            System.out.println("Tipo de pregunta: " + tipoPreguntaActual);
            System.out.println("idPregunta: " + idPregunta);
            System.out.println("encuestar todos: " + encuestarTodos);
            System.out.println("**************FIN DATOS SECCION****************\n\n");
        }

    }

    private int leerSiguientePregunta() {
        EncuestaDAO encuestaDAO = new EncuestaDAO();
        EncuestaPregunta encuestaPregunta;
        //ir a BD para sacar la primera preguna de dicha ecuesta
        encuestaPregunta = encuestaDAO.obtenerPreguntaEncuesta(PreguntasActivity.this, idPregunta);
        // Setear datos para la primera pregunta
        if (encuestaPregunta != null) {
            nombreSecccion = encuestaPregunta.getSec_nombre();
            numeroSecccion = encuestaPregunta.getSec_numero_seccion();
            numeroSecccion = encuestaPregunta.getSec_numero_seccion();
            numeroSubSeccion = encuestaPregunta.getSus_numero_subseccion();
            nombreSubSeccion = encuestaPregunta.getSus_nombre();
            idPregunta = encuestaPregunta.getPre_id();

            System.out.println("2.- SECCION : " + this.numeroSecccion + " - nombre: " + this.nombreSecccion);
            System.out.println("2.- SUBSECCION : " + this.numeroSubSeccion + " - nombre: " + this.nombreSubSeccion);
            //pregutna
            tipoPreguntaActual = encuestaPregunta.getPre_tipo_rpta();
            enunciadoPregunta = encuestaPregunta.getPre_enunciado();
            numeroPreguntaBD = encuestaPregunta.getPre_numero();
            encuestarTodos = Integer.parseInt(encuestaPregunta.getPre_unico_patron()); // 0:todos   -  1: una persona


            //para las restricciones en las respuesta de tipo abierta
            subtipo = encuestaPregunta.getPre_subtipo();  //alfanumerico, numerico, cadena
            tiponumerico = encuestaPregunta.getPre_tiponumerico(); // decimal, entero
            desde = encuestaPregunta.getPre_desde();
            hasta = encuestaPregunta.getPre_hasta();


            //vemos si esa pregunta fue respondida
            //obtener el id de la ultima cabecera
            CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();
            CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);
            String respuestaP = encuestaDAO.obtenerRespuestaDeUnaPregunta(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc()+"", idPregunta);
            if(!respuestaP.equals("")){
                preguntaRespondida = true;
                Toast.makeText(PreguntasActivity.this, "Pregunta ya contestada:: "+respuestaP, Toast.LENGTH_LONG).show();
                StringTokenizer tokens = new StringTokenizer(respuestaP, "&");
                mapAllegadoRespuesta = new HashMap();
                while(tokens.hasMoreTokens()){
                    String token = tokens.nextToken();
                    System.out.println("MAP Siguiente:: "+token.substring(0,4) + "  "+token.substring(4,token.length()));
                    mapAllegadoRespuesta.put(token.substring(0, 4), token.substring(4, token.length()));
                }
                //verificar quienes respondieron la pregunta y quienes no
                verificaQuienesRespondieron();
            }
            else{
                preguntaRespondida = false;
                Toast.makeText(PreguntasActivity.this, "Pregunta no respondida", Toast.LENGTH_LONG).show();
            }

            if(encuestaPregunta.getPre_comentario().equals(""))
                comentario = "";
            else
                comentario = encuestaPregunta.getPre_comentario();


            try {
                System.out.println("obtener ordenIMportancia");
                ordenImportancia = Integer.parseInt(encuestaPregunta.getPre_importarordenrptamu()); //Que va a retornar
            } catch (Exception ex) {
                System.out.println("CATCH");
                ex.printStackTrace();
                ordenImportancia = 0;
            }
            try {
                System.out.println("obtener mumMaxChequeados");
                System.out.println("Numero maz chequeados: " + encuestaPregunta.getPre_nummaxrptamu());
                mumMaxChequeados = Integer.parseInt(encuestaPregunta.getPre_nummaxrptamu()); //campo que indica el numero maximo de items a chequear para las preguntas mixtas
                System.out.println("FIN TRY");
            } catch (Exception ex) {
                System.out.println("CATCH");
                ex.printStackTrace();
                mumMaxChequeados = 1;
            }
            //obtener alternativas
            if (!tipoPreguntaActual.equals("AB")){
                listPreguntaAlterntiva = encuestaDAO.obtenerAlternativas(PreguntasActivity.this, idPregunta);
                if(listPreguntaAlterntiva==null)
                    System.out.println("Lista de preguntas alternativa es null");
                else
                    System.out.println("ASDFG: "+listPreguntaAlterntiva.get(0).getOpc_nombre());

            }


            //System.out.println("IDPREGUNTA:: "+idPregunta);
            //System.out.println("Se obtuvo num de alternativas:: "+listPreguntaAlterntiva.size());

            if (tipoPreguntaActual.equals("MS") || tipoPreguntaActual.equals("MM"))
                listPreguntaItems = encuestaDAO.obtenerItems(PreguntasActivity.this, idPregunta);
            System.out.println("Se obtuvo num de items");

            return 0;
        }
        return 1;
    }

    private int leerPreguntaPorNumPregunta() {
        EncuestaDAO encuestaDAO = new EncuestaDAO();
        EncuestaPregunta encuestaPregunta;

        if ((etnumPregunta.getText().toString().trim().length() == 0) || (etnumSeccion.getText().toString().trim().length() == 0)) {
            Toast.makeText(PreguntasActivity.this, "Ingrese la sección y el número de la pregunta a buscar", Toast.LENGTH_LONG).show();
            return -1;
        } else
            encuestaPregunta = encuestaDAO.obtenerPreguntaPorNumPregunta(PreguntasActivity.this,
                    etnumPregunta.getText().toString().trim(), etnumSeccion.getText().toString().trim());

        if (encuestaPregunta != null) {
            //antes de setear los datos de la pregunta encontrada, guardas las respuestas de la pregunta actual
            leeryGuardarDatos();

            nombreSecccion = encuestaPregunta.getSec_nombre();
            numeroSecccion = encuestaPregunta.getSec_numero_seccion();
            numeroSecccion = encuestaPregunta.getSec_numero_seccion();
            numeroSubSeccion = encuestaPregunta.getSus_numero_subseccion();
            nombreSubSeccion = encuestaPregunta.getSus_nombre();
            idPregunta = encuestaPregunta.getPre_id();
            //pregutna
            tipoPreguntaActual = encuestaPregunta.getPre_tipo_rpta();
            enunciadoPregunta = encuestaPregunta.getPre_enunciado();
            numeroPreguntaBD = encuestaPregunta.getPre_numero();
            encuestarTodos = Integer.parseInt(encuestaPregunta.getPre_unico_patron()); // 0:todos   -  1: una persona


            //para las restricciones en las respuesta de tipo abierta
            subtipo = encuestaPregunta.getPre_subtipo();  //alfanumerico, numerico, cadena
            tiponumerico = encuestaPregunta.getPre_tiponumerico(); // decimal, entero
            desde = encuestaPregunta.getPre_desde();
            hasta = encuestaPregunta.getPre_hasta();

            //tambien leemos la cantida de num max chequeados
            try {
                System.out.println("obtener mumMaxChequeados");
                System.out.println("Numero maz chequeados pregxnumpreg: "+encuestaPregunta.getPre_nummaxrptamu());
                mumMaxChequeados = Integer.parseInt(encuestaPregunta.getPre_nummaxrptamu()); //campo que indica el numero maximo de items a chequear para las preguntas mixtas
                System.out.println("FIN TRY");
            } catch (Exception ex) {
                System.out.println("CATCH");
                ex.printStackTrace();
                mumMaxChequeados = 1;
            }

            //vemos si esa pregunta fue respondida
            //obtener el id de la ultima cabecera
            CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();
            CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);
            String respuestaP = encuestaDAO.obtenerRespuestaDeUnaPregunta(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc()+"", idPregunta);
            if(!respuestaP.equals("")){
                preguntaRespondida = true;
                Toast.makeText(PreguntasActivity.this, "Pregunta ya contestada:: "+respuestaP, Toast.LENGTH_LONG).show();
                StringTokenizer tokens = new StringTokenizer(respuestaP, "&");
                mapAllegadoRespuesta = new HashMap();
                while(tokens.hasMoreTokens()){
                    String token = tokens.nextToken();
                    System.out.println("MAP preguntaProNumPregunta:: "+token.substring(0,4) + "  "+token.substring(4,token.length()));
                    mapAllegadoRespuesta.put(token.substring(0, 4), token.substring(4, token.length()));
                }
                //verificar quienes respondieron la pregunta y quienes no
                verificaQuienesRespondieron();
            }
            else{
                preguntaRespondida = false;
                Toast.makeText(PreguntasActivity.this, "Pregunta no respondida", Toast.LENGTH_LONG).show();
            }


            try {
                ordenImportancia = Integer.parseInt(encuestaPregunta.getPre_importarordenrptamu()); //Que va a retornar
            } catch (Exception ex) {
                ex.printStackTrace();
                ordenImportancia = 0;
            }
            //obtener alternativas
            if (!tipoPreguntaActual.equals("AB")) {
                System.out.println("OBTENER ALTERNATIVAS");
                listPreguntaAlterntiva = encuestaDAO.obtenerAlternativas(PreguntasActivity.this, idPregunta);
            }

            if (tipoPreguntaActual.equals("MS") || tipoPreguntaActual.equals("MM")) {
                System.out.println("OBTENER ITEMS");
                listPreguntaItems = encuestaDAO.obtenerItems(PreguntasActivity.this, idPregunta);
                System.out.println("NUMERO DE ITEMS: " + listPreguntaItems.size());
            }
        } else {
            limpiarLista();
            new AlertDialog.Builder(PreguntasActivity.this).setTitle("Alerta").setMessage("El número de pregunta no existe")
                    .setNeutralButton("Aceptar", alertaCancelarOnClickListener)
                    .setCancelable(false).show();
            return -1;
        }
        return 0;
    }

    View.OnClickListener btnSiguientesetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //leer los datos de la pregunta actual y guardar en la base de datos
            leeryGuardarDatos();
            if (!ultimaPregunta) {
                //leer la siguiente pregunta

                System.out.println("Se ejecuto leerSiguientePregunta de la pregunta " + idPregunta);
                System.out.println("ultimoIdPregunta siguiente: " + ultimoIdPregunta);

                if (leerSiguientePregunta() == 0) {
                    //Aun no se llega a la ultima pregunta
                    //mostrar interfaz segun la pregunta leida
                    leerTipoPreguntaxPregunta();
                } else {
                       // if(Integer.parseInt(idPregunta) != ultimoIdPregunta )
                    ultimaPregunta = true;
                    btnSiguiente.setEnabled(false);

                    //Le decimos al encuestador que no hay mas preguntas
                    tvSeccion.setText("No hay mas preguntas en la Encuesta");
                    tvEnunciadoPregunta.setText("Seleccione la manera de Guardar");
                    //tvSubSeccion.setText("Se ha finalizado la encuesta");
                }
            } else {
                btnSiguiente.setEnabled(false);
                Toast.makeText(PreguntasActivity.this, "Se han respondido todas las preguntas", Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener btnGuardarEncuestasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //
            editTextObservacion = new EditText(context);
            editTextObservacion.setHint("Observaciones");
            editTextObservacion.setTextColor(getResources().getColor(R.color.color_texto));

            //ahora solo hara un guardado parcial
            /*if (ultimaPregunta) {
                new AlertDialog.Builder(PreguntasActivity.this)
                        .setTitle("Alerta")
                        .setView(editTextObservacion)
                        .setMessage("¿Desea guardar la encuesta?")
                        .setIcon(R.drawable.ic_save_black_24dp)
                        .setPositiveButton("Terminar encuesta", alertaAceptarEncuestaFinalizadaOnClickListener)
                        .setNegativeButton("Continuar encuesta", alertaCancelarOnClickListener)
                        .setCancelable(false).show();
            } else
             */   new AlertDialog.Builder(PreguntasActivity.this)
                        .setTitle("Alerta").setView(editTextObservacion)
                        .setIcon(R.drawable.cancel)
                        .setMessage("¿Desea finalizar la encuesta sin terminar la ejecucion de las preguntas?")
                        .setPositiveButton("Terminar encuesta", alertaAceptarOnClickListener)
                        .setNegativeButton("Continuar encuesta", alertaCancelarOnClickListener)
                        .setCancelable(false).show();
        }
    };

    View.OnClickListener btnFinalizarEncuestasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editTextObservacionFinalizar = new EditText(context);
            editTextObservacionFinalizar.setHint("Observaciones");
            editTextObservacionFinalizar.setTextColor(getResources().getColor(R.color.color_texto));
            new AlertDialog.Builder(PreguntasActivity.this)
                    .setTitle("Mensaje")
                    .setView(editTextObservacionFinalizar)
                    .setMessage("¿Desea finalizar la encuesta con estado Completo?")
                    .setIcon(R.drawable.ic_save_black_24dp)
                    .setPositiveButton("Finalizar encuesta", alertaAceptarEncuestaFinalizadaOnClickListener)
                    .setNegativeButton("Continuar encuesta", alertaCancelarOnClickListener)
                    .setCancelable(false).show();

        }
    };

    //Buscar pregunta por numero de pregunta
    View.OnClickListener btnBuscarPreguntasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (leerPreguntaPorNumPregunta() == 0) {
                if (!ultimaPregunta) {
                    etnumPregunta.setText("");
                    etnumSeccion.setText("");
                    if (Integer.parseInt(idPregunta) != ultimoIdPregunta) {
                        //Aun no se llega a la ultima pregunta, mostrar interfaz segun la pregunta leida
                        leerTipoPreguntaxPregunta();
                    } else
                        ultimaPregunta = true;
                } else {
                    btnSiguiente.setEnabled(false);
                    Toast.makeText(PreguntasActivity.this, "Ésta es la última pregunta de la encuesta", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    DialogInterface.OnClickListener alertaAceptarOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            //leemos y guardamos la pregunta actual antes del guardado incompleto
            leeryGuardarDatos();
            String fechaInicio;
            String diferenciaHoras = "";
            String fechaFin = Util.obtenerFechayHora();

            CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();

            //obtener el id de la ultima cabecera
            CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);

            //obtener la hora de inicio de l encuesta
            fechaInicio = cabeceraRespuestaDAO.obtenerFechayHoraInicioxEncuesta(PreguntasActivity.this,
                    "" + cabeceraRespuesta.getIdCabeceraEnc());
            //si no hay errr al obtener la fecha inicio
            if (fechaInicio != null)
                diferenciaHoras = TiempoDiferencia.retornarDiferenciaTiempo(fechaInicio, fechaFin);

            cabeceraRespuestaDAO.actualizarCabEncFinalEjecucion(PreguntasActivity.this,
                    "I",//estado
                    fechaFin,// horaFin
                    diferenciaHoras,// tiempo
                    "0",//Estado enviado
                    " ",// fecha_envio
                    editTextObservacion.getText().toString().trim(),
                    cabeceraRespuesta.getIdCabeceraEnc());

            new AlertDialog.Builder(PreguntasActivity.this).setTitle("Mensaje")
                    .setMessage("Se ha guardado la encuesta satisfactoriamente")
                    .setNeutralButton("Aceptar", alertaAceptarGuardarEncuestaOnClickListener)
                    .setCancelable(false).show();
        }
    };

    DialogInterface.OnClickListener alertaAceptarRechazarOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            String fechaInicio;
            String diferenciaHoras = "";
            String fechaFin = Util.obtenerFechayHora();

            CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();

            //obtener el id de la ultima cabecera
            CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);

            //obtener la hora de inicio de l encuesta
            fechaInicio = cabeceraRespuestaDAO.obtenerFechayHoraInicioxEncuesta(PreguntasActivity.this,
                    "" + cabeceraRespuesta.getIdCabeceraEnc());
            //si no hay errr al obtener la fecha inicio
            if (fechaInicio != null)
                diferenciaHoras = TiempoDiferencia.retornarDiferenciaTiempo(fechaInicio, fechaFin);

            //Guardar la encuesta con estado incompleto
            cabeceraRespuestaDAO.actualizarCabEncFinalEjecucion(PreguntasActivity.this,
                    "R",                         //estado
                    fechaFin,   // horaFin
                    diferenciaHoras,// tiempo
                    "0",//Estado enviado
                    " ",// fecha_envio
                    editTextObservacionRechazar.getText().toString().trim(),
                    cabeceraRespuesta.getIdCabeceraEnc());

            int numeroEncuesta = cabeceraRespuestaDAO.obtenerNumeroDeEncuesta(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc()+"");
            System.out.println("Actualizar numero encuesta por que se cancelo: "+cabeceraRespuesta.getIdCabeceraEnc()+"  "+numeroEncuesta);
            cabeceraRespuestaDAO.actualizarNumeroDeEncuesta(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc()+"",numeroEncuesta);
            System.out.println("Numero de encuesta cambiada: "+cabeceraRespuestaDAO.obtenerNumeroDeEncuesta(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc()+""));

            new AlertDialog.Builder(PreguntasActivity.this).setTitle("Mensaje")
                    .setMessage("Se ha guardado la encuesta satisfactoriamente")
                    .setNeutralButton("Aceptar", alertaAceptarGuardarEncuestaOnClickListener)
                    .setCancelable(false).show();
        }
    };
    DialogInterface.OnClickListener alertaAceptarEncuestaFinalizadaOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            //leemos y guardamos la pregunta actual antes del guardado incompleto
            leeryGuardarDatos();

            String fechaInicio;
            String diferenciaHoras = "";
            String fechaFin = Util.obtenerFechayHora();
            CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();

            //obtener el id de la ultima cabecera
            CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);

            //obtener la hora de inicio de l encuesta
            fechaInicio = cabeceraRespuestaDAO.obtenerFechayHoraInicioxEncuesta(PreguntasActivity.this,
                    "" + cabeceraRespuesta.getIdCabeceraEnc());
            //si no hay errr al obtener la fecha inicio
            if (fechaInicio != null)
                diferenciaHoras = TiempoDiferencia.retornarDiferenciaTiempo(fechaInicio, fechaFin);

            cabeceraRespuestaDAO.actualizarCabEncFinalEjecucion(PreguntasActivity.this,
                    "C",            //estado
                    fechaFin,       // horaFin
                    diferenciaHoras,// tiempo
                    "0",            //Estado enviado
                    "",             // fecha_envio
                    editTextObservacionFinalizar.getText().toString().trim(), // Obs
                    cabeceraRespuesta.getIdCabeceraEnc()); // cabRpta_id

            EnvioServiceUtil envioServiceUtil = new EnvioServiceUtil();
            boolean estTemp = false;
            //Si hay conexion a internet invocar al WS para envviar la data
            Log.i("AL SD", "ANTES DE EJECUTAR LA FUNCION");
            //envioServiceUtil.guardarSDEncuestaEjecutada(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc() + "");
            Log.i("AL SD", "DESPUES DE EJECUTAR LA FUNCION");
            if (conectadoInternet()) {
                estTemp = envioServiceUtil.enviarEncuestaEjecutada(PreguntasActivity.this, cabeceraRespuesta.getIdCabeceraEnc() + "");
            }

            //el envio de data al WS fue satisfactorio
            if (estTemp) {
                cabeceraRespuestaDAO.actualizarCabEncFinalEjecucion(PreguntasActivity.this,
                        "C",
                        fechaFin, // horaFin
                        diferenciaHoras,//tiempo
                        "1", //enviado
                        Util.obtenerFechayHora(), //fecha envio
                        editTextObservacionFinalizar.getText().toString().trim(),
                        cabeceraRespuesta.getIdCabeceraEnc());

                new AlertDialog.Builder(PreguntasActivity.this).setTitle("Mensaje")
                        .setMessage("Se ha enviado la encuesta al servidor satisfactoriamente")
                        .setNeutralButton("Aceptar", alertaAceptarGuardarEncuestaOnClickListener)
                        .setCancelable(false).show();
            } else {
                new AlertDialog.Builder(PreguntasActivity.this).setTitle("Mensaje")
                        .setMessage("Se ha guardado la encuesta completada satisfactoriamente")
                        .setNeutralButton("Aceptar", alertaAceptarGuardarEncuestaOnClickListener)
                        .setCancelable(false).show();
            }

        }
    };
    DialogInterface.OnClickListener alertaAceptarGuardarEncuestaOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent intent = new Intent(PreguntasActivity.this, PrincipalEncuestaActivity.class);
            startActivity(intent);
            dialogInterface.dismiss();
            limpiarLista();
            finish();

        }
    };
    DialogInterface.OnClickListener alertaCancelarOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }

    };

    /*
        Muestra los datos como enunciado y seccion
        Lee el tipo de pregunta y muestra las preguntas para responder (Los campos ya deben estar seteados)
    */
    private void leerTipoPreguntaxPregunta() {

        //mostrar datos de seccion, subseccion, enunciado y opciones
        mostrarDatosSeccion();
        //Mostrar lista segun el tipo de pregunta
        //Tipo de pregunta Unica
        if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaUnica))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaUnica();

            //Tipo de pregunta Multiple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMultiple))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMultiple();

            //Tipo de pregunta abierta
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaAbierta))) {
            //TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaAbierta();

            //Tipo de pregunta Matriz Simple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatSimple))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMatrizSimple();

            //Tipo de pregunta Matriz Multiple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatMultiple))) {
            //tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMatrizMultiple();

            //Tipo de pregunta Mixta
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMixta))) {
            // tipoPreguntaAbiertaAdapter.limpiarLista();
            poblarLista_TipoPreguntaMixta();
        }
    }

    private void leeryGuardarDatos() {
        //Leer los dato segun el tipo de pregunta actual
        if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaUnica))) {
            leerRespuestasTipoUnica();
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMultiple))) {
            leerRespuestasTipoMultiple();
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaAbierta))) {
            leerRespuestasTipoAbierta();
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatSimple))) {
            leerRespuestasTipoMatrizSimple();
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatMultiple))) {
            leerRespuestasTipoMatrizMultiple();
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMixta))) {
            leerRespuestasTipoMixta();
        }
    }

    private void leerRespuestasTipoAbierta() {
        if(!preguntaRespondida) { //todo normal como antes
            String respuestaAbierta = "";

            System.out.println("numero de personas :" + codigosIdentEncuestados.size());
            System.out.println("TipoPreguntaAbiertaAdapter: " + TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size());

            for (int i = 0; i < TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size(); i++) {
                TipoPreguntaAbiertaItem tipoPreguntaAbiertaItem = TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.getItem(i);
                //agregar el codigo de identificacion
                Formatter codIdent = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaAbierta = respuestaAbierta + "[" + codIdent + "]";

                //en caso el usuario no haya escrito nada
                if (nombresEncuestados.get(i).trim().equals(tipoPreguntaAbiertaItem.getDescription().trim())) {
                    respuestaAbierta = respuestaAbierta + "null";
                } else {
                    respuestaAbierta = respuestaAbierta + tipoPreguntaAbiertaItem.getDescription();
                }
                if (i != TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size() - 1) {
                    respuestaAbierta = "" + respuestaAbierta + "&";
                }
            }

            //guardar en base de datos la respuesta
            TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.limpiarLista();
            System.out.println("RESP TA: " + respuestaAbierta);
            guardarRespuesta(respuestaAbierta);
        }
        else{
            String respuestaAbierta = "";

            System.out.println("numero de personas :" + codigosIdentEncuestados.size());
            System.out.println("TipoPreguntaAbiertaAdapter: " + TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size());

            for (int i = 0; i < TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size(); i++) {
                TipoPreguntaAbiertaItem tipoPreguntaAbiertaItem = TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.getItem(i);
                //agregar el codigo de identificacion
                Formatter codIdent = new Formatter();
                Formatter codIdentSec = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaAbierta = respuestaAbierta + "[" + codIdent + "]";

                if(respondioAllegadosPregunta.get(i)==1) {//si esta persona ya respondio la pregunta, asi que hay que tomar la respuesta que ya habia respondido antes y que esta guardada en la base de datos
                    respuestaAbierta = respuestaAbierta + mapAllegadoRespuesta.get("["+codIdentSec.format("%02d", codigosIdentEncuestados.get(i))+"]"); //obtenemos la respuesta que ya se habia guardado en la base de datos para este allegado
                }
                else{//esta persona no respondio la pregunta asi que hay que tomar en cuenta lo que ahora marco como respuesta
                    //en caso el usuario no haya escrito nada
                    if (nombresEncuestados.get(i).trim().equals(tipoPreguntaAbiertaItem.getDescription().trim())) {
                        respuestaAbierta = respuestaAbierta + "null";
                    } else {
                        respuestaAbierta = respuestaAbierta + tipoPreguntaAbiertaItem.getDescription();
                    }
                }

                if (i != TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size() - 1) {
                    respuestaAbierta = "" + respuestaAbierta + "&";
                }
            }

            //actualizar en base de datos la respuesta
            TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.limpiarLista();
            System.out.println("RESP TA: actualizando" + respuestaAbierta);
            actualizarRespuesta(respuestaAbierta);
        }

    }

    private void leerRespuestasTipoUnica() {
        if(!preguntaRespondida){ //todo normal como antes
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaUnica = "";

            for (int i = 0; i < TipoPreguntaUnicaAdapter.myListPreguntaUnica.size(); i++) {

                TipoPreguntaUnicaItem tipoPreguntaUnicaItem = TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.getItem(i);
                //if(enunciadoPregunta.equals("¿Cuál es su parentesco con el jefe del hogar?")){
                if(numeroPreguntaBD.equals("1") && numeroSecccion.equals("1")){
                    //aca deberiamos tomar las acciones de allegados
                    if (tipoPreguntaUnicaItem.getPos() != 0){
                        System.out.println("parametro: " + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), tipoPreguntaUnicaItem.getRespuesta().trim()));
                        atualizarValoresCodigoAllegado(encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), tipoPreguntaUnicaItem.getRespuesta().trim()), i);
                    }
                }
            }
            for (int i = 0; i < TipoPreguntaUnicaAdapter.myListPreguntaUnica.size(); i++) {
                TipoPreguntaUnicaItem tipoPreguntaUnicaItem = TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.getItem(i);
                //agregar el codigo de identificacion
                Formatter codIdent = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaUnica = respuestaUnica + "[" + codIdent + "]";

                if (tipoPreguntaUnicaItem.getPos() == 0)
                    respuestaUnica = respuestaUnica + "null";
                else {
                    //respuestaUnica = respuestaUnica + tipoPreguntaUnicaItem.getRespuesta();
                    respuestaUnica = respuestaUnica + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), tipoPreguntaUnicaItem.getRespuesta().trim());


                }
                if (i != TipoPreguntaUnicaAdapter.myListPreguntaUnica.size() - 1)
                    respuestaUnica = "" + respuestaUnica + "&";
            }
            TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.limpiarLista();
            System.out.println("RESP TU: " + respuestaUnica);
            guardarRespuesta(respuestaUnica);
        }
        else{// en el caso de que hay que actualizar la pregunta por que una persona que no respondio en una pregunta ahora lo hace
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaUnica = "";

            //Ya no actalizo los codigos de identificacion de los allegados, cuado se retoma una encuesta
            /*for (int i = 0; i < TipoPreguntaUnicaAdapter.myListPreguntaUnica.size(); i++) {
                TipoPreguntaUnicaItem tipoPreguntaUnicaItem = TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.getItem(i);
                if(enunciadoPregunta.equals("¿Cuál es su parentesco con el jefe del hogar?")){
                    //aca deberiamos tomar las acciones de allegados
                    if (tipoPreguntaUnicaItem.getPos() != 0){
                        System.out.println("parametro: " + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), tipoPreguntaUnicaItem.getRespuesta().trim()));
                        atualizarValoresCodigoAllegado(encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), tipoPreguntaUnicaItem.getRespuesta().trim()), i);
                    }
                }
            }*/

            for (int i = 0; i < TipoPreguntaUnicaAdapter.myListPreguntaUnica.size(); i++) {
                TipoPreguntaUnicaItem tipoPreguntaUnicaItem = TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.getItem(i);
                //agregar el codigo de identificacion
                Formatter codIdent = new Formatter();
                Formatter codIdentSec = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaUnica = respuestaUnica + "[" + codIdent + "]";
                System.out.println("ACT 01::: "+respuestaUnica);

                if(respondioAllegadosPregunta.get(i)==1){//si esta persona ya respondio la pregunta, asi que hay que tomar la respuesta que ya habia respondido antes y que esta guardada en la base de datos

                    respuestaUnica = respuestaUnica+ mapAllegadoRespuesta.get("["+codIdentSec.format("%02d", codigosIdentEncuestados.get(i))+"]"); //obtenemos la respuesta que ya se habia guardado en la base de datos para este allegado
                    System.out.println("ACT 02::: "+respuestaUnica+"-["+codIdent.format("%02d", codigosIdentEncuestados.get(i))+"]");
                }
                else{//esta persona no respondio la pregunta asi que hay que tomar en cuenta lo que ahora marco como respuesta
                    if (tipoPreguntaUnicaItem.getPos() == 0){
                        respuestaUnica = respuestaUnica + "null";
                        System.out.println("ACT 03::: "+respuestaUnica);
                    }
                    else {
                        //respuestaUnica = respuestaUnica + tipoPreguntaUnicaItem.getRespuesta();
                        respuestaUnica = respuestaUnica + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), tipoPreguntaUnicaItem.getRespuesta().trim());
                        System.out.println("ACT 04::: "+respuestaUnica);
                    }
                }

                if (i != TipoPreguntaUnicaAdapter.myListPreguntaUnica.size() - 1)
                    respuestaUnica = "" + respuestaUnica + "&";
                    System.out.println("ACT 05::: "+respuestaUnica);
            }
            TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.limpiarLista();
            System.out.println("RESP TU: actualizado " + respuestaUnica);
            actualizarRespuesta(respuestaUnica);
        }

    }

    private void atualizarValoresCodigoAllegado(String resptaUnica, int pocision) {
        if(resptaUnica.equals("1")){ //en caso de que sea el jefe de hogar
            codigosIdentEncuestados.set(pocision, 1);
            for (int i = 0; i < pocision; i++) {
                int valantiguo = codigosIdentEncuestados.get(i);
                codigosIdentEncuestados.set(i, (valantiguo+1));
            }
        }
        System.out.println("==================CODIGOS DE ID CAMBIADOS=========================");
        for(int i=0;i<codigosIdentEncuestados.size();i++){
            System.out.println("--Val: "+codigosIdentEncuestados.get(i));
        }

        System.out.println("==================UPDATE DE CODIGO CAMBIADOS EN LA BD=========================");
        PersonaDAO personaDAO = new PersonaDAO();
        CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();
        int idcab= cabeceraRespuestaDAO.obtenerUltimoNumeroEncuestaCabecera(this);





        for(int i=0;i<codigosIdentEncuestados.size();i++){
            Formatter codIdent = new Formatter();
            System.out.println("--Val: " + codigosIdentEncuestados.get(i));
            //nombresEncuestados;
            String codIdFormat = codIdent.format("%02d", codigosIdentEncuestados.get(i)).toString();
            System.out.println("--Val FORMATEADO: " + codIdFormat);
            personaDAO.modificarCodigoIdentificacioAllegado(this, nomEncuestados.get(i), apePatEncuestados.get(i), apeMatEncuestados.get(i), codIdFormat);
        }

    }

    private void leerRespuestasTipoMultiple() {
        if(!preguntaRespondida){ //todo normal como antes
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMultiple = "";
            ArrayList<TipoPreguntaMultipleItem> tipoPreguntaMultipleItems = TipoPreguntaMultipleAdapter.myListPreguntaMultiple;
            int cont = 0;
            //agregar el codigo de identificacion

            for (int i = 0; i < tipoPreguntaMultipleItems.size(); i++) {
                Formatter codIdent = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMultiple = respuestaMultiple + "[" + codIdent + "]";

                for (int j = 0; j < tipoPreguntaMultipleItems.get(i).getRespuestas().size(); j++) {
                    //respuestaMultiple = respuestaMultiple + tipoPreguntaMultipleItems.get(i).getRespuestas().get(j).trim();
                    respuestaMultiple = respuestaMultiple + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), ((tipoPreguntaMultipleItems.get(i)).getRespuestas().get(j)).trim());
                    if (j != tipoPreguntaMultipleItems.get(i).getRespuestas().size() - 1)
                        respuestaMultiple = "" + respuestaMultiple + "$";
                    cont++;
                }
                if (i != tipoPreguntaMultipleItems.size() - 1) {
                    if (cont == 0)
                        respuestaMultiple = respuestaMultiple + "null";
                    respuestaMultiple = "" + respuestaMultiple + "&";
                }
                cont = 0;
            }
            //guardar en base de datos

            //System.out.println("respuesta: {" + respuestaMultiple + "}");   // RRSS agregado comentario para debug
            //guardar en base de datos la respuesta

            TipoPreguntaMultipleAdapter.tipoPreguntaMultipleAdapter.limpiarLista();
            System.out.println("RESP MTM: " + respuestaMultiple);
            guardarRespuesta(respuestaMultiple);
            System.out.println("---------FIN TOMA DE DATOS RESPUESTAS TIPO MULTIPLE");
        }
        else{
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMultiple = "";
            ArrayList<TipoPreguntaMultipleItem> tipoPreguntaMultipleItems = TipoPreguntaMultipleAdapter.myListPreguntaMultiple;
            int cont = 0;
            //agregar el codigo de identificacion

            for (int i = 0; i < tipoPreguntaMultipleItems.size(); i++) {
                Formatter codIdent = new Formatter();
                Formatter codIdentSec = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMultiple = respuestaMultiple + "[" + codIdent + "]";

                if(respondioAllegadosPregunta.get(i)==1){//si esta persona ya respondio la pregunta, asi que hay que tomar la respuesta que ya habia respondido antes y que esta guardada en la base de datos, y que recuperamos en el map
                    respuestaMultiple = respuestaMultiple + mapAllegadoRespuesta.get("["+codIdentSec.format("%02d", codigosIdentEncuestados.get(i))+"]"); //obtenemos la respuesta que ya se habia guardado en la base de datos para este allegado
                    cont++;
                }
                else{//esta persona no respondio la pregunta asi que hay que tomar en cuenta lo que ahora marco como respuesta
                    for (int j = 0; j < tipoPreguntaMultipleItems.get(i).getRespuestas().size(); j++) {
                        //respuestaMultiple = respuestaMultiple + tipoPreguntaMultipleItems.get(i).getRespuestas().get(j).trim();
                        respuestaMultiple = respuestaMultiple + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), ((tipoPreguntaMultipleItems.get(i)).getRespuestas().get(j)).trim());
                        if (j != tipoPreguntaMultipleItems.get(i).getRespuestas().size() - 1)
                            respuestaMultiple = "" + respuestaMultiple + "$";
                        cont++;
                    }
                }

                if (i != tipoPreguntaMultipleItems.size() - 1) {
                    if (cont == 0)
                        respuestaMultiple = respuestaMultiple + "null";
                    respuestaMultiple = "" + respuestaMultiple + "&";
                }
                cont = 0;
            }
            //guardar en base de datos

            //System.out.println("respuesta: {" + respuestaMultiple + "}");   // RRSS agregado comentario para debug
            //guardar en base de datos la respuesta

            TipoPreguntaMultipleAdapter.tipoPreguntaMultipleAdapter.limpiarLista();
            System.out.println("RESP MTM actualizando: " + respuestaMultiple);
            actualizarRespuesta(respuestaMultiple);
            System.out.println("---------FIN TOMA DE DATOS RESPUESTAS TIPO MULTIPLE");
        }
    }

    private void leerRespuestasTipoMixta() {
        if(!preguntaRespondida) { //todo normal como antes
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMixta = "";
            ArrayList<TipoPreguntaMixtaItem> tipoPreguntaMixtaItems = TipoPreguntaMixtaAdapter.myListPreguntaMixta;
            int cont = 0;
            //agregar el codigo de identificacion

            for (int i = 0; i < tipoPreguntaMixtaItems.size(); i++) {
                Formatter codIdent = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMixta = respuestaMixta + "[" + codIdent + "]";

                int sizeAlternativas = tipoPreguntaMixtaItems.get(i).getAlternativas().size();
                ArrayList<String> respuestas = tipoPreguntaMixtaItems.get(i).getRespuestas();

                for (int j = 0; j < respuestas.size(); j++) {
                    System.out.println("1 FOR MIX: "+respuestas.size());
                    String rpta;
                    //En caso de que se haya seleccionado otro, ¿cuál? y se ingrese un texto
                    if (respuestas.get(j).equals(tipoPreguntaMixtaItems.get(i).getAlternativas().get(sizeAlternativas - 1))) {
                        rpta = "(" + tipoPreguntaMixtaItems.get(i).getRespuestas().get(j).trim() + ")";
                        rpta = rpta + tipoPreguntaMixtaItems.get(i).getPreguntaMixta();
                        respuestaMixta = respuestaMixta + rpta;
                        System.out.println("1 respuestaMixta: "+respuestaMixta);
                    } else {
                        rpta = tipoPreguntaMixtaItems.get(i).getRespuestas().get(j).trim();
                        respuestaMixta = respuestaMixta + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), rpta.trim());
                        System.out.println("2 respuestaMixta: "+respuestaMixta);
                    }
                    //respuestaMixta = respuestaMixta + rpta;

                    if (j != tipoPreguntaMixtaItems.get(i).getRespuestas().size() - 1)
                        respuestaMixta = "" + respuestaMixta + "$";
                    System.out.println("3 respuestaMixta: "+respuestaMixta);
                    cont++;
                }
                if (i != tipoPreguntaMixtaItems.size() - 1) {
                    if (cont == 0)
                        respuestaMixta = respuestaMixta + "null";
                    System.out.println("4 respuestaMixta: "+respuestaMixta);
                    respuestaMixta = "" + respuestaMixta + "&";
                    System.out.println("5 respuestaMixta: "+respuestaMixta);
                }
                cont = 0;
            }
            //System.out.println("respuesta: {" + respuestaMixta + "}");   // RRSS agregado comentario para debug

            //guardar en base de datos
            TipoPreguntaMixtaAdapter.tipoPreguntaMixtaAdapter.limpiarLista();
            System.out.println("RESP TMixta: " + respuestaMixta);
            guardarRespuesta(respuestaMixta);
            System.out.println("---------FIN TOMA DE DATOS RESPUESTAS TIPO MIXTA");
        }
        else{
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMixta = "";
            ArrayList<TipoPreguntaMixtaItem> tipoPreguntaMixtaItems = TipoPreguntaMixtaAdapter.myListPreguntaMixta;
            int cont = 0;
            //agregar el codigo de identificacion

            for (int i = 0; i < tipoPreguntaMixtaItems.size(); i++) {
                Formatter codIdent = new Formatter();
                Formatter codIdentSec = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMixta = respuestaMixta + "[" + codIdent + "]";

                int sizeAlternativas = tipoPreguntaMixtaItems.get(i).getAlternativas().size();
                ArrayList<String> respuestas = tipoPreguntaMixtaItems.get(i).getRespuestas();

                if(respondioAllegadosPregunta.get(i)==1) {//si esta persona ya respondio la pregunta, asi que hay que tomar la respuesta que ya habia respondido antes y que esta guardada en la base de datos
                    respuestaMixta = respuestaMixta+ mapAllegadoRespuesta.get("["+codIdentSec.format("%02d", codigosIdentEncuestados.get(i))+"]"); //obtenemos la respuesta que ya se habia guardado en la base de datos para este allegado
                    cont++;
                }
                else{
                    for (int j = 0; j < respuestas.size(); j++) {
                        System.out.println("1 FOR MIX: "+respuestas.size());
                        String rpta;
                        //En caso de que se haya seleccionado otro, ¿cuál? y se ingrese un texto
                        if (respuestas.get(j).equals(tipoPreguntaMixtaItems.get(i).getAlternativas().get(sizeAlternativas - 1))) {
                            rpta = "(" + tipoPreguntaMixtaItems.get(i).getRespuestas().get(j).trim() + ")";
                            rpta = rpta + tipoPreguntaMixtaItems.get(i).getPreguntaMixta();
                            respuestaMixta = respuestaMixta + rpta;
                            System.out.println("1 respuestaMixta: "+respuestaMixta);
                        } else {
                            rpta = tipoPreguntaMixtaItems.get(i).getRespuestas().get(j).trim();
                            respuestaMixta = respuestaMixta + encuestaDAO.obtenerValorIdOpcion(this, Integer.parseInt(this.idPregunta), rpta.trim());
                            System.out.println("2 respuestaMixta: "+respuestaMixta);
                        }
                        //respuestaMixta = respuestaMixta + rpta;

                        if (j != tipoPreguntaMixtaItems.get(i).getRespuestas().size() - 1)
                            respuestaMixta = "" + respuestaMixta + "$";
                        System.out.println("3 respuestaMixta: "+respuestaMixta);
                        cont++;
                    }
                }

                if (i != tipoPreguntaMixtaItems.size() - 1) {
                    if (cont == 0)
                        respuestaMixta = respuestaMixta + "null";
                    System.out.println("4 respuestaMixta: "+respuestaMixta);
                    respuestaMixta = "" + respuestaMixta + "&";
                    System.out.println("5 respuestaMixta: "+respuestaMixta);
                }
                cont = 0;
            }
            //System.out.println("respuesta: {" + respuestaMixta + "}");   // RRSS agregado comentario para debug

            //actualizar en base de datos
            TipoPreguntaMixtaAdapter.tipoPreguntaMixtaAdapter.limpiarLista();
            System.out.println("RESP TMixta actualiza: " + respuestaMixta);
            actualizarRespuesta(respuestaMixta);
            System.out.println("---------FIN TOMA DE DATOS RESPUESTAS TIPO MIXTA");
        }

    }

    private void leerRespuestasTipoMatrizSimple() {
        if(!preguntaRespondida) { //todo normal como antes
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMatrizS = "";
            System.out.println("dimension myListPreguntaMatrizSimple:" + TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.size());
            ArrayList<TipoPreguntaMatrizSimpleItem> tipoPreguntaMatrizSimpleItems = TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz;

            System.out.println("size: " + tipoPreguntaMatrizSimpleItems.size());

            for (int i = 0; i < tipoPreguntaMatrizSimpleItems.size(); i++) {
                Formatter codIdent = new Formatter();

                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMatrizS = respuestaMatrizS + "[" + codIdent + "]";

                for (int k = 0; k < tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().size(); k++) {
                    System.out.println("vertical: " + tipoPreguntaMatrizSimpleItems.get(i).getVertical().get(k));
                    System.out.println("respuesta " + k + " :" + tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().get(k).trim());

                    respuestaMatrizS = respuestaMatrizS + tipoPreguntaMatrizSimpleItems.get(i).getVertical().get(k).trim();
                    respuestaMatrizS = respuestaMatrizS + "%";

                    if (tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().get(k).trim().equals(""))
                        respuestaMatrizS = respuestaMatrizS + "null";
                    else {
                        //respuestaMatrizS = respuestaMatrizS + tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().get(k).trim();
                        respuestaMatrizS = respuestaMatrizS + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), ((tipoPreguntaMatrizSimpleItems.get(i)).getRespuestas().get(k)).trim()).trim();
                    }
                    if (k != tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().size() - 1)
                        respuestaMatrizS = "" + respuestaMatrizS + "$";
                }
                if (i != tipoPreguntaMatrizSimpleItems.size() - 1)
                    respuestaMatrizS = "" + respuestaMatrizS + "&";
            }

            //System.out.println("respuesta: {" + respuestaMatrizS + "}");   //RRSS descomentar
            //guardar en base de datos la respuesta
            System.out.println("RESP TMatriz simple: " + respuestaMatrizS);
            TipoPreguntaMatrizSimpleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();
            guardarRespuesta(respuestaMatrizS);
            System.out.println("---------FIN TOMA DE DATOS MATRIZ SIMPLE");
        }
        else{//actualizamos la pregunta con lo que se ingrese
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMatrizS = "";
            System.out.println("dimension myListPreguntaMatrizSimple:" + TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.size());
            ArrayList<TipoPreguntaMatrizSimpleItem> tipoPreguntaMatrizSimpleItems = TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz;

            System.out.println("size: " + tipoPreguntaMatrizSimpleItems.size());

            for (int i = 0; i < tipoPreguntaMatrizSimpleItems.size(); i++) {
                Formatter codIdent = new Formatter();
                Formatter codIdentSec = new Formatter();

                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMatrizS = respuestaMatrizS + "[" + codIdent + "]";
                if(respondioAllegadosPregunta.get(i)==1) {//si esta persona ya respondio la pregunta, asi que hay que tomar la respuesta que ya habia respondido antes y que esta guardada en la base de datos
                    respuestaMatrizS = respuestaMatrizS+ mapAllegadoRespuesta.get("["+codIdentSec.format("%02d", codigosIdentEncuestados.get(i))+"]"); //obtenemos la respuesta que ya se habia guardado en la base de datos para este allegado
                }
                else{//esta persona no respondio la pregunta asi que hay que tomar en cuenta lo que ahora marco como respuesta
                    for (int k = 0; k < tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().size(); k++) {
                        System.out.println("vertical: " + tipoPreguntaMatrizSimpleItems.get(i).getVertical().get(k));
                        System.out.println("respuesta " + k + " :" + tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().get(k).trim());

                        respuestaMatrizS = respuestaMatrizS + tipoPreguntaMatrizSimpleItems.get(i).getVertical().get(k).trim();
                        respuestaMatrizS = respuestaMatrizS + "%";

                        if (tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().get(k).trim().equals(""))
                            respuestaMatrizS = respuestaMatrizS + "null";
                        else {
                            //respuestaMatrizS = respuestaMatrizS + tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().get(k).trim();
                            respuestaMatrizS = respuestaMatrizS + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), ((tipoPreguntaMatrizSimpleItems.get(i)).getRespuestas().get(k)).trim()).trim();
                        }
                        if (k != tipoPreguntaMatrizSimpleItems.get(i).getRespuestas().size() - 1)
                            respuestaMatrizS = "" + respuestaMatrizS + "$";
                    }
                }


                if (i != tipoPreguntaMatrizSimpleItems.size() - 1)
                    respuestaMatrizS = "" + respuestaMatrizS + "&";
            }

            //System.out.println("respuesta: {" + respuestaMatrizS + "}");   //RRSS descomentar
            //actualizr en base de datos la respuesta
            System.out.println("RESP TMatriz simple actualiza: " + respuestaMatrizS);
            TipoPreguntaMatrizSimpleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();
            actualizarRespuesta(respuestaMatrizS);
            System.out.println("---------FIN TOMA DE DATOS MATRIZ SIMPLE actualiza");
        }

    }

    private void leerRespuestasTipoMatrizMultiple222() {
        EncuestaDAO encuestaDAO = new EncuestaDAO();
        String respuestaMatrizM = "";
        System.out.println("dimension myListPreguntaMatrizMultiple:" + TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.size());
        ArrayList<TipoPreguntaMatrizMultipleItem> tipoPreguntaMatrizMultipleItems = TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz;
        int cont = 0;

        for (int i = 0; i < tipoPreguntaMatrizMultipleItems.size(); i++) {
            System.out.println("1 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.size());
            Formatter codIdent = new Formatter();
            codIdent.format("%02d", codigosIdentEncuestados.get(i));
            respuestaMatrizM = respuestaMatrizM + "[" + codIdent + "]";

            ArrayList<RespuestaItem> respuestas = tipoPreguntaMatrizMultipleItems.get(i).getRespuestas();
            System.out.println("usuario " + (i + 1) + "size:" + respuestas.size());


           // for (int l = 0; l < respuestas.size(); l++) {
                System.out.println("2 FOR SIZE : " + respuestas.size());
                for (int j = 0; j < tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size(); j++) {
                    System.out.println("3 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size());
                    for (int k = 0; k < tipoPreguntaMatrizMultipleItems.get(i).getVertical().size(); k++) {
                        System.out.println("4 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.get(i).getVertical().size());
                        //nombre de opcion
                        respuestaMatrizM = respuestaMatrizM + tipoPreguntaMatrizMultipleItems.get(i).getVertical().get(k);
                        System.out.println("1 MM: "+respuestaMatrizM);

                        //if (respuestas.get(l).getCol() == j && respuestas.get(l).getRow() == k) {

                            System.out.println("posicion: " + j + " , " + k);
                           // System.out.println("multiple usuario " + (i + 1) + ": " + respuestas.get(l).getTexto());

                            respuestaMatrizM = respuestaMatrizM + "%";
                            System.out.println("2 MM: "+respuestaMatrizM);
                            //respuestaMatrizM = respuestaMatrizM + respuestas.get(l).getTexto();
                            if(tipoPreguntaMatrizMultipleItems.get(i).isRespuesta(k, j)!= null){
                                //respuestaMatrizM = respuestaMatrizM + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), (respuestas.get(l)).getTexto());
                                respuestaMatrizM = respuestaMatrizM + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), (tipoPreguntaMatrizMultipleItems.get(i).isRespuesta(k, j)).getTexto());
                                System.out.println("3 MM: "+respuestaMatrizM);
                            }
                            else{
                                respuestaMatrizM = respuestaMatrizM + "null";
                                System.out.println("7 MM: "+respuestaMatrizM);
                            }

                         //   cont++;
                        //}
                       // if (cont == 0)
                        //    respuestaMatrizM = respuestaMatrizM + "null";
                         //   System.out.println("4 MM: "+respuestaMatrizM);
                        if (k != tipoPreguntaMatrizMultipleItems.get(i).getVertical().size() - 1)
                            respuestaMatrizM = respuestaMatrizM + "$";
                            System.out.println("5 MM: "+respuestaMatrizM);
                        cont = 0;
                    }

                }
           // }
            if (i != tipoPreguntaMatrizMultipleItems.size() - 1) {
                if (cont == 0){
                    respuestaMatrizM = respuestaMatrizM + "null";
                    System.out.println("6 MM: "+respuestaMatrizM);
                }

            }

            cont = 0;
        }

        //guardar en base de datos la respuesta
        TipoPreguntaMatrizMultipleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();
        System.out.println("RESP TMatriz mult: " + respuestaMatrizM);
        guardarRespuesta(respuestaMatrizM);
        System.out.println("---------Fin toma de datos Matriz multiple-----");
    }


    private void leerRespuestasTipoMatrizMultiple() {
        if(!preguntaRespondida) { //todo normal como antes
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMatrizM = "";
            System.out.println("dimension myListPreguntaMatrizMultiple:" + TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.size());
            ArrayList<TipoPreguntaMatrizMultipleItem> tipoPreguntaMatrizMultipleItems = TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz;
            int cont = 0;

            for (int i = 0; i < tipoPreguntaMatrizMultipleItems.size(); i++) {
                System.out.println("1 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.size());
                Formatter codIdent = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMatrizM = respuestaMatrizM + "[" + codIdent + "]";

                ArrayList<RespuestaItem> respuestas = tipoPreguntaMatrizMultipleItems.get(i).getRespuestas();
                System.out.println("usuario " + (i + 1) + "size:" + respuestas.size());


                // for (int l = 0; l < respuestas.size(); l++) {
                System.out.println("2 FOR SIZE : " + respuestas.size());
                for (int k = 0; k < tipoPreguntaMatrizMultipleItems.get(i).getVertical().size(); k++) {
                        System.out.println("4 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.get(i).getVertical().size());

                        //nombre de opcion
                        respuestaMatrizM = respuestaMatrizM + tipoPreguntaMatrizMultipleItems.get(i).getVertical().get(k);
                        System.out.println("1 MM: "+respuestaMatrizM);

                        for (int j = 0; j < tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size(); j++) {
                            System.out.println("3 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size());


                            //if (respuestas.get(l).getCol() == j && respuestas.get(l).getRow() == k) {

                            System.out.println("posicion: " + j + " , " + k);
                            // System.out.println("multiple usuario " + (i + 1) + ": " + respuestas.get(l).getTexto());

                            respuestaMatrizM = respuestaMatrizM + "%";
                            System.out.println("2 MM: "+respuestaMatrizM);
                            //respuestaMatrizM = respuestaMatrizM + respuestas.get(l).getTexto();
                            if(tipoPreguntaMatrizMultipleItems.get(i).isRespuesta(k, j)!= null){
                                //respuestaMatrizM = respuestaMatrizM + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), (respuestas.get(l)).getTexto());
                                respuestaMatrizM = respuestaMatrizM + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), (tipoPreguntaMatrizMultipleItems.get(i).isRespuesta(k, j)).getTexto());
                                System.out.println("3 MM: "+respuestaMatrizM);
                            }
                            else{
                                respuestaMatrizM = respuestaMatrizM + "null";
                                System.out.println("7 MM: "+respuestaMatrizM);
                            }

                            //   cont++;
                            //}
                            // if (cont == 0)
                            //    respuestaMatrizM = respuestaMatrizM + "null";
                            //   System.out.println("4 MM: "+respuestaMatrizM);
                            //if (k != tipoPreguntaMatrizMultipleItems.get(i).getVertical().size() - 1)
                            //    respuestaMatrizM = respuestaMatrizM + "$";
                            if ((j == (tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size()-1))&&(k <  (tipoPreguntaMatrizMultipleItems.get(i).getVertical().size() - 1)))
                                respuestaMatrizM = respuestaMatrizM + "$";

                            System.out.println("5 MM: "+respuestaMatrizM);
                            cont = 0;
                        }

                    }


                // }
                //if (i != tipoPreguntaMatrizMultipleItems.size() - 1) {
                //    if (cont == 0){
                //        respuestaMatrizM = respuestaMatrizM + "null";
                //        System.out.println("6 MM: "+respuestaMatrizM);
                //    }
                //}

                if (i != tipoPreguntaMatrizMultipleItems.size() - 1)
                    respuestaMatrizM = "" + respuestaMatrizM + "&";
                System.out.println("6 MM: "+respuestaMatrizM);


            }

            //guardar en base de datos la respuesta
            TipoPreguntaMatrizMultipleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();
            System.out.println("RESP TMatriz mult: " + respuestaMatrizM);
            guardarRespuesta(respuestaMatrizM);
            System.out.println("---------Fin toma de datos Matriz multiple-----");
        }
        else{
            EncuestaDAO encuestaDAO = new EncuestaDAO();
            String respuestaMatrizM = "";
            System.out.println("dimension myListPreguntaMatrizMultiple:" + TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.size());
            ArrayList<TipoPreguntaMatrizMultipleItem> tipoPreguntaMatrizMultipleItems = TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz;
            int cont = 0;

            for (int i = 0; i < tipoPreguntaMatrizMultipleItems.size(); i++) {
                System.out.println("1 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.size());
                Formatter codIdent = new Formatter();
                Formatter codIdentSec = new Formatter();
                codIdent.format("%02d", codigosIdentEncuestados.get(i));
                respuestaMatrizM = respuestaMatrizM + "[" + codIdent + "]";

                ArrayList<RespuestaItem> respuestas = tipoPreguntaMatrizMultipleItems.get(i).getRespuestas();
                System.out.println("usuario " + (i + 1) + "size:" + respuestas.size());


                // for (int l = 0; l < respuestas.size(); l++) {
                System.out.println("2 FOR SIZE : " + respuestas.size());
                if(respondioAllegadosPregunta.get(i)==1) {//si esta persona ya respondio la pregunta, asi que hay que tomar la respuesta que ya habia respondido antes y que esta guardada en la base de datos
                    respuestaMatrizM = respuestaMatrizM+ mapAllegadoRespuesta.get("["+codIdentSec.format("%02d", codigosIdentEncuestados.get(i))+"]"); //obtenemos la respuesta que ya se habia guardado en la base de datos para este allegado
                }
                else{//esta persona no respondio la pregunta asi que hay que tomar en cuenta lo que ahora marco como respuesta
                    for (int k = 0; k < tipoPreguntaMatrizMultipleItems.get(i).getVertical().size(); k++) {
                        System.out.println("4 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.get(i).getVertical().size());

                        //nombre de opcion
                        respuestaMatrizM = respuestaMatrizM + tipoPreguntaMatrizMultipleItems.get(i).getVertical().get(k);
                        System.out.println("1 MM: "+respuestaMatrizM);

                        for (int j = 0; j < tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size(); j++) {
                            System.out.println("3 FOR SIZE : " + tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size());


                            //if (respuestas.get(l).getCol() == j && respuestas.get(l).getRow() == k) {

                            System.out.println("posicion: " + j + " , " + k);
                            // System.out.println("multiple usuario " + (i + 1) + ": " + respuestas.get(l).getTexto());

                            respuestaMatrizM = respuestaMatrizM + "%";
                            System.out.println("2 MM: "+respuestaMatrizM);
                            //respuestaMatrizM = respuestaMatrizM + respuestas.get(l).getTexto();
                            if(tipoPreguntaMatrizMultipleItems.get(i).isRespuesta(k, j)!= null){
                                //respuestaMatrizM = respuestaMatrizM + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), (respuestas.get(l)).getTexto());
                                respuestaMatrizM = respuestaMatrizM + encuestaDAO.obtenerValorIdItem(this, Integer.parseInt(this.idPregunta), (tipoPreguntaMatrizMultipleItems.get(i).isRespuesta(k, j)).getTexto());
                                System.out.println("3 MM: "+respuestaMatrizM);
                            }
                            else{
                                respuestaMatrizM = respuestaMatrizM + "null";
                                System.out.println("7 MM: "+respuestaMatrizM);
                            }

                            //   cont++;
                            //}
                            // if (cont == 0)
                            //    respuestaMatrizM = respuestaMatrizM + "null";
                            //   System.out.println("4 MM: "+respuestaMatrizM);
                            //if (k != tipoPreguntaMatrizMultipleItems.get(i).getVertical().size() - 1)
                            //    respuestaMatrizM = respuestaMatrizM + "$";
                            if ((j == (tipoPreguntaMatrizMultipleItems.get(i).getHorizontal().size()-1))&&(k <  (tipoPreguntaMatrizMultipleItems.get(i).getVertical().size() - 1)))
                                respuestaMatrizM = respuestaMatrizM + "$";

                            System.out.println("5 MM: "+respuestaMatrizM);
                            cont = 0;
                        }

                    }
                }

                // }
                //if (i != tipoPreguntaMatrizMultipleItems.size() - 1) {
                //    if (cont == 0){
                //        respuestaMatrizM = respuestaMatrizM + "null";
                //        System.out.println("6 MM: "+respuestaMatrizM);
                //    }
                //}

                if (i != tipoPreguntaMatrizMultipleItems.size() - 1)
                    respuestaMatrizM = "" + respuestaMatrizM + "&";
                System.out.println("6 MM: "+respuestaMatrizM);


            }

            //guardar en base de datos la respuesta
            TipoPreguntaMatrizMultipleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();
            System.out.println("RESP TMatriz mult actualiza: " + respuestaMatrizM);
            actualizarRespuesta(respuestaMatrizM);
            System.out.println("---------Fin toma de datos Matriz multiple actualiza-----");
        }

    }


    private void poblarLista_TipoPreguntaAbierta() {
        //respondioAllegadosPreguntaTemp = new ArrayList<>();//solo para indicar que ninguna persona respodio la pregunta
        //TipoPreguntaAbiertaAdapter.respondidoPreguntas = new ArrayList<>();
        if(!preguntaRespondida) {

            //cargar datos a la lista

            //for (int i = 0; i < nombresEncuestados.size(); i++) {
                //respondioAllegadosPreguntaTemp.add(0);
              //  TipoPreguntaAbiertaAdapter.respondidoPreguntas.add(0);
              // }


            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaAbiertaItem tipoPreguntaAbiertaItem = new TipoPreguntaAbiertaItem();
                tipoPreguntaAbiertaItem.setTitle(nombresEncuestados.get(i));
                tipoPreguntaAbiertaItem.setDescription(nombresEncuestados.get(i));
                tipoPreguntaAbiertaItem.setRespondido("0"); //no respondido por defecto todos

                TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.add(tipoPreguntaAbiertaItem);
                //TipoPreguntaAbiertaAdapter.MyViewHolder.getTvTitle;
                //EditText etRpta = new EditText(context);
                //etRpta.setInputType(InputType.TYPE_CLASS_NUMBER);

                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            //System.out.println("TipoPreguntaAbiertaAdapter.respondidoPreguntas: " + TipoPreguntaAbiertaAdapter.respondidoPreguntas.size());
            //System.out.println("tam parametro antes de pasar: " + respondioAllegadosPreguntaTemp.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaAbiertaAdapter(context, TipoPreguntaAbiertaAdapter.myListPreguntaAbierta, subtipo, tiponumerico, desde, hasta));//respondioAllegadosPreguntaTemp con puros 0s por que es una pregunta que aun no se ha respondido
        }
        else{
            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaAbiertaItem tipoPreguntaAbiertaItem = new TipoPreguntaAbiertaItem();
                if(respondioAllegadosPregunta.get(i)==1){
                    tipoPreguntaAbiertaItem.setTitle(nombresEncuestados.get(i) + " (ya respondió)");
                    tipoPreguntaAbiertaItem.setDescription(nombresEncuestados.get(i));
                    tipoPreguntaAbiertaItem.setRespondido(respondioAllegadosPregunta.get(i)+"");
                    TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.add(tipoPreguntaAbiertaItem);

                    if (i == 0 && encuestarTodos == 1)
                        break;

                }
                else{
                    tipoPreguntaAbiertaItem.setTitle(nombresEncuestados.get(i));
                    tipoPreguntaAbiertaItem.setDescription(nombresEncuestados.get(i));
                    tipoPreguntaAbiertaItem.setRespondido(respondioAllegadosPregunta.get(i)+"");
                    TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.add(tipoPreguntaAbiertaItem);

                    if (i == 0 && encuestarTodos == 1)
                        break;

                }

            }

                //TipoPreguntaAbiertaAdapter.MyViewHolder.getTvTitle;
                //EditText etRpta = new EditText(context);
                //etRpta.setInputType(InputType.TYPE_CLASS_NUMBER);

            }
            System.out.println("miListaTipoPreguntaAbierta: " + TipoPreguntaAbiertaAdapter.myListPreguntaAbierta.size());

            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaAbiertaAdapter(context, TipoPreguntaAbiertaAdapter.myListPreguntaAbierta, subtipo, tiponumerico, desde, hasta));//enviamos la cadena en donde indica quienes ya respodieron la pregunta y quiene sno
            System.out.println("num pregunta: " + numPregunta);
    }

    private void poblarLista_TipoPreguntaUnica() {

        if(!preguntaRespondida){
            HashMap<Integer, String> alternativas = new HashMap<>();

            alternativas.put(0, "Seleccione alternativa");
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                alternativas.put(i + 1, listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }

            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                System.out.println("Tamaño lista encuestados:: "+nombresEncuestados.size());
                TipoPreguntaUnicaItem tipoPreguntaUnicaItem = new TipoPreguntaUnicaItem();
                tipoPreguntaUnicaItem.setTitle(nombresEncuestados.get(i));
                tipoPreguntaUnicaItem.setAlternativas(alternativas);
                TipoPreguntaUnicaAdapter.myListPreguntaUnica.add(tipoPreguntaUnicaItem);
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaUnicaAdapter(context, TipoPreguntaUnicaAdapter.myListPreguntaUnica));
        }
        else{
            HashMap<Integer, String> alternativas = new HashMap<>();

            alternativas.put(0, "Seleccione alternativa");
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                alternativas.put(i + 1, listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }

            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                System.out.println("Tamaño lista encuestados:: " + nombresEncuestados.size());
                TipoPreguntaUnicaItem tipoPreguntaUnicaItem = new TipoPreguntaUnicaItem();
                if(respondioAllegadosPregunta.get(i)==1){//esta persona ha respondido
                    tipoPreguntaUnicaItem.setTitle(nombresEncuestados.get(i)+" (ya respondió)");
                    //ya no le damos alternativas a seleccionar
                    HashMap<Integer, String> alternativas2 = new HashMap<>();//opcinoes en blanco
                    tipoPreguntaUnicaItem.setAlternativas(alternativas2);
                    TipoPreguntaUnicaAdapter.myListPreguntaUnica.add(tipoPreguntaUnicaItem);
                }
                else{
                    tipoPreguntaUnicaItem.setTitle(nombresEncuestados.get(i));
                    tipoPreguntaUnicaItem.setAlternativas(alternativas);
                    TipoPreguntaUnicaAdapter.myListPreguntaUnica.add(tipoPreguntaUnicaItem);
                }
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaUnicaAdapter(context, TipoPreguntaUnicaAdapter.myListPreguntaUnica));
        }

    }

    private void poblarLista_TipoPreguntaMultiple() {

        if(!preguntaRespondida) {
            ArrayList<String> alternativas = new ArrayList<>();
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                alternativas.add(listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }

            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMultipleItem tipoPreguntaMultipleItem = new TipoPreguntaMultipleItem();
                tipoPreguntaMultipleItem.setTitle(nombresEncuestados.get(i));
                tipoPreguntaMultipleItem.setAlternativas(alternativas);
                TipoPreguntaMultipleAdapter.myListPreguntaMultiple.add(tipoPreguntaMultipleItem);
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            System.out.println("miListaTipoPreguntaMixta: " + TipoPreguntaMultipleAdapter.myListPreguntaMultiple.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMultipleAdapter(context, TipoPreguntaMultipleAdapter.myListPreguntaMultiple));
        }
        else{
            ArrayList<String> alternativas = new ArrayList<>();
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                alternativas.add(listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }

            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMultipleItem tipoPreguntaMultipleItem = new TipoPreguntaMultipleItem();
                if(respondioAllegadosPregunta.get(i)==1){//esta persona ha respondido
                    tipoPreguntaMultipleItem.setTitle(nombresEncuestados.get(i)+" (ya respondió)");
                    //ya no le damos alternativas a seleccionar
                    ArrayList<String> alternativas2 = new ArrayList<>();//opcinoes en blanco
                    tipoPreguntaMultipleItem.setAlternativas(alternativas2);
                    TipoPreguntaMultipleAdapter.myListPreguntaMultiple.add(tipoPreguntaMultipleItem);
                }
                else{
                    tipoPreguntaMultipleItem.setTitle(nombresEncuestados.get(i));
                    tipoPreguntaMultipleItem.setAlternativas(alternativas);
                    TipoPreguntaMultipleAdapter.myListPreguntaMultiple.add(tipoPreguntaMultipleItem);
                }
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            System.out.println("miListaTipoPreguntaMixta: " + TipoPreguntaMultipleAdapter.myListPreguntaMultiple.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMultipleAdapter(context, TipoPreguntaMultipleAdapter.myListPreguntaMultiple));

        }

    }

    private void poblarLista_TipoPreguntaMixta() {
        //mumMaxChequeados = 3;
        if(!preguntaRespondida) {
            ArrayList<String> alternativas = new ArrayList<>();
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                alternativas.add(listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }

            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMixtaItem tipoPreguntaMixtaItem = new TipoPreguntaMixtaItem();
                tipoPreguntaMixtaItem.setTitle(nombresEncuestados.get(i));
                tipoPreguntaMixtaItem.setAlternativas(alternativas);
                TipoPreguntaMixtaAdapter.myListPreguntaMixta.add(tipoPreguntaMixtaItem);
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            System.out.println("miListaTipoPreguntaMixta: " + TipoPreguntaMixtaAdapter.myListPreguntaMixta.size());

            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMixtaAdapter(context, TipoPreguntaMixtaAdapter.myListPreguntaMixta,
                    mumMaxChequeados, ordenImportancia));
        }
        else{
            ArrayList<String> alternativas = new ArrayList<>();
            for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
                alternativas.add(listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            }

            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMixtaItem tipoPreguntaMixtaItem = new TipoPreguntaMixtaItem();
                if(respondioAllegadosPregunta.get(i)==1){//esta persona ha respondido
                    tipoPreguntaMixtaItem.setTitle(nombresEncuestados.get(i) + " (ya respondió)");
                    ArrayList<String> alternativas2 = new ArrayList<>();//opcinoes en blanco
                    tipoPreguntaMixtaItem.setAlternativas(alternativas2);
                    TipoPreguntaMixtaAdapter.myListPreguntaMixta.add(tipoPreguntaMixtaItem);
                    if (i == 0 && encuestarTodos == 1)
                        break;
                }
                else{
                    tipoPreguntaMixtaItem.setTitle(nombresEncuestados.get(i));
                    tipoPreguntaMixtaItem.setAlternativas(alternativas);
                    TipoPreguntaMixtaAdapter.myListPreguntaMixta.add(tipoPreguntaMixtaItem);
                    if (i == 0 && encuestarTodos == 1)
                        break;
                }

            }
            System.out.println("miListaTipoPreguntaMixta: " + TipoPreguntaMixtaAdapter.myListPreguntaMixta.size());

            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMixtaAdapter(context, TipoPreguntaMixtaAdapter.myListPreguntaMixta,
                    mumMaxChequeados, ordenImportancia));
        }

    }

    private void poblarLista_TipoPreguntaMatrizSimple() {
        ArrayList<String> horizontales = new ArrayList<>();

        System.out.println("poblarLista_TipoPreguntaMatrizSimple- listPreguntaItems" + listPreguntaItems.size());

        for (int i = 0; i < listPreguntaItems.size(); i++) {
            horizontales.add(listPreguntaItems.get(i).getIte_nombre().trim());
            //temporal
            System.out.println("ITEM " + (i + 1) + ": " + listPreguntaItems.get(i).getIte_nombre().trim());
            //
        }

        ArrayList<String> verticales = new ArrayList<>();
        for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
            System.out.println("Vertical " + (i + 1) + ": " + listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
            verticales.add(listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
        }

        System.out.println("Len horizontales: " + horizontales.size());
        System.out.println("Len verticales: " + verticales.size());

        if(!preguntaRespondida) {
            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMatrizSimpleItem tipoPreguntaMatrizSimpleItem = new TipoPreguntaMatrizSimpleItem();
                tipoPreguntaMatrizSimpleItem.setTitle(nombresEncuestados.get(i));
                tipoPreguntaMatrizSimpleItem.setHorizontal(horizontales);
                tipoPreguntaMatrizSimpleItem.setVertical(verticales);
                TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizSimpleItem);
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            System.out.println("miListaTipoPreguntaMatrizSimple: " + TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMatrizSimpleAdapter(context, TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz));
            System.out.println("num pregunta: " + numPregunta);
        }
        else{
            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMatrizSimpleItem tipoPreguntaMatrizSimpleItem = new TipoPreguntaMatrizSimpleItem();
                if(respondioAllegadosPregunta.get(i)==1){
                    tipoPreguntaMatrizSimpleItem.setTitle(nombresEncuestados.get(i)+" (ya respondió)");
                    ArrayList<String> horizontalesVacio = new ArrayList<>(); //para no mostrar opciones
                    ArrayList<String> verticalesVacio = new ArrayList<>();//para no mostrar opciones
                    tipoPreguntaMatrizSimpleItem.setHorizontal(horizontalesVacio);
                    tipoPreguntaMatrizSimpleItem.setVertical(verticalesVacio);
                    TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizSimpleItem);
                    if (i == 0 && encuestarTodos == 1)
                        break;
                }
                else{
                    tipoPreguntaMatrizSimpleItem.setTitle(nombresEncuestados.get(i));
                    tipoPreguntaMatrizSimpleItem.setHorizontal(horizontales);
                    tipoPreguntaMatrizSimpleItem.setVertical(verticales);
                    TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizSimpleItem);
                    if (i == 0 && encuestarTodos == 1)
                        break;
                }

            }
            System.out.println("miListaTipoPreguntaMatrizSimple: " + TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMatrizSimpleAdapter(context, TipoPreguntaMatrizSimpleAdapter.myListPreguntaMatriz));
            System.out.println("num pregunta: " + numPregunta);
        }

    }

    private void poblarLista_TipoPreguntaMatrizMultiple() {
        ArrayList<String> horizontales = new ArrayList<>();
        for (int i = 0; i < listPreguntaItems.size(); i++) {
            horizontales.add(listPreguntaItems.get(i).getIte_nombre().trim());
        }

        ArrayList<String> verticales = new ArrayList<>();
        for (int i = 0; i < listPreguntaAlterntiva.size(); i++) {
            verticales.add(listPreguntaAlterntiva.get(i).getOpc_nombre().trim());
        }

        if(!preguntaRespondida) {
            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMatrizMultipleItem tipoPreguntaMatrizMultipleItem = new TipoPreguntaMatrizMultipleItem();
                tipoPreguntaMatrizMultipleItem.setTitle(nombresEncuestados.get(i));
                tipoPreguntaMatrizMultipleItem.setHorizontal(horizontales);
                tipoPreguntaMatrizMultipleItem.setVertical(verticales);
                TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizMultipleItem);
                if (i == 0 && encuestarTodos == 1)
                    break;
            }
            System.out.println("miListaTipoPreguntaMatrizMultiple: " + TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMatrizMultipleAdapter(context, TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz));
            System.out.println("num pregunta: " + numPregunta);

        }
        else{
            //cargar datos a la lista
            for (int i = 0; i < nombresEncuestados.size(); i++) {
                TipoPreguntaMatrizMultipleItem tipoPreguntaMatrizMultipleItem = new TipoPreguntaMatrizMultipleItem();
                if(respondioAllegadosPregunta.get(i)==1){
                    tipoPreguntaMatrizMultipleItem.setTitle(nombresEncuestados.get(i)+" (ya respondió)");
                    ArrayList<String> horizontalesVacio = new ArrayList<>(); //para no mostrar opciones
                    ArrayList<String> verticalesVacio = new ArrayList<>();//para no mostrar opciones
                    tipoPreguntaMatrizMultipleItem.setHorizontal(horizontalesVacio);
                    tipoPreguntaMatrizMultipleItem.setVertical(verticalesVacio);
                    TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizMultipleItem);
                    if (i == 0 && encuestarTodos == 1)
                        break;
                }else{
                    tipoPreguntaMatrizMultipleItem.setTitle(nombresEncuestados.get(i));
                    tipoPreguntaMatrizMultipleItem.setHorizontal(horizontales);
                    tipoPreguntaMatrizMultipleItem.setVertical(verticales);
                    TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.add(tipoPreguntaMatrizMultipleItem);
                    if (i == 0 && encuestarTodos == 1)
                        break;
                }

            }
            System.out.println("miListaTipoPreguntaMatrizMultiple: " + TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz.size());
            lvRespuestas_tipoGeneral.setAdapter(new TipoPreguntaMatrizMultipleAdapter(context, TipoPreguntaMatrizMultipleAdapter.myListPreguntaMatriz));
            System.out.println("num pregunta: " + numPregunta);
        }

    }

    private void guardarRespuesta(String rpta) {

        CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();
        System.out.println("Guardar respuesta: " + rpta);

        //Obtener el ultimo Id de la tabla cab_enc_rpta, para ser usado al guardar la pregunta
        CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);
        System.out.println("idCabecera obtenida: " + cabeceraRespuesta.getIdCabeceraEnc());

        //Guardar la respuesta de cada pregunta en BD
        cabeceraRespuestaDAO.insertarDetEnc(PreguntasActivity.this, rpta, String.valueOf(cabeceraRespuesta.getIdCabeceraEnc()), idPregunta);

    }

    private void actualizarRespuesta(String rpta) {

        CabeceraRespuestaDAO cabeceraRespuestaDAO = new CabeceraRespuestaDAO();
        System.out.println("Actualizar respuesta: " + rpta);


        CabeceraRespuesta cabeceraRespuesta = cabeceraRespuestaDAO.obteneridUltimaCabecera(PreguntasActivity.this);
        //System.out.println("idCabecera obtenida: " + cabeceraRespuesta.getIdCabeceraEnc());

        //Guardar la respuesta de cada pregunta en BD

        if(retomarEncuesta==null) {
            cabeceraRespuestaDAO.actualizarDetEnc(PreguntasActivity.this, rpta, String.valueOf(cabeceraRespuesta.getIdCabeceraEnc()), idPregunta);
        }
        else{
            cabeceraRespuestaDAO.actualizarDetEnc(PreguntasActivity.this, rpta, caer_id_retoma, idPregunta);
        }


    }

    private void limpiarLista() {
        if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaUnica))) {
            TipoPreguntaUnicaAdapter.tipoPreguntaUnicaAdapter.limpiarLista();

            //Tipo de pregunta Multiple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMultiple))) {
            TipoPreguntaMultipleAdapter.tipoPreguntaMultipleAdapter.limpiarLista();

            //Tipo de pregunta abierta
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaAbierta))) {
            TipoPreguntaAbiertaAdapter.tipoPreguntaAbiertaAdapter.limpiarLista();

            //Tipo de pregunta Matriz Simple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatSimple))) {
            TipoPreguntaMatrizSimpleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();

            //Tipo de pregunta Matriz Multiple
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMatMultiple))) {
            TipoPreguntaMatrizMultipleAdapter.tipoPreguntaMatrizAdapter.limpiarLista();

            //Tipo de pregunta Mixta
        } else if (tipoPreguntaActual.equals(getResources().getString(R.string.tipoPreguntaMixta))) {
            TipoPreguntaMixtaAdapter.tipoPreguntaMixtaAdapter.limpiarLista();
        }
    }

    public void retomarEncuesta() {
        //se buscara la primera pregunta que no haya sido respondida por todos.
        DetalleEncRptaDAO detalleEncRptaDAO = new DetalleEncRptaDAO();
        EncuestaDAO encuestaDAO = new EncuestaDAO();

        ArrayList<String> respuesta = null;


        int numeroPreguntas = encuestaDAO.obtenerNumeroPreguntas(PreguntasActivity.this);

        if (numeroPreguntas != -1) {
            //recorrer las respuestas hasta encontrar una que no haya sido respondita totalmente
            //for (int i = 0; i < numeroPreguntas; i++) {

            //if (i == 0)
            // {
            respuesta = detalleEncRptaDAO.obtenerRpta(PreguntasActivity.this);
            //} else {
            //   respuesta = detalleEncRptaDAO.obtenerRptaxId(PreguntasActivity.this,idPregunta);
            //}

            //idRespuesta = Integer.parseInt(respuesta.get(0));
            valorRespuesta = respuesta.get(1).toLowerCase();

            if (valorRespuesta.contains("null")) //pregunta que no ha sido respondida totalmente
                idPregunta = respuesta.get(2); // se asigna el id de la Pregunta que no ha sido respondida
            //}

            //setear los datos en las variables globalesu
            leerSiguientePregunta();

            //invocar al metodo para mostrar las preguntas a responder
            leerTipoPreguntaxPregunta();
        }
        //error al obtener el numero de preguntas
        else {
            Toast.makeText(PreguntasActivity.this, "Error en el sistema. Consulte con su Adm.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.finalizarEncuesta:
                editTextObservacionFinalizar = new EditText(context);
                editTextObservacionFinalizar.setHint("Observaciones");
                editTextObservacionFinalizar.setTextColor(getResources().getColor(R.color.color_texto));
                new AlertDialog.Builder(PreguntasActivity.this)
                        .setTitle("Mensaje")
                        .setView(editTextObservacionFinalizar)
                        .setMessage("¿Desea finalizar la encuesta con estado Completo?")
                        .setIcon(R.drawable.ic_save_black_24dp)
                        .setPositiveButton("Finalizar encuesta", alertaAceptarEncuestaFinalizadaOnClickListener)
                        .setNegativeButton("Continuar encuesta", alertaCancelarOnClickListener)
                        .setCancelable(false).show();
                return true;
                */
            case R.id.rechazarEncuesta:

                editTextObservacionRechazar = new EditText(context);
                editTextObservacionRechazar.setHint("Observaciones");
                editTextObservacionRechazar.setTextColor(getResources().getColor(R.color.color_texto));

                new AlertDialog.Builder(PreguntasActivity.this)
                        .setTitle("Alerta")
                        .setView(editTextObservacionRechazar)
                        .setMessage("¿Desea rechazar la encuesta?")
                        .setPositiveButton("Rechazar encuesta", alertaAceptarRechazarOnClickListener)
                        .setNegativeButton("Continuar encuesta", alertaCancelarOnClickListener)
                        .setCancelable(false).show();
                return true;
        }
        return true;
    }

    protected boolean conectadoInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }

        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }

        return false;
    }
}


