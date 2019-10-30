package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Dal.DatabaseSchemaContracts.*;
import com.example.vanapp.Dal.DatabaseSchema.*;

import java.util.ArrayList;
import java.util.Date;

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

    // INICIO [OPERACIONES_USUARIO]

    /* Retorna un usuario mapeado a través de los datos proporcionados por el cursor  */
    private Usuario mapearUsuario(Cursor cursor){
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setIdUsuario(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.ID_USUARIO)));
        nuevoUsuario.setNombre(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.NOMBRE)));
        nuevoUsuario.setApellido1(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.APELLIDO_1)));
        nuevoUsuario.setApellido2(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.APELLIDO_2)));
        nuevoUsuario.setAlias(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.ALIAS)));
        nuevoUsuario.setEmail(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.EMAIL)));
        nuevoUsuario.setColorUsuario(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.COLOR_USUARIO)));
        Date fechaAltaParseada = Utilidades.getFechaFromString(cursor.getString(cursor.getColumnIndex(ColumnasTablaUsuarios.FECHA_ALTA)));
        nuevoUsuario.setFechaAlta(fechaAltaParseada);
        nuevoUsuario.setActivo(cursor.getInt(cursor.getColumnIndex(ColumnasTablaUsuarios.ACTIVO)) == 1);
        return nuevoUsuario;
    }

    public ArrayList obtenerUsuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.USUARIOS);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst())
        {
            do{
                listaUsuarios.add(mapearUsuario(cursor));
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
            nuevoUsuario = mapearUsuario(cursor);
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
        valores.put(Usuarios.EMAIL, usuario.getEmail());
        valores.put(Usuarios.COLOR_USUARIO, usuario.getColorUsuario());
        valores.put(Usuarios.ACTIVO, usuario.esActivo());
        valores.put(Usuarios.FECHA_ALTA, Utilidades.getFechaToString(usuario.getFechaAlta()));

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
        valores.put(Usuarios.EMAIL, usuario.getEmail());
        valores.put(Usuarios.COLOR_USUARIO, usuario.getColorUsuario());
        valores.put(Usuarios.ACTIVO, usuario.esActivo());

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

    // FIN [OPERACIONES_USUARIO]

    // INICIO [OPERACIONES_COCHE]

    /* Retorna un usuario mapeado a través de los datos proporcionados por el cursor  */
    private Coche mapearCoche(Cursor cursor){
        Coche nuevoCoche = new Coche();
        nuevoCoche.setIdCoche(cursor.getString(cursor.getColumnIndex(ColumnasTablaCoches.ID_COCHE)));
        nuevoCoche.setNombre(cursor.getString(cursor.getColumnIndex(ColumnasTablaCoches.NOMBRE)));
        nuevoCoche.setMatricula(cursor.getString(cursor.getColumnIndex(ColumnasTablaCoches.MATRICULA)));
        nuevoCoche.setNumPlazas(cursor.getInt(cursor.getColumnIndex(ColumnasTablaCoches.NUMERO_PLAZAS)));
        nuevoCoche.setColorCoche(cursor.getString(cursor.getColumnIndex(ColumnasTablaCoches.COLOR_COCHE)));
        nuevoCoche.setActivo(cursor.getInt(cursor.getColumnIndex(ColumnasTablaCoches.ACTIVO)) == 1);
        Date fechaAltaParseada = Utilidades.getFechaFromString(cursor.getString(cursor.getColumnIndex(ColumnasTablaCoches.FECHA_ALTA)));
        nuevoCoche.setFechaAlta(fechaAltaParseada);
        return nuevoCoche;
    }

    public ArrayList obtenerCoches() {
        ArrayList<Coche> listaCoches = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.COCHES);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst())
        {
            do{
                listaCoches.add(mapearCoche(cursor));
            }while (cursor.moveToNext());
        }

        return listaCoches;
    }

    public Coche obtenerCoche(String idCoche){
        Coche nuevoCoche = null;

        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + Tablas.COCHES + " WHERE Id = '" + idCoche + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            nuevoCoche = mapearCoche(cursor);
        }

        return nuevoCoche;
    }

    public boolean insertarCoche(Coche coche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Coches.ID_COCHE, coche.getIdCoche());
        valores.put(Coches.NOMBRE, coche.getNombre());
        valores.put(Coches.MATRICULA, coche.getMatricula());
        valores.put(Coches.NUMERO_PLAZAS, coche.getNumPlazas());
        valores.put(Coches.COLOR_COCHE, coche.getColorCoche());
        valores.put(Coches.FECHA_ALTA, Utilidades.getFechaToString(coche.getFechaAlta()));
        valores.put(Coches.ACTIVO, coche.esActivo());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(Tablas.COCHES, null, valores);

        return rowId > 0 ? true : false;
    }

    public boolean actualizarCoche(Coche coche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Coches.NOMBRE, coche.getNombre());
        valores.put(Coches.MATRICULA, coche.getMatricula());
        valores.put(Coches.NUMERO_PLAZAS, coche.getNumPlazas());
        valores.put(Coches.COLOR_COCHE, coche.getColorCoche());
        valores.put(Coches.ACTIVO, coche.esActivo());

        String whereClause = String.format("%s=?", Coches.ID_COCHE);
        final String[] whereArgs = {coche.getIdCoche()};

        int resultado = db.update(Tablas.COCHES, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina todos los usuarios o uno en concreto.
     */
    public boolean eliminarCochesTodos() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(Tablas.COCHES, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un usuario concreto.
     * @param idCoche Si no se pasa nada, eliminará toda la tabla.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarCoche(String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(Coches.ACTIVO, false);

            String whereClause = String.format("%s=?", Coches.ID_COCHE);
            final String[] whereArgs = {idCoche};

            resultado = db.update(Tablas.COCHES, valores, whereClause, whereArgs);
        }else{
            String whereClause = String.format("%s=?", Coches.ID_COCHE);
            final String[] whereArgs = {idCoche};
            resultado = db.delete(Tablas.COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    // FIN [OPERACIONES_COCHE]
}

