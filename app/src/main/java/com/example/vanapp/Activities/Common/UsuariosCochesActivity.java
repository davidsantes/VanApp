package com.example.vanapp.Activities.Common;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;
import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.R;

import java.util.ArrayList;

public class UsuariosCochesActivity extends MasterActivity {

    DatabaseManager databaseManager;
    private Coche cocheActual;
    String idCoche;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private TextView textViewNombreCoche;
    private TextView textViewMatricula;
    private Button btnVincularUsuario;
    ListView listViewUsuariosEnElCoche;

    //Variables
    ArrayList<UsuarioCoche> listaUsuariosDelCoche;
    ArrayList<Usuario> listaUsuariosNoIncluidosEnCoche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_coches);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        idCoche = setIdCoche();
        cocheActual = null;

        enlazarEventosConObjetos();
        mostrarCabecera();
        mostrarUsuariosEnCoche();

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

    private void enlazarEventosConObjetos(){
        btnVincularUsuario = findViewById(R.id.btnVincularUsuario);
        btnVincularUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoUsuariosCoches(idCoche);
            }
        });

        listViewUsuariosEnElCoche = findViewById(R.id.listViewUsuariosEnElCoche);
        listViewUsuariosEnElCoche.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean  onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                UsuarioCoche usuarioItem = (UsuarioCoche)listViewUsuariosEnElCoche.getItemAtPosition(position);
                confirmarEliminarUsuario(usuarioItem.getUsuarioDetalle().getIdUsuario(), usuarioItem.getIdCoche());
                return true;
            }
        });
    }

    private void mostrarCabecera(){
        textViewNombreCoche = findViewById(R.id.textViewNombreCoche);
        textViewNombreCoche.setText(getCocheActual().getNombre());

        textViewMatricula = findViewById(R.id.textViewMatricula);
        textViewMatricula.setText(cocheActual.getMatricula());

        ImageView ivAvatarCoche = findViewById(R.id.iv_avatar);
        ivAvatarCoche.setColorFilter(Color.parseColor("#" + cocheActual.getColorCoche()));
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    private void mostrarUsuariosEnCoche(){
        listaUsuariosDelCoche = new ArrayList<UsuarioCoche>();
        listViewUsuariosEnElCoche = findViewById(R.id.listViewUsuariosEnElCoche);

        //Se inserta la lista rellenada dentro del adapter
        listaUsuariosDelCoche = getCocheActual().getListaUsuariosEnCoche();
        if (listaUsuariosDelCoche != null && listaUsuariosDelCoche.size() > 0){
            UsuariosCochesAdapter adapter = new UsuariosCochesAdapter(this, listaUsuariosDelCoche);
            listViewUsuariosEnElCoche.setAdapter(adapter);
        }
    }

    private void mostrarDialogoUsuariosCoches(final String idCoche) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.titulo_usuario_selecciona);

        //Carga de los usuarios que aún no están incluidos en el coche
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UsuariosCochesActivity.this, android.R.layout.select_dialog_singlechoice);
        listaUsuariosNoIncluidosEnCoche = databaseManager.obtenerInstancia(getApplicationContext()).obtenerUsuariosNoDelCoche(idCoche);
        for (Usuario usuario: listaUsuariosNoIncluidosEnCoche) {
            String usuarioAlias = usuario.getAlias();
            arrayAdapter.add(usuarioAlias);
        }

        alertDialog.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String usuarioSeleccionadoAlias = arrayAdapter.getItem(which);

                Usuario nuevoUsuario = Utilidades.encuentraUsuarioEnLista(listaUsuariosNoIncluidosEnCoche, "", usuarioSeleccionadoAlias);
                UsuarioCoche nuevoUsuarioCoche = new UsuarioCoche(nuevoUsuario.getIdUsuario(), idCoche);
                nuevoUsuarioCoche.setUsuarioDetalle(nuevoUsuario);

                databaseManager.obtenerInstancia(getApplicationContext()).insertarUsuarioCoche(nuevoUsuarioCoche);

                AlertDialog.Builder builderInner = new AlertDialog.Builder(UsuariosCochesActivity.this);
                builderInner.setTitle(R.string.titulo_usuario_selecciona);
                builderInner.setMessage(R.string.msgOperacionOk);
                builderInner.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mostrarActividadUsuariosCoche(idCoche);
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        alertDialog.show();
    }

    private void confirmarEliminarUsuario(final String idUsuario, final String idCoche){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarRelacionUsuarioCoche);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarUsuario(idUsuario, idCoche);
            }
        });

        builder.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void eliminarUsuario(String idUsuario, String idCoche){
        boolean esOperacionCorrecta = false;
        esOperacionCorrecta = databaseManager.obtenerInstancia(getApplicationContext()).eliminarRelacionDeUsuarioConCoche(idUsuario, idCoche, false);

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            mostrarActividadUsuariosCoche(idCoche);
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarActividadUsuariosCoche(String idCoche) {
        Intent intentActividad = new Intent(this, UsuariosCochesActivity.class);
        intentActividad.putExtra("ID_COCHE", idCoche);
        startActivity(intentActividad);
    }
}