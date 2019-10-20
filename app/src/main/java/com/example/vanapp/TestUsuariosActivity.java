package com.example.vanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Mocks.UsuariosMocks;

import java.util.ArrayList;

public class TestUsuariosActivity extends AppCompatActivity {
    ListView listViewUsuarios;
    ArrayList<Usuario> listaUsuarios;
    ArrayAdapter adaptador;
    DatabaseManager databaseManager;
    boolean esOperacionCorrecta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_usuarios);

        getApplicationContext().deleteDatabase("pedidos.db;");

        listaUsuarios = new ArrayList<Usuario>();
        listViewUsuarios = (ListView)findViewById(R.id.listViewUsuarios);
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        esOperacionCorrecta = databaseManager.eliminarUsuariosTodos();
        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(5);

        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
        }

        this.listaUsuarios = databaseManager.obtenerUsuarios();

        ArrayList<String> listaUsuariosParseados = new ArrayList<>();
        for (Usuario usuario: this.listaUsuarios) {
            listaUsuariosParseados.add(usuario.toString());
        }

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaUsuariosParseados);
        listViewUsuarios.setAdapter(adaptador);
    }
}
