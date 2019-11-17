package com.example.vanapp.Activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vanapp.Activities.Calendario.CalendarioRondaElegirActivity;
import com.example.vanapp.Activities.Coches.CochesActivity;
import com.example.vanapp.Activities.Usuarios.UsuariosActivity;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.R;

public class MenuInicialActivity extends MasterActivity {

    //Variables
    private DatabaseManager databaseManager;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private ImageButton imageViewUsuarios;
    private ImageButton imageViewCoches;
    private ImageButton imageViewCalendario;
    private ImageButton imageViewPagos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

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
        boolean existenUsuarios = databaseManager.existenUsuarios();
        if (existenUsuarios){
            Intent intentActividad = new Intent(this, CochesActivity.class);
            startActivity(intentActividad);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.msgNoExistenUsuarios, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarActividadCalendario() {
        boolean existenRondasConUsuariosAsignados = databaseManager.existenRondas();
        if (existenRondasConUsuariosAsignados){
            Intent intentActividad = new Intent(this, CalendarioRondaElegirActivity.class);
            startActivity(intentActividad);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.msgNoExistenRondas, Toast.LENGTH_SHORT).show();
        }
    }

    //TODO: por implementar
    private void mostrarActividadPagos() {
        Toast.makeText(getApplicationContext(), R.string.msgProximaVersion, Toast.LENGTH_SHORT).show();
        //Intent intentActividad = new Intent(this, UsuarioDetalleActivity.class);
        //startActivity(intentActividad);
    }
}
