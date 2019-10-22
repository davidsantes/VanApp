package com.example.vanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.*;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class ColorPickerTestActivity extends AppCompatActivity {

    private TextView txtColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker_test);

        txtColor = (TextView)findViewById(R.id.txtcolor);
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
