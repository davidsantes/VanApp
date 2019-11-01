package com.example.vanapp;

import android.content.Context;

import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.Mocks.CochesMocks;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Mocks.RondasMocks;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RondasTest {
    ArrayList<Coche> listaCoches;
    ArrayList<Ronda> listaRondas;
    DatabaseManager databaseManager;
    boolean esOperacionCorrecta;

    @Test
    public void esInsercionDeRondaCorrecta() {
        //Arrange
        String idCoche = "";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        int numeroCochesParaInsertar = 1;
        int numeroRondasParaInsertar = 5;

        esOperacionCorrecta = databaseManager.eliminarCochesTodos();
        listaCoches = CochesMocks.obtenerCochesRandom(numeroCochesParaInsertar);
        for (Coche nuevoCoche: listaCoches) {
            idCoche = nuevoCoche.getIdCoche();
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
        }

        //Act
        listaRondas = RondasMocks.obtenerRondasRandom(numeroRondasParaInsertar, idCoche);
        for (Ronda nuevaRonda: listaRondas) {
            esOperacionCorrecta = databaseManager.insertarRondaDelCoche(nuevaRonda);
        }

        listaRondas = databaseManager.obtenerRondasDelCoche(idCoche);

        //Assert
        assertEquals(numeroRondasParaInsertar, listaRondas.size());
    }
}
