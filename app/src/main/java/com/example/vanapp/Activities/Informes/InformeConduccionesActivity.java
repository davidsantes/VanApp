package com.example.vanapp.Activities.Informes;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.UsuarioInformeConducciones;
import com.example.vanapp.R;

import java.util.ArrayList;
import java.util.Calendar;

public class InformeConduccionesActivity extends MasterActivity {

    //Variables
    private ArrayList<UsuarioInformeConducciones> listaUsuarios;
    private DatabaseManager databaseManager;
    private int anyoBusqueda = Calendar.getInstance().get(Calendar.YEAR);

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private ListView listViewInforme;
    private TextView textViewAnyo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe_conducciones);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setAnyoActual();
        enlazarEventosConObjetos();
        mostrarResultados();
    }

    private void enlazarEventosConObjetos(){
        textViewAnyo = findViewById(R.id.textViewAnyo);
        String literal = textViewAnyo.getText() + Integer.toString(anyoBusqueda);
        textViewAnyo.setText(literal);
    }

    /*Retorna el id pasado a la actividad */
    private void setAnyoActual()
    {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            anyoBusqueda = Calendar.getInstance().get(Calendar.YEAR);
        }
        else {
            anyoBusqueda = Integer.parseInt(extras.getString("ANYO_BUSQUEDA"));
        }
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    private void mostrarResultados(){
        listaUsuarios = new ArrayList<UsuarioInformeConducciones>();
        listViewInforme = findViewById(R.id.listViewInforme);
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se inserta la lista rellenada dentro del adapter
        listaUsuarios = databaseManager.obtenerUsuariosInformeConducciones(anyoBusqueda);
        InformesConduccionesAdapter adapter = new InformesConduccionesAdapter(this, listaUsuarios);
        listViewInforme.setAdapter(adapter);
    }
}
