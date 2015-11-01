package com.instituto.cuanto.sisgene.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.instituto.cuanto.sisgene.R;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaAbiertaItem;

import java.util.ArrayList;

/**
 * Created by Gustavo on 27/10/2015.
 */
public class TipoPreguntaAbiertaAdapter extends BaseAdapter {

    ArrayList<TipoPreguntaAbiertaItem> myList = new ArrayList<TipoPreguntaAbiertaItem>();
    LayoutInflater inflater;
    Context context;

    public TipoPreguntaAbiertaAdapter() {
    }

    public TipoPreguntaAbiertaAdapter(Context context, ArrayList<TipoPreguntaAbiertaItem> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public void limpiarLista() {
        int dim = myList.size();
        System.out.println("dim myList:" + dim);
        //for (int i = 0; i < dim; i++)
//            myList.remove(0);

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public TipoPreguntaAbiertaItem getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tipopreguntaabiertaitem_layout, parent, false);
            mViewHolder = new MyViewHolder();
            mViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvNombreEncuestado);
            mViewHolder.etRpta = (EditText) convertView.findViewById(R.id.etRespuestaPreguntaEncuestado);
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

    private class MyViewHolder {
        TextView tvTitle;
        EditText etRpta;

    }

}
