package com.example.vanapp.Mocks;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Usuario;

import java.util.ArrayList;

public class UsuariosMocks {
    public static ArrayList obtenerUsuariosRandom(int numeroUsuariosParaObtener) {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        for (int i = 0; i < numeroUsuariosParaObtener; i++){
            Usuario nuevoUsuario = new Usuario();

            nuevoUsuario.setNombre(UtilidadesMock.generarRandomString(5));
            nuevoUsuario.setApellido1(UtilidadesMock.generarRandomString(5));
            nuevoUsuario.setApellido2(UtilidadesMock.generarRandomString(5));
            nuevoUsuario.setAlias(UtilidadesMock.generarRandomString(10));
            nuevoUsuario.setEmail("aaa@gmail.com");
            nuevoUsuario.setColorUsuario("E46AFF");

            listaUsuarios.add(nuevoUsuario);
        }

        return listaUsuarios;
    }
}
