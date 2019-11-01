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
import com.example.vanapp.Entities.UsuarioCoche;

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

    public ArrayList obtenerUsuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.USUARIOS);
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
        String sqlQuery = "SELECT * FROM " + Tablas.USUARIOS + " WHERE Id = '" + idUsuario + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            nuevoUsuario = DatabaseMapping.obtenerInstancia().mapearUsuario(cursor);
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

        this.eliminarUsuarioCochesTodos();

        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(Tablas.USUARIOS, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un usuario concreto.
     * @param idUsuario usuario a eliminar
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarUsuario(String idUsuario, boolean esBorradoLogico) {

        this.eliminarRelacionDeUsuarioConTodosLosCoches(idUsuario, esBorradoLogico);

        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();
        String whereClause = String.format("%s=?", Usuarios.ID_USUARIO);
        final String[] whereArgs = {idUsuario};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(Usuarios.ACTIVO, false);
            resultado = db.update(Tablas.USUARIOS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(Tablas.USUARIOS, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    // FIN [OPERACIONES_USUARIO]

    // INICIO [OPERACIONES_COCHE]

    public ArrayList obtenerCoches() {
        ArrayList<Coche> listaCoches = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", Tablas.COCHES);
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst())
        {
            do{
                listaCoches.add(DatabaseMapping.obtenerInstancia().mapearCoche(cursor));
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
            nuevoCoche = DatabaseMapping.obtenerInstancia().mapearCoche(cursor);
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
        this.eliminarUsuarioCochesTodos();

        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(Tablas.COCHES, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un coche concreto.
     * @param idCoche coche concreto a eliminar
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarCoche(String idCoche, boolean esBorradoLogico) {

        this.eliminarRelacionDeCocheConTodosLosUsuarios(idCoche, esBorradoLogico);

        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Coches.ID_COCHE);
        final String[] whereArgs = {idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(Coches.ACTIVO, false);
            resultado = db.update(Tablas.COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(Tablas.COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    // FIN [OPERACIONES_COCHE]

    // INICIO [OPERACIONES_USUARIOS_COCHE]
    public ArrayList obtenerUsuariosDelCoche(String idCoche) {
        ArrayList<UsuarioCoche> listaUsuariosDelCoche = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Coches.Id, Coches.Nombre, Coches.Activo,";
        sqlSelect += " Usuarios_Coches.IdUsuario, Usuarios_Coches.IdCoche, Usuarios_Coches.EsConductor, Usuarios_Coches.Activo,";
        sqlSelect += " Usuarios.Id, Usuarios.Nombre, Usuarios.Apellido1, Usuarios.Apellido2,";
        sqlSelect += " Usuarios.Alias, Usuarios.Email, Usuarios.ColorUsuario, Usuarios.FechaAlta, Usuarios.Activo";
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

    public boolean insertarUsuarioCoche(UsuarioCoche usuarioCoche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(UsuariosCoches.ID_USUARIO, usuarioCoche.getUsuarioDetalle().getIdUsuario());
        valores.put(UsuariosCoches.ID_COCHE, usuarioCoche.getIdCoche());
        valores.put(UsuariosCoches.ES_CONDUCTOR, usuarioCoche.esCondutor());
        valores.put(UsuariosCoches.ACTIVO, usuarioCoche.esActivo());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(Tablas.USUARIOS_COCHES, null, valores);

        return rowId > 0 ? true : false;
    }

    public boolean actualizarUsuarioCoche(UsuarioCoche usuarioCoche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(UsuariosCoches.ES_CONDUCTOR, usuarioCoche.esCondutor());
        valores.put(Coches.ACTIVO, usuarioCoche.esActivo());

        String whereClause = String.format("%s=? AND %s=?", UsuariosCoches.ID_USUARIO, UsuariosCoches.ID_COCHE);

        final String[] whereArgs = {usuarioCoche.getUsuarioDetalle().getIdUsuario(), usuarioCoche.getIdCoche()};

        int resultado = db.update(Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina todos los usuarios o uno en concreto.
     */
    private boolean eliminarUsuarioCochesTodos() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(Tablas.USUARIOS_COCHES, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un usuario concreto.
     * @param idCoche Si no se pasa nada, eliminará toda la tabla.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarUsuarioCoche(String idUsuario, String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=? AND %s=?", UsuariosCoches.ID_USUARIO, UsuariosCoches.ID_COCHE);
        final String[] whereArgs = {idUsuario, idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(UsuariosCoches.ACTIVO, false);

            resultado = db.update(Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(Tablas.USUARIOS_COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    /**
     * Elimina un usuario concreto.
     * @param idUsuario .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConTodosLosCoches(String idUsuario, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", UsuariosCoches.ID_USUARIO);
        final String[] whereArgs = {idUsuario};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(UsuariosCoches.ACTIVO, false);

            resultado = db.update(Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(Tablas.USUARIOS_COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }


    /**
     * Elimina un usuario concreto.
     * @param idCoche .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeCocheConTodosLosUsuarios(String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", UsuariosCoches.ID_COCHE);
        final String[] whereArgs = {idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(UsuariosCoches.ACTIVO, false);

            resultado = db.update(Tablas.USUARIOS_COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(Tablas.USUARIOS_COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    // FIN [OPERACIONES_USUARIOS_COCHE]
}

