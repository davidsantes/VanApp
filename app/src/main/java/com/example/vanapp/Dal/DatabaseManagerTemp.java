package com.example.vanapp.Dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.vanapp.EntitiesTemp.CabeceraPedido;
import com.example.vanapp.EntitiesTemp.Cliente;
import com.example.vanapp.EntitiesTemp.DetallePedido;
import com.example.vanapp.EntitiesTemp.FormaPago;
import com.example.vanapp.EntitiesTemp.Producto;
import com.example.vanapp.Dal.DatabaseVanAppTemp.Tablas;

import com.example.vanapp.Dal.DatabaseContractsTemp.CabecerasPedido;
import com.example.vanapp.Dal.DatabaseContractsTemp.DetallesPedido;
import com.example.vanapp.Dal.DatabaseContractsTemp.Productos;
import com.example.vanapp.Dal.DatabaseContractsTemp.Clientes;
import com.example.vanapp.Dal.DatabaseContractsTemp.FormasPago;

/**
 * Clase auxiliar que implementa a {@link DatabaseVanAppTemp} para llevar a cabo el CRUD
 * sobre las entidades existentes.
 * Se realiza mediante un patrón singleton para crear una única instancia de la clase
 */
public final class DatabaseManagerTemp {
    private static DatabaseVanAppTemp baseDatos;

    private static DatabaseManagerTemp instancia = new DatabaseManagerTemp();

    private DatabaseManagerTemp() {
    }

    public static DatabaseManagerTemp obtenerInstancia(Context contexto) {
        if (baseDatos == null) {
            baseDatos = new DatabaseVanAppTemp(contexto);
        }
        return instancia;
    }

    private static final String CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO = "cabecera_pedido " +
            "INNER JOIN cliente " +
            "ON cabecera_pedido.id_cliente = cliente.id " +
            "INNER JOIN forma_pago " +
            "ON cabecera_pedido.id_forma_pago = forma_pago.id";

    private final String[] proyCabeceraPedido = new String[]{
            Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID_CABECERA_PEDIDO,
            CabecerasPedido.FECHA,
            Clientes.NOMBRES,
            Clientes.APELLIDOS,
            FormasPago.NOMBRE
    };

    public Cursor obtenerCabecerasPedidos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        return builder.query(db, proyCabeceraPedido, null, null, null, null, null);
    }

    public Cursor obtenerCabeceraPorId(String id) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s=?", CabecerasPedido.ID_CABECERA_PEDIDO);
        String[] selectionArgs = {id};

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(CABECERA_PEDIDO_JOIN_CLIENTE_Y_FORMA_PAGO);

        String[] proyeccion = {
                Tablas.CABECERA_PEDIDO + "." + CabecerasPedido.ID_CABECERA_PEDIDO,
                CabecerasPedido.FECHA,
                Clientes.NOMBRES,
                Clientes.APELLIDOS,
                FormasPago.NOMBRE};

        return builder.query(db, proyeccion, selection, selectionArgs, null, null, null);
    }

    public String insertarCabeceraPedido(CabeceraPedido pedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCabeceraPedido = CabecerasPedido.generarIdCabeceraPedido();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.ID_CABECERA_PEDIDO, idCabeceraPedido);
        valores.put(CabecerasPedido.FECHA, pedido.fecha);
        valores.put(CabecerasPedido.ID_CLIENTE, pedido.idCliente);
        valores.put(CabecerasPedido.ID_FORMA_PAGO, pedido.idFormaPago);

        // Insertar cabecera
        db.insertOrThrow(Tablas.CABECERA_PEDIDO, null, valores);

        return idCabeceraPedido;
    }

    public boolean actualizarCabeceraPedido(CabeceraPedido pedidoNuevo) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(CabecerasPedido.FECHA, pedidoNuevo.fecha);
        valores.put(CabecerasPedido.ID_CLIENTE, pedidoNuevo.idCliente);
        valores.put(CabecerasPedido.ID_FORMA_PAGO, pedidoNuevo.idFormaPago);

        String whereClause = String.format("%s=?", CabecerasPedido.ID_CABECERA_PEDIDO);
        String[] whereArgs = {pedidoNuevo.idCabeceraPedido};

        int resultado = db.update(Tablas.CABECERA_PEDIDO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCabeceraPedido(String idCabeceraPedido) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = CabecerasPedido.ID_CABECERA_PEDIDO + "=?";
        String[] whereArgs = {idCabeceraPedido};

        int resultado = db.delete(Tablas.CABECERA_PEDIDO, whereClause, whereArgs);

        return resultado > 0;
    }

    public Cursor obtenerDetallesPorIdPedido(String idCabeceraPedido) {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s WHERE %s=?",
                Tablas.DETALLE_PEDIDO, CabecerasPedido.ID_CABECERA_PEDIDO);

        String[] selectionArgs = {idCabeceraPedido};

        return db.rawQuery(sql, selectionArgs);

    }

    public String insertarDetallePedido(DetallePedido detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.ID_CABECERA_PEDIDO, detalle.idCabeceraPedido);
        valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
        valores.put(DetallesPedido.ID_PRODUCTO, detalle.idProducto);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        db.insertOrThrow(Tablas.DETALLE_PEDIDO, null, valores);

        return String.format("%s#%d", detalle.idCabeceraPedido, detalle.secuencia);
    }

    public boolean actualizarDetallePedido(DetallePedido detalle) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(DetallesPedido.SECUENCIA, detalle.secuencia);
        valores.put(DetallesPedido.CANTIDAD, detalle.cantidad);
        valores.put(DetallesPedido.PRECIO, detalle.precio);

        String selection = String.format("%s=? AND %s=?",
                DetallesPedido.ID_CABECERA_PEDIDO, DetallesPedido.SECUENCIA);
        final String[] whereArgs = {detalle.idCabeceraPedido, String.valueOf(detalle.secuencia)};

        int resultado = db.update(Tablas.DETALLE_PEDIDO, valores, selection, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarDetallePedido(String idCabeceraPedido, int secuencia) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String selection = String.format("%s=? AND %s=?",
                DetallesPedido.ID_CABECERA_PEDIDO, DetallesPedido.SECUENCIA);
        String[] whereArgs = {idCabeceraPedido, String.valueOf(secuencia)};

        int resultado = db.delete(Tablas.DETALLE_PEDIDO, selection, whereArgs);

        return resultado > 0;
    }

    // [OPERACIONES_PRODUCTO]
    public Cursor obtenerProductos() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.PRODUCTO);

        return db.rawQuery(sql, null);
    }

    public String insertarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        // Generar Pk
        String idProducto = Productos.generarIdProducto();
        valores.put(Productos.ID_PRODUCTO, idProducto);
        valores.put(Productos.NOMBRE, producto.nombre);
        valores.put(Productos.PRECIO, producto.precio);
        valores.put(Productos.EXISTENCIAS, producto.existencias);

        db.insertOrThrow(Tablas.PRODUCTO, null, valores);

        return idProducto;

    }

    public boolean actualizarProducto(Producto producto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Productos.NOMBRE, producto.nombre);
        valores.put(Productos.PRECIO, producto.precio);
        valores.put(Productos.EXISTENCIAS, producto.existencias);

        String whereClause = String.format("%s=?", Productos.ID_PRODUCTO);
        String[] whereArgs = {producto.idProducto};

        int resultado = db.update(Tablas.PRODUCTO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarProducto(String idProducto) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Productos.ID_PRODUCTO);
        String[] whereArgs = {idProducto};

        int resultado = db.delete(Tablas.PRODUCTO, whereClause, whereArgs);

        return resultado > 0;
    }
// [/OPERACIONES_PRODUCTO]

    // [OPERACIONES_CLIENTE]
    public Cursor obtenerClientes() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.CLIENTE);

        return db.rawQuery(sql, null);
    }

    public String insertarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idCliente = Clientes.generarIdCliente();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.ID_CLIENTE, idCliente);
        valores.put(Clientes.NOMBRES, cliente.nombres);
        valores.put(Clientes.APELLIDOS, cliente.apellidos);
        valores.put(Clientes.TELEFONO, cliente.telefono);

        return db.insertOrThrow(Tablas.CLIENTE, null, valores) > 0 ? idCliente : null;
    }

    public boolean actualizarCliente(Cliente cliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Clientes.NOMBRES, cliente.nombres);
        valores.put(Clientes.APELLIDOS, cliente.apellidos);
        valores.put(Clientes.TELEFONO, cliente.telefono);

        String whereClause = String.format("%s=?", Clientes.ID_CLIENTE);
        final String[] whereArgs = {cliente.idCliente};

        int resultado = db.update(Tablas.CLIENTE, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarCliente(String idCliente) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", Clientes.ID_CLIENTE);
        final String[] whereArgs = {idCliente};

        int resultado = db.delete(Tablas.CLIENTE, whereClause, whereArgs);

        return resultado > 0;
    }
// [/OPERACIONES_CLIENTE]

    // [OPERACIONES_FORMA_PAGO]
    public Cursor obtenerFormasPago() {
        SQLiteDatabase db = baseDatos.getReadableDatabase();

        String sql = String.format("SELECT * FROM %s", Tablas.FORMA_PAGO);

        return db.rawQuery(sql, null);
    }

    public String insertarFormaPago(FormaPago formaPago) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        // Generar Pk
        String idFormaPago = FormasPago.generarIdFormaPago();

        ContentValues valores = new ContentValues();
        valores.put(FormasPago.ID_FORMA_PAGO, idFormaPago);
        valores.put(FormasPago.NOMBRE, formaPago.nombre);

        return db.insertOrThrow(Tablas.FORMA_PAGO, null, valores) > 0 ? idFormaPago : null;
    }

    public boolean actualizarFormaPago(FormaPago formaPago) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(FormasPago.NOMBRE, formaPago.nombre);

        String whereClause = String.format("%s=?", FormasPago.ID_FORMA_PAGO);
        String[] whereArgs = {formaPago.idFormaPago};

        int resultado = db.update(Tablas.FORMA_PAGO, valores, whereClause, whereArgs);

        return resultado > 0;
    }

    public boolean eliminarFormaPago(String idFormaPago) {
        SQLiteDatabase db = baseDatos.getWritableDatabase();

        String whereClause = String.format("%s=?", FormasPago.ID_FORMA_PAGO);
        String[] whereArgs = {idFormaPago};

        int resultado = db.delete(Tablas.FORMA_PAGO, whereClause, whereArgs);

        return resultado > 0;
    }

    public SQLiteDatabase getDb() {
        return baseDatos.getWritableDatabase();
    }


// [/OPERACIONES_FORMA_PAGO]
}
