package com.example.vanapp;

import android.content.Context;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Mocks.UsuariosMocks;
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
public class UsuarioInstrumentedTest {
    ArrayList<Usuario> listaUsuarios;
    DatabaseManager databaseManager;
    boolean esOperacionCorrecta;

    @Test
    public void esInsercionDeUsuariosCorrecto() {
        //Arrange
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        int numeroUsuariosParaInsertar = 10;

        //Act
        esOperacionCorrecta = databaseManager.eliminarUsuariosTodos();
        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(numeroUsuariosParaInsertar);
        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
        }

        listaUsuarios = databaseManager.obtenerUsuarios();

        //Assert
        assertEquals(numeroUsuariosParaInsertar, listaUsuarios.size());
    }

    @Test
    public void esBorradoTodosUsuariosFisicoCorrecto() {
        //Arrange
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(1);
        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
        }

        //Act
        esOperacionCorrecta = databaseManager.eliminarUsuariosTodos();
        listaUsuarios = databaseManager.obtenerUsuarios();

        //Assert
        int numeroDeUsuariosDespuesBorrado = 0;
        assertEquals(numeroDeUsuariosDespuesBorrado, listaUsuarios.size());
    }

    @Test
    public void esBorradoDeUnUsuarioLogicoCorrecto() {
        //Arrange
        Usuario usuarioInvestigar;
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        String idUsuario = "";

        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(1);
        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
            idUsuario = nuevoUsuario.getIdUsuario();
        }

        //Act
        esOperacionCorrecta = databaseManager.eliminarUsuario(idUsuario, true);
        usuarioInvestigar = databaseManager.obtenerUsuario(idUsuario);

        //Assert
        assertEquals(false, usuarioInvestigar.esActivo());
    }

    @Test
    public void esActualizadoDeUnUsuarioCorrecto() {
        //Arrange
        Usuario usuarioInvestigar;
        Usuario usuarioEsperado = new Usuario();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);

        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(1);
        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
            usuarioEsperado = nuevoUsuario;
        }

        usuarioEsperado.setNombre("David");
        usuarioEsperado.setApellido1("Santesteban");
        usuarioEsperado.setApellido2("Herrero");
        usuarioEsperado.setEmail("davidsantesilerna@gmail.com");

        //Act
        esOperacionCorrecta = databaseManager.actualizarUsuario(usuarioEsperado);
        usuarioInvestigar = databaseManager.obtenerUsuario(usuarioEsperado.getIdUsuario());

        //Assert
        assertEquals(usuarioEsperado.getNombreCompleto(), usuarioInvestigar.getNombreCompleto());
        assertEquals(true, usuarioInvestigar.esEstadoValido());
    }
}