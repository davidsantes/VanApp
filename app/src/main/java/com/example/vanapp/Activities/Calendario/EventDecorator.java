package com.example.vanapp.Activities.Calendario;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import static android.graphics.Color.*;

public class EventDecorator implements DayViewDecorator {

    private final String colorParaMostrar;
    private final CalendarDay fechaParaMostrar;

    public EventDecorator(String colorParaMostrar, CalendarDay fecha) {
        this.colorParaMostrar = colorParaMostrar;
        this.fechaParaMostrar = fecha;
    }

    /*
    * Compara si coincide la fecha que hay que señalar con la fecha que tiene el calendario
    * */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (day.getDay() == fechaParaMostrar.getDay()
            && day.getMonth() == fechaParaMostrar.getMonth()
            && day.getYear() == fechaParaMostrar.getYear()
        )
            return true;
        else
            return false;
    }

    /*
     * En caso de que haya que decorar, se muestra un punto con el color del usuario
     * */
    @Override
    public void decorate(DayViewFacade view) {
        int colorParseado = parseColor("#" + colorParaMostrar);
        view.addSpan(new DotSpan(15, colorParseado));
    }
}
