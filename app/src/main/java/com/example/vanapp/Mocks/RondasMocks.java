package com.example.vanapp.Mocks;

import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioRonda;

import java.util.ArrayList;
import java.util.Calendar;

public class RondasMocks {
    public static ArrayList obtenerRondasRandom(int numeroRondasParaObtener, String idCoche) {
        ArrayList<Ronda> listaRondas = new ArrayList<>();

        for (int i = 0; i < numeroRondasParaObtener; i++){
            Ronda nuevaRonda = new Ronda();
            nuevaRonda.setIdCoche(idCoche);
            nuevaRonda.setAlias(UtilidadesMock.generarRandomString(10));

            listaRondas.add(nuevaRonda);
        }

        return listaRondas;
    }

    public static ArrayList obtenerUsuariosRondasRandom(ArrayList<Usuario> listaUsuarios, String idRonda) {
        ArrayList<UsuarioRonda> listaUsuariosEnRondas = new ArrayList<>();

        for (Usuario nuevoUsuario: listaUsuarios) {
            UsuarioRonda nuevoUsuarioEnRonda = new UsuarioRonda();
            nuevoUsuarioEnRonda.setIdUsuario(nuevoUsuario.getIdUsuario());
            nuevoUsuarioEnRonda.setIdRonda(idRonda);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 7);
            cal.getTime();
            nuevoUsuarioEnRonda.setFechaDeConduccion(cal.getTime());
            listaUsuariosEnRondas.add(nuevoUsuarioEnRonda);
        }

        return listaUsuariosEnRondas;
    }
}
