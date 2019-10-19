package com.example.vanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.database.DatabaseUtils;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.CabeceraPedido;
import com.example.vanapp.Entities.Cliente;
import com.example.vanapp.Entities.DetallePedido;
import com.example.vanapp.Entities.FormaPago;
import com.example.vanapp.Entities.Producto;

import java.util.Calendar;

public class DatabaseTestActivity extends AppCompatActivity {

    DatabaseManager datos;

    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {

                datos.getDb().beginTransaction();

                // Inserción Clientes
                String cliente1 = datos.insertarCliente(new Cliente(null, "Veronica", "Del Topo", "4552000"));
                String cliente2 = datos.insertarCliente(new Cliente(null, "Carlos", "Villagran", "4440000"));

                // Inserción Formas de pago
                String formaPago1 = datos.insertarFormaPago(new FormaPago(null, "Efectivo"));
                String formaPago2 = datos.insertarFormaPago(new FormaPago(null, "Crédito"));

                // Inserción Productos
                String producto1 = datos.insertarProducto(new Producto(null, "Manzana unidad", 2, 100));
                String producto2 = datos.insertarProducto(new Producto(null, "Pera unidad", 3, 230));
                String producto3 = datos.insertarProducto(new Producto(null, "Guayaba unidad", 5, 55));
                String producto4 = datos.insertarProducto(new Producto(null, "Maní unidad", 3.6f, 60));

                // Inserción Pedidos
                String pedido1 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente1, formaPago1));
                String pedido2 = datos.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual,cliente2, formaPago2));

                // Inserción Detalles
                datos.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
                datos.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
                datos.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 3.6f));

                // Eliminación Pedido
                datos.eliminarCabeceraPedido(pedido1);

                // Actualización Cliente
                datos.actualizarCliente(new Cliente(cliente2, "Carlos Alberto", "Villagran", "3333333"));

                datos.getDb().setTransactionSuccessful();
            } finally {
                datos.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Clientes","Clientes");
            DatabaseUtils.dumpCursor(datos.obtenerClientes());
            Log.d("Formas de pago", "Formas de pago");
            DatabaseUtils.dumpCursor(datos.obtenerFormasPago());
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(datos.obtenerProductos());
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(datos.obtenerCabecerasPedidos());
            //Log.d("Detalles de pedido", "Detalles de pedido");
            //DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        getApplicationContext().deleteDatabase("pedidos.db");
        datos = DatabaseManager
                .obtenerInstancia(getApplicationContext());

        new TareaPruebaDatos().execute();
    }
}
