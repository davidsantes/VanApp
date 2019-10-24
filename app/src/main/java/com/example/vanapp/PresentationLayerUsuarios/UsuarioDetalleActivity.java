package com.example.vanapp.PresentationLayerUsuarios;

import androidx.appcompat.widget.Toolbar;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.MasterActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Common.Constantes;
import com.example.vanapp.Mocks.UtilidadesMock;
import com.example.vanapp.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import com.example.vanapp.Entities.Usuario;

public class UsuarioDetalleActivity extends MasterActivity {

    DatabaseManager databaseManager;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;

    private TextInputLayout til_nombre;
    private TextInputLayout til_apellido1;
    private TextInputLayout til_apellido2;
    private TextInputLayout til_alias;
    private TextInputLayout til_email;

    private EditText txt_nombre;
    private EditText txt_apellido1;
    private EditText txt_apellido2;
    private EditText txt_alias;
    private EditText txt_email;
    private TextView txtColor;
    private TextView tv_fecha_alta;

    private Button botonCancelar;
    private Button botonAceptar;
    private Button botonEligeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void enlazarEventosConObjetos(){
        // Referencias TILs
        til_nombre = findViewById(R.id.til_nombre);
        til_apellido1 = findViewById(R.id.til_apellido1);
        til_apellido2 = findViewById(R.id.til_apellido2);
        til_alias = findViewById(R.id.til_alias);
        til_email =  findViewById(R.id.til_email);

        // Referencias TILs
        txt_nombre = findViewById(R.id.txt_nombre);
        txt_apellido1 = findViewById(R.id.txt_apellido1);
        txt_apellido2 = findViewById(R.id.txt_apellido2);
        txt_alias = findViewById(R.id.txt_alias);
        txt_email = findViewById(R.id.txt_email);

        txtColor = findViewById(R.id.txtcolor);
        tv_fecha_alta = findViewById(R.id.tv_fecha_alta);

        // Referencias Botones
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonAceptar = findViewById(R.id.boton_aceptar);
        botonEligeColor = findViewById(R.id.boton_eligeColor);

        botonEligeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eligeColor(v);
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarActividadListadoUsuarios();
            }
        });

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (esEntradaDatosCorrecta() && insertarUsuario()){
                    Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_nombre);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        txt_apellido1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_apellido1);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        txt_apellido2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_apellido2);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

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

        txt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_email);
            }
            @Override
            public void afterTextChanged(Editable s) { }
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
        if (esValorValido(til_nombre) &&
        esValorValido(til_apellido1) &&
        esValorValido(til_apellido2) &&
        esValorValido(til_alias) &&
        esValorValido(til_email) &&
        txtColor.getText().length() > 0){
            return true;
        }

        return false;
    }

    private boolean insertarUsuario() {
        boolean esOperacionCorrecta = false;

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(UtilidadesMock.generarRandomString(5));
        nuevoUsuario.setApellido1(UtilidadesMock.generarRandomString(5));
        nuevoUsuario.setApellido2(UtilidadesMock.generarRandomString(5));
        nuevoUsuario.setAlias(UtilidadesMock.generarRandomString(10));
        nuevoUsuario.setEmail("aaa@gmail.com");
        nuevoUsuario.setColorUsuario("E46AFF");

        esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
        return esOperacionCorrecta;
    }

    private void eligeColor(View view)
    {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle(R.string.msgInfoEligeColor)
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton(R.string.txtAceptar, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                    txtColor.setText(Integer.toHexString(selectedColor));
                    txtColor.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void mostrarActividadListadoUsuarios() {
        Intent intent = new Intent(this, UsuariosActivity.class);
        startActivity(intent);
        finish();
    }
}
