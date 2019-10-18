package com.example.vanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vanapp.Dal.DatabaseHelper;
import com.example.vanapp.Entities.Persona;
import java.util.ArrayList;

public class UsuariosActivity extends AppCompatActivity {

    ListView lstPersonas;
    ArrayList<Persona> personas;
    ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        personas = new ArrayList<Persona>();
        lstPersonas = (ListView)findViewById(R.id.lstUsuarios);
        DatabaseHelper db = new DatabaseHelper(this);
        personas = db.obtenerPersonas();

        ArrayList<String> listaPersonasParseada = new ArrayList<>();
        for (Persona persona: personas) {
            String personaParseada = "\n" + " Nombre: " + persona.getNombre() + "\n" + " Edad: " + persona.getEdad() + "\n" + " Páis: " + persona.getPais();
            listaPersonasParseada.add(personaParseada);
        }

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaPersonasParseada);
        lstPersonas.setAdapter(adaptador);
    }
}
