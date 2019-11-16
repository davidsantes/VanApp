package com.example.vanapp.Activities.Calendario;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final CalendarDay date;
    //CalendarDay currentDay = CalendarDay.from(new Date());
    private Drawable drawable;

    public EventDecorator(int color, CalendarDay fecha) {
        this.color = color;
        this.date = fecha;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        //return date.toString().contains(day.toString());
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new DotSpan(5, color));
        //view.setSelectionDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.RED));
        view.addSpan(new DotSpan(10, Color.RED));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));

    }
}
