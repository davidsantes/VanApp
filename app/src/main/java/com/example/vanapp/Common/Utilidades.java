package com.example.vanapp.Common;

import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.vanapp.Common.Constantes.FORMATO_FECHA;

public class Utilidades {
    public static boolean esTelefonoValido(String telefono) {
        String regExpn =
                "^(\\+350|00350|350)?[\\s|\\-|\\.]?([0-9][\\s|\\-|\\.]?){9}$";

        CharSequence inputStr = telefono;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean esEmailValido(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    /*Sqlite no tiene formato DateTime, con lo que se debe guardar como String*/
    public static String getFechaToString(Date fecha){
        DateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);
        String fechaFormateada = "";
        fechaFormateada = dateFormat.format(fecha);
        return fechaFormateada;
    }

    /*Convierte un string de fecha a un formato Date sin horas, minutos, etc*/
    public static Date getFechaFromString(String fechaString) {
        Date fechaDate = null;
        try {
            fechaDate = new SimpleDateFormat(FORMATO_FECHA).parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fechaDate;
    }

    public static UsuarioCoche encuentraUsuarioCocheEnLista(ArrayList<UsuarioCoche> listaUsuariosCoches
            , String idUsuario, String idCoche)
    {
        Iterator<UsuarioCoche> iterator = listaUsuariosCoches.iterator();
        while (iterator.hasNext()) {
            UsuarioCoche usuarioCoche = iterator.next();
            if (usuarioCoche.getIdCoche().equals(idCoche) && usuarioCoche.getUsuarioDetalle().getIdUsuario().equals(idUsuario)) {
                return usuarioCoche;
            }
        }
        return null;
    }

    public static Usuario encuentraUsuarioEnLista(ArrayList<Usuario> listaUsuarios
            , String idUsuario, String alias)
    {
        Iterator<Usuario> iterator = listaUsuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getIdUsuario().equals(idUsuario)
                    || usuario.getAlias().equals(alias)) {
                return usuario;
            }
        }
        return null;
    }
}
