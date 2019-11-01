package com.example.vanapp.Entities;

import com.example.vanapp.Common.Utilidades;
import java.util.Date;
import java.util.UUID;
import static com.example.vanapp.Common.Constantes.*;
import static com.example.vanapp.Common.Utilidades.*;

public class Usuario {
    private String idUsuario;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String alias;
    private boolean activo;
    private String email;
    private String colorUsuario;
    private Date fechaAlta = new Date();

    public Usuario(){
        this.setIdUsuario(generarIdUnico());
        this.setActivo(true);
        this.setColorUsuario(COLOR_POR_DEFECTO_USUARIO);
    }

    public Usuario(String nombre,
                   String apellido1,
                   String apellido2,
                   String alias,
                   String email)
    {
        this.setIdUsuario(generarIdUnico());
        this.setNombre(nombre);
        this.setApellido1(apellido1);
        this.setApellido2(apellido2);
        this.setAlias(alias);
        this.setActivo(true);
        this.setEmail(email);
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
    public boolean esActivo() { return activo; }
    public String getEmail() {
        return email;
    }
    public String getColorUsuario() {
        return colorUsuario;
    }
    public Date getFechaAlta() { return fechaAlta; }
    public String getNombreCompleto() {
        return nombre + " " + apellido1 + " " + apellido2;
    }

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
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setColorUsuario(String colorUsuario) {
        this.colorUsuario = colorUsuario;
    }
    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /*
     * Crear un Id único para la entidad
     * */
    private String generarIdUnico() {
        return "USU_" + UUID.randomUUID().toString();
    }

    /*
    * Verifica si la clase está en un estado válido para su tratamiento en operaciones de base de datos CRUD
    * */
    public boolean esEstadoValido() {
        boolean esEstadoValido = false;

        if(idUsuario != null && !idUsuario.isEmpty()
                && nombre != null && !nombre.isEmpty()
                && apellido1 != null && !apellido1.isEmpty()
                && apellido2 != null && !apellido2.isEmpty()
                && alias != null && !alias.isEmpty()
                && email != null && !email.isEmpty()
                && colorUsuario != null && !colorUsuario.isEmpty()
                && esEmailValido(email)
        ){
            esEstadoValido = true;
        }

        return esEstadoValido;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getIdUsuario()).append(SALTO_LINEA);
        sb.append("Nombre completo: ").append(getNombreCompleto()).append(SALTO_LINEA);
        sb.append("Alias: ").append(getAlias()).append(SALTO_LINEA);
        sb.append("Activo: ").append(esActivo()).append(SALTO_LINEA);
        sb.append("Email: ").append(getEmail()).append(SALTO_LINEA);
        sb.append("Color de usuario: ").append(getColorUsuario()).append(SALTO_LINEA);
        sb.append("FechaAlta: ").append(Utilidades.getFechaToString(getFechaAlta())).append(SALTO_LINEA);
        sb.append("EsEstadoValido: ").append(esEstadoValido()).append(SALTO_LINEA);
        return sb.toString();
    }
}
