package com.example.vanapp.PresentationLayerUsuarios;

import androidx.appcompat.widget.Toolbar;

import com.example.vanapp.ColorPickerTestActivity;
import com.example.vanapp.MasterActivity;

import android.content.DialogInterface;
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
import com.example.vanapp.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

public class UsuarioExistenteActivity extends MasterActivity {

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

    private Button botonAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_existente);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Referencias TILs
        til_nombre = (TextInputLayout) findViewById(R.id.til_nombre);
        til_apellido1 = (TextInputLayout) findViewById(R.id.til_apellido1);
        til_apellido2 = (TextInputLayout) findViewById(R.id.til_apellido2);
        til_alias = (TextInputLayout) findViewById(R.id.til_alias);
        til_email = (TextInputLayout) findViewById(R.id.til_email);

        // Referencias TILs
        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_apellido1 = (EditText) findViewById(R.id.txt_apellido1);
        txt_apellido2 = (EditText) findViewById(R.id.txt_apellido2);
        txt_alias = (EditText) findViewById(R.id.txt_alias);
        txt_email = (EditText) findViewById(R.id.txt_email);

        txtColor = (TextView)findViewById(R.id.txtcolor);
        tv_fecha_alta = (TextView) findViewById(R.id.tv_fecha_alta);

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

        // Referencia Botón
        botonAceptar = (Button) findViewById(R.id.boton_aceptar);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
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

    private void validarDatos() {
        String campoParaAnalizar = til_nombre.getEditText().getText().toString();
        esValorValido(til_nombre);

        campoParaAnalizar = til_apellido1.getEditText().getText().toString();
        esValorValido(til_apellido1);

        campoParaAnalizar = til_apellido2.getEditText().getText().toString();
        esValorValido(til_apellido2);

        campoParaAnalizar = til_alias.getEditText().getText().toString();
        esValorValido(til_alias);

        campoParaAnalizar = til_email.getEditText().getText().toString();
        esValorValido(til_email);
    }

    public void pickColor(View view)
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
                        Toast.makeText(UsuarioExistenteActivity.this, R.string.msgInfoColorSeleccionado + ": 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
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
}
