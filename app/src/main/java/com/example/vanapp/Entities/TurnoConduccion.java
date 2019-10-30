package com.example.vanapp.Entities;

import java.util.Date;

public class TurnoConduccion {
    private String idUsuario;
    private String idRonda;
    private Date fechaDeConduccion;
    private boolean activo;

    public String getIdUsuario() { return idUsuario; }
    public String getIdRonda() { return idRonda; }
    public Date getFechaDeConduccion() { return fechaDeConduccion; }
    public boolean esActivo() { return activo; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public void setIdRonda(String idRonda) { this.idRonda = idRonda; }
    public void setFechaDeConduccion(Date fechaDeConduccion) { this.fechaDeConduccion = fechaDeConduccion; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
