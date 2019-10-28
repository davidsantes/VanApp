package com.example.vanapp.Entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import static com.example.vanapp.Common.Constantes.*;

public class Coche {
    private String idCoche;
    private String nombre;
    private String matricula;
    private int numPlazas = 0;
    private boolean activo = true;
    private Date fechaAlta = new Date();

    public Coche(){
        this.setIdCoche(generarIdUnico());
        this.setActivo(true);
    }

    public Coche(String nombre,
                 String matricula,
                 int numPlazas)
    {
        this.setIdCoche(generarIdUnico());
        this.setNombre(nombre);
        this.setMatricula(matricula);
        this.setNumPlazas(numPlazas);
        this.setActivo(activo);
    }

    // Zona de getters
    public String getIdCoche() { return idCoche; }
    public String getNombre() { return nombre; }
    public String getMatricula() { return matricula; }
    public int getNumPlazas() { return numPlazas; }
    public boolean getActivo() { return activo; }
    public Date getFechaAlta() { return fechaAlta; }
    public String getNombreCompleto() {
        return nombre + " - " + matricula;
    }
    /*Sqlite no tiene formato DateTime, con lo que se debe guardar como String*/
    public String getFechaToString(){
        DateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);
        return dateFormat.format(getFechaAlta());
    }

    // Zona de setters
    public void setIdCoche(String idCoche) {
        this.idCoche = idCoche;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setMatricula(String apellido1) {
        this.matricula = apellido1;
    }
    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public void setFechaAltaFromString(String fechaAlta) {
        try {
            setFechaAlta(new SimpleDateFormat(FORMATO_FECHA).parse(fechaAlta));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /*
     * Crear un Id único para la entidad
     * */
    private String generarIdUnico() {
        return "COCHE_" + UUID.randomUUID().toString();
    }

    /*
     * Verifica si la clase está en un estado válido para su tratamiento en operaciones de base de datos CRUD
     * */
    public boolean esEstadoValido() {
        boolean esEstadoValido = false;

        if(nombre != null && !nombre.isEmpty()
                && nombre != null && !nombre.isEmpty()
                && matricula != null && !matricula.isEmpty()
        ){
            esEstadoValido = true;
        }

        return esEstadoValido;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getIdCoche()).append(SALTO_LINEA);
        sb.append("Nombre: ").append(getNombreCompleto()).append(SALTO_LINEA);
        sb.append("Número plazas: ").append(getNumPlazas()).append(SALTO_LINEA);
        sb.append("Activo: ").append(getActivo()).append(SALTO_LINEA);
        sb.append("FechaAlta: ").append(getFechaToString()).append(SALTO_LINEA);
        sb.append("EsEstadoValido: ").append(esEstadoValido()).append(SALTO_LINEA);
        return sb.toString();
    }
}