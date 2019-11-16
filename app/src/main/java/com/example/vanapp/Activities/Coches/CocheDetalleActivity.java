package com.example.vanapp.Activities.Coches;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Activities.MasterActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Common.Constantes;
import com.example.vanapp.Activities.Common.UsuariosCochesActivity;
import com.example.vanapp.Activities.Rondas.RondasCocheActivity;
import com.example.vanapp.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import com.example.vanapp.Entities.Coche;

public class CocheDetalleActivity extends MasterActivity {

    DatabaseManager databaseManager;
    Coche cocheActual;
    String idCoche;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private TextInputLayout til_nombre;
    private TextInputLayout til_matricula;
    private TextInputLayout til_num_plazas;
    private EditText txt_nombre;
    private EditText txt_matricula;
    private EditText txt_num_plazas;
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
        setContentView(R.layout.activity_coche_detalle);

        idCoche = setIdCoche();

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        enlazarEventosConObjetos();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        botonEliminar.setVisibility(View.INVISIBLE);
        if (!esAltaCoche())
        {
            mostrarDetalleCoche();
        }
    }


    /*Además del menú de MasterActivity, inyecta otro menú*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_extra_coche_detalle, menu);

        //Establece el color del menú secundario
        Drawable dwUsuariosDelCoche = menu.findItem(R.id.opcionUsuariosDelCoche).getIcon();
        Drawable dwRondasDelCoche = menu.findItem(R.id.opcionRondasDelCoche).getIcon();

        dwUsuariosDelCoche = DrawableCompat.wrap(dwUsuariosDelCoche);
        dwRondasDelCoche = DrawableCompat.wrap(dwRondasDelCoche);

        DrawableCompat.setTint(dwUsuariosDelCoche, ContextCompat.getColor(this, R.color.colorWhite));
        DrawableCompat.setTint(dwRondasDelCoche, ContextCompat.getColor(this, R.color.colorWhite));

        menu.findItem(R.id.opcionUsuariosDelCoche).setIcon(dwUsuariosDelCoche);
        menu.findItem(R.id.opcionRondasDelCoche).setIcon(dwRondasDelCoche);

        return true;
    }

    @Override
    //Para poder interactuar con el segundo menú propio de esta pantalla
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.opcionUsuariosDelCoche:
                mostrarActividadUsuariosEnCoche();
                break;
            case R.id.opcionRondasDelCoche:
                mostrarActividadRondasEnCoche();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*En caso de que retorne "" quiere decir que es un alta */
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

    private boolean esAltaCoche(){
        if (idCoche.equals(""))
            return true;
        else
            return false;
    }

    private void enlazarEventosConObjetos(){
        // Referencias TILs
        til_nombre = findViewById(R.id.til_nombre);
        til_matricula = findViewById(R.id.til_matricula);
        til_num_plazas = findViewById(R.id.til_num_plazas);

        // Referencias TILs
        txt_nombre = findViewById(R.id.txt_nombre);
        txt_matricula = findViewById(R.id.txt_matricula);
        txt_num_plazas = findViewById(R.id.txt_num_plazas);

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
                confirmarEliminarCoche();
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
                mostrarActividadListadoCoches();
            }
        });

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarOActualizarCoche();
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

        txt_matricula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_matricula);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        txt_num_plazas.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esValorValido(til_num_plazas);
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
                esValorValido(til_matricula) &&
                esValorValido(til_num_plazas)){
            return true;
        }

        return false;
    }

    private boolean insertarOActualizarCoche()
    {
        boolean operacionOk = false;
        if (esEntradaDatosCorrecta()){
            if (esAltaCoche())
            {
                operacionOk = insertarCoche();
            }
            else {
                operacionOk = actualizarCoche();
            }

            if (operacionOk){
                Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
                mostrarActividadListadoCoches();
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

    private Coche bindCoche(){
        cocheActual = new Coche();
        cocheActual.setNombre(txt_nombre.getText().toString());
        cocheActual.setMatricula(txt_matricula.getText().toString());
        cocheActual.setNumPlazas(Integer.parseInt(txt_num_plazas.getText().toString()));
        cocheActual.setColorCoche(tv_color.getText().toString());

        return cocheActual;
    }

    private boolean insertarCoche() {
        cocheActual = bindCoche();
        if (cocheActual.esEstadoValido())
            return databaseManager.insertarCoche(cocheActual);
        else
            return false;
    }

    private boolean actualizarCoche() {
        cocheActual = bindCoche();
        cocheActual.setIdCoche(setIdCoche());
        if (cocheActual.esEstadoValido())
            return databaseManager.actualizarCoche(cocheActual);
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

    private void confirmarEliminarCoche(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tituloConfirmaEliminar);
        builder.setMessage(R.string.msgConfirmarEliminarCoche);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.txtAceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarCoche();
            }
        });

        builder.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void eliminarCoche(){
        boolean esOperacionCorrecta = false;
        esOperacionCorrecta = databaseManager.eliminarCoche(idCoche, false);

        if (esOperacionCorrecta){
            Toast.makeText(getApplicationContext(), R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
            mostrarActividadListadoCoches();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDetalleCoche(){
        cocheActual = databaseManager.obtenerCoche(idCoche);

        txt_nombre.setText(cocheActual.getNombre());
        txt_matricula.setText(cocheActual.getMatricula());
        txt_num_plazas.setText(Integer.toString(cocheActual.getNumPlazas()));

        String fechaParseada = Utilidades.getFechaToString(cocheActual.getFechaAlta());
        tv_fecha_alta.setText(fechaParseada);
        tv_color.setText(cocheActual.getColorCoche());

        int colorParseado = Color.parseColor("#" + (cocheActual.getColorCoche()));
        tv_color.setBackgroundColor(colorParseado);
        iv_avatar.setColorFilter(colorParseado);

        //Sólo motrará el botón para eliminar si es un coche existente
        botonEliminar.setVisibility(View.VISIBLE);
    }

    private void mostrarActividadListadoCoches() {
        Intent intent = new Intent(this, CochesActivity.class);
        startActivity(intent);
        finish();
    }

    private void mostrarActividadUsuariosEnCoche() {
        Intent intent = new Intent(this, UsuariosCochesActivity.class);
        intent.putExtra("ID_COCHE", idCoche);
        startActivity(intent);
    }

    private void mostrarActividadRondasEnCoche() {
        Intent intent = new Intent(this, RondasCocheActivity.class);
        intent.putExtra("ID_COCHE", idCoche);
        startActivity(intent);
    }
}