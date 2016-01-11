package com.onemorebit.supersimpletodo.Models;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Euro on 1/11/16 AD.
 */
public class DateTime {
    long notificationId;
    int year;
    int monthOfYear;
    int dayOfMonth;
    int hourOfDay;
    int minute;

    public DateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.notificationId = System.currentTimeMillis();
    }

    public void setDateTime(DateTime dateTime) {
        this.year = dateTime.year;
        this.monthOfYear = dateTime.monthOfYear;
        this.dayOfMonth = dateTime.dayOfMonth;
        this.hourOfDay = dateTime.hourOfDay;
        this.minute = dateTime.minute;
        this.notificationId = System.currentTimeMillis();
    }

    public long getNotificationId() {
        return notificationId;
    }

    public String getFormattedTime(){
        return dayOfMonth + "/" + monthOfYear + 1 + "/" + year + ", " + hourOfDay + ":" + minute;
    }

    public Calendar getCalendar(){
        /* Set calendar value */

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
