package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Dal.DatabaseSchemaContracts.*;
import com.example.vanapp.Dal.DatabaseSchema.*;

import java.util.ArrayList;

/**
 * Clase auxiliar que implementa a {@link DatabaseSchema} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 * Se realiza mediante un patrón singleton para crear una única instancia de la clase
 */
public class DatabaseManager {
    private static DatabaseSchema baseDatos;
    private static DatabaseManager instancia = new DatabaseManager();

    private DatabaseManager() {
    }

    public static DatabaseManager obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new DatabaseSchema(contexto);
        }
        return instancia;
    }

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    // [OPERACIONES_USUARIO]
    public ArrayList obtenerUsuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.USUARIOS);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst())
        {
            do{
                Usuario nuevoUsuario = new Usuario();

                nuevoUsuario.setIdUsuario(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.ID_USUARIO)));
                nuevoUsuario.setNombre(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.NOMBRE)));
                nuevoUsuario.setApellido1(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.APELLIDO_1)));
                nuevoUsuario.setApellido2(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.APELLIDO_2)));
                nuevoUsuario.setAlias(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.ALIAS)));
                nuevoUsuario.setActivo(cursor.getInt(cursor.getColumnIndex(ColumnasTablaUsuarios.ACTIVO)) == 1);
                nuevoUsuario.setEmail(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.EMAIL)));
                nuevoUsuario.setFechaAltaFromString(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.FECHA_ALTA)));

                listaUsuarios.add(nuevoUsuario);
            }while (cursor.moveToNext());
        }

        return listaUsuarios;
    }

    public Usuario obtenerUsuario(String idUsuario){
        Usuario nuevoUsuario = null;

        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + Tablas.USUARIOS + " WHERE Id = '" + idUsuario + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            nuevoUsuario = new Usuario();
            nuevoUsuario.setIdUsuario(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.ID_USUARIO)));
            nuevoUsuario.setNombre(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.NOMBRE)));
            nuevoUsuario.setApellido1(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.APELLIDO_1)));
            nuevoUsuario.setApellido2(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.APELLIDO_2)));
            nuevoUsuario.setAlias(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.ALIAS)));
            nuevoUsuario.setActivo(cursor.getInt(cursor.getColumnIndex(ColumnasTablaUsuarios.ACTIVO)) == 1);
            nuevoUsuario.setEmail(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.EMAIL)));
            nuevoUsuario.setFechaAltaFromString(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.FECHA_ALTA)));
        }

        return nuevoUsuario;
    }

    public boolean insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Usuarios.ID_USUARIO, usuario.getIdUsuario());
        valores.put(Usuarios.NOMBRE, usuario.getNombre());
        valores.put(Usuarios.APELLIDO_1, usuario.getApellido1());
        valores.put(Usuarios.APELLIDO_2, usuario.getApellido2());
        valores.put(Usuarios.ALIAS, usuario.getAlias());
        valores.put(Usuarios.ACTIVO, usuario.getActivo());
        valores.put(Usuarios.EMAIL, usuario.getEmail());
        valores.put(Usuarios.FECHA_ALTA, usuario.getFechaToString());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(Tablas.USUARIOS, null, valores);

        return rowId > 0 ? true : false;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Usuarios.NOMBRE, usuario.getNombre());
        valores.put(Usuarios.APELLIDO_1, usuario.getApellido1());
        valores.put(Usuarios.APELLIDO_2, usuario.getApellido2());
        valores.put(Usuarios.ALIAS, usuario.getAlias());
        valores.put(Usuarios.ACTIVO, usuario.getActivo());
        valores.put(Usuarios.EMAIL, usuario.getEmail());
        valores.put(Usuarios.FECHA_ALTA, usuario.getFechaToString());

        String whereClause = String.format("%s=?", Usuarios.ID_USUARIO);
        final String[] whereArgs = {usuario.getIdUsuario()};

        int resultado = db.update(Tablas.USUARIOS, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina todos los usuarios o uno en concreto.
     */
    public boolean eliminarUsuariosTodos() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(Tablas.USUARIOS, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un usuario concreto.
     * @param idUsuario Si no se pasa nada, eliminará toda la tabla.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarUsuario(String idUsuario, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(Usuarios.ACTIVO, false);

            String whereClause = String.format("%s=?", Usuarios.ID_USUARIO);
            final String[] whereArgs = {idUsuario};

            resultado = db.update(Tablas.USUARIOS, valores, whereClause, whereArgs);
        }else{
            String whereClause = String.format("%s=?", Usuarios.ID_USUARIO);
            final String[] whereArgs = {idUsuario};
            resultado = db.delete(Tablas.USUARIOS, whereClause, whereArgs);
        }

        return resultado > 0;
    }
}

