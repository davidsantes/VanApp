package com.example.vanapp.PresentationLayerUsuarios;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.MasterActivity;
import com.example.vanapp.R;

import java.util.ArrayList;

public class UsuariosActivity extends MasterActivity {
    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    Button btn_nuevo_usuario;
    Button btn_sign_out;
    ListView listViewUsuarios;

    //Variables
    ArrayList<Usuario> listaUsuarios;
    ArrayAdapter adaptador;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        enlazarEventosConObjetos();
        mostrarResultados();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enlazarEventosConObjetos(){
        btn_nuevo_usuario = (Button)findViewById(R.id.btn_nuevo_usuario);
        btn_nuevo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mostrarActividadNuevoUsuario();
            }
        });
    }

    private void mostrarActividadNuevoUsuario() {
        Intent intentActividadUsuarioDetalleNuevo = new Intent(this, UsuarioDetalleActivity.class);
        startActivity(intentActividadUsuarioDetalleNuevo);
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    private void mostrarResultados(){
        listaUsuarios = new ArrayList<Usuario>();
        listViewUsuarios = (ListView)findViewById(R.id.listViewUsuarios);
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());
        
        listaUsuarios = databaseManager.obtenerUsuarios();

        // Create the adapter to convert the array to views
        UsuariosAdapter adapter = new UsuariosAdapter(this, listaUsuarios);

        // Attach the adapter to a ListView
        listViewUsuarios = (ListView) findViewById(R.id.listViewUsuarios);
        listViewUsuarios.setAdapter(adapter);

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuarioItem = (Usuario)listViewUsuarios.getItemAtPosition(position);

                Toast.makeText(getApplicationContext(), usuarioItem.getAlias(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
