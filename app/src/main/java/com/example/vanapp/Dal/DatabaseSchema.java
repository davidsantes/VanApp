package com.example.vanapp.Dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.example.vanapp.Dal.DatabaseSchemaContracts.Usuarios;
import com.example.vanapp.Dal.DatabaseSchemaContracts.Coches;
import com.example.vanapp.Dal.DatabaseSchemaContracts.Rondas;
import com.example.vanapp.Dal.DatabaseSchemaContracts.UsuariosRondas;
import com.example.vanapp.Dal.DatabaseSchemaContracts.UsuariosCoches;

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
        String COCHES = "Coches";
        String RONDAS = "Rondas";
        String USUARIOS_COCHES = "Usuarios_Coches";
        String USUARIOS_RONDAS = "Usuarios_Rondas";
    }

    interface Referencias {
        String ID_USUARIO = String.format("REFERENCES %s(%s)",
                Tablas.USUARIOS, Usuarios.ID_USUARIO);
        String ID_COCHE = String.format("REFERENCES %s(%s)",
                Tablas.COCHES, Coches.ID_COCHE);
        String ID_RONDA = String.format("REFERENCES %s(%s)",
                Tablas.RONDAS, Rondas.ID_RONDA);
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
        CrearTablaUsuarios(db);
        CrearTablaCoches(db);
        CrearTablaRondas(db);
        CrearTablaUsuariosRondas(db);
        CrearTablaUsuariosCoches(db);
    }

    /*
    * Sólo se llama cuando el fichero de BDD existe pero el número de versión es inferior al solicitado en el constructor
    * */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Realiza el borrado en cascada
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIOS_RONDAS);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIOS_COCHES);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.RONDAS);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.USUARIOS);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + Tablas.COCHES);
        onCreate(db);
    }

    /*
    * CREATE TABLE Usuarios ( Id TEXT PRIMARY KEY,Nombre TEXT NOT NULL,Apellido1 TEXT NOT NULL,Apellido2 TEXT NOT NULL,Alias TEXT NOT NULL,Email TEXT NOT NULL,ColorUsuario TEXT NOT NULL,FechaAlta DATE NOT NULL,Activo INTEGER NOT NULL )
    * */
    private void CrearTablaUsuarios(SQLiteDatabase db){
        String queryCreateTablaUsuarios = String.format("CREATE TABLE %s ( "
                        //+ Opción si se elige autoincremental "%s INTEGER PRIMARY KEY AUTOINCREMENT"
                        + "%s TEXT PRIMARY KEY" //Usuarios.ID_USUARIO
                        + ",%s TEXT NOT NULL"   //Usuarios.NOMBRE
                        + ",%s TEXT NOT NULL"   //Usuarios.APELLIDO_1
                        + ",%s TEXT NOT NULL"   //Usuarios.APELLIDO_2
                        + ",%s TEXT NOT NULL"   //Usuarios.ALIAS
                        + ",%s TEXT NOT NULL"   //Usuarios.EMAIL
                        + ",%s INTEGER NOT NULL"   //Usuarios.ES_CONDUCTOR
                        + ",%s TEXT NOT NULL"   //Usuarios.COLOR_USUARIO
                        + ",%s DATE NOT NULL"   //Usuarios.FECHA_ALTA
                        + ",%s INTEGER NOT NULL"   //Usuarios.ACTIVO
                        + " )",
                Tablas.USUARIOS
                , Usuarios.ID_USUARIO
                , Usuarios.NOMBRE
                , Usuarios.APELLIDO_1
                , Usuarios.APELLIDO_2
                , Usuarios.ALIAS
                , Usuarios.EMAIL
                , Usuarios.ES_CONDUCTOR
                , Usuarios.COLOR_USUARIO
                , Usuarios.FECHA_ALTA
                , Usuarios.ACTIVO
        );

        db.execSQL(queryCreateTablaUsuarios);
    }

    /*
     * CREATE TABLE Coches ( Id TEXT PRIMARY KEY,Nombre TEXT NOT NULL,Matricula TEXT NOT NULL,NumeroPlazas TEXT NOT NULL,ColorCoche TEXT NOT NULL,FechaAlta DATE NOT NULL,Activo INTEGER NOT NULL )
     * */
    private void CrearTablaCoches(SQLiteDatabase db){
        String queryCreateTablaCoches = String.format("CREATE TABLE %s ( "
                        + "%s TEXT PRIMARY KEY" //Coches.ID_COCHE
                        + ",%s TEXT NOT NULL"   //Coches.NOMBRE
                        + ",%s TEXT NOT NULL"   //Coches.MATRICULA
                        + ",%s TEXT NOT NULL"   //Coches.NUMERO_PLAZAS
                        + ",%s TEXT NOT NULL"   //Coches.COLOR_COCHE
                        + ",%s DATE NOT NULL"   //Coches.FECHA_ALTA
                        + ",%s INTEGER NOT NULL"   //Coches.ACTIVO
                        + " )",
                Tablas.COCHES
                , Coches.ID_COCHE
                , Coches.NOMBRE
                , Coches.MATRICULA
                , Coches.NUMERO_PLAZAS
                , Coches.COLOR_COCHE
                , Coches.FECHA_ALTA
                , Coches.ACTIVO
        );

        db.execSQL(queryCreateTablaCoches);
    }

    /*
     * CREATE TABLE Rondas ( Id TEXT PRIMARY KEY,IdCoche TEXT NOT NULL REFERENCES Coches(Id),Alias TEXT NOT NULL,FechaInicio DATE NOT NULL,FechaFin DATE NOT NULL,EsRondaFinalizada INTEGER NOT NULL,Activo INTEGER NOT NULL )
     * */
    private void CrearTablaRondas(SQLiteDatabase db){
        String queryCreateTablaCoches = String.format("CREATE TABLE %s ( "
                        + "%s TEXT PRIMARY KEY" //Rondas.ID_RONDA
                        + ",%s TEXT NOT NULL %s"   //Rondas.ID_COCHE, Referencias.ID_COCHE
                        + ",%s TEXT NOT NULL"   //Rondas.ALIAS
                        + ",%s DATE NOT NULL"   //Rondas.FECHA_INICIO
                        + ",%s DATE NOT NULL"   //Rondas.FECHA_FIN
                        + ",%s INTEGER NOT NULL"   //Rondas.ES_RONDA_FINALIZADA
                        + ",%s INTEGER NOT NULL"   //Rondas.ACTIVO
                        + " )",
                Tablas.RONDAS
                , Rondas.ID_RONDA
                , Rondas.ID_COCHE, Referencias.ID_COCHE
                , Rondas.ALIAS
                , Rondas.FECHA_INICIO
                , Rondas.FECHA_FIN
                , Rondas.ES_RONDA_FINALIZADA
                , Rondas.ACTIVO
        );

        db.execSQL(queryCreateTablaCoches);
    }

    /*
     * CREATE TABLE Usuarios_Rondas ( IdUsuario TEXT NOT NULL REFERENCES Usuarios(Id),IdRonda TEXT NOT NULL REFERENCES Rondas(Id),FechaDeConduccion DATE NOT NULL,Activo INTEGER NOT NULL )
     * */
    private void CrearTablaUsuariosRondas(SQLiteDatabase db){
        String queryCreateTablaCoches = String.format("CREATE TABLE %s ( "
                        + "%s TEXT NOT NULL %s" //UsuariosRondas.ID_USUARIO, Referencias.ID_USUARIO
                        + ",%s TEXT NOT NULL %s" //UsuariosRondas.ID_RONDA, Referencias.ID_RONDA
                        + ",%s DATE NOT NULL"    //UsuariosRondas.FECHA_DE_CONDUCCION
                        + ",%s INTEGER NOT NULL"   //UsuariosRondas.ACTIVO
                        + " )",
                Tablas.USUARIOS_RONDAS
                , UsuariosRondas.ID_USUARIO, Referencias.ID_USUARIO
                , UsuariosRondas.ID_RONDA, Referencias.ID_RONDA
                , UsuariosRondas.FECHA_DE_CONDUCCION
                , UsuariosRondas.ACTIVO
        );

        db.execSQL(queryCreateTablaCoches);
    }

    /*
     * CREATE TABLE Usuarios_Coches ( IdUsuario TEXT NOT NULL REFERENCES Usuarios(Id),IdCoche TEXT NOT NULL REFERENCES Coches(Id),EsConductor INTEGER NOT NULL,Activo INTEGER NOT NULL )
     * */
    private void CrearTablaUsuariosCoches(SQLiteDatabase db){
        String queryCreateTablaCoches = String.format("CREATE TABLE %s ( "
                        + "%s TEXT NOT NULL %s"   //UsuariosCoches.ID_USUARIO, Referencias.ID_USUARIO
                        + ",%s TEXT NOT NULL %s"   //UsuariosCoches.ID_COCHE, Referencias.ID_COCHE
                        + ",%s INTEGER NOT NULL"   //UsuariosCoches.ACTIVO
                        + " )",
                Tablas.USUARIOS_COCHES
                , UsuariosCoches.ID_USUARIO, Referencias.ID_USUARIO
                , UsuariosCoches.ID_COCHE, Referencias.ID_COCHE
                , UsuariosCoches.ACTIVO
        );

        db.execSQL(queryCreateTablaCoches);
    }
}
