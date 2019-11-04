package com.example.vanapp.Activities.Rondas;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.R;

public class RondasCocheActivity extends MasterActivity {
    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondas_coche);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
