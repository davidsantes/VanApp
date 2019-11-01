package com.example.vanapp.Dal;

import android.database.Cursor;
import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;

import java.util.Date;

public class DatabaseMapping {
    private static DatabaseMapping instancia = new DatabaseMapping();

    private DatabaseMapping() {
    }

    public static DatabaseMapping obtenerInstancia() {
        return instancia;
    }

    /* Retorna un usuario mapeado a través de los datos proporcionados por el cursor  */
    public Usuario mapearUsuario(Cursor cursor){
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setIdUsuario(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.ID_USUARIO)));
        nuevoUsuario.setNombre(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.NOMBRE)));
        nuevoUsuario.setApellido1(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.APELLIDO_1)));
        nuevoUsuario.setApellido2(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.APELLIDO_2)));
        nuevoUsuario.setAlias(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.ALIAS)));
        nuevoUsuario.setEmail(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.EMAIL)));
        nuevoUsuario.setColorUsuario(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.COLOR_USUARIO)));
        Date fechaAltaParseada = Utilidades.getFechaFromString(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.FECHA_ALTA)));
        nuevoUsuario.setFechaAlta(fechaAltaParseada);
        nuevoUsuario.setActivo(cursor.getInt(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuarios.ACTIVO)) == 1);
        return nuevoUsuario;
    }

    /* Retorna un coche mapeado a través de los datos proporcionados por el cursor  */
    public Coche mapearCoche(Cursor cursor){
        Coche nuevoCoche = new Coche();
        nuevoCoche.setIdCoche(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.ID_COCHE)));
        nuevoCoche.setNombre(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.NOMBRE)));
        nuevoCoche.setMatricula(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.MATRICULA)));
        nuevoCoche.setNumPlazas(cursor.getInt(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.NUMERO_PLAZAS)));
        nuevoCoche.setColorCoche(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.COLOR_COCHE)));
        nuevoCoche.setActivo(cursor.getInt(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.ACTIVO)) == 1);
        Date fechaAltaParseada = Utilidades.getFechaFromString(cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaCoches.FECHA_ALTA)));
        nuevoCoche.setFechaAlta(fechaAltaParseada);
        return nuevoCoche;
    }

    /* Retorna un usuario - coche mapeado a través de los datos proporcionados por el cursor  */
    public UsuarioCoche mapearUsuarioCoche(Cursor cursor){
        String idUsuario = cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuariosCoches.ID_USUARIO));
        String idCoche = cursor.getString(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuariosCoches.ID_COCHE));

        UsuarioCoche nuevoUsuarioCoche = new UsuarioCoche(idUsuario, idCoche);
        nuevoUsuarioCoche.setEsCondutor(cursor.getInt(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuariosCoches.ES_CONDUCTOR)) == 1);
        nuevoUsuarioCoche.setActivo(cursor.getInt(cursor.getColumnIndex(DatabaseSchemaContracts.ColumnasTablaUsuariosCoches.ACTIVO)) == 1);

        nuevoUsuarioCoche.setUsuarioDetalle(mapearUsuario(cursor));

        return nuevoUsuarioCoche;
    }
}
