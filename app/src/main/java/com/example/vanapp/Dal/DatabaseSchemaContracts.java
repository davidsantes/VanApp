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
        String ES_CONDUCTOR = "EsConductor";
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

    //RONDAS
    interface ColumnasTablaRondas {
        String ID_RONDA = "Id";
        String ID_COCHE = "IdCoche";
        String ALIAS = "Alias";
        String FECHA_INICIO = "FechaInicio";
        String FECHA_FIN = "FechaFin";
        String ES_RONDA_FINALIZADA = "EsRondaFinalizada";
        String ACTIVO = "Activo";
    }

    public static class Rondas implements DatabaseSchemaContracts.ColumnasTablaRondas {

    }

    //USUARIOS - RONDAS
    interface ColumnasTablaUsuariosRondas {
        String ID_USUARIO = "IdUsuario";
        String ID_RONDA = "IdRonda";
        String FECHA_DE_CONDUCCION = "FechaDeConduccion";
        String ACTIVO = "Activo";
    }

    public static class UsuariosRondas implements DatabaseSchemaContracts.ColumnasTablaUsuariosRondas {

    }

    //USUARIOS - COCHES
    interface ColumnasTablaUsuariosCoches {
        String ID_USUARIO = "IdUsuario";
        String ID_COCHE = "IdCoche";
        String ACTIVO = "Activo";
    }

    public static class UsuariosCoches implements DatabaseSchemaContracts.ColumnasTablaUsuariosCoches {

    }

}
