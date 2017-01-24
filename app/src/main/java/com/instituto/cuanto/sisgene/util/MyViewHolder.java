package com.instituto.cuanto.sisgene.util;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by rusve on 14/09/2016.
 */
public class MyViewHolder {

    public TextView tvTitle;
    public EditText etRpta;

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
