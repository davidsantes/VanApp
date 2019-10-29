package com.example.vanapp.PresentationLayerUsuarios;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.R;

import java.util.ArrayList;

//https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
public class UsuariosAdapter extends ArrayAdapter<Usuario> {
    public UsuariosAdapter(Context context, ArrayList<Usuario> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Recoge el usuario concreto
        Usuario usuarioActual = getItem(position);
        // Verifica si la vista está siendo reusada, en caso contrario, se inyecta
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_usuario, parent, false);
        }

        // Lookup para alimentar de datos
        TextView textViewNombreCompleto = (TextView) convertView.findViewById(R.id.textViewNombreCompleto);
        TextView textViewAlias = (TextView) convertView.findViewById(R.id.textViewAlias);
        ImageView iv_avatar = (ImageView)convertView.findViewById(R.id.iv_avatar);
        iv_avatar.setColorFilter(Color.parseColor("#" + usuarioActual.getColorUsuario()));

        textViewNombreCompleto.setText(usuarioActual.getNombreCompleto());
        textViewAlias.setText(usuarioActual.getAlias());

        // Se retorna la vista completa para renderizarla en pantalla
        return convertView;
    }
}
