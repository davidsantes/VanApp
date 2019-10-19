package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.vanapp.Entities.Persona;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    public DatabaseHelper(Context context){
        super(context, DatabaseContantesTemp.DATABASE_NAME, null, DatabaseContantesTemp.DATABASE_VERSION);
        this.context = context;
    }

    /* Se lanza únicamente si el fichero de BDD no existe
     * Por defecto el archivo de la base de datos será almacenado en:
     * data/data/<paquete>/databases/<nombre-de-la-bd>.db
     * */
    @Override
    public void onCreate(SQLiteDatabase db){
        //Importante los espacios, comas, etcétera
        String queryCreateTablaPersonas = "CREATE TABLE " + DatabaseContantesTemp.TABLA_LISTA_PERSONAS + " (" +
                DatabaseContantesTemp.TABLA_LISTA_PERSONAS_NOMBRE + " TEXT NOT NULL PRIMARY KEY, " +
                DatabaseContantesTemp.TABLA_LISTA_PERSONAS_EDAD + " INTEGER, " +
                DatabaseContantesTemp.TABLA_LISTA_PERSONAS_PAIS + " TEXT" +
                ");";

        String queryCreateTablaUsuarios = "CREATE TABLE " + DatabaseContantesTemp.TABLA_USUARIOS + " (" +
                DatabaseContantesTemp.TABLA_USUARIOS_COLUMNA_APELLIDO1 + " " + DatabaseContantesTemp.TABLA_USUARIOS_COLUMNA_APELLIDO1_TIPO + ", " +
                DatabaseContantesTemp.TABLA_USUARIOS_COLUMNA_APELLIDO2 + " " + DatabaseContantesTemp.TABLA_USUARIOS_COLUMNA_APELLIDO2_TIPO +
                ");";

        db.execSQL(queryCreateTablaPersonas);
        db.execSQL(queryCreateTablaUsuarios);
    }

    /* Sólo se llama cuando el fichero de BDD existe pero el número de versión es inferior al solicitado en el constructor
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Si ya existe que la borre y vuelva a crear la base de datos
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContantesTemp.TABLA_LISTA_PERSONAS);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContantesTemp.TABLA_USUARIOS);
        onCreate(db);
    }

    public void insertarPersonaEnLaLista(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(DatabaseContantesTemp.TABLA_LISTA_PERSONAS, null, contentValues);
        db.close();
    }

    public boolean buscarPersona(String nombre){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + DatabaseContantesTemp.TABLA_LISTA_PERSONAS + " WHERE nombre = '" + nombre + "'";
        Cursor personaEncontrada = db.rawQuery(sqlQuery, null);
        if (personaEncontrada.moveToFirst()){
            return true;
        } else{
            return false;
        }
    }

    public ArrayList obtenerPersonas(){
        ArrayList <Persona> personas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + DatabaseContantesTemp.TABLA_LISTA_PERSONAS;
        Cursor registros = db.rawQuery(sqlQuery, null);

        if (registros.moveToFirst())
        {
            do{
                Persona nuevaPersona = new Persona();

                nuevaPersona.setNombre(registros.getString(registros.getColumnIndex(DatabaseContantesTemp.TABLA_LISTA_PERSONAS_NOMBRE)));
                nuevaPersona.setEdad(registros.getInt(registros.getColumnIndex(DatabaseContantesTemp.TABLA_LISTA_PERSONAS_EDAD)));
                nuevaPersona.setPais(registros.getString(registros.getColumnIndex(DatabaseContantesTemp.TABLA_LISTA_PERSONAS_PAIS)));

                personas.add(nuevaPersona);
            }while (registros.moveToNext());
        }

        return personas;
    }

    //No se utiliza, pero se eliminaría un registro
    public void borrarRegistroPersona(String nombre){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContantesTemp.TABLA_LISTA_PERSONAS, DatabaseContantesTemp.TABLA_LISTA_PERSONAS_NOMBRE + "=?", new String[]{nombre});
    }

    public void borrarTablaPersonas(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseContantesTemp.TABLA_LISTA_PERSONAS);
    }
}
