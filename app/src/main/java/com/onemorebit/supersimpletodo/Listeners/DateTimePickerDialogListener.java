package com.onemorebit.supersimpletodo.Listeners;

import android.app.Activity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

/**
 * Created by Euro on 1/6/16 AD.
 */
public abstract class DateTimePickerDialogListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private final Activity activity;
    private int year, monthOfYear, dayOfMonth;
    private int hourOfDay, minute;

    public DateTimePickerDialogListener(Activity activity) {
        this.activity = activity;
    }

    public void show(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(activity.getFragmentManager(), "DatePickerDialog");
    }

    public abstract void onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

    @Override public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year = year;

        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
            this,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        );
        dpd.show(activity.getFragmentManager(), "ShowPickerDialog");
    }

    @Override public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        onDateTimeSet(year, monthOfYear, dayOfMonth, hourOfDay, minute);
    }
}
