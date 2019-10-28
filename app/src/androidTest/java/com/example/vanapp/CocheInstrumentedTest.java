package com.example.vanapp;

import android.content.Context;

import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Mocks.CochesMocks;
import com.example.vanapp.Dal.DatabaseManager;
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
public class CocheInstrumentedTest {
    ArrayList<Coche> listaCoches;
    DatabaseManager databaseManager;
    boolean esOperacionCorrecta;

    @Test
    public void esInsercionDeCochesCorrecto() {
        //Arrange
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        int numeroCochesParaInsertar = 10;

        //Act
        esOperacionCorrecta = databaseManager.eliminarCochesTodos();
        listaCoches = CochesMocks.obtenerCochesRandom(numeroCochesParaInsertar);
        for (Coche nuevoCoche: listaCoches) {
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
        }

        listaCoches = databaseManager.obtenerCoches();

        //Assert
        assertEquals(numeroCochesParaInsertar, listaCoches.size());
    }

    @Test
    public void esBorradoTodosCochesFisicoCorrecto() {
        //Arrange
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        listaCoches = CochesMocks.obtenerCochesRandom(1);
        for (Coche nuevoCoche: listaCoches) {
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
        }

        //Act
        esOperacionCorrecta = databaseManager.eliminarCochesTodos();
        listaCoches = databaseManager.obtenerCoches();

        //Assert
        int numeroDeCochesDespuesBorrado = 0;
        assertEquals(numeroDeCochesDespuesBorrado, listaCoches.size());
    }

    @Test
    public void esBorradoDeUnCocheLogicoCorrecto() {
        //Arrange
        Coche cocheInvestigar;
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        String idCoche = "";

        listaCoches = CochesMocks.obtenerCochesRandom(1);
        for (Coche nuevoCoche: listaCoches) {
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
            idCoche = nuevoCoche.getIdCoche();
        }

        //Act
        esOperacionCorrecta = databaseManager.eliminarCoche(idCoche, true);
        cocheInvestigar = databaseManager.obtenerCoche(idCoche);

        //Assert
        assertEquals(false, cocheInvestigar.getActivo());
    }

    @Test
    public void esActualizadoDeUnCocheCorrecto() {
        //Arrange
        Coche cocheInvestigar;
        Coche cocheEsperado = new Coche();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);

        listaCoches = CochesMocks.obtenerCochesRandom(1);
        for (Coche nuevoCoche: listaCoches) {
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
            cocheEsperado = nuevoCoche;
        }

        cocheEsperado.setNombre("David");
        cocheEsperado.setMatricula("Santesteban");
        cocheEsperado.setNumPlazas(7);

        //Act
        esOperacionCorrecta = databaseManager.actualizarCoche(cocheEsperado);
        cocheInvestigar = databaseManager.obtenerCoche(cocheEsperado.getIdCoche());

        //Assert
        assertEquals(cocheEsperado.getNombreCompleto(), cocheInvestigar.getNombreCompleto());
        assertEquals(true, cocheInvestigar.esEstadoValido());
    }
}