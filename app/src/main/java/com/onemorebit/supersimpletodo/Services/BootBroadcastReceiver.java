package com.onemorebit.supersimpletodo.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.onemorebit.supersimpletodo.Utils.Contextor;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.Utils.NotificationHelper;
import java.util.Calendar;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class BootBroadcastReceiver extends WakefulBroadcastReceiver {


    @Override public void onReceive(Context context, Intent intent) {

        if(intent.getType().equals(NotificationService.ACTION_SET_NOTIFICATION) || intent.getType().equals(NotificationService.ACTION_CANCEL_NOTIFICATION)){
            Logger.i(BootBroadcastReceiver.class, NotificationService.EXTRA_NOTI_ID + ": " + intent.getIntExtra(NotificationService.EXTRA_NOTI_ID, 0));
            Intent notificationService = new Intent(context, NotificationService.class);
            notificationService.putExtra(NotificationService.EXTRA_TITLE, intent.getStringExtra(NotificationService.EXTRA_TITLE));
            notificationService.putExtra(NotificationService.EXTRA_CONTENT, intent.getStringExtra(NotificationService.EXTRA_CONTENT));
            notificationService.putExtra(NotificationService.EXTRA_IC, intent.getIntExtra(NotificationService.EXTRA_IC, 0));
            notificationService.putExtra(NotificationService.EXTRA_NOTI_ID, intent.getIntExtra(NotificationService.EXTRA_NOTI_ID, 0));
            notificationService.putExtra(NotificationService.EXTRA_TAB_NUMBER, intent.getIntExtra(NotificationService.EXTRA_TAB_NUMBER, 0));
            notificationService.setAction(intent.getAction());
            notificationService.setType(intent.getType());
            context.startService(notificationService);
        }else{
            Logger.i(BootBroadcastReceiver.class, "Not Set Noti or cancel noti");
        }
    }


}
