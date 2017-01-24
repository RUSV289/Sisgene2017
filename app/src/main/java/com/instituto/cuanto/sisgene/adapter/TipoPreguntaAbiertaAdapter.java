package com.instituto.cuanto.sisgene.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.instituto.cuanto.sisgene.R;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaAbiertaItem;
import com.instituto.cuanto.sisgene.util.InputFilterMinMax;
import com.instituto.cuanto.sisgene.util.MyViewHolder;

import java.util.ArrayList;

/**
 * Created by Gustavo on 27/10/2015.
 */
public class TipoPreguntaAbiertaAdapter extends BaseAdapter {

    public static MyViewHolder mViewHolder;
    private String subtipo;
    private String tiponumerico;
    private String desde;
    private String hasta;

    //public static ArrayList<Integer> respondidoPreguntas = new ArrayList<Integer>();


    public static ArrayList<TipoPreguntaAbiertaItem> myListPreguntaAbierta = new ArrayList<TipoPreguntaAbiertaItem>();
    LayoutInflater inflater;
    Context context;
    public static TipoPreguntaAbiertaAdapter tipoPreguntaAbiertaAdapter;
    //public TipoPreguntaAbiertaAdapter tipoPreguntaAbiertaAdapter;

    public TipoPreguntaAbiertaAdapter() {
    }

    public TipoPreguntaAbiertaAdapter(Context context, ArrayList<TipoPreguntaAbiertaItem> myListPreguntaAbierta, String subtipo, String tiponumerico, String desde, String hasta) {
        this.myListPreguntaAbierta = myListPreguntaAbierta;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.tipoPreguntaAbiertaAdapter = this;

        this.subtipo = subtipo;
        this.tiponumerico = tiponumerico;
        this.desde = desde;
        this.hasta = hasta;


        System.out.println("respondido  this.respondidoPreguntas:" + this.myListPreguntaAbierta.get(0).getRespondido());
    }

    public void limpiarLista() {
        int dim = myListPreguntaAbierta.size();
        System.out.println("dim myListPreguntaAbierta:" + myListPreguntaAbierta.size());
        if (dim != 0)
            for (int i = 0; i < dim; i++) {
                myListPreguntaAbierta.remove(0);
            }
        tipoPreguntaAbiertaAdapter.notifyDataSetChanged();
        System.out.println("dim myListPreguntaAbierta despues:" + myListPreguntaAbierta.size());
    }

    @Override
    public int getCount() {
        return myListPreguntaAbierta.size();
    }

    @Override
    public TipoPreguntaAbiertaItem getItem(int position) {
        return myListPreguntaAbierta.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tipo_pregunta_abierta_item_layout, parent, false);
            mViewHolder = new MyViewHolder();
            mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvNombreEncuestado);
            mViewHolder.etRpta = (EditText) convertView.findViewById(R.id.etRespuestaPreguntaEncuestado);

            //mViewHolder.etRpta.setInputType(InputType.TYPE_CLASS_NUMBER);
            //mViewHolder.etRpta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            System.out.println("Position: "+position);
            //System.out.println("PrespondidoPreguntas tam: "+respondidoPreguntas.size());
            if(myListPreguntaAbierta.get(position).getRespondido().equals("1")){//esta pregunta ha sido respodida
                mViewHolder.etRpta.setEnabled(false);
            }
            else{
                mViewHolder.etRpta.setEnabled(true);
            }

            if(subtipo.equals("numerico")){
                if(tiponumerico.equals("entero")){
                    System.out.println("Restringir a numerico entero");
                    mViewHolder.etRpta.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if(!(desde.equals("")||hasta.equals(""))){
                        //EditText et = (EditText) findViewById(R.id.myEditText);
                        //et.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
                        mViewHolder.etRpta.setFilters(new InputFilter[]{new InputFilterMinMax(desde, hasta)});
                    }
                }
                else
                    if(tiponumerico.equals("decimal")){
                        System.out.println("Restringir a numerico decimal");
                        mViewHolder.etRpta.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            }


            pasarTextoMayucula(mViewHolder.etRpta);

            convertView.setTag(mViewHolder);
            notifyDataSetChanged();
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final TipoPreguntaAbiertaItem currentTipoPreguntaAbiertaItem = getItem(position);

        mViewHolder.tvTitle.setText(currentTipoPreguntaAbiertaItem.getTitle());
        mViewHolder.etRpta.setHint(currentTipoPreguntaAbiertaItem.getDescription());



        final int i = position;
        mViewHolder.etRpta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //((ListItem) myItems.get(i)).caption = s.toString();
                currentTipoPreguntaAbiertaItem.setDescription(s.toString());
            }
        });

        return convertView;
    }

    public class MyViewHolder2 {
        TextView tvTitle;
        EditText etRpta;

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
        }

        public EditText getEtRpta() {
            return etRpta;
        }

        public void setEtRpta(EditText etRpta) {
            this.etRpta = etRpta;
        }
    }

    public void pasarTextoMayucula(EditText et2){
        final EditText et = et2;
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String cadena = s.toString();
                if(!cadena.equals(cadena.toUpperCase())){
                    cadena = cadena.toUpperCase();
                    et.setText(cadena);
                }
                int textLength = et.getText().length();
                et.setSelection(textLength, textLength);
            }
        });
    }

    public MyViewHolder getmViewHolder() {
        return mViewHolder;
    }

    public void setmViewHolder(MyViewHolder mViewHolder) {
        this.mViewHolder = mViewHolder;
    }
}
