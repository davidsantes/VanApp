package com.example.vanapp.PresentationLayerCommon;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import com.example.vanapp.Entities.UsuarioCoche;
import com.example.vanapp.R;

import java.util.ArrayList;

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class UsuariosCochesAdapter extends ArrayAdapter<UsuarioCoche> {
    public UsuariosCochesAdapter(Context context, ArrayList<UsuarioCoche> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recoge el UsuarioCoche concreto
        UsuarioCoche usuarioActual = getItem(position);
        // Verifica si la vista está siendo reusada, en caso contrario, se inyecta
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_usuario_coche, parent, false);
        }

        // Lookup para alimentar de datos
        TextView textViewNombreCompleto = (TextView) convertView.findViewById(R.id.textViewNombreCompleto);
        TextView textViewAlias = (TextView) convertView.findViewById(R.id.textViewAlias);
        ImageView iv_avatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
        Switch switchConduce = (Switch)convertView.findViewById(R.id.switchConduce);

        textViewNombreCompleto.setText(usuarioActual.getUsuarioDetalle().getNombre());
        textViewAlias.setText(usuarioActual.getUsuarioDetalle().getAlias());
        iv_avatar.setColorFilter(Color.parseColor("#" + usuarioActual.getUsuarioDetalle().getColorUsuario()));
        switchConduce.setChecked(usuarioActual.getUsuarioDetalle().esConductor());

        // Se retorna la vista completa para renderizarla en pantalla
        return convertView;
    }
}