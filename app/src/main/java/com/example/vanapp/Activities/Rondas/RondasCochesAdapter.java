package com.example.vanapp.Activities.Rondas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.R;

import java.util.ArrayList;

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class RondasCochesAdapter extends ArrayAdapter<Ronda> {
    public RondasCochesAdapter(Context context, ArrayList<Ronda> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recoge el UsuarioCoche concreto
        Ronda rondaActual = getItem(position);
        // Verifica si la vista está siendo reusada, en caso contrario, se inyecta
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_ronda, parent, false);
        }

        // Lookup para alimentar de datos
        TextView textViewPeriodo = (TextView) convertView.findViewById(R.id.textViewPeriodo);
        TextView textViewNombreRonda = (TextView) convertView.findViewById(R.id.textViewNombreRonda);
        Switch switchRondaFinalizada = (Switch)convertView.findViewById(R.id.switchRondaFinalizada);

        String periodo = Utilidades.getFechaToString(rondaActual.getFechaInicio())
                        + " - "
                        + Utilidades.getFechaToString(rondaActual.getFechaFin());

        textViewPeriodo.setText(periodo);
        textViewNombreRonda.setText(rondaActual.getAlias());
        switchRondaFinalizada.setChecked(rondaActual.EsRondaFinalizada());

        // Se retorna la vista completa para renderizarla en pantalla
        return convertView;
    }
}