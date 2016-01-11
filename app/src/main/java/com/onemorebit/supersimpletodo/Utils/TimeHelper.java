package com.onemorebit.supersimpletodo.Utils;

import android.text.Html;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class TimeHelper {
    public static String getFormattedTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute){
        return dayOfMonth + "/" + monthOfYear + 1 + "/" + year + ", " + hourOfDay + ":" + minute;
    }
}
