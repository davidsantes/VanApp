package com.example.vanapp.Dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.vanapp.Dal.DatabaseSchemaContracts.Usuarios;

public class DatabaseSchema extends SQLiteOpenHelper {

    private final Context contexto;
    private static final String NOMBRE_BASE_DATOS = "vanapp.db";

    private static final int VERSION_ACTUAL = 1;

    public DatabaseSchema(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    interface Tablas {
        String USUARIOS = "Usuarios";
    }

    interface Referencias {
        String ID_USUARIO = String.format("REFERENCES %s(%s)",
                Tablas.USUARIOS, Usuarios.ID_USUARIO);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                //Para poder activar las foreing keys
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    /* Se lanza únicamente si el fichero de BDD no existe
     * Por defecto el archivo de la base de datos será almacenado en:
     * data/data/<paquete>/databases/NOMBRE_BASE_DATOS.db
     * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTablaUsuarios = String.format("CREATE TABLE %s ( "
                        + "%s TEXT PRIMARY KEY" //Usuarios.ID_USUARIO
                        + ",%s TEXT NOT NULL"   //Usuarios.NOMBRE
                        + ",%s TEXT NOT NULL"   //Usuarios.APELLIDO_1
                        + ",%s TEXT NOT NULL"   //Usuarios.APELLIDO_2
                        + ",%s TEXT NOT NULL"   //Usuarios.ALIAS
                        + ",%s TEXT NOT NULL"   //Usuarios.ACTIVO
                        + ",%s TEXT NOT NULL"   //Usuarios.EMAIL
                        + ",%s LONG NOT NULL"   //Usuarios.FECHA_ALTA
                        + " )",
                        Tablas.USUARIOS
                        , Usuarios.ID_USUARIO
                        , Usuarios.NOMBRE
                        , Usuarios.APELLIDO_1
                        , Usuarios.APELLIDO_2
                        , Usuarios.ALIAS
                        , Usuarios.ACTIVO
                        , Usuarios.EMAIL
                        , Usuarios.FECHA_ALTA

        );

        db.execSQL(queryCreateTablaUsuarios);
    }

    /* Sólo se llama cuando el fichero de BDD existe pero el número de versión es inferior al solicitado en el constructor
     * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIOS);

        onCreate(db);
    }
}
