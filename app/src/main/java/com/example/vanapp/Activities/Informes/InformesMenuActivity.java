package com.example.vanapp.Activities.Informes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.R;

import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

public class InformesMenuActivity extends MasterActivity {

    //Variables
    private int anyoBusqueda = Calendar.getInstance().get(Calendar.YEAR);

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private Button btIrInformeTotalConducciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes_menu);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enlazarEventosConObjetos();
    }

    private void enlazarEventosConObjetos(){
        btIrInformeTotalConducciones = findViewById(R.id.btIrInformeTotalConducciones);
        btIrInformeTotalConducciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostraDialogAnyoInforme();
            }
        });
    }

    private void mostraDialogAnyoInforme(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.tituloIntroduceAnyoInforme);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(Integer.toString(anyoBusqueda));
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                anyoBusqueda = Integer.parseInt(input.getText().toString());
                mostrarActividadInformeTotalConducciones();
            }
        });
        alert.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    private void mostrarActividadInformeTotalConducciones(){
        Intent intentActividad = new Intent(this, InformeConduccionesActivity.class);
        intentActividad.putExtra("ANYO_BUSQUEDA", Integer.toString(anyoBusqueda));
        startActivity(intentActividad);
    }
}
