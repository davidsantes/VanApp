package com.example.vanapp.Dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.vanapp.Entities.UsuarioInformeConducciones;
import java.util.ArrayList;

public class RepositoryInformes {
    private DatabaseSchema baseDatos;

    public RepositoryInformes(DatabaseSchema baseDatos) {
        this.baseDatos = baseDatos;
    }

    public ArrayList obtenerUsuariosInformeConducciones(int anyoBusqueda) {
        ArrayList<UsuarioInformeConducciones> listaUsuariosInformeConducciones = new ArrayList<>();
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sqlTotal = "";
        String sqlSelect = "";
        String sqlFrom = "";
        String sqlgroupBy = "";
        String sqlWhere = "";
        String sqlOrderBy = "";

        sqlSelect = " select Id, Nombre, Apellido1, Apellido2, Alias, ColorUsuario, count(*) as TotalConducciones, substr(Usuarios_Rondas.FechaDeConduccion, 7) as Anyo";
        sqlFrom += " from Usuarios_Rondas";
        sqlFrom += " inner join Usuarios on Usuarios.Id = Usuarios_Rondas.IdUsuario";
        sqlWhere += " where Usuarios.Activo = 1 AND Usuarios_Rondas.Activo = 1";
        sqlWhere += " AND Anyo = '" + Integer.toString(anyoBusqueda) + "'";
        sqlgroupBy += " group by IdUsuario, Anyo ";
        sqlOrderBy += " order by TotalConducciones desc";

        sqlTotal = sqlSelect + sqlFrom + sqlWhere + sqlgroupBy + sqlOrderBy;
        Cursor cursor = db.rawQuery(sqlTotal, null);

        if (cursor.moveToFirst())
        {
            do{
                listaUsuariosInformeConducciones.add(DatabaseMapping.obtenerInstancia().mapearUsuarioInformeConducciones(cursor));
            }while (cursor.moveToNext());
        }

        return listaUsuariosInformeConducciones;
    }
}
