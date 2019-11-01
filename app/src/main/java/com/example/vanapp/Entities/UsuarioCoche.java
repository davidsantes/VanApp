package com.example.vanapp.Entities;

public class UsuarioCoche {
    private String idCoche;
    private boolean esCondutor;
    private boolean activo;
    private Usuario usuarioDetalle;

    public UsuarioCoche(String idUsuario,
                        String idCoche){
        this.setIdCoche(idCoche);
        this.setEsCondutor(true);
        this.setActivo(true);

        setUsuarioDetalle(new Usuario());
        getUsuarioDetalle().setIdUsuario(idUsuario);
    }

    // Zona de getters
    public String getIdCoche() { return idCoche; }
    public boolean esCondutor() { return esCondutor; }
    public boolean esActivo() { return activo; }
    public Usuario getUsuarioDetalle() { return usuarioDetalle; }

    // Zona de setters
    public void setIdCoche(String idCoche) { this.idCoche = idCoche; }
    public void setEsCondutor(boolean esCondutor) { this.esCondutor = esCondutor; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public void setUsuarioDetalle(Usuario usuarioDetalle) { this.usuarioDetalle = usuarioDetalle; }

    /*
     * Verifica si la clase está en un estado válido para su tratamiento en operaciones de base de datos CRUD
     * */
    public boolean esEstadoValido() {
        boolean esEstadoValido = false;

        if(idCoche != null && !idCoche.isEmpty()
                && getUsuarioDetalle() != null
                && getUsuarioDetalle().esEstadoValido()
        ){
            esEstadoValido = true;
        }

        return esEstadoValido;
    }
}
