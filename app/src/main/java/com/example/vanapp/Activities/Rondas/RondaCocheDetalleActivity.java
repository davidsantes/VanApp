package com.example.vanapp.Activities.Rondas;

import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.R;

import java.util.Calendar;

public class RondaCocheDetalleActivity extends MasterActivity {
    //Variables
    private DatabaseManager databaseManager;
    private Coche cocheActual;
    private String idCoche;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private Switch switchRondaFinalizada;
    private Button boton_FechaInicio;
    private Button boton_FechaFin;
    private Button boton_eliminar;
    private Button boton_cancelar;
    private Button boton_aceptar;
    private TextView tv_fecha_inicio;
    private TextView tv_fecha_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ronda_coche_detalle);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        idCoche = setIdCoche();
        cocheActual = null;

        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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

    private void enlazarEventosConObjetos(){
        tv_fecha_inicio = findViewById(R.id.tv_fecha_inicio);
        tv_fecha_fin = findViewById(R.id.tv_fecha_fin);

        boton_FechaInicio = findViewById(R.id.boton_FechaInicio);
        boton_FechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar calendario = Calendar.getInstance();
                int anioActual = calendario.get(Calendar.YEAR);
                int mesActual = calendario.get(Calendar.MONTH);
                int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RondaCocheDetalleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tv_fecha_inicio.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, anioActual, mesActual, diaActual);
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.getInstance().getFirstDayOfWeek());
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                datePickerDialog.show();
            }
        });

        boton_FechaFin = findViewById(R.id.boton_FechaFin);
        boton_FechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar calendario = Calendar.getInstance();
                int anioActual = calendario.get(Calendar.YEAR);
                int mesActual = calendario.get(Calendar.MONTH);
                int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(RondaCocheDetalleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tv_fecha_fin.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, anioActual, mesActual, diaActual);
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.getInstance().getFirstDayOfWeek());
                datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
                datePickerDialog.show();
            }
        });

        boton_eliminar = findViewById(R.id.boton_eliminar);
        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        boton_cancelar = findViewById(R.id.boton_cancelar);
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        boton_aceptar = findViewById(R.id.boton_aceptar);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
