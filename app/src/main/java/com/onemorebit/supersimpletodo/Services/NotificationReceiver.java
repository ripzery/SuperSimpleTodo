package com.onemorebit.supersimpletodo.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.onemorebit.supersimpletodo.Utils.Contextor;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.Utils.NotificationHelper;
import com.onemorebit.supersimpletodo.Utils.TimeHelper;
import java.util.Calendar;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class NotificationReceiver extends BroadcastReceiver {
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_IC = "ic";
    public static final String ACTION_SET_NOTIFICATION = "SET_NOTI";

    @Override public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SET_NOTIFICATION)) {

            String title = intent.getStringExtra(EXTRA_TITLE);
            String content = intent.getStringExtra(EXTRA_CONTENT);
            Logger.i(NotificationReceiver.class, "onReceive_20: " + content);
            int ic = intent.getIntExtra(EXTRA_IC, 0);
            NotificationHelper.createNotification(title, content, ic);
        }
    }

    public static void broadcastNotificationIntent(int tabNumber, String title, String description, int icon, Calendar calendar) {
        AlarmManager alarmMgr = (AlarmManager) Contextor.getInstance().getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Contextor.getInstance().getContext(), NotificationReceiver.class);
        intent.setAction(NotificationReceiver.ACTION_SET_NOTIFICATION);
        intent.putExtra(NotificationReceiver.EXTRA_TITLE, title);
        intent.putExtra(NotificationReceiver.EXTRA_CONTENT, description);
        intent.putExtra(NotificationReceiver.EXTRA_IC, icon);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Contextor.getInstance().getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, TimeHelper.getTimeSpecificTime(calendar), alarmIntent);
    }
}
