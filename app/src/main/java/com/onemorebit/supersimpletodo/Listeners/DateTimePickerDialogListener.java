package com.onemorebit.supersimpletodo.Listeners;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import com.onemorebit.supersimpletodo.Models.DateTime;
import com.onemorebit.supersimpletodo.Utils.TodoStateColorBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import java.util.Calendar;

/**
 * Created by Euro on 1/6/16 AD.
 */
public abstract class DateTimePickerDialogListener implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private final Activity activity;
    private final int dialogColor;
    private int year, monthOfYear, dayOfMonth;
    private int hourOfDay, minute;
    private int tabNumber;

    public DateTimePickerDialogListener(Activity activity, int tabNumber) {
        this.activity = activity;
        this.tabNumber = tabNumber;
        this.dialogColor = ContextCompat.getColor(activity, TodoStateColorBinding.getColorView(0));
    }

    public void show() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        dpd.setMinDate(Calendar.getInstance());
        dpd.setThemeDark(false);
        dpd.setAccentColor(dialogColor);
        dpd.show(activity.getFragmentManager(), "DatePickerDialog");
    }

    public abstract void onDateTimeSet(DateTime dateTime);

    @Override public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
        this.year = year;

        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        dpd.setMinTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), 0);
        dpd.setThemeDark(false);
        dpd.setAccentColor(dialogColor);
        dpd.show(activity.getFragmentManager(), "ShowPickerDialog");
    }

    @Override public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        onDateTimeSet(new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minute));
    }
}
