package com.example.vanapp.Entities;

import static com.example.vanapp.Common.Constantes.COLOR_POR_DEFECTO_USUARIO;

public class UsuarioInformeConducciones  {
    private String idUsuario;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String alias;
    private String colorUsuario;
    private Integer totalConducciones;

    public UsuarioInformeConducciones(){
        this.setColorUsuario(COLOR_POR_DEFECTO_USUARIO);
    }

    // Zona de getters
    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido1() {
        return apellido1;
    }
    public String getApellido2() {
        return apellido2;
    }
    public String getAlias() {
        return alias;
    }
    public String getColorUsuario() {
        return colorUsuario;
    }
    public String getNombreCompleto() {
        return nombre + " " + apellido1;
    }
    public Integer getTotalConducciones() { return totalConducciones; }

    // Zona de setters
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public void setColorUsuario(String colorUsuario) {
        this.colorUsuario = colorUsuario;
    }
    public void setTotalConducciones(Integer totalConducciones) { this.totalConducciones = totalConducciones; }
}
