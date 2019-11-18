package com.example.vanapp.Activities;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vanapp.Activities.Calendario.CalendarioRondaElegirActivity;
import com.example.vanapp.Activities.Coches.CochesActivity;
import com.example.vanapp.Activities.Informes.InformesMenuActivity;
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
    private ImageButton imageViewInformes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enlazarEventosConObjetos(){
        imageViewUsuarios = findViewById(R.id.imageViewUsuarios);
        imageViewCoches = findViewById(R.id.imageViewCoches);
        imageViewCalendario = findViewById(R.id.imageViewCalendario);
        imageViewInformes = findViewById(R.id.imageViewInformes);

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

        imageViewInformes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadInformes();
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
        boolean existenRondas = databaseManager.existenRondas();
        if (existenRondas){
            Intent intentActividad = new Intent(this, CalendarioRondaElegirActivity.class);
            startActivity(intentActividad);
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.msgNoExistenRondas, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarActividadInformes() {
        Intent intentActividad = new Intent(this, InformesMenuActivity.class);
        startActivity(intentActividad);
    }
}
