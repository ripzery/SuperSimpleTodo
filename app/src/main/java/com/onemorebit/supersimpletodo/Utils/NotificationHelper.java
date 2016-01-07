package com.onemorebit.supersimpletodo.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import com.onemorebit.supersimpletodo.MainActivity;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/6/16 AD.
 */

public class NotificationHelper {
    public static void createNotification(String title, String msg, int icon, int notificationId, int tabNumber){
        Intent resultIntent = new Intent(Contextor.getInstance().getContext(), MainActivity.class);

        PendingIntent resultPendingIntent =
            PendingIntent.getActivity(
                Contextor.getInstance().getContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_ONE_SHOT
            );

        Logger.i(NotificationHelper.class, "createNotification_30: ");

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(Contextor.getInstance().getContext())
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setSound(alarmSound)
                .setColor(ContextCompat.getColor(Contextor.getInstance().getContext(), TodoStateColorBinding.getColorView(tabNumber)))
                .setAutoCancel(true)
                .setVibrate(new long[]{1000})
                .setContentText(msg);


        NotificationManager notificationManager = (NotificationManager) Contextor.getInstance().getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, mBuilder.build());

    }
}
