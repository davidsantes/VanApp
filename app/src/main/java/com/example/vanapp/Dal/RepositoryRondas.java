package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Entities.Ronda;

import java.util.ArrayList;

public class RepositoryRondas {
    private DatabaseSchema baseDatos;

    public RepositoryRondas(DatabaseSchema baseDatos) {
        this.baseDatos = baseDatos;
    }

    public boolean existenRondas() {
        boolean existenRondas = false;
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";

        sqlSelect = " SELECT Count(*) AS TotalRondas";
        sqlFrom += " FROM Rondas";
        sqlWhere += " WHERE Rondas.Activo=1";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                int totalRondas = cursor.getInt(cursor.getColumnIndex("TotalRondas"));
                existenRondas = totalRondas > 0 ? true : false;
            }while (cursor.moveToNext());
        }

        return existenRondas;
    }

    public ArrayList obtenerRondasDelCoche(String idCoche) {
        ArrayList<Ronda> listaRondasDelCoche = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlWhere = "";
        String sqlOrderBy = "";

        sqlSelect = " SELECT Coches.Id, Coches.Nombre, Coches.Activo,";
        sqlSelect += " Rondas.Id, Rondas.IdCoche, Rondas.Alias, Rondas.FechaInicio, Rondas.FechaFin, Rondas.EsRondaFinalizada, Rondas.Activo";
        sqlFrom += " FROM Coches INNER JOIN Rondas ON Coches.Id = Rondas.IdCoche";
        sqlWhere += " WHERE Coches.Id='" + idCoche + "'";
        sqlWhere += " AND Coches.Activo=1 AND Rondas.Activo=1";
        sqlOrderBy += " Order by Rondas.FechaInicio ";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere + sqlOrderBy;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                listaRondasDelCoche.add(DatabaseMapping.obtenerInstancia().mapearRonda(cursor));
            }while (cursor.moveToNext());
        }

        return listaRondasDelCoche;
    }

    public Ronda obtenerRonda(String idRonda){
        Ronda nuevaRonda = null;

        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + DatabaseSchema.Tablas.RONDAS + " WHERE Id = '" + idRonda + "'";
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()){
            nuevaRonda = DatabaseMapping.obtenerInstancia().mapearRonda(cursor);
        }

        return nuevaRonda;
    }

    public boolean insertarRondaDelCoche(Ronda ronda) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.Rondas.ID_RONDA, ronda.getIdRonda());
        valores.put(DatabaseSchemaContracts.Rondas.ID_COCHE, ronda.getIdCoche());
        valores.put(DatabaseSchemaContracts.Rondas.ALIAS, ronda.getAlias());
        valores.put(DatabaseSchemaContracts.Rondas.FECHA_INICIO, Utilidades.getFechaToString(ronda.getFechaInicio()));
        valores.put(DatabaseSchemaContracts.Rondas.FECHA_FIN, Utilidades.getFechaToString(ronda.getFechaFin()));
        valores.put(DatabaseSchemaContracts.Rondas.ES_RONDA_FINALIZADA, ronda.EsRondaFinalizada());
        valores.put(DatabaseSchemaContracts.Rondas.ACTIVO, ronda.EsActivo());

        //Retorna el id de la fila del nuevo registro insertado, o -1 si ha ocurrido un error
        long rowId = db.insertOrThrow(DatabaseSchema.Tablas.RONDAS, null, valores);

        return rowId > 0 ? true : false;
    }

    public boolean actualizarRondaDelCoche(Ronda ronda) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DatabaseSchemaContracts.Rondas.ALIAS, ronda.getAlias());
        valores.put(DatabaseSchemaContracts.Rondas.FECHA_INICIO, Utilidades.getFechaToString(ronda.getFechaInicio()));
        valores.put(DatabaseSchemaContracts.Rondas.FECHA_FIN, Utilidades.getFechaToString(ronda.getFechaFin()));
        valores.put(DatabaseSchemaContracts.Rondas.ES_RONDA_FINALIZADA, ronda.EsRondaFinalizada());
        valores.put(DatabaseSchemaContracts.Rondas.ACTIVO, ronda.EsActivo());

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Rondas.ID_RONDA);

        final String[] whereArgs = {ronda.getIdRonda()};

        int resultado = db.update(DatabaseSchema.Tablas.RONDAS, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina todas las rondas.
     */
    public boolean eliminarRondasTodas() {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        final String[] whereArgs = {};
        resultado = db.delete(DatabaseSchema.Tablas.RONDAS, "", whereArgs);

        return resultado > 0;
    }

    /**
     * Elimina una relación concreta.
     * @param idRonda a eliminar.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRonda(String idRonda, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Rondas.ID_RONDA);
        final String[] whereArgs = {idRonda};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.Rondas.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.RONDAS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.RONDAS, whereClause, whereArgs);
        }

        return resultado > 0;
    }

    /**
     * Elimina todas las relaciones de un coche concreto con todas las rondas.
     * @param idCoche .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRondasDeUnCoche(String idCoche, boolean esBorradoLogico) {
        int resultado = 0;
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", DatabaseSchemaContracts.Rondas.ID_COCHE);
        final String[] whereArgs = {idCoche};

        if (esBorradoLogico) {
            ContentValues valores = new ContentValues();
            valores.put(DatabaseSchemaContracts.Rondas.ACTIVO, false);

            resultado = db.update(DatabaseSchema.Tablas.RONDAS, valores, whereClause, whereArgs);
        }else{
            resultado = db.delete(DatabaseSchema.Tablas.RONDAS, whereClause, whereArgs);
        }

        return resultado > 0;
    }
}
