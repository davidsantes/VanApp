package com.example.vanapp.Activities.Calendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanapp.Activities.Common.UsuariosCochesAdapter;
import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;
import com.example.vanapp.Entities.UsuarioRonda;
import com.example.vanapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarioRondaDetalleActivity extends MasterActivity {

    //Variables
    private DatabaseManager databaseManager;
    private String idCocheElegido;
    private String idRondaElegida;
    private Ronda rondaActual;
    private boolean mostrarRango;
    UsuarioRonda nuevoConductorEnRonda;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private MaterialCalendarView calendarioRonda;
    private Button botonMostrarRango;
    private Button boton_aceptar;

    private TextView textViewPeriodo;
    private TextView textViewNombreRonda;
    private Switch switchRondaFinalizada;

    private ListView listViewPosiblesConductores;
    private ArrayList<UsuarioCoche> listaPosiblesConductores;

    private TextView textViewNombreCompleto;
    private TextView textViewAlias;
    private ImageView iv_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_ronda_detalle);

        menuMasterToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(menuMasterToolbar);

        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        enlazarDatosActividadPadre();
        obtenerRonda();
        enlazarEventosConObjetos();
        mostraRonda();

        //Necesario para mostrar el botón para regresar al padre
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*Además del menú de MasterActivity, inyecta otro menú*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_extra_compartir, menu);

        //Establece el color del menú secundario
        Drawable dwCompartirTurnos = menu.findItem(R.id.opcionCompartir).getIcon();
        dwCompartirTurnos = DrawableCompat.wrap(dwCompartirTurnos);
        DrawableCompat.setTint(dwCompartirTurnos, ContextCompat.getColor(this, R.color.colorWhite));

        menu.findItem(R.id.opcionCompartir).setIcon(dwCompartirTurnos);

        return true;
    }

    @Override
    //Para poder interactuar con el segundo menú propio de esta pantalla
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.opcionCompartir:
                //mostrarActividadUsuariosEnCoche();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enlazarDatosActividadPadre(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idCocheElegido = "";
            idRondaElegida = "";
        }
        else{
            idCocheElegido = extras.getString("ID_COCHE");
            idRondaElegida = extras.getString("ID_RONDA");
        }
    }

    private void obtenerRonda(){
        rondaActual = databaseManager.obtenerRonda(idRondaElegida);
    }

    private void enlazarEventosConObjetos(){
        textViewPeriodo = findViewById(R.id.textViewPeriodo);
        textViewNombreRonda = findViewById(R.id.textViewNombreRonda);
        switchRondaFinalizada = findViewById(R.id.switchRondaFinalizada);

        calendarioRonda = findViewById(R.id.calendarioRonda);
        CalendarDay diaInicioRondaPeriodo = Utilidades.getCalendarDayFromDate(rondaActual.getFechaInicio());
        CalendarDay diaFinRondaPeriodo = Utilidades.getCalendarDayFromDate(rondaActual.getFechaFin());

        //Se establecen los mínimos y máximos que se pueden seleccionar
        calendarioRonda.state().edit()
                .setMinimumDate(diaInicioRondaPeriodo)
                .setMaximumDate(diaFinRondaPeriodo)
                .commit();

        //Work around para solucionar problema con el header
        //https://stackoverflow.com/questions/41828592/android-calendar-header-shows-wrong-month-year-in-material-calendarview?rq=1
        final Calendar calendarFecha = Calendar.getInstance();
        calendarioRonda.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                calendarFecha.setTime(Utilidades.getDateFromCalendarDay(calendarioRonda.getCurrentDate()));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.US);
                String monthAndYear = simpleDateFormat.format(calendarFecha.getTime());
                return monthAndYear;
            }
        });

        calendarioRonda.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                final Date fechaDeConduccion = Utilidades.getDateFromCalendarDay(date);
                abrirDialogoEligeConductor(fechaDeConduccion);
                //Toast.makeText(CalendarioRondaDetalleActivity.this, String.valueOf(date.getDay()), Toast.LENGTH_SHORT).show();
            }
        });

        botonMostrarRango = findViewById(R.id.botonMostrarRango);
        botonMostrarRango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarRango = !mostrarRango;
                dibujarRangoRonda();
            }
        });

        boton_aceptar = findViewById(R.id.boton_aceptar);
        boton_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abrirDialogoEligeConductor();
            }
        });
    }

    private void mostraRonda(){
        String periodo = Utilidades.getFechaToString(rondaActual.getFechaInicio())
                + " - "
                + Utilidades.getFechaToString(rondaActual.getFechaFin());
        textViewPeriodo.setText(periodo);
        textViewNombreRonda.setText(rondaActual.getAlias());
        switchRondaFinalizada.setChecked(rondaActual.EsRondaFinalizada());
    }

    private void dibujarRangoRonda(){
        if (mostrarRango){
            CalendarDay diaInicioRondaPeriodo = Utilidades.getCalendarDayFromDate(rondaActual.getFechaInicio());
            CalendarDay diaFinRondaPeriodo = Utilidades.getCalendarDayFromDate(rondaActual.getFechaFin());

            calendarioRonda.selectRange(diaInicioRondaPeriodo, diaFinRondaPeriodo);
        }else{
            calendarioRonda.clearSelection();
        }
    }

    private void abrirDialogoEligeConductor(final Date fechaDeConduccion){
        if (rondaActual.EsRondaFinalizada()){
            Toast.makeText(CalendarioRondaDetalleActivity.this, R.string.msgRondaCerrada, Toast.LENGTH_SHORT).show();
            return;
        }

        listaPosiblesConductores = new ArrayList<UsuarioCoche>();
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se configura la lista que posteriormente se insertará en el diálogo
        LayoutInflater inflater = getLayoutInflater();
        View vistaConductores = inflater.inflate(R.layout.lista_conductores_calendario, null);
        listViewPosiblesConductores = vistaConductores.findViewById(R.id.listViewConductoresEnElCoche);

        //Se inserta la lista rellenada dentro del adapter
        listaPosiblesConductores = databaseManager.obtenerConductoresDelCoche(rondaActual.getIdCoche());
        UsuariosCochesAdapter conductoresCocheAdapter = new UsuariosCochesAdapter(this, listaPosiblesConductores);
        listViewPosiblesConductores.setAdapter(conductoresCocheAdapter);

        //Se configura la cabecera del diálogo
        mostrarConductorTurno(fechaDeConduccion, vistaConductores);

        //Se configura el diálogo
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CalendarioRondaDetalleActivity.this);
        alertDialog.setTitle(R.string.txtEligeConductor);
        alertDialog.setView(vistaConductores);
        alertDialog.setPositiveButton(R.string.txtGuardar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Inserta el conductor en la base de datos
                boolean operacionOk = false;
                operacionOk = databaseManager.insertarUsuarioRonda(nuevoConductorEnRonda);
                if (operacionOk){

                    //COJONUDO?
                    //https://src-bin.com/es/q/1ff5d17
                    CalendarDay fechaEscogida = Utilidades.getCalendarDayFromDate(nuevoConductorEnRonda.getFechaDeConduccion());
                    calendarioRonda.addDecorator(new EventDecorator(16777215, fechaEscogida));


                    //https://stackoverflow.com/questions/52396913/android-material-calendar-view-how-to-highlight-date-range-with-rounded-corner-b
                    //https://github.com/prolificinteractive/material-calendarview/issues/739
                    //calendarioRonda.setSelectionColor(Color.parseColor("#00BCD4"));
                    //https://codeday.me/es/qa/20190903/1372605.html
                }

                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.txtCancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        listViewPosiblesConductores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsuarioCoche conductorEscogido = (UsuarioCoche)listViewPosiblesConductores.getItemAtPosition(position);

                nuevoConductorEnRonda = new UsuarioRonda();
                nuevoConductorEnRonda.setIdRonda(idRondaElegida);
                nuevoConductorEnRonda.setIdUsuario(conductorEscogido.getUsuarioDetalle().getIdUsuario());
                nuevoConductorEnRonda.setFechaDeConduccion(fechaDeConduccion);
                nuevoConductorEnRonda.setActivo(true);

                //Accede a los controles del padre
                textViewNombreCompleto = view.getRootView().findViewById(R.id.textViewNombreCompleto);
                textViewAlias = view.getRootView().findViewById(R.id.textViewAlias);
                iv_avatar = view.getRootView().findViewById(R.id.iv_avatar);

                textViewNombreCompleto.setText(conductorEscogido.getUsuarioDetalle().getNombreCompleto());
                textViewAlias.setText(conductorEscogido.getUsuarioDetalle().getAlias());
                iv_avatar.setColorFilter(Color.parseColor("#" + conductorEscogido.getUsuarioDetalle().getColorUsuario()));
            }
        });

        alertDialog.show();
    }

    private void mostrarConductorTurno(Date fechaDeConduccion, View vistaConductores){
        Usuario conductor = databaseManager.obtenerConductorEnTurnoDeConduccion(idRondaElegida, fechaDeConduccion);

        textViewNombreCompleto = vistaConductores.findViewById(R.id.textViewNombreCompleto);
        textViewAlias = vistaConductores.findViewById(R.id.textViewAlias);
        iv_avatar = vistaConductores.findViewById(R.id.iv_avatar);
        if (conductor != null){
            iv_avatar.setColorFilter(Color.parseColor("#" + conductor.getColorUsuario()));
            textViewNombreCompleto.setText(conductor.getNombreCompleto());
            textViewAlias.setText(conductor.getAlias());
        }else
        {
            textViewNombreCompleto.setText("Por definir");
            textViewAlias.setText("Por definir");
        }
    }
}
