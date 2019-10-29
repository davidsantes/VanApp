package com.example.vanapp.PresentationLayerCoches;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import com.example.vanapp.Entities.Coche;
import com.example.vanapp.R;

import java.util.ArrayList;

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class CochesAdapter extends ArrayAdapter<Coche> {
    public CochesAdapter(Context context, ArrayList<Coche> coches) {
        super(context, 0, coches);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recoge el coche concreto
        Coche cocheActual = getItem(position);
        // Verifica si la vista está siendo reusada, en caso contrario, se inyecta
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_coche, parent, false);
        }

        // Lookup para alimentar de datos
        TextView textViewNombreCoche = (TextView) convertView.findViewById(R.id.textViewNombreCoche);
        TextView textViewMatricula = (TextView) convertView.findViewById(R.id.textViewMatricula);
        ImageView iv_avatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
        iv_avatar.setColorFilter(Color.parseColor("#" + cocheActual.getColorCoche()));

        textViewNombreCoche.setText(cocheActual.getNombre());
        textViewMatricula.setText(cocheActual.getMatricula());

        // Se retorna la vista completa para renderizarla en pantalla
        return convertView;
    }
}