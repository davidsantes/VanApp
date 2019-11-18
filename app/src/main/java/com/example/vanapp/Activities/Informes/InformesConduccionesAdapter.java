package com.example.vanapp.Activities.Informes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import com.example.vanapp.Entities.UsuarioInformeConducciones;
import com.example.vanapp.R;

import java.util.ArrayList;

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class InformesConduccionesAdapter extends ArrayAdapter<UsuarioInformeConducciones> {
    public InformesConduccionesAdapter(Context context, ArrayList<UsuarioInformeConducciones> usuarioInformes) {
        super(context, 0, usuarioInformes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recoge el usuario concreto
        UsuarioInformeConducciones usuarioActual = getItem(position);
        // Verifica si la vista está siendo reusada, en caso contrario, se inyecta
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_usuario_informe_conduccion, parent, false);
        }

        // Lookup para alimentar de datos
        TextView textViewNombreCompleto = (TextView) convertView.findViewById(R.id.textViewNombreCompleto);
        TextView textViewAlias = (TextView) convertView.findViewById(R.id.textViewAlias);
        ImageView iv_avatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
        iv_avatar.setColorFilter(Color.parseColor("#" + usuarioActual.getColorUsuario()));
        TextView textViewTotalConducciones = (TextView) convertView.findViewById(R.id.textViewTotalConducciones);

        textViewNombreCompleto.setText(usuarioActual.getNombreCompleto());
        textViewAlias.setText(usuarioActual.getAlias());
        textViewTotalConducciones.setText(Integer.toString(usuarioActual.getTotalConducciones()));

        // Se retorna la vista completa para renderizarla en pantalla
        return convertView;
    }
}
