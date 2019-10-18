package com.example.vanapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vanapp.Dal.DatabaseHelper;
import com.example.vanapp.Dal.DatabaseSchema;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class TestActivity extends AppCompatActivity {

    Button btn_sign_out;

    EditText etNombrePersona;
    EditText etEdad;
    EditText etPais;

    String nombre;
    int edad;
    String pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn_sign_out = (Button)findViewById(R.id.btn_sign_out);
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout
                AuthUI.getInstance()
                        .signOut(TestActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish(); //Para que se elimine la actividad de presentación
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TestActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void anyadirPersona (View v){
        etNombrePersona = (EditText)findViewById(R.id.etNombrePersona);
        etEdad = (EditText)findViewById(R.id.etEdad);
        etPais = (EditText)findViewById(R.id.etPais);

        nombre = etNombrePersona.getText().toString();
        pais = etPais.getText().toString();
        String numEdad = etEdad.getText().toString();
        edad = Integer.parseInt(numEdad);

        if (nombre.trim().length() == 0 || nombre == null || numEdad.trim().length() == 0 || numEdad == null  || pais.trim().length() == 0 || pais == null ){
            Toast.makeText(this, "Es obligatorio introducir los tres datos", Toast.LENGTH_LONG).show();
        }
        else{

            DatabaseHelper db = new DatabaseHelper(this);

            if (db.buscarPersona(nombre)){
                Toast.makeText(this, "Esta persona ya se ha introducido en la BDD", Toast.LENGTH_LONG).show();
            }
            else{
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseSchema.TABLA_LISTA_PERSONAS_NOMBRE, nombre);
                contentValues.put(DatabaseSchema.TABLA_LISTA_PERSONAS_EDAD, edad);
                contentValues.put(DatabaseSchema.TABLA_LISTA_PERSONAS_PAIS, pais);
                db.insertarPersonaEnLaLista(contentValues);

                //Va a la misma vista, pero con otras acciones
                Intent intent = new Intent(this, TestActivity.class);
                startActivity(intent);

                Toast.makeText(this, "Se ha añadido la persona en VanApp", Toast.LENGTH_LONG).show();
                finish();
            }

            //DIALOGOS: https://developer.android.com/guide/topics/ui/dialogs?hl=es-419
            //DETALLES De un datos específico de una vista: https://stackoverflow.com/questions/5299324/how-to-view-detailed-data-of-a-specific-list-view-item
        }
    }

    public void irAVerPersonas(View v){{
    }
        Intent intent = new Intent(this, UsuariosActivity.class);
        startActivity(intent);
    }

    public void borrarVista(View v){
        DatabaseHelper db = new DatabaseHelper(this);
        db.borrarTablaPersonas();
        Toast.makeText(this, "Se ha borrado la lista de personas", Toast.LENGTH_LONG).show();
    }
}
