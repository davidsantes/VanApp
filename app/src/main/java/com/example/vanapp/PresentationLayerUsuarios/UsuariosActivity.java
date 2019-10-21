package com.example.vanapp.PresentationLayerUsuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.vanapp.MainActivity;
import com.example.vanapp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class UsuariosActivity extends AppCompatActivity {
    //Componentes de la capa de presentación
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

        btn_nuevo_usuario = (Button)findViewById(R.id.btn_nuevo_usuario);
        btn_nuevo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mostrarActividadNuevoUsuario();
            }
        });

        btn_sign_out = (Button)findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //Logout
            AuthUI.getInstance()
                    .signOut(UsuariosActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(UsuariosActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); //Para que se elimine la actividad de presentación
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UsuariosActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            }
        });

        mostrarResultados();
    }

    private void mostrarActividadNuevoUsuario() {
        Intent intentActividadUsuarioDetalleNuevo = new Intent(this, UsuarioExistenteActivity.class);
        startActivity(intentActividadUsuarioDetalleNuevo);
    }

    /**
     * Muestra los datos que hay en la tabla de resultados
     */
    public void mostrarResultados(){
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
