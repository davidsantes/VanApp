package com.example.vanapp.Activities.Rondas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Common.Constantes;
import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class RondaCocheDetalleActivity extends MasterActivity {
    //Variables
    private DatabaseManager databaseManager;
    private Ronda rondaActual;
    private String idCoche;
    private String idRonda;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private Switch switchRondaFinalizada;
    private TextInputLayout til_alias;
    private EditText txt_alias;
    private TextView tv_fecha_inicio;
    private TextView tv_fecha_fin;

    private Button boton_FechaInicio;
    private Button boton_FechaFin;
    private Button boton_eliminar;
    private Button boton_cancelar;
    private Button boton_aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ronda_coche_detalle);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        setExtras();
        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boton_eliminar.setVisibility(View.INVISIBLE);
        if (esAltaRonda())
        {
            mostrarAltaRonda();
        }else{
            mostrarDetalleRonda();
        }
    }

    private void setExtras()
    {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idCoche = idRonda = "";
        }
        else {
            idCoche = extras.getString("ID_COCHE");
            idRonda = extras.getString("ID_RONDA");
        }
    }

    private boolean esAltaRonda(){
        if (idRonda.equals(""))
            return true;
        else
            return false;
    }

    private void enlazarEventosConObjetos(){
        // Referencias
        til_alias = findViewById(R.id.til_alias);
        txt_alias = findViewById(R.id.txt_alias);
        switchRondaFinalizada = findViewById(R.id.switchRondaFinalizada);
        tv_fecha_inicio = findViewById(R.id.tv_fecha_inicio);
        tv_fecha_fin = findViewById(R.id.tv_fecha_fin);

        txt_alias.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_alias);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

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
                confirmarEliminarRonda();
            }
        });

        boton_cancelar = findViewById(R.id.boton_cancelar);
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarActividadRondasCoche(idCoche);
            }
        });

        boton_aceptar = findViewById(R.id.boton_aceptar);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertarOActualizarRonda();
            }
        });
    }

    private boolean esValorValido(TextInputLayout inputLayoutError) {
        String campoParaAnalizar = inputLayoutError.getEditText().getText().toString();
        if (campoParaAnalizar.length() > Constantes.LONGITUD_CAMPO_LARGA || campoParaAnalizar.length() == 0) {
            inputLayoutError.setError(getResources().getString(R.string.msgErrorValorInvalido));
            return false;
        } else {
            inputLayoutError.setError(null);
        }

        return true;
    }

    private boolean esEntradaDatosCorrecta() {
        if (esValorValido(til_alias)
                && this.tv_fecha_inicio.getText().length() > 0
                && this.tv_fecha_fin.getText().length() > 0
                && Utilidades.esRangoFechasCorrecto(Utilidades.getFechaFromString(this.tv_fecha_inicio.getText().toString())
                , Utilidades.getFechaFromString(this.tv_fecha_fin.getText().toString())))
        {
            return true;
        }

        return false;
    }

    private void mostrarAltaRonda() {
        switchRondaFinalizada.setChecked(false);
    }

    private void mostrarDetalleRonda(){
        rondaActual = databaseManager.obtenerRonda(idRonda);
        txt_alias.setText(rondaActual.getAlias());
        switchRondaFinalizada.setChecked(rondaActual.EsRondaFinalizada());
        String fechaInicioParseada = "";
        fechaInicioParseada = Utilidades.getFechaToString(rondaActual.getFechaInicio());
        tv_fecha_inicio.setText(fechaInicioParseada);

        String fechaFinParseada = "";
        fechaFinParseada = Utilidades.getFechaToString(rondaActual.getFechaFin());
        tv_fecha_fin.setText(fechaFinParseada);

        //Sólo motrará el botón para eliminar si es un usuario existente
        boton_eliminar.setVisibility(View.VISIBLE);
    }

    private boolean insertarOActualizarRonda()
    {
        boolean operacionOk = false;
        if (esEntradaDatosCorrecta()){
            if (esAltaRonda())
            {
                operacionOk = insertarRonda();
            }
            else {
                operacionOk = actualizarRonda();
            }

            if (operacionOk){
                Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
                mostrarActividadRondasCoche(idCoche);
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgDatosIncorrectos, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private Ronda bindRonda(){
        rondaActual = new Ronda();
        rondaActual.setIdCoche(idCoche);
        rondaActual.setAlias(txt_alias.getText().toString());
        rondaActual.setFechaInicio(Utilidades.getFechaFromString(tv_fecha_inicio.getText().toString()));
        rondaActual.setFechaFin(Utilidades.getFechaFromString(tv_fecha_fin.getText().toString()));
        rondaActual.setEsRondaFinalizada(switchRondaFinalizada.isChecked());
        return rondaActual;
    }

    private boolean insertarRonda() {
        rondaActual = bindRonda();
        if (rondaActual.esEstadoValido())
            return databaseManager.insertarRondaDelCoche(rondaActual);
        else
            return false;
    }

    private boolean actualizarRonda() {
        rondaActual = bindRonda();
        rondaActual.setIdRonda(idRonda);
        if (rondaActual.esEstadoValido())
            return databaseManager.actualizarRondaDelCoche(rondaActual);
        else
            return false;
    }

    private void confirmarEliminarRonda(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarRonda);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarRonda();
            }
        });

        builder.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void eliminarRonda(){
        boolean esOperacionCorrecta = false;
        esOperacionCorrecta = databaseManager.eliminarRonda(idRonda, false);

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            mostrarActividadRondasCoche(idCoche);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarActividadRondasCoche(String idCoche) {
        Intent intentActividad = new Intent(this, RondasCocheActivity.class);
        intentActividad.putExtra("ID_COCHE", idCoche);
        startActivity(intentActividad);
    }
}
