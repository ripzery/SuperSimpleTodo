package com.onemorebit.supersimpletodo.Utils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.onemorebit.supersimpletodo.Services.BootBroadcastReceiver;
import com.onemorebit.supersimpletodo.Services.NotificationService;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class AlarmManagerUtil {
    public static void cancelReminder(int notificationId){
        AlarmManager alarmManager = (AlarmManager) Contextor.getInstance().getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Contextor.getInstance().getContext(), BootBroadcastReceiver.class);
        intent.setType(NotificationService.ACTION_SET_NOTIFICATION);
        intent.setAction(notificationId+"");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Contextor.getInstance().getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(alarmIntent);

    }
}
