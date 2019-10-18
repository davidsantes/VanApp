package com.example.vanapp.Dal;

public class DatabaseSchema {
    public static final String DATABASE_NAME = "VanApp";
    public static final int DATABASE_VERSION = 1; //Versión de la base de datos

    //Sólo tenemos una tabla
    public static final String TABLA_LISTA_PERSONAS = "personas"; //Nombre de la tabla
    public static final String TABLA_LISTA_PERSONAS_NOMBRE = "nombre"; //Columna
    public static final String TABLA_LISTA_PERSONAS_EDAD = "edad"; //Columna
    public static final String TABLA_LISTA_PERSONAS_PAIS = "pais"; //Columna

    //Tabla Usuarios
    public static final String TABLA_USUARIOS = "Usuarios"; //Nombre de la tabla
    public static final String TABLA_USUARIOS_COLUMNA_APELLIDO1 = "Apellido1";
    public static final String TABLA_USUARIOS_COLUMNA_APELLIDO1_TIPO = "TEXT NOT NULL";

    public static final String TABLA_USUARIOS_COLUMNA_APELLIDO2 = "Apellido2";
    public static final String TABLA_USUARIOS_COLUMNA_APELLIDO2_TIPO = "TEXT NOT NULL";

    public static final String TABLA_USUARIOS_COLUMNA_ALIAS = "Alias";
    public static final String TABLA_USUARIOS_COLUMNA_ACTIVO = "Activo";
    public static final String TABLA_USUARIOS_COLUMNA_EMAIL = "Email";
    public static final String TABLA_USUARIOS_COLUMNA_FECHAALTA = "FechaAlta";
}
