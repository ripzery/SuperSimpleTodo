package com.onemorebit.supersimpletodo.Utils;

import java.util.Calendar;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class TimeHelper {
    public static long getTimeDiffFromNow(Calendar calendar) {

        long currentTimestamp = Calendar.getInstance().getTimeInMillis();
        long diffTimestamp = calendar.getTimeInMillis() - currentTimestamp;
        long myDelay = (diffTimestamp < 0 ? 0 : diffTimestamp);

        return myDelay;
    }


    public static long getTimeSpecificTime(Calendar desiredCalendar) {

        Calendar calendar = Calendar.getInstance();
        long currentTimestamp = Calendar.getInstance().getTimeInMillis();
        calendar.set(desiredCalendar.get(Calendar.YEAR), desiredCalendar.get(Calendar.MONTH), desiredCalendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, desiredCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, desiredCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        long diffTimestamp = calendar.getTimeInMillis() - currentTimestamp;
        long myDelay = (diffTimestamp < 0 ? currentTimestamp : currentTimestamp + diffTimestamp);

        return myDelay;
    }

    public static int[] getTimeInt(String time) {
        String times[] = time.split(":");
        return new int[] { Integer.parseInt(times[0]), Integer.parseInt(times[1]) };
    }
}
