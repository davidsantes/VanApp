package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;

import java.util.ArrayList;

public class RepositoryUsuariosCoche {
    private DatabaseSchema baseDatos;

    public RepositoryUsuariosCoche(DatabaseSchema baseDatos) {
        this.baseDatos = baseDatos;
    }

    public ArrayList obtenerUsuariosDelCoche(String idCoche) {
        ArrayList<UsuarioCoche> listaUsuariosDelCoche = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Coches.Id, Coches.Nombre, Coches.Activo,";
        sqlSelect += " Usuarios_Coches.IdUsuario, Usuarios_Coches.IdCoche, Usuarios_Coches.Activo,";
        sqlSelect += " Usuarios.Id, Usuarios.Nombre, Usuarios.Apellido1, Usuarios.Apellido2,";
        sqlSelect += " Usuarios.Alias, Usuarios.Email, Usuarios.EsConductor, Usuarios.ColorUsuario, Usuarios.FechaAlta, Usuarios.Activo";
        sqlFrom += " FROM Coches INNER JOIN Usuarios_Coches ON Coches.Id = Usuarios_Coches.IdCoche";
        sqlFrom += " INNER JOIN Usuarios ON Usuarios.Id = Usuarios_Coches.IdUsuario";
        sqlWhere += " WHERE Coches.Id='" + idCoche + "'";
        sqlWhere += " AND Coches.Activo=1 AND Usuarios.Activo=1 AND Usuarios_Coches.Activo=1";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                listaUsuariosDelCoche.add(DatabaseMapping.obtenerInstancia().mapearUsuarioCoche(cursor));
            }while (cursor.moveToNext());
        }

        return listaUsuariosDelCoche;
    }

    public ArrayList obtenerUsuariosNoDelCoche(String idCoche) {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Usuarios.*";
        sqlFrom += " FROM Usuarios";
        sqlWhere += " WHERE Id NOT IN";
        sqlWhere += " (SELECT IdUsuario FROM Usuarios_Coches WHERE IdCoche = '" + idCoche + "') ";
        sqlWhere += " AND Usuarios.Activo=1";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                listaUsuarios.add(DatabaseMapping.obtenerInstancia().mapearUsuario(cursor));
            }while (cursor.moveToNext());
        }

        return listaUsuarios;
    }

    public boolean insertarUsuarioCoche(UsuarioCoche usuarioCoche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.UsuariosCoches.ID_USUARIO, usuarioCoche.getUsuarioDetalle().getIdUsuario());
        valores.put(DatabaseSchemaContracts.UsuariosCoches.ID_COCHE, usuarioCoche.getIdCoche());
        valores.put(DatabaseSchemaContracts.UsuariosCoches.ACTIVO, usuarioCoche.esActivo());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(DatabaseSchema.Tablas.USUARIOS_COCHES, null, valores);

        return rowId > 0 ? true : false;
    }

    /**
     * Elimina todas las relaciones entre usuarios y coches.
     */
    public boolean eliminarRelacionDeUsuariosConCochesTodos() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_COCHES, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina una relación concreta.
     * @param idUsuario a eliminar.
     * @param idCoche a eliminar.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConCoche(String idUsuario, String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=? AND %s=?", DatabaseSchemaContracts.UsuariosCoches.ID_USUARIO, DatabaseSchemaContracts.UsuariosCoches.ID_COCHE);
        final String[] whereArgs = {idUsuario, idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.UsuariosCoches.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    /**
     * Elimina todas las relaciones de un usuario concreto con todos los coches.
     * @param idUsuario .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConTodosLosCoches(String idUsuario, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.UsuariosCoches.ID_USUARIO);
        final String[] whereArgs = {idUsuario};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.UsuariosCoches.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    /**
     * Elimina todas las relaciones de un coche concreto con todos los usuarios.
     * @param idCoche .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeCocheConTodosLosUsuarios(String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.UsuariosCoches.ID_COCHE);
        final String[] whereArgs = {idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.UsuariosCoches.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS_COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }
}
