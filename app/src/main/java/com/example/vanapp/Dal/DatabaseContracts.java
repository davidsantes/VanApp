package com.example.vanapp.Dal;

import java.util.UUID;

/**
 * Clase que establece los nombres a usar en la base de datos
 */
public class DatabaseContracts {
    interface ColumnasCabeceraPedido {
        String ID_CABECERA_PEDIDO = "id";
        String FECHA = "fecha";
        String ID_CLIENTE = "id_cliente";
        String ID_FORMA_PAGO = "id_forma_pago";
    }

    interface ColumnasDetallePedido {
        String ID_CABECERA_PEDIDO = "id";
        String SECUENCIA = "secuencia";
        String ID_PRODUCTO = "id_producto";
        String CANTIDAD = "cantidad";
        String PRECIO = "precio";
    }

    interface ColumnasProducto {
        String ID_PRODUCTO = "id";
        String NOMBRE = "nombre";
        String PRECIO = "precio";
        String EXISTENCIAS = "existencias";
    }

    interface ColumnasCliente {
        String ID_CLIENTE = "id";
        String NOMBRES = "nombres";
        String APELLIDOS = "apellidos";
        String TELEFONO = "telefono";
    }

    interface ColumnasFormaPago {
        String ID_FORMA_PAGO = "id";
        String NOMBRE = "nombre";
    }

    public static class CabecerasPedido implements ColumnasCabeceraPedido {
        public static String generarIdCabeceraPedido() {
            return "CP-" + UUID.randomUUID().toString();
        }
    }

    public static class DetallesPedido implements ColumnasDetallePedido {
        // Métodos auxiliares
    }

    public static class Productos implements ColumnasProducto{
        public static String generarIdProducto() {
            return "PRO-" + UUID.randomUUID().toString();
        }
    }

    public static class Clientes implements ColumnasCliente{
        public static String generarIdCliente() {
            return "CLI-" + UUID.randomUUID().toString();
        }
    }

    public static class FormasPago implements ColumnasFormaPago{
        public static String generarIdFormaPago() {
            return "FP-" + UUID.randomUUID().toString();
        }
    }

    private DatabaseContracts() {
    }
}
