package com.example.vanapp.Dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;
import com.example.vanapp.Entities.UsuarioRonda;

import java.util.ArrayList;
import java.util.Date;

/**
 * Clase auxiliar que implementa a {@link DatabaseSchema} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 * Se realiza mediante un patrón singleton para crear una única instancia de la clase
 */
public class DatabaseManager {
    private static DatabaseSchema baseDatos;
    private static DatabaseManager instancia = new DatabaseManager();
    private static RepositoryUsuarios repositoryUsuarios;
    private static RepositoryCoches repositoryCoches;
    private static RepositoryUsuariosCoche repositoryUsuariosCoche;
    private static RepositoryRondas repositoryRondas;
    private static RepositoryUsuariosRonda repositoryUsuariosRonda;

    private DatabaseManager() {

    }

    public static DatabaseManager obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new DatabaseSchema(contexto);
        }
        if (repositoryUsuarios == null){
            repositoryUsuarios = new RepositoryUsuarios(baseDatos);
        }
        if (repositoryCoches == null){
            repositoryCoches = new RepositoryCoches(baseDatos);
        }
        if (repositoryUsuariosCoche == null){
            repositoryUsuariosCoche = new RepositoryUsuariosCoche(baseDatos);
        }
        if (repositoryRondas == null){
            repositoryRondas = new RepositoryRondas(baseDatos);
        }
        if (repositoryUsuariosRonda == null){
            repositoryUsuariosRonda = new RepositoryUsuariosRonda(baseDatos);
        }

        return instancia;
    }

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }

    // INICIO [OPERACIONES_USUARIO]
    public boolean existenUsuarios(){
        return repositoryUsuarios.existenUsuarios();
    }

    public ArrayList obtenerUsuarios() {
        return repositoryUsuarios.obtenerUsuarios();
    }

    public Usuario obtenerUsuario(String idUsuario){
        return repositoryUsuarios.obtenerUsuario(idUsuario);
    }

    public boolean insertarUsuario(Usuario usuario) {
        return repositoryUsuarios.insertarUsuario(usuario);
    }

    public boolean actualizarUsuario(Usuario usuario) {
        return repositoryUsuarios.actualizarUsuario(usuario);
    }

    /**
     * Elimina todos los usuarios.
     */
    public boolean eliminarUsuariosTodos() {
        this.eliminarRelacionDeUsuariosConCochesTodos();
        this.eliminarRelacionDeUsuariosConRondasTodos();
        return repositoryUsuarios.eliminarUsuariosTodos();
    }

    /**
     * Elimina un usuario concreto.
     * @param idUsuario usuario a eliminar
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarUsuario(String idUsuario, boolean esBorradoLogico) {
        this.eliminarRelacionDeUsuarioConTodosLosCoches(idUsuario, esBorradoLogico);
        return repositoryUsuarios.eliminarUsuario(idUsuario, esBorradoLogico);
    }
    // FIN [OPERACIONES_USUARIO]

    // INICIO [OPERACIONES_COCHE]
    public ArrayList obtenerCoches() {
        return repositoryCoches.obtenerCoches();
    }

    public Coche obtenerCoche(String idCoche){
        Coche nuevoCoche = repositoryCoches.obtenerCoche(idCoche);

        nuevoCoche.setListaUsuariosEnCoche(obtenerUsuariosDelCoche(idCoche));
        nuevoCoche.setListaRondasDelCoche(obtenerRondasDelCoche(idCoche));

        return nuevoCoche;
    }

    public boolean insertarCoche(Coche coche) {
        return repositoryCoches.insertarCoche(coche);
    }

    public boolean actualizarCoche(Coche coche) {
        return repositoryCoches.actualizarCoche(coche);
    }

    /**
     * Elimina todos los coches.
     */
    public boolean eliminarCochesTodos() {
        this.eliminarRelacionDeUsuariosConCochesTodos();
        this.eliminarRondasTodas();
        return repositoryCoches.eliminarCochesTodos();
    }

    /**
     * Elimina un coche concreto.
     * @param idCoche coche concreto a eliminar
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarCoche(String idCoche, boolean esBorradoLogico) {
        this.eliminarRelacionDeCocheConTodosLosUsuarios(idCoche, esBorradoLogico);

        ArrayList<Ronda> listaRondas = new ArrayList<>();
        listaRondas = this.obtenerRondasDelCoche(idCoche);
        for (Ronda ronda: listaRondas) {
            this.eliminarRonda(ronda.getIdRonda(), esBorradoLogico);
        }

        return repositoryCoches.eliminarCoche(idCoche, esBorradoLogico);
    }
    // FIN [OPERACIONES_COCHE]

    // INICIO [OPERACIONES_USUARIOS_COCHE]
    public ArrayList obtenerUsuariosDelCoche(String idCoche) {
        return repositoryUsuariosCoche.obtenerUsuariosDelCoche(idCoche, false);
    }

    public ArrayList obtenerConductoresDelCoche(String idCoche) {
        ArrayList<UsuarioCoche> listaUsuariosDelCoche = new ArrayList<>();
        listaUsuariosDelCoche = repositoryUsuariosCoche.obtenerUsuariosDelCoche(idCoche, true);

        ArrayList<Usuario> listaConductores = new ArrayList<>();
        for (UsuarioCoche usuarioCoche: listaUsuariosDelCoche) {
            Usuario nuevoConductor = new Usuario();
            nuevoConductor = usuarioCoche.getUsuarioDetalle();
            listaConductores.add(nuevoConductor);
        }

        return listaConductores;
    }

    public ArrayList obtenerUsuariosNoDelCoche(String idCoche) {
        return repositoryUsuariosCoche.obtenerUsuariosNoDelCoche(idCoche);
    }

    public boolean insertarUsuarioCoche(UsuarioCoche usuarioCoche) {
        return repositoryUsuariosCoche.insertarUsuarioCoche(usuarioCoche);
    }

    /**
     * Elimina todas las relaciones entre usuarios y coches.
     */
    private boolean eliminarRelacionDeUsuariosConCochesTodos() {
        return repositoryUsuariosCoche.eliminarRelacionDeUsuariosConCochesTodos();
    }

    /**
     * Elimina una relación concreta.
     * @param idUsuario a eliminar.
     * @param idCoche a eliminar.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConCoche(String idUsuario, String idCoche, boolean esBorradoLogico) {
        return repositoryUsuariosCoche.eliminarRelacionDeUsuarioConCoche(idUsuario, idCoche, esBorradoLogico);
    }

    /**
     * Elimina todas las relaciones de un usuario concreto con todos los coches.
     * @param idUsuario .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConTodosLosCoches(String idUsuario, boolean esBorradoLogico) {
        return repositoryUsuariosCoche.eliminarRelacionDeUsuarioConTodosLosCoches(idUsuario, esBorradoLogico);
    }

    /**
     * Elimina todas las relaciones de un coche concreto con todos los usuarios.
     * @param idCoche .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeCocheConTodosLosUsuarios(String idCoche, boolean esBorradoLogico) {
        return repositoryUsuariosCoche.eliminarRelacionDeCocheConTodosLosUsuarios(idCoche, esBorradoLogico);
    }
    // FIN [OPERACIONES_USUARIOS_COCHE]

    // INICIO [RONDAS]
    public ArrayList obtenerRondasDelCoche(String idCoche) {
        return repositoryRondas.obtenerRondasDelCoche(idCoche);
    }

    public Ronda obtenerRonda(String idRonda){
        Ronda nuevaRonda = null;
        nuevaRonda = repositoryRondas.obtenerRonda(idRonda);

        if (nuevaRonda != null){
            ArrayList<UsuarioRonda> listaUsuariosDeLaRonda = new ArrayList<>();
            listaUsuariosDeLaRonda = this.obtenerUsuariosDeLaRonda(idRonda);
            if (listaUsuariosDeLaRonda != null){
                nuevaRonda.setListaTurnosDeConduccion(listaUsuariosDeLaRonda);
            }
        }

        return nuevaRonda;
    }

    public boolean insertarRondaDelCoche(Ronda ronda) {
        return repositoryRondas.insertarRondaDelCoche(ronda);
    }

    public boolean actualizarRondaDelCoche(Ronda ronda) {
        return repositoryRondas.actualizarRondaDelCoche(ronda);
    }

    /**
     * Elimina todas las rondas.
     */
    private boolean eliminarRondasTodas() {
        this.eliminarRelacionDeUsuariosConRondasTodos();
        return repositoryRondas.eliminarRondasTodas();
    }

    /**
     * Elimina una relación concreta.
     * @param idRonda a eliminar.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRonda(String idRonda, boolean esBorradoLogico) {
        this.eliminarRelacionDeRondaConTodosLosUsuarios(idRonda, esBorradoLogico);
        return repositoryRondas.eliminarRonda(idRonda, esBorradoLogico);
    }

    /**
     * Elimina todas las relaciones de un coche concreto con todas las rondas.
     * @param idCoche .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRondasDeUnCoche(String idCoche, boolean esBorradoLogico) {
        //Eliminación en cascada
        ArrayList<Ronda> listaRondasDelCoche;
        listaRondasDelCoche = obtenerRondasDelCoche(idCoche);
        boolean operacionOk = false;

        for (Ronda rondaParaEliminar: listaRondasDelCoche) {
            operacionOk = eliminarRonda(rondaParaEliminar.getIdRonda(), esBorradoLogico);
        }

        return operacionOk;
    }
    // FIN [RONDAS]

    // INICIO [OPERACIONES_USUARIOS_RONDA]
    public boolean existenRondas(){
        return repositoryRondas.existenRondas();
    }

    public ArrayList obtenerUsuariosDeLaRonda(String idRonda) {
        return repositoryUsuariosRonda.obtenerUsuariosDeLaRonda(idRonda);
    }

    public Usuario obtenerConductorEnTurnoDeConduccion(String idRonda, Date fechaDeConduccion) {
        UsuarioRonda usuarioEnTurnoDeConduccion;
        Usuario conductor;
        usuarioEnTurnoDeConduccion = repositoryUsuariosRonda.obtenerConductorEnTurnoDeConduccion(idRonda, fechaDeConduccion);
        if (usuarioEnTurnoDeConduccion != null && usuarioEnTurnoDeConduccion.getIdUsuario() != null)
        {
            conductor = this.obtenerUsuario(usuarioEnTurnoDeConduccion.getIdUsuario());
        }
        else {
            conductor = null;
        }
        return conductor;
    }

    public boolean insertarUsuarioRonda(UsuarioRonda usuarioRonda) {
        this.eliminarRelacionDeUnDiaEnLaRonda(usuarioRonda.getIdRonda(), usuarioRonda.getFechaDeConduccion(), false);
        return repositoryUsuariosRonda.insertarUsuarioRonda(usuarioRonda);
    }

    /**
     * Elimina todas las relaciones entre usuarios y rondas.
     */
    private boolean eliminarRelacionDeUsuariosConRondasTodos() {
        return repositoryUsuariosRonda.eliminarRelacionDeUsuariosConRondasTodos();
    }

    /**
     * Elimina una relación concreta.
     * @param idRonda a eliminar.
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUnDiaEnLaRonda(String idRonda, Date fechaDeConduccion, boolean esBorradoLogico) {
        return repositoryUsuariosRonda.eliminarRelacionDeUnDiaEnLaRonda(idRonda, fechaDeConduccion, esBorradoLogico);
    }

    /**
     * Elimina todas las relaciones de un usuario concreto con todas las rondas.
     * @param idUsuario .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeUsuarioConTodasLasRondas(String idUsuario, boolean esBorradoLogico) {
        return repositoryUsuariosRonda.eliminarRelacionDeUsuarioConTodasLasRondas(idUsuario, esBorradoLogico);
    }

    /**
     * Elimina todas las relaciones de una ronda en concreto con todos los usuarios.
     * @param idRonda .
     * @param esBorradoLogico indica si el borrado es lógico (se actualiza el campo activo) o físico (se elimina definitivamente de la Bdd)
     */
    public boolean eliminarRelacionDeRondaConTodosLosUsuarios(String idRonda, boolean esBorradoLogico) {
        return repositoryUsuariosRonda.eliminarRelacionDeRondaConTodosLosUsuarios(idRonda, esBorradoLogico);
    }

    // FIN [OPERACIONES_USUARIOS_RONDA]
}