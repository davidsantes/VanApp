package com.example.vanapp;

import android.content.Context;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;
import com.example.vanapp.Mocks.CochesMocks;
import com.example.vanapp.Dal.DatabaseManager;
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
public class CochesConUsuariosTest {
    ArrayList<Coche> listaCoches;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<UsuarioCoche> listaUsuariosCoches;
    DatabaseManager databaseManager;
    boolean esOperacionCorrecta;

    @Test
    public void esInsercionDeCochesConUsuariosCorrecto() {
        //Arrange
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);
        int numeroCochesParaInsertar = 1;
        int numeroUsuariosParaInsertarEnCoche = 3;

        esOperacionCorrecta = databaseManager.eliminarCochesTodos();
        esOperacionCorrecta = databaseManager.eliminarUsuariosTodos();
        listaCoches = CochesMocks.obtenerCochesRandom(numeroCochesParaInsertar);
        for (Coche nuevoCoche: listaCoches) {
            esOperacionCorrecta = databaseManager.insertarCoche(nuevoCoche);
        }
        listaUsuarios = UsuariosMocks.obtenerUsuariosRandom(numeroUsuariosParaInsertarEnCoche);
        for (Usuario nuevoUsuario: listaUsuarios) {
            esOperacionCorrecta = databaseManager.insertarUsuario(nuevoUsuario);
        }
        String idCoche = listaCoches.get(numeroCochesParaInsertar-1).getIdCoche();

        //Act
        for (Usuario usuario: listaUsuarios) {
            String idUsuario = usuario.getIdUsuario();
            UsuarioCoche usuarioCocheParaAnalizar = new UsuarioCoche(idUsuario, idCoche);
            esOperacionCorrecta = databaseManager.insertarUsuarioCoche(usuarioCocheParaAnalizar);
        }

        //Assert
        listaUsuariosCoches = databaseManager.obtenerUsuariosDelCoche(idCoche);
        assertEquals(listaUsuarios.size(), listaUsuariosCoches.size());
        assertEquals(true, listaUsuariosCoches.get(0).esEstadoValido());
    }

    /*
     * Verifica que un usuario en un coche, se elimina.
     * Es necesario arrancar el test unitario esInsercionDeCochesConUsuariosCorrecto, que inserta datos en la BDD
     * */
    @Test
    public void esBorradoFisicoDeUnUsuarioEnUnCocheCorrecto() {
        //Arrange
        String idUsuario = "";
        String idCoche = "";
        UsuarioCoche usuarioCocheInvestigar = null;

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        databaseManager = DatabaseManager.obtenerInstancia(appContext);

        listaCoches = databaseManager.obtenerCoches();
        idCoche = listaCoches.get(0).getIdCoche();
        listaUsuariosCoches = databaseManager.obtenerUsuariosDelCoche(idCoche);
        if (listaUsuariosCoches.size() > 0 )
            usuarioCocheInvestigar = listaUsuariosCoches.get(0);

        //Act
        idUsuario = usuarioCocheInvestigar.getUsuarioDetalle().getIdUsuario();
        esOperacionCorrecta = databaseManager.eliminarRelacionDeUsuarioConCoche(idUsuario, idCoche, false);

        listaUsuariosCoches = databaseManager.obtenerUsuariosDelCoche(idCoche);
        usuarioCocheInvestigar = Utilidades.encuentraUsuarioCocheEnLista(listaUsuariosCoches, idUsuario, idCoche);
        //Assert
        // Indica que ya no existe
        assertEquals(null, usuarioCocheInvestigar);
    }
}
