package com.instituto.cuanto.sisgene.entities;

import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by USUARIO on 08/11/2015.
 */
public class TipoPreguntaMatrizMultipleItem {
    ArrayList<RespuestaItem> respuestas;
    ArrayList<String> vertical;
    ArrayList<String> horizontal;
    Boolean hasView;
    String title;
    public TableLayout tbLayout;

    public TipoPreguntaMatrizMultipleItem(){
        hasView = false;
        respuestas = new ArrayList<>();
    }

    public ArrayList<RespuestaItem> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(ArrayList<RespuestaItem> respuestas) {
        this.respuestas = respuestas;
    }

    public Boolean getHasView() {
        return hasView;
    }

    public void setHasView(Boolean hasView) {
        this.hasView = hasView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getVertical() {
        return vertical;
    }

    public void setVertical(ArrayList<String> vertical) {
        this.vertical = vertical;
    }

    public ArrayList<String> getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(ArrayList<String> horizontal) {
        this.horizontal = horizontal;
    }

    public int buscarRespuestas(String checkbox) {
        int found = -1;
        for (int i=0;i<respuestas.size();i++){
            if(respuestas.get(i).getTexto().equals(checkbox)){
                found = i;
            }
        }
        return found;
    }

    public RespuestaItem isRespuesta(int row, int col){
        for(int i = 0; i < respuestas.size(); i++){
            if(respuestas.get(i).getRow() == row && respuestas.get(i).getCol() == col)
                return respuestas.get(i);
        }
        return null;
    }
}
