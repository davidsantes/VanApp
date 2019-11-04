package com.example.vanapp.Activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vanapp.Activities.Coches.CochesActivity;
import com.example.vanapp.Activities.Usuarios.UsuariosActivity;
import com.example.vanapp.R;

public class MenuInicialActivity extends MasterActivity {

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    ImageButton imageViewUsuarios;
    ImageButton imageViewCoches;
    ImageButton imageViewCalendario;
    ImageButton imageViewPagos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        enlazarEventosConObjetos();
    }

    private void enlazarEventosConObjetos(){
        imageViewUsuarios = findViewById(R.id.imageViewUsuarios);
        imageViewCoches = findViewById(R.id.imageViewCoches);
        imageViewCalendario = findViewById(R.id.imageViewCalendario);
        imageViewPagos = findViewById(R.id.imageViewPagos);

        imageViewUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadUsuarios();
            }
        });

        imageViewCoches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadCoches();
            }
        });

        imageViewCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadCalendario();
            }
        });

        imageViewPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadPagos();
            }
        });
    }

    private void mostrarActividadUsuarios() {
        Intent intentActividad = new Intent(this, UsuariosActivity.class);
        startActivity(intentActividad);
    }

    private void mostrarActividadCoches() {
        Intent intentActividad = new Intent(this, CochesActivity.class);
        startActivity(intentActividad);
    }

    //TODO: por implementar
    private void mostrarActividadCalendario() {
        Toast.makeText(getApplicationContext(), "Por implementar", Toast.LENGTH_SHORT).show();
        //Intent intentActividad = new Intent(this, UsuarioDetalleActivity.class);
        //startActivity(intentActividad);
    }

    //TODO: por implementar
    private void mostrarActividadPagos() {
        Toast.makeText(getApplicationContext(), "¡Tendrás que esperar a la siguiente versión! :)", Toast.LENGTH_SHORT).show();
        //Intent intentActividad = new Intent(this, UsuarioDetalleActivity.class);
        //startActivity(intentActividad);
    }
}
