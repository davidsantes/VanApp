package com.example.vanapp.Activities.Usuarios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.R;

import java.util.ArrayList;

public class UsuariosActivity extends MasterActivity {

    //Variables
    private ArrayList<Usuario> listaUsuarios;
    private DatabaseManager databaseManager;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private Button btn_nuevo_usuario;
    private Button btn_eliminar_todos;
    private ListView listViewUsuarios;

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
        btn_nuevo_usuario = findViewById(R.id.btn_nuevo_usuario);
        btn_nuevo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadUsuarioDetalle("");
            }
        });

        btn_eliminar_todos = findViewById(R.id.btn_eliminar_todos);
        btn_eliminar_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarEliminarTodos();
            }
        });
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    private void mostrarResultados(){
        listaUsuarios = new ArrayList<Usuario>();
        listViewUsuarios = findViewById(R.id.listViewUsuarios);
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se inserta la lista rellenada dentro del adapter
        listaUsuarios = databaseManager.obtenerUsuarios();
        UsuariosAdapter adapter = new UsuariosAdapter(this, listaUsuarios);
        listViewUsuarios.setAdapter(adapter);

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Usuario usuarioItem = (Usuario)listViewUsuarios.getItemAtPosition(position);
            mostrarActividadUsuarioDetalle(usuarioItem.getIdUsuario());
            }
        });
    }

    private void confirmarEliminarTodos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarTodosUsuarios);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarTodos();
            }
        });

        builder.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void eliminarTodos(){
        boolean esOperacionCorrecta = false;
        esOperacionCorrecta = databaseManager.eliminarUsuariosTodos();

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            //Se carga a sí misma
            Intent intentActividad = new Intent(this, UsuariosActivity.class);
            startActivity(intentActividad);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarActividadUsuarioDetalle(String idUsuario) {
        Intent intentActividad = new Intent(this, UsuarioDetalleActivity.class);
        intentActividad.putExtra("ID_USUARIO", idUsuario);
        startActivity(intentActividad);
    }
}
