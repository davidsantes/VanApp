package com.example.vanapp.Activities.Calendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.vanapp.Activities.MasterActivity;
import com.example.vanapp.Activities.Usuarios.UsuariosAdapter;
import com.example.vanapp.Common.Utilidades;
import com.example.vanapp.Dal.DatabaseManager;
import com.example.vanapp.Entities.Coche;
import com.example.vanapp.Entities.Ronda;
import com.example.vanapp.Entities.Usuario;
import com.example.vanapp.Entities.UsuarioCoche;
import com.example.vanapp.Entities.UsuarioRonda;
import com.example.vanapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.vanapp.Common.Constantes.SALTO_LINEA;

public class CalendarioRondaDetalleActivity extends MasterActivity {

    //Variables
    private DatabaseManager databaseManager;
    private String idRondaElegida;
    private Ronda rondaActual;
    private boolean mostrarRango;
    private UsuarioRonda nuevoConductorEnRonda;
    private ArrayList<Usuario> listaPosiblesConductores;

    //Componentes de la capa de presentación
    private Toolbar menuMasterToolbar;
    private MaterialCalendarView calendarioRonda;
    private Button botonMostrarRango;

    //Cabecera
    private TextView textViewPeriodo;
    private TextView textViewNombreRonda;
    private Switch switchRondaFinalizada;

    //Diálogo
    private ListView listViewPosiblesConductores;
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
        obtenerActualizarRonda();
        enlazarEventosConObjetos();
        mostrarCabecera();
        mostrarRangoConConductores();

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
                mailEviar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enlazarDatosActividadPadre(){
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            idRondaElegida = "";
        }
        else{
            idRondaElegida = extras.getString("ID_RONDA");
        }
    }

    private void obtenerActualizarRonda(){
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

        //Work around para solucionar problema con el header del calendario (no muestra el mes correcto)
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
            }
        });

        botonMostrarRango = findViewById(R.id.botonMostrarRango);
        botonMostrarRango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarRango = !mostrarRango;
                mostrarRango();
            }
        });
    }

    private void mostrarCabecera(){
        String periodo = Utilidades.getFechaToString(rondaActual.getFechaInicio())
                + " - "
                + Utilidades.getFechaToString(rondaActual.getFechaFin());
        textViewPeriodo.setText(periodo);
        textViewNombreRonda.setText(rondaActual.getAlias());
        switchRondaFinalizada.setChecked(rondaActual.EsRondaFinalizada());
    }

    private void mostrarRango(){
        if (mostrarRango){
            CalendarDay diaInicioRondaPeriodo = Utilidades.getCalendarDayFromDate(rondaActual.getFechaInicio());
            CalendarDay diaFinRondaPeriodo = Utilidades.getCalendarDayFromDate(rondaActual.getFechaFin());

            calendarioRonda.selectRange(diaInicioRondaPeriodo, diaFinRondaPeriodo);
        }else{
            calendarioRonda.clearSelection();
        }
    }

    private void mostrarRangoConConductores(){
        ArrayList<UsuarioRonda> listaConductoresDeLaRonda = new ArrayList<>();
        listaConductoresDeLaRonda = databaseManager.obtenerUsuariosDeLaRonda(idRondaElegida);

        //calendarioRonda.removeDecorators();
        for (UsuarioRonda conductor: listaConductoresDeLaRonda) {
            CalendarDay fechaEscogida = Utilidades.getCalendarDayFromDate(conductor.getFechaDeConduccion());
            Usuario conductorDetalle = databaseManager.obtenerUsuario(conductor.getIdUsuario());

            calendarioRonda.addDecorator(new EventDecorator(conductorDetalle.getColorUsuario(), fechaEscogida));
        }
    }

    /*
    * Crea un usuario vacío
    * */
    private Usuario GetConductorPorDefinir()
    {
        Usuario usuarioPorDefinir = new Usuario();
        usuarioPorDefinir.setIdUsuario("");
        usuarioPorDefinir.setNombre("Por definir");
        usuarioPorDefinir.setApellido1("");
        usuarioPorDefinir.setApellido2("");
        usuarioPorDefinir.setColorUsuario("9a9a9a");
        return usuarioPorDefinir;
    }

    private void abrirDialogoEligeConductor(final Date fechaDeConduccion){
        if (rondaActual.EsRondaFinalizada()){
            Toast.makeText(CalendarioRondaDetalleActivity.this, R.string.msgRondaCerrada, Toast.LENGTH_SHORT).show();
            return;
        }

        listaPosiblesConductores = new ArrayList<Usuario>();
        databaseManager = DatabaseManager.obtenerInstancia(getApplicationContext());

        //Se configura la lista que posteriormente se insertará en el diálogo
        LayoutInflater inflater = getLayoutInflater();
        View vistaConductores = inflater.inflate(R.layout.dialogo_conductores_calendario, null);
        listViewPosiblesConductores = vistaConductores.findViewById(R.id.listViewConductoresEnElCoche);

        //Se inserta la lista rellenada dentro del adapter, con un usuario vacío
        listaPosiblesConductores = databaseManager.obtenerConductoresDelCoche(rondaActual.getIdCoche());
        listaPosiblesConductores.add(GetConductorPorDefinir());

        UsuariosAdapter conductoresCocheAdapter = new UsuariosAdapter(this, listaPosiblesConductores);
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
                if (nuevoConductorEnRonda.getIdUsuario() == "")
                {
                    operacionOk = databaseManager.eliminarRelacionDeUnDiaEnLaRonda(idRondaElegida, nuevoConductorEnRonda.getFechaDeConduccion(), false);
                }
                else
                {
                    operacionOk = databaseManager.insertarUsuarioRonda(nuevoConductorEnRonda);
                }

                if (operacionOk){
                    Toast.makeText(CalendarioRondaDetalleActivity.this, R.string.msgOperacionOk, Toast.LENGTH_SHORT).show();
                    mostrarRangoConConductores();
                }
                else{
                    Toast.makeText(CalendarioRondaDetalleActivity.this, R.string.msgOperacionKo, Toast.LENGTH_SHORT).show();
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
                Usuario conductorEscogido = (Usuario)listViewPosiblesConductores.getItemAtPosition(position);

                nuevoConductorEnRonda = new UsuarioRonda();
                nuevoConductorEnRonda.setIdRonda(idRondaElegida);
                nuevoConductorEnRonda.setIdUsuario(conductorEscogido.getIdUsuario());
                nuevoConductorEnRonda.setFechaDeConduccion(fechaDeConduccion);
                nuevoConductorEnRonda.setActivo(true);

                //Accede a los controles del padre
                textViewNombreCompleto = view.getRootView().findViewById(R.id.textViewNombreCompleto);
                textViewAlias = view.getRootView().findViewById(R.id.textViewAlias);
                iv_avatar = view.getRootView().findViewById(R.id.iv_avatar);

                textViewNombreCompleto.setText(conductorEscogido.getNombreCompleto());
                textViewAlias.setText(conductorEscogido.getAlias());
                iv_avatar.setColorFilter(Color.parseColor("#" + conductorEscogido.getColorUsuario()));
            }
        });

        alertDialog.show();
    }

    private void mostrarConductorTurno(Date fechaDeConduccion, View vistaConductores){
        Usuario conductor = databaseManager.obtenerConductorEnTurnoDeConduccion(idRondaElegida, fechaDeConduccion);

        textViewNombreCompleto = vistaConductores.findViewById(R.id.textViewNombreCompleto);
        textViewAlias = vistaConductores.findViewById(R.id.textViewAlias);
        iv_avatar = vistaConductores.findViewById(R.id.iv_avatar);
        if (conductor == null){
            conductor = GetConductorPorDefinir();
        }

        textViewNombreCompleto.setText(conductor.getNombreCompleto());
        textViewAlias.setText(conductor.getAlias());
        iv_avatar.setColorFilter(Color.parseColor("#" + conductor.getColorUsuario()));
    }

    private void mailEviar(){
        obtenerActualizarRonda();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mailComponerDestinatarios());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, mailComponerAsunto());
        emailIntent.putExtra(Intent.EXTRA_TEXT, mailComponerCuerpoMail());
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviando mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CalendarioRondaDetalleActivity.this, "No ha un cliente para enviar mails instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] mailComponerDestinatarios()
    {
        String[] listaDestinatarios;
        ArrayList<UsuarioCoche> listaDestinatariosMensaje;
        listaDestinatariosMensaje = databaseManager.obtenerUsuariosDelCoche(rondaActual.getIdCoche());
        listaDestinatarios = new String[listaDestinatariosMensaje.size()];

        if(listaDestinatariosMensaje != null){
            for (int i = 0; i < listaDestinatariosMensaje.size(); i++) {
                listaDestinatarios[i] = listaDestinatariosMensaje.get(i).getUsuarioDetalle().getEmail();
            }
        }

        return listaDestinatarios;
    }

    private String mailComponerAsunto(){
        String asunto = "";
        asunto += "Turnos de la Ronda: ";
        asunto += rondaActual.getAlias();
        asunto += " (" + Utilidades.getFechaToString(rondaActual.getFechaInicio());
        asunto += " - " + Utilidades.getFechaToString(rondaActual.getFechaFin())+ ")";
        return asunto;
    }

    private String mailComponerCuerpoMail(){
        Coche cocheActual = databaseManager.obtenerCoche(rondaActual.getIdCoche());

        String cuerpoMail = "";

        cuerpoMail += "Coche: " + cocheActual.getNombre() + " - " + cocheActual.getMatricula();
        cuerpoMail += SALTO_LINEA + SALTO_LINEA;

        cuerpoMail += "Ronda: " + rondaActual.getAlias();
        cuerpoMail += " (" + Utilidades.getFechaToString(rondaActual.getFechaInicio());
        cuerpoMail += " - " + Utilidades.getFechaToString(rondaActual.getFechaFin())+ ")";
        cuerpoMail += SALTO_LINEA + SALTO_LINEA;

        cuerpoMail += "Turnos: " + SALTO_LINEA;

        if (rondaActual.getListaTurnosDeConduccion() != null && rondaActual.getListaTurnosDeConduccion().size() > 0){
            for (UsuarioRonda turnoConductor: rondaActual.getListaTurnosDeConduccion()) {
                Usuario conductorDetalle = databaseManager.obtenerUsuario(turnoConductor.getIdUsuario());
                cuerpoMail += "- " + conductorDetalle.getAlias() + ": " + Utilidades.getFechaToString(turnoConductor.getFechaDeConduccion());
                cuerpoMail += SALTO_LINEA;
            }
        }

        return cuerpoMail;
    }
}