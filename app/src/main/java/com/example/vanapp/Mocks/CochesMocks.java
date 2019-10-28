package com.example.vanapp.Mocks;

import com.example.vanapp.Entities.Coche;

import java.util.ArrayList;

public class CochesMocks {
    public static ArrayList obtenerCochesRandom(int numeroCochesParaObtener) {
        ArrayList<Coche> listaCochess = new ArrayList<>();

        for (int i = 0; i < numeroCochesParaObtener; i++){
            Coche nuevoCoche = new Coche();

            nuevoCoche.setNombre(UtilidadesMock.generarRandomString(5));
            nuevoCoche.setMatricula(UtilidadesMock.generarRandomString(10));
            nuevoCoche.setNumPlazas(5);

            listaCochess.add(nuevoCoche);
        }

        return listaCochess;
    }
}
