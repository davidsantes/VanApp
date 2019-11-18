package com.example.vanapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vanapp.R;

import androidx.appcompat.widget.Toolbar;

public class QuienesSomosActivity extends MasterActivity {

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private Button btIrGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quienes_somos);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enlazarEventosConObjetos() {
        btIrGithub = findViewById(R.id.btIrGithub);

        btIrGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebURL("https://github.com/davidsantes");
            }
        });
    }

    public void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );
        startActivity( browse );
    }
}
