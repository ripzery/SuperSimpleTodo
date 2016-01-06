package com.onemorebit.supersimpletodo.Utils;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Euro on 1/5/16 AD.
 */
public class Command {
    public static final String OPTION_REMIND = ".remind";

    public static String process(String input) {
        //String[] regexReminder = new String[] { "-t ", ".remind"};

        if(input.contains(OPTION_REMIND)){
            return OPTION_REMIND;
        }
        /* Checked for regex reminder */
        //for (String regex : regexReminder) {
        //    /* checked if input contain regex */
        //    if (input.contains(regex)) {
        //        //return checkTime(input);
        //    }
        //
        //    if(input.contains(regex))
        //}

        return "";
    }

    private static String checkTime(String input) {
        String timeRegex = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

        final int startIndexTime = input.lastIndexOf("-t ") + 3;
        String time = input.substring(startIndexTime, startIndexTime + 5);

        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(time);
        if (matcher.matches()) {
            Logger.i(Command.class, "Match : " + time);
            return time;
        } else {
            Logger.i(Command.class, "Not Match! ");
            return "";
        }
    }

    public static String trimOptions(String input){
        final int startIndexTime = input.lastIndexOf(OPTION_REMIND);
        return input.substring(0, startIndexTime);
    }
}
