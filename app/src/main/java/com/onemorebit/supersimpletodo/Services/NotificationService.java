package com.onemorebit.supersimpletodo.Services;

import android.app.AlarmManager;
import android.app.IntentService;
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
public class NotificationService extends IntentService {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_IC = "ic";
    public static final String EXTRA_NOTI_ID = "notification_id";
    public static final String EXTRA_TAB_NUMBER = "tabNumber";
    public static final String ACTION_SET_NOTIFICATION = "SET_NOTI";
    public static final String ACTION_CANCEL_NOTIFICATION = "CANCEL_NOTI";

    public NotificationService(String name) {
        super(name);
    }

    public NotificationService(){
        super("NotificationService");
    }

    @Override protected void onHandleIntent(Intent intent) {

        switch (intent.getType()){
            case ACTION_SET_NOTIFICATION:
                String title = intent.getStringExtra(EXTRA_TITLE);
                String content = intent.getStringExtra(EXTRA_CONTENT);
                int ic = intent.getIntExtra(EXTRA_IC, 0);
                int notificationId = intent.getIntExtra(EXTRA_NOTI_ID, 0);
                int tabNumber = intent.getIntExtra(EXTRA_TAB_NUMBER, 0);
                NotificationHelper.createNotification(title, content, ic, notificationId, tabNumber);
                Logger.i(NotificationService.class, "NotificationId: " + notificationId);
                break;
            case ACTION_CANCEL_NOTIFICATION:

                break;
            default:

        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    public static void broadcastNotificationIntent(String title, String description, int icon, long awakeTime, long notificationId, int tabNumber) {
        AlarmManager alarmMgr = (AlarmManager) Contextor.getInstance().getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Contextor.getInstance().getContext(), BootBroadcastReceiver.class);
        intent.setType(NotificationService.ACTION_SET_NOTIFICATION);
        intent.setAction(notificationId+"");
        intent.putExtra(NotificationService.EXTRA_TITLE, title);
        intent.putExtra(NotificationService.EXTRA_CONTENT, description);
        intent.putExtra(NotificationService.EXTRA_IC, icon);
        intent.putExtra(NotificationService.EXTRA_TAB_NUMBER, tabNumber);
        intent.putExtra(NotificationService.EXTRA_NOTI_ID, (int)notificationId);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Contextor.getInstance().getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, awakeTime, alarmIntent);
        Logger.i(NotificationService.class, "broadcastNotificationIntent_66: " + title + "/" + description);
    }
}
