package com.example.vanapp.Activities.Calendario;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.vanapp.Activities.Coches.CochesAdapter;
import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Activities.Rondas.RondasCochesAdapter;
import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.R;

import java.util.ArrayList;

public class CalendarioRondaElegirActivity extends MasterActivity {

    //Variables
    private ArrayList<Coche> listaCoches;
    private ArrayList<Ronda> listaRondasDelCoche;
    private DatabaseManager databaseManager;
    private String idCocheElegido;
    private String idRondaElegida;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private Button btnEligeCoche;
    private Button btnEligeRonda;
    private Button btnIrAlCanlendario;
    private Button btnLimpiarValores;
    ListView listViewCoches;
    private ListView listViewRondasEnElCoche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_ronda_elegir);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        idCocheElegido = "";
        idRondaElegida = "";

        enlazarEventosConObjetos();
        visualizacionBotones();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enlazarEventosConObjetos(){
        btnEligeCoche = findViewById(R.id.botonEligeCoche);
        btnEligeCoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogoEligeCoche();
            }
        });

        btnEligeRonda = findViewById(R.id.botonEligeRonda);
        btnEligeRonda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialogoEligeRonda();
            }
        });

        btnIrAlCanlendario = findViewById(R.id.btnIrAlCanlendario);
        btnIrAlCanlendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadCalendarioRondaDetalle("","");
            }
        });

        btnLimpiarValores = findViewById(R.id.btnLimpiarValores);
        btnLimpiarValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarValores();
                visualizacionBotones();
            }
        });
    }

    private void abrirDialogoEligeCoche(){
        listaCoches = new ArrayList<Coche>();
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se configura la lista que posteriormente se insertará en el diálogo
        LayoutInflater inflater = getLayoutInflater();
        View vistaListaCochesPersonalizada = inflater.inflate(R.layout.lista_coches_calendario, null);
        listViewCoches = vistaListaCochesPersonalizada.findViewById(R.id.listViewCoches);
        //Se inserta la lista rellenada dentro del adapter
        listaCoches = databaseManager.obtenerCoches();
        CochesAdapter adapter = new CochesAdapter(this, listaCoches);
        listViewCoches.setAdapter(adapter);

        //Se configura el diálogo
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CalendarioRondaElegirActivity.this);
        alertDialog.setTitle(R.string.txtEligeCoche);
        alertDialog.setView(vistaListaCochesPersonalizada);
        alertDialog.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                idCocheElegido = "";
                visualizacionBotones();
                dialog.dismiss();
            }
        });

        listViewCoches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Coche cocheActual = (Coche) listViewCoches.getItemAtPosition(position);

                TextView textViewNombreCoche = findViewById(R.id.textViewNombreCoche);
                TextView textViewMatricula = findViewById(R.id.textViewMatricula);
                ImageView iv_avatar = findViewById(R.id.iv_avatar);
                iv_avatar.setColorFilter(Color.parseColor("#" + cocheActual.getColorCoche()));

                textViewNombreCoche.setText(cocheActual.getNombre());
                textViewMatricula.setText(cocheActual.getMatricula());

                idCocheElegido = cocheActual.getIdCoche();
                idRondaElegida = "";

                visualizacionBotones();
            }
        });

        alertDialog.show();
    }

    private void abrirDialogoEligeRonda(){
        listaRondasDelCoche = new ArrayList<Ronda>();
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se configura la lista que posteriormente se insertará en el diálogo
        LayoutInflater inflater = getLayoutInflater();
        View vistaListaRondasDelCochePersonalizada = inflater.inflate(R.layout.lista_rondas_calendario, null);
        listViewRondasEnElCoche = vistaListaRondasDelCochePersonalizada.findViewById(R.id.listViewRondasEnElCoche);
        //Se inserta la lista rellenada dentro del adapter
        listaRondasDelCoche = databaseManager.obtenerRondasDelCoche(idCocheElegido);
        RondasCochesAdapter adapter = new RondasCochesAdapter(this, listaRondasDelCoche);
        listViewRondasEnElCoche.setAdapter(adapter);

        //Se configura el diálogo
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CalendarioRondaElegirActivity.this);
        alertDialog.setTitle(R.string.txtEligeRonda);
        alertDialog.setView(vistaListaRondasDelCochePersonalizada);
        alertDialog.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                idRondaElegida = "";
                visualizacionBotones();
                dialog.dismiss();
            }
        });

        listViewRondasEnElCoche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ronda rondaActual = (Ronda) listViewRondasEnElCoche.getItemAtPosition(position);

                TextView textViewPeriodo = findViewById(R.id.textViewPeriodo);
                TextView textViewNombreRonda = findViewById(R.id.textViewNombreRonda);
                Switch switchRondaFinalizada = findViewById(R.id.switchRondaFinalizada);

                String periodo = Utilidades.getFechaToString(rondaActual.getFechaInicio())
                        + " - "
                        + Utilidades.getFechaToString(rondaActual.getFechaFin());
                textViewPeriodo.setText(periodo);
                textViewNombreRonda.setText(rondaActual.getAlias());
                switchRondaFinalizada.setChecked(rondaActual.EsRondaFinalizada());

                idCocheElegido = rondaActual.getIdCoche();
                idRondaElegida = rondaActual.getIdRonda();

                visualizacionBotones();
            }
        });

        alertDialog.show();
    }

    private void visualizacionBotones()
    {
        //Si tengo todos los datos para ir al calendario
        if (idCocheElegido.length() > 0  && idRondaElegida.length() > 0 ){
            btnIrAlCanlendario.setEnabled(true);
            btnEligeCoche.setEnabled(false);
            btnEligeRonda.setEnabled(false);
        }
        //Si no he elegido coche ni ronda
        if (idCocheElegido.length() == 0 && idRondaElegida.length() == 0) {
            btnIrAlCanlendario.setEnabled(false);
            btnEligeCoche.setEnabled(true);
            btnEligeRonda.setEnabled(false);
        }
        //Si he elegido coche pero no ronda
        if (idCocheElegido.length() > 0 && idRondaElegida.length() == 0)
        {
            btnIrAlCanlendario.setEnabled(false);
            btnEligeCoche.setEnabled(false);
            btnEligeRonda.setEnabled(true);
        }
    }

    private void limpiarValores(){
        idCocheElegido = "";
        idRondaElegida = "";
        TextView textViewNombreCoche = findViewById(R.id.textViewNombreCoche);
        TextView textViewMatricula = findViewById(R.id.textViewMatricula);
        textViewNombreCoche.setText("NombreCoche");
        textViewMatricula.setText("Matricula");

        TextView textViewPeriodo = findViewById(R.id.textViewPeriodo);
        TextView textViewNombreRonda = findViewById(R.id.textViewNombreRonda);
        Switch switchRondaFinalizada = findViewById(R.id.switchRondaFinalizada);
        textViewPeriodo.setText("Periodo");
        textViewNombreRonda.setText("Ronda");
        switchRondaFinalizada.setChecked(false);

    }

    private void mostrarActividadCalendarioRondaDetalle(String idCoche, String idRonda) {
        Intent intentActividad = new Intent(this, CalendarioRondaDetalleActivity.class);
        intentActividad.putExtra("ID_COCHE", idCoche);
        intentActividad.putExtra("ID_RONDA", idRonda);
        startActivity(intentActividad);
    }
}