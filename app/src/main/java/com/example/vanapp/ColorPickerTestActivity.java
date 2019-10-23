package com.example.vanapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.*;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import androidx.appcompat.widget.Toolbar;

public class ColorPickerTestActivity extends MasterActivity {

    private Toolbar menuToolbar;
    private TextView txtColor;

    /*Además del menú de MasterActivity, inyecta otra opción más*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compartir, menu);

        return true;
    }

    @Override
    //Para poder interactuar con el segundo menú propio de esta pantalla
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.opcionCompartir:
                Toast.makeText(this,  "share!!!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_test);

        menuToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuToolbar);

        txtColor = (TextView)findViewById(R.id.txtcolor);

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        Toast.makeText(ColorPickerTestActivity.this, R.string.msgInfoColorSeleccionado + ": 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
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
