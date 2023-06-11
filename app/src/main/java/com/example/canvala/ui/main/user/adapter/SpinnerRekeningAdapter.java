package com.example.canvala.ui.main.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.canvala.data.model.RekeningModel;

import java.util.List;

public class SpinnerRekeningAdapter extends ArrayAdapter<RekeningModel> {

   public SpinnerRekeningAdapter(@NonNull Context context, List<RekeningModel> rekeningmodel){
            super(context, android.R.layout.simple_spinner_item, rekeningmodel);
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setText(getItem(position).getBankName());
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setText(getItem(position).getBankName());
            return view;
        }



    public Integer getRekeningId(int position) {
        return getItem(position).getIdRekening();
    }





    }
