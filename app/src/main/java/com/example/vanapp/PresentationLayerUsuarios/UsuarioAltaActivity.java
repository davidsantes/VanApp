package com.example.vanapp.PresentationLayerUsuarios;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.vanapp.MasterActivity;
import com.example.vanapp.R;

public class UsuarioAltaActivity extends MasterActivity {

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_alta);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
