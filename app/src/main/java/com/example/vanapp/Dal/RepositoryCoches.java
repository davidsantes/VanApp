package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Coche;

import java.util.ArrayList;

public class RepositoryCoches {
    private DatabaseSchema baseDatos;

    public RepositoryCoches(DatabaseSchema baseDatos) {
        this.baseDatos = baseDatos;
    }

    public ArrayList obtenerCoches() {
        ArrayList<Coche> listaCoches = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sql = String.format("SELECT * FROM %s", DatabaseSchema.Tablas.COCHES);
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
        String sqlQuery = "SELECT * FROM " + DatabaseSchema.Tablas.COCHES + " WHERE Id = '" + idCoche + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            nuevoCoche = DatabaseMapping.obtenerInstancia().mapearCoche(cursor);
        }

        return nuevoCoche;
    }

    public boolean insertarCoche(Coche coche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.Coches.ID_COCHE, coche.getIdCoche());
        valores.put(DatabaseSchemaContracts.Coches.NOMBRE, coche.getNombre());
        valores.put(DatabaseSchemaContracts.Coches.MATRICULA, coche.getMatricula());
        valores.put(DatabaseSchemaContracts.Coches.NUMERO_PLAZAS, coche.getNumPlazas());
        valores.put(DatabaseSchemaContracts.Coches.COLOR_COCHE, coche.getColorCoche());
        valores.put(DatabaseSchemaContracts.Coches.FECHA_ALTA, Utilidades.getFechaToString(coche.getFechaAlta()));
        valores.put(DatabaseSchemaContracts.Coches.ACTIVO, coche.esActivo());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(DatabaseSchema.Tablas.COCHES, null, valores);

        return rowId > 0 ? true : false;
    }

    public boolean actualizarCoche(Coche coche) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.Coches.NOMBRE, coche.getNombre());
        valores.put(DatabaseSchemaContracts.Coches.MATRICULA, coche.getMatricula());
        valores.put(DatabaseSchemaContracts.Coches.NUMERO_PLAZAS, coche.getNumPlazas());
        valores.put(DatabaseSchemaContracts.Coches.COLOR_COCHE, coche.getColorCoche());
        valores.put(DatabaseSchemaContracts.Coches.ACTIVO, coche.esActivo());

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Coches.ID_COCHE);
        final String[] whereArgs = {coche.getIdCoche()};

        int resultado = db.update(DatabaseSchema.Tablas.COCHES, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina todos los coches.
     */
    public boolean eliminarCochesTodos() {
         int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(DatabaseSchema.Tablas.COCHES, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina un coche concreto.
     * @param idCoche coche concreto a eliminar
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarCoche(String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Coches.ID_COCHE);
        final String[] whereArgs = {idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.Coches.ACTIVO, false);
            resultado = db.update(DatabaseSchema.Tablas.COCHES, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.COCHES, whereClause, whereArgs);
        }

        return resultado > 0;
    }
}
