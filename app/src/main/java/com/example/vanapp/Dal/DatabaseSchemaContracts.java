package com.example.vanapp.Dal;

import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class DatabaseSchemaContracts {
    private DatabaseSchemaContracts() {
    }

    //USUARIOS
    interface ColumnasTablaUsuarios {
        String ID_USUARIO = "Id";
        String NOMBRE = "Nombre";
        String APELLIDO_1 = "Apellido1";
        String APELLIDO_2 = "Apellido2";
        String ALIAS = "Alias";
        String EMAIL = "Email";
        String COLOR_USUARIO = "ColorUsuario";
        String FECHA_ALTA = "FechaAlta";
        String ACTIVO = "Activo";
    }

    public static class Usuarios implements DatabaseSchemaContracts.ColumnasTablaUsuarios {

    }

    //COCHES
    interface ColumnasTablaCoches {
        String ID_COCHE = "Id";
        String NOMBRE = "Nombre";
        String MATRICULA = "Matricula";
        String NUMERO_PLAZAS = "NumeroPlazas";
        String COLOR_COCHE = "ColorCoche";
        String FECHA_ALTA = "FechaAlta";
        String ACTIVO = "Activo";
    }

    public static class Coches implements DatabaseSchemaContracts.ColumnasTablaCoches {

    }
}
