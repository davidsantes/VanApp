package com.example.vanapp.PresentationLayerUsuarios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.MasterActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Common.Constantes;
import com.example.vanapp.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import com.example.vanapp.Entities.Usuario;

public class UsuarioDetalleActivity extends MasterActivity {

    DatabaseManager databaseManager;
    Usuario usuarioActual;
    String idUsuario;

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
    private TextView tv_color;
    private TextView tv_fecha_alta;

    private Button botonEliminar;
    private Button botonCancelar;
    private Button botonAceptar;
    private Button botonEligeColor;

    private ImageView iv_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalle);

        idUsuario = setIdUsuario();

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        botonEliminar.setVisibility(View.INVISIBLE);
        if (!esAltaUsuario())
        {
            mostrarDetalleUsuario();
        }
    }

    /*En caso de que retorne "" quiere decir que es un alta */
    private String setIdUsuario()
    {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            return "";
        }
        else {
            return extras.getString("ID_USUARIO");
        }
    }

    private boolean esAltaUsuario(){
        if (idUsuario.equals(""))
            return true;
        else
            return false;
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

        tv_color = findViewById(R.id.tv_color);
        tv_fecha_alta = findViewById(R.id.tv_fecha_alta);

        // Referencias Botones
        botonEliminar = findViewById(R.id.boton_eliminar);
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonAceptar = findViewById(R.id.boton_aceptar);
        botonEligeColor = findViewById(R.id.boton_eligeColor);
        iv_avatar = findViewById(R.id.iv_avatar);

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarEliminarUsuario();
            }
        });

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
                insertarOActualizarUsuario();
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
            esValorValido(til_email)){
            return true;
        }

        return false;
    }

    private boolean insertarOActualizarUsuario()
    {
        boolean operacionOk = false;
        if (esEntradaDatosCorrecta()){
            if (esAltaUsuario())
            {
                operacionOk = insertarUsuario();
            }
            else {
                operacionOk = actualizarUsuario();
            }

            if (operacionOk){
                Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
                mostrarActividadListadoUsuarios();
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

    private Usuario bindUsuario(){
        usuarioActual = new Usuario();
        usuarioActual.setNombre(txt_nombre.getText().toString());
        usuarioActual.setApellido1(txt_apellido1.getText().toString());
        usuarioActual.setApellido2(txt_apellido2.getText().toString());
        usuarioActual.setAlias(txt_alias.getText().toString());
        usuarioActual.setEmail(txt_email.getText().toString());
        usuarioActual.setColorUsuario(tv_color.getText().toString());

        return usuarioActual;
    }

    private boolean insertarUsuario() {
        usuarioActual = bindUsuario();
        if (usuarioActual.esEstadoValido())
            return databaseManager.insertarUsuario(usuarioActual);
        else
            return false;
    }

    private boolean actualizarUsuario() {
        usuarioActual = bindUsuario();
        usuarioActual.setIdUsuario(setIdUsuario());
        if (usuarioActual.esEstadoValido())
            return databaseManager.actualizarUsuario(usuarioActual);
        else
            return false;
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
                        tv_color.setText(Integer.toHexString(selectedColor));
                        tv_color.setBackgroundColor(selectedColor);
                        iv_avatar.setColorFilter(selectedColor);
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

    private void confirmarEliminarUsuario(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarUsuario);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarUsuario();
            }
        });

        builder.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void eliminarUsuario(){
        boolean esOperacionCorrecta = false;
        esOperacionCorrecta = databaseManager.eliminarUsuario(idUsuario, false);

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            mostrarActividadListadoUsuarios();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDetalleUsuario(){
        usuarioActual = databaseManager.obtenerUsuario(idUsuario);

        txt_nombre.setText(usuarioActual.getNombre());
        txt_apellido1.setText(usuarioActual.getApellido1());
        txt_apellido2.setText(usuarioActual.getApellido2());
        txt_alias.setText(usuarioActual.getAlias());
        txt_email.setText(usuarioActual.getEmail());
        tv_fecha_alta.setText(usuarioActual.getFechaToString());
        tv_color.setText(usuarioActual.getColorUsuario());

        int colorParseado = Color.parseColor("#" + (usuarioActual.getColorUsuario()));
        tv_color.setBackgroundColor(colorParseado);
        iv_avatar.setColorFilter(colorParseado);

        //Sólo motrará el botón para eliminar si es un usuario existente
        botonEliminar.setVisibility(View.VISIBLE);
    }

    private void mostrarActividadListadoUsuarios() {
        Intent intent = new Intent(this, UsuariosActivity.class);
        startActivity(intent);
        finish();
    }
}
