package com.example.vanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.database.DatabaseUtils;

import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Dal.DatabaseManagerTemp;
import com.example.vanapp.EntitiesTemp.CabeceraPedido;
import com.example.vanapp.EntitiesTemp.Cliente;
import com.example.vanapp.EntitiesTemp.DetallePedido;
import com.example.vanapp.EntitiesTemp.FormaPago;
import com.example.vanapp.EntitiesTemp.Producto;

import java.util.Calendar;

public class DatabaseTestActivity extends AppCompatActivity {

    DatabaseManagerTemp databaseManagerTemporal;
    DatabaseManager databaseManager;

    public class TareaPruebaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            // [INSERCIONES]
            String fechaActual = Calendar.getInstance().getTime().toString();

            try {
                databaseManager.getDb().beginTransaction();
                databaseManagerTemporal.getDb().beginTransaction();

                // Inserción Clientes
                String cliente1 = databaseManagerTemporal.insertarCliente(new Cliente(null, "Veronica", "Del Topo", "4552000"));
                String cliente2 = databaseManagerTemporal.insertarCliente(new Cliente(null, "Carlos", "Villagran", "4440000"));

                // Inserción Formas de pago
                String formaPago1 = databaseManagerTemporal.insertarFormaPago(new FormaPago(null, "Efectivo"));
                String formaPago2 = databaseManagerTemporal.insertarFormaPago(new FormaPago(null, "Crédito"));

                // Inserción Productos
                String producto1 = databaseManagerTemporal.insertarProducto(new Producto(null, "Manzana unidad", 2, 100));
                String producto2 = databaseManagerTemporal.insertarProducto(new Producto(null, "Pera unidad", 3, 230));
                String producto3 = databaseManagerTemporal.insertarProducto(new Producto(null, "Guayaba unidad", 5, 55));
                String producto4 = databaseManagerTemporal.insertarProducto(new Producto(null, "Maní unidad", 3.6f, 60));

                // Inserción Pedidos
                String pedido1 = databaseManagerTemporal.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual, cliente1, formaPago1));
                String pedido2 = databaseManagerTemporal.insertarCabeceraPedido(
                        new CabeceraPedido(null, fechaActual,cliente2, formaPago2));

                // Inserción Detalles
                databaseManagerTemporal.insertarDetallePedido(new DetallePedido(pedido1, 1, producto1, 5, 2));
                databaseManagerTemporal.insertarDetallePedido(new DetallePedido(pedido1, 2, producto2, 10, 3));
                databaseManagerTemporal.insertarDetallePedido(new DetallePedido(pedido2, 1, producto3, 30, 5));
                databaseManagerTemporal.insertarDetallePedido(new DetallePedido(pedido2, 2, producto4, 20, 3.6f));

                // Eliminación Pedido
                databaseManagerTemporal.eliminarCabeceraPedido(pedido1);

                // Actualización Cliente
                databaseManagerTemporal.actualizarCliente(new Cliente(cliente2, "Carlos Alberto", "Villagran", "3333333"));

                databaseManager.getDb().setTransactionSuccessful();
                databaseManagerTemporal.getDb().setTransactionSuccessful();
            } finally {
                databaseManager.getDb().endTransaction();
                databaseManagerTemporal.getDb().endTransaction();
            }

            // [QUERIES]
            Log.d("Clientes","Clientes");
            DatabaseUtils.dumpCursor(databaseManagerTemporal.obtenerClientes());
            Log.d("Formas de pago", "Formas de pago");
            DatabaseUtils.dumpCursor(databaseManagerTemporal.obtenerFormasPago());
            Log.d("Productos", "Productos");
            DatabaseUtils.dumpCursor(databaseManagerTemporal.obtenerProductos());
            Log.d("Cabeceras de pedido", "Cabeceras de pedido");
            DatabaseUtils.dumpCursor(databaseManagerTemporal.obtenerCabecerasPedidos());
            //Log.d("Detalles de pedido", "Detalles de pedido");
            //DatabaseUtils.dumpCursor(datos.obtenerDetallesPedido());

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_database_test);

        getApplicationContext().deleteDatabase("pedidos.db;");
        getApplicationContext().deleteDatabase("vanapp.db");

        databaseManagerTemporal = DatabaseManagerTemp
                .obtenerInstancia(getApplicationContext());

        databaseManager = DatabaseManager
                .obtenerInstancia(getApplicationContext());

        new TareaPruebaDatos().execute();
    }
}
