package com.example.vanapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.vanapp.PresentationLayerUsuarios.UsuariosActivity;

public class MainSplashActivity extends AppCompatActivity {
    static final int TIEMPO_EN_PANTALLA = 5000; //5 SEGUNDOS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);

        TextView texto = (TextView) findViewById(R.id.tvTituloProgramaAnimado);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacion_splash);
        texto.startAnimation(animation);

        //Mostrará la pantalla durante 2 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainSplashActivity.this, MenuInicialActivity.class);
                startActivity(intent);
                finish(); //Para que se elimine la actividad de presentación
            }
        },TIEMPO_EN_PANTALLA);
    }
}

