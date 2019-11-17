package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Usuario;

import java.util.ArrayList;

public class RepositoryUsuarios {
    private DatabaseSchema baseDatos;

    public RepositoryUsuarios(DatabaseSchema baseDatos) {
        this.baseDatos = baseDatos;
    }

    public boolean existenUsuarios() {
        boolean existenUsuarios = false;
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Count(*) AS TotalUsuarios";
        sqlFrom += " FROM USUARIOS";
        sqlWhere += " WHERE Usuarios.Activo=1";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                int totalUsuarios = cursor.getInt(cursor.getColumnIndex("TotalUsuarios"));
                existenUsuarios = totalUsuarios > 0 ? true : false;
            }while (cursor.moveToNext());
        }

        return existenUsuarios;
    }

    public ArrayList obtenerUsuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", DatabaseSchema.Tablas.USUARIOS);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst())
        {
            do{
                listaUsuarios.add(DatabaseMapping.obtenerInstancia().mapearUsuario(cursor));
            }while (cursor.moveToNext());
        }

        return listaUsuarios;
    }

    public Usuario obtenerUsuario(String idUsuario){
        Usuario nuevoUsuario = null;

        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + DatabaseSchema.Tablas.USUARIOS + " WHERE Id = '" + idUsuario + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            nuevoUsuario = DatabaseMapping.obtenerInstancia().mapearUsuario(cursor);
        }

        return nuevoUsuario;
    }

    public boolean insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.Usuarios.ID_USUARIO, usuario.getIdUsuario());
        valores.put(DatabaseSchemaContracts.Usuarios.NOMBRE, usuario.getNombre());
        valores.put(DatabaseSchemaContracts.Usuarios.APELLIDO_1, usuario.getApellido1());
        valores.put(DatabaseSchemaContracts.Usuarios.APELLIDO_2, usuario.getApellido2());
        valores.put(DatabaseSchemaContracts.Usuarios.ALIAS, usuario.getAlias());
        valores.put(DatabaseSchemaContracts.Usuarios.EMAIL, usuario.getEmail());
        valores.put(DatabaseSchemaContracts.Usuarios.ES_CONDUCTOR, usuario.esConductor());
        valores.put(DatabaseSchemaContracts.Usuarios.COLOR_USUARIO, usuario.getColorUsuario());
        valores.put(DatabaseSchemaContracts.Usuarios.ACTIVO, usuario.esActivo());
        valores.put(DatabaseSchemaContracts.Usuarios.FECHA_ALTA, Utilidades.getFechaToString(usuario.getFechaAlta()));

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(DatabaseSchema.Tablas.USUARIOS, null, valores);

        return rowId > 0 ? true : false;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.Usuarios.NOMBRE, usuario.getNombre());
        valores.put(DatabaseSchemaContracts.Usuarios.APELLIDO_1, usuario.getApellido1());
        valores.put(DatabaseSchemaContracts.Usuarios.APELLIDO_2, usuario.getApellido2());
        valores.put(DatabaseSchemaContracts.Usuarios.ALIAS, usuario.getAlias());
        valores.put(DatabaseSchemaContracts.Usuarios.EMAIL, usuario.getEmail());
        valores.put(DatabaseSchemaContracts.Usuarios.ES_CONDUCTOR, usuario.esConductor());
        valores.put(DatabaseSchemaContracts.Usuarios.COLOR_USUARIO, usuario.getColorUsuario());
        valores.put(DatabaseSchemaContracts.Usuarios.ACTIVO, usuario.esActivo());

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Usuarios.ID_USUARIO);
        final String[] whereArgs = {usuario.getIdUsuario()};

        int resultado = db.update(DatabaseSchema.Tablas.USUARIOS, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina todos los usuarios.
     */
    public boolean eliminarUsuariosTodos() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(DatabaseSchema.Tablas.USUARIOS, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un usuario concreto.
     * @param idUsuario usuario a eliminar
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarUsuario(String idUsuario, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Usuarios.ID_USUARIO);
        final String[] whereArgs = {idUsuario};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.Usuarios.ACTIVO, false);
            resultado = db.update(DatabaseSchema.Tablas.USUARIOS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.USUARIOS, whereClause, whereArgs);
        }

        return resultado > 0;
    }
}
