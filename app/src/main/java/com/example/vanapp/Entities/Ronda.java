package com.example.vanapp.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Ronda {
    private String idRonda;
    private String idCoche;
    private String alias;
    private Date fechaInicio = new Date();
    private Date fechaFin = new Date();
    private boolean esRondaFinalizada;
    private boolean activo;
    private ArrayList<UsuarioRonda> listaTurnosDeConduccion;

    public Ronda(){
        this.setIdRonda(generarIdUnico());
        this.setActivo(true);
        this.setEsRondaFinalizada(false);
    }

    // Zona de getters
    public String getIdRonda() { return idRonda; }
    public String getIdCoche() { return idCoche; }
    public String getAlias() { return alias; }
    public Date getFechaInicio() { return fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public boolean EsRondaFinalizada() { return esRondaFinalizada; }
    public boolean EsActivo() { return activo; }
    public ArrayList<UsuarioRonda> getListaTurnosDeConduccion() {
        return listaTurnosDeConduccion;
    }

    // Zona de setters
    public void setIdRonda(String idRonda) { this.idRonda = idRonda; }
    public void setIdCoche(String idCoche) { this.idCoche = idCoche; }
    public void setAlias(String alias) { this.alias = alias; }
    public void setFechaInicio(Date fechaDeInicio) { this.fechaInicio = fechaDeInicio; }
    public void setFechaFin(Date fechaDeFin) { this.fechaFin = fechaDeFin; }
    public void setEsRondaFinalizada(boolean esRondaFinalizada) { this.esRondaFinalizada = esRondaFinalizada; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public void setListaTurnosDeConduccion(ArrayList<UsuarioRonda> listaTurnosDeConduccion) {
        this.listaTurnosDeConduccion = listaTurnosDeConduccion;
    }

    /*
     * Crear un Id único para la entidad
     * */
    private String generarIdUnico() {
        return "RONDA_" + UUID.randomUUID().toString();
    }

    /*
     * Verifica si la clase está en un estado válido para su tratamiento en operaciones de base de datos CRUD
     * */
    public boolean esEstadoValido() {
        boolean esEstadoValido = false;

        if(idCoche != null && !idCoche.isEmpty()
                && alias != null && !alias.isEmpty()
                && fechaInicio != null
                && fechaFin != null
                && fechaFin.compareTo(fechaInicio) >= 0
        ){
            esEstadoValido = true;
        }

        return esEstadoValido;
    }
}
