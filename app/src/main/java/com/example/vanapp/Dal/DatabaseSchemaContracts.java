package com.example.vanapp.Dal;

import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class DatabaseSchemaContracts {
    private DatabaseSchemaContracts() {
    }

    interface ColumnasTablaUsuarios {
        String ID_USUARIO = "Id";
        String NOMBRE = "Nombre";
        String APELLIDO_1 = "Apellido1";
        String APELLIDO_2 = "Apellido2";
        String ALIAS = "Alias";
        String ACTIVO = "Activo";
        String EMAIL = "Email";
        String FECHA_ALTA = "FechaAlta";
    }

    public static class Usuarios implements DatabaseSchemaContracts.ColumnasTablaUsuarios {

    }
}
