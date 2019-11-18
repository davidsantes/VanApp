package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.UsuarioRonda;

import java.util.ArrayList;
import java.util.Date;

public class RepositoryUsuariosRonda {
    private DatabaseSchema baseDatos;

    public RepositoryUsuariosRonda(DatabaseSchema baseDatos) {
        this.baseDatos = baseDatos;
    }

    public boolean existenRondasParaUsuarioEnCoche(String idUsuario, String idCoche) {
        boolean existenRondasParaUsuarioEnCoche = false;
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Count(*) AS TotalTurnos";
        sqlFrom += " FROM Coches INNER JOIN Rondas ON Coches.Id = Rondas.IdCoche";
        sqlFrom += " INNER JOIN Usuarios_Rondas ON Rondas.Id = Usuarios_Rondas.IdRonda";
        sqlWhere += " WHERE Usuarios_Rondas.IdUsuario='" + idUsuario + "'";
        sqlWhere += " AND Rondas.IdCoche='" + idCoche + "'";
        sqlWhere += " AND Coches.Activo=1 AND Rondas.Activo=1 AND Usuarios_Rondas.Activo=1";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                int totalTurnos = cursor.getInt(cursor.getColumnIndex("TotalTurnos"));
                existenRondasParaUsuarioEnCoche = totalTurnos > 0 ? true : false;
            }while (cursor.moveToNext());
        }

        return existenRondasParaUsuarioEnCoche;
    }

    public ArrayList obtenerUsuariosDeLaRonda(String idRonda) {
        ArrayList<UsuarioRonda> listaUsuariosDeLaRonda = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";

        sqlSelect = " SELECT Usuarios_Rondas.IdUsuario, Usuarios_Rondas.IdRonda, Usuarios_Rondas.FechaDeConduccion, Usuarios_Rondas.Activo";
        sqlFrom += " FROM USUARIOS_RONDAS";
        sqlWhere += " WHERE Usuarios_Rondas.IdRonda='" + idRonda + "'";
        sqlWhere += " AND Usuarios_Rondas.Activo=1";
        sqlOrderBy += " ORDER BY FechaDeConduccion";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere + sqlOrderBy;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                listaUsuariosDeLaRonda.add(DatabaseMapping.obtenerInstancia().mapearUsuarioRonda(cursor));
            }while (cursor.moveToNext());
        }

        return listaUsuariosDeLaRonda;
    }

    public UsuarioRonda obtenerConductorEnTurnoDeConduccion(String idRonda, Date fechaDeConduccion) {
        UsuarioRonda usuarioEnTurnoDeConduccion = new UsuarioRonda();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Usuarios_Rondas.IdUsuario, Usuarios_Rondas.IdRonda, Usuarios_Rondas.FechaDeConduccion, Usuarios_Rondas.Activo";
        sqlFrom += " FROM USUARIOS_RONDAS";
        sqlWhere += " WHERE Usuarios_Rondas.IdRonda='" + idRonda + "'";
        sqlWhere += " AND Usuarios_Rondas.Activo=1";
        sqlWhere += " AND Usuarios_Rondas.FechaDeConduccion='" + Utilidades.getFechaToString(fechaDeConduccion) + "'";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            usuarioEnTurnoDeConduccion = null;
            do{
                usuarioEnTurnoDeConduccion = DatabaseMapping.obtenerInstancia().mapearUsuarioRonda(cursor);
            }while (cursor.moveToNext());
        }

        return usuarioEnTurnoDeConduccion;
    }

    public boolean insertarUsuarioRonda(UsuarioRonda usuarioRonda) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.UsuariosRondas.ID_USUARIO, usuarioRonda.getIdUsuario());
        valores.put(DatabaseSchemaContracts.UsuariosRondas.ID_RONDA, usuarioRonda.getIdRonda());
        valores.put(DatabaseSchemaContracts.UsuariosRondas.FECHA_DE_CONDUCCION, Utilidades.getFechaToString(usuarioRonda.getFechaDeConduccion()));
        valores.put(DatabaseSchemaContracts.UsuariosRondas.ACTIVO, usuarioRonda.esActivo());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(DatabaseSchema.Tablas.USUARIOS_RONDAS, null, valores);

        return rowId > 0 ? true : false;
    }

    /**
     * Elimina todas las relaciones entre usuarios y rondas.
     */
    public boolean eliminarRelacionDeUsuariosConRondasTodos() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_RONDAS, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina una relación concreta.
     * @param idRonda a eliminar.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUnDiaEnLaRonda(String idRonda, Date fechaDeConduccion, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=? AND %s=?"
                , DatabaseSchemaContracts.UsuariosRondas.ID_RONDA
                , DatabaseSchemaContracts.UsuariosRondas.FECHA_DE_CONDUCCION);
        final String[] whereArgs = {idRonda, Utilidades.getFechaToString(fechaDeConduccion)};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.UsuariosCoches.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.USUARIOS_RONDAS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_RONDAS, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    /**
     * Elimina todas las relaciones de un usuario concreto con todas las rondas.
     * @param idUsuario .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConTodasLasRondas(String idUsuario, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.UsuariosRondas.ID_USUARIO);
        final String[] whereArgs = {idUsuario};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.UsuariosRondas.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.USUARIOS_RONDAS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_RONDAS, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    /**
     * Elimina todas las relaciones de una ronda en concreto con todos los usuarios.
     * @param idRonda .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeRondaConTodosLosUsuarios(String idRonda, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.UsuariosRondas.ID_RONDA);
        final String[] whereArgs = {idRonda};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.UsuariosRondas.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.USUARIOS_RONDAS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_RONDAS, whereClause, whereArgs);
        }

        return resultado > 0;
    }
}
