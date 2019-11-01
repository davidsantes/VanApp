package com.example.vanapp;

import android.content.Context;

import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioRonda;
import com.example.vanapp.Mocks.CochesMocks;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Mocks.RondasMocks;
import com.example.vanapp.Mocks.UsuariosMocks;

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
public class RondasConUsuariosTest {
    ArrayList<Coche> listaCoches;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<Ronda> listaRondas;
    ArrayList<UsuarioRonda> listaDeUsuariosEnRonda;
    DatabaseManager databaseManager;
    boolean esOperacionCorrecta;

    @Test
    public void esInsercionDeRondaConUsuariosCorrecta() {
        //Arrange
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        int numeroCochesParaInsertar = 1;
        int numeroUsuariosParaRondas = 1;
        int numeroRondasParaInsertar = 1;
        String idCoche = "";
        String idRonda = "";

        //Eliminamos todos los datos
        esOperacionCorrecta = databaseManager.eliminarCochesTodos();
        esOperacionCorrecta = databaseManager.eliminarUsuariosTodos();

        //Inserción de coches
        listaCoches = CochesMocks.obtenerCochesRandom(numeroCochesParaInsertar);
        for (Coche nuevoCoche: listaCoches) {
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
        }

        //Inserción de usuarios
        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(numeroUsuariosParaRondas);
        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
        }
        //Recogemos el primer coche insertado
        idCoche = listaCoches.get(numeroCochesParaInsertar-1).getIdCoche();

        //Inserción de rondas
        listaRondas = RondasMocks.obtenerRondasRandom(numeroRondasParaInsertar, idCoche);
        for (Ronda nuevaRonda: listaRondas) {
            esOperacionCorrecta = databaseManager.insertarRondaDelCoche(nuevaRonda);
        }
        //Recogemos la primera ronda insertada
        idRonda = listaRondas.get(numeroRondasParaInsertar-1).getIdRonda();

        listaDeUsuariosEnRonda = RondasMocks.obtenerUsuariosRondasRandom(listaUsuarios, idRonda);
        for (UsuarioRonda nuevoUsuarioEnRonda: listaDeUsuariosEnRonda) {
            esOperacionCorrecta = databaseManager.insertarUsuarioRonda(nuevoUsuarioEnRonda);
        }

        //Act
        listaDeUsuariosEnRonda = databaseManager.obtenerUsuariosDeLaRonda(idRonda);

        //Assert
        assertEquals(numeroUsuariosParaRondas, listaDeUsuariosEnRonda.size());
    }
}
