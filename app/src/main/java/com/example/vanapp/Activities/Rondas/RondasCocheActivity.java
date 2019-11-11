package com.example.vanapp.Activities.Rondas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.R;

import java.util.ArrayList;

public class RondasCocheActivity extends MasterActivity {

    //Variables
    private DatabaseManager databaseManager;
    private Coche cocheActual;
    private String idCoche;
    private ArrayList<Ronda> listaRondasDelCoche;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private TextView textViewNombreCoche;
    private TextView textViewMatricula;
    private ListView listViewRondasEnElCoche;
    private Button btn_nueva_ronda;
    private Button btn_eliminar_todas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rondas_coche);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        idCoche = setIdCoche();
        cocheActual = null;

        enlazarEventosConObjetos();
        mostrarCabecera();
        mostrarRondasEnCoche();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Coche getCocheActual() {
        if (cocheActual == null){
            cocheActual = databaseManager.obtenerInstancia(getApplicationContext()).obtenerCoche(idCoche);
        }
        return cocheActual;
    }

    /*Retorna el id pasado a la actividad */
    private String setIdCoche()
    {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            return "";
        }
        else {
            return extras.getString("ID_COCHE");
        }
    }

    private void mostrarCabecera(){
        textViewNombreCoche = findViewById(R.id.textViewNombreCoche);
        textViewNombreCoche.setText(getCocheActual().getNombre());

        textViewMatricula = findViewById(R.id.textViewMatricula);
        textViewMatricula.setText(cocheActual.getMatricula());

        ImageView ivAvatarCoche = findViewById(R.id.iv_avatar);
        ivAvatarCoche.setColorFilter(Color.parseColor("#" + cocheActual.getColorCoche()));
    }

    private void enlazarEventosConObjetos(){
        btn_nueva_ronda = findViewById(R.id.btn_nueva_ronda);
        btn_nueva_ronda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadRondaDetalle("", idCoche);
            }
        });

        btn_eliminar_todas = findViewById(R.id.btn_eliminar_todas);
        btn_eliminar_todas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarEliminarTodos();
            }
        });
    }

    private void confirmarEliminarTodos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarTodasRondas);
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
        esOperacionCorrecta = databaseManager.eliminarRondasDeUnCoche(setIdCoche(), false);

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            //Se carga a sí misma
            Intent intentActividad = new Intent(this, RondasCocheActivity.class);
            intentActividad.putExtra("ID_COCHE", idCoche);
            startActivity(intentActividad);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    private void mostrarRondasEnCoche(){
        listaRondasDelCoche = new ArrayList<Ronda>();
        listViewRondasEnElCoche = findViewById(R.id.listViewRondasEnElCoche);

        //Se inserta la lista rellenada dentro del adapter
        listaRondasDelCoche = getCocheActual().getListaRondasDelCoche();
        if (listaRondasDelCoche != null && listaRondasDelCoche.size() > 0){
            RondasCochesAdapter adapter = new RondasCochesAdapter(this, listaRondasDelCoche);
            listViewRondasEnElCoche.setAdapter(adapter);
        }

        listViewRondasEnElCoche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ronda rondaItem = (Ronda)listViewRondasEnElCoche.getItemAtPosition(position);
                mostrarActividadRondaDetalle(rondaItem.getIdRonda(), rondaItem.getIdCoche());
            }
        });
    }

    private void mostrarActividadRondaDetalle(String idRonda, String idCoche) {
        Intent intentActividad = new Intent(this, RondaCocheDetalleActivity.class);
        intentActividad.putExtra("ID_COCHE", idCoche);
        intentActividad.putExtra("ID_RONDA", idRonda);
        startActivity(intentActividad);
    }
}
