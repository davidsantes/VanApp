package com.example.vanapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vanapp.Activities.Coches.CochesActivity;
import com.example.vanapp.Activities.Usuarios.UsuariosActivity;
import com.example.vanapp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Sugerencia de la guía de desarrolladores de Android, para compartir métodos
// https://developer.android.com/guide/topics/ui/menus
public abstract class MasterActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compartido, menu);
        return true;
    }

    @Override
    //Para poder interactuar con un menú
    public boolean onOptionsItemSelected (MenuItem item){
        Intent intentActivity;

        switch (item.getItemId()){
            case R.id.opcionUsuarios:
                intentActivity = new Intent(this, UsuariosActivity.class);
                startActivity(intentActivity);
                break;
            case R.id.opcionCoches:
                intentActivity = new Intent(this, CochesActivity.class);
                startActivity(intentActivity);
                break;
            case R.id.opcionRondas:
                intentActivity = new Intent(this, UsuariosActivity.class);
                startActivity(intentActivity);
                break;
            case R.id.opcionSalir:
                confirmarSalir();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmarSalir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaSalir);
        builder.setMessage(R.string.msgConfirmarSalir);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                salirFirebase();
            }
        });

        builder.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void salirFirebase(){
        //Logout
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MasterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); //Para que se elimine la actividad de presentación
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MasterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}