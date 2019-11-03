package com.example.vanapp.PresentationLayerCoches;

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
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.MasterActivity;
import com.example.vanapp.R;

import java.util.ArrayList;

public class CochesActivity extends MasterActivity {
    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    Button btn_nuevo_coche;
    Button btn_eliminar_todos;
    ListView listViewCoches;

    //Variables
    ArrayList<Coche> listaCoches;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coches);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        enlazarEventosConObjetos();
        mostrarResultados();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enlazarEventosConObjetos(){
        btn_nuevo_coche = findViewById(R.id.btn_nuevo_coche);
        btn_nuevo_coche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadCocheDetalle("");
            }
        });

        btn_eliminar_todos = findViewById(R.id.btn_eliminar_todos);
        btn_eliminar_todos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarEliminarTodos();
            }
        });

        listViewCoches = findViewById(R.id.listViewCoches);
        listViewCoches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Coche cocheItem = (Coche) listViewCoches.getItemAtPosition(position);
                mostrarActividadCocheDetalle(cocheItem.getIdCoche());
            }
        });
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    private void mostrarResultados(){
        listaCoches = new ArrayList<Coche>();
        listViewCoches = findViewById(R.id.listViewCoches);
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se inserta la lista rellenada dentro del adapter
        listaCoches = databaseManager.obtenerCoches();
        if (listaCoches != null && listaCoches.size() > 0){
            CochesAdapter adapter = new CochesAdapter(this, listaCoches);
            listViewCoches.setAdapter(adapter);
        }
    }

    private void confirmarEliminarTodos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarTodosCoches);
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
        esOperacionCorrecta = databaseManager.eliminarCochesTodos();

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            //Se carga a sí misma
            Intent intentActividad = new Intent(this, CochesActivity.class);
            startActivity(intentActividad);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarActividadCocheDetalle(String idCoche) {
        Intent intentActividad = new Intent(this, CocheDetalleActivity.class);
        intentActividad.putExtra("ID_COCHE", idCoche);
        startActivity(intentActividad);
    }
}
