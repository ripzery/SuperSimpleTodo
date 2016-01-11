package com.onemorebit.supersimpletodo.Services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.Utils.TabHelper;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    public static final int TOTAL_TAB = 2;
    public static final String ACTION_BOOT_COMPLETE = "android.intent.action.BOOT_COMPLETED";

    @Override public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_BOOT_COMPLETE)) {
            Logger.i(BootBroadcastReceiver.class, "onReceive In broadcast receiver " + intent.toString());

            /* Restart alarm after reboot */
            restartAlarmReminder();
        } else if (intent.getType().equals(NotificationService.ACTION_SET_NOTIFICATION) || intent.getType()
            .equals(NotificationService.ACTION_CANCEL_NOTIFICATION)) {
            Logger.i(BootBroadcastReceiver.class, NotificationService.EXTRA_NOTI_ID + ": " + intent.getIntExtra(NotificationService.EXTRA_NOTI_ID, 0));
            Intent notificationService = new Intent(context, NotificationService.class);

            /* put extra item needed*/
            notificationService.putExtra(NotificationService.EXTRA_TITLE, intent.getStringExtra(NotificationService.EXTRA_TITLE));
            notificationService.putExtra(NotificationService.EXTRA_CONTENT, intent.getStringExtra(NotificationService.EXTRA_CONTENT));
            notificationService.putExtra(NotificationService.EXTRA_IC, intent.getIntExtra(NotificationService.EXTRA_IC, 0));
            notificationService.putExtra(NotificationService.EXTRA_NOTI_ID, intent.getIntExtra(NotificationService.EXTRA_NOTI_ID, 0));
            notificationService.putExtra(NotificationService.EXTRA_TAB_NUMBER, intent.getIntExtra(NotificationService.EXTRA_TAB_NUMBER, 0));

            notificationService.setAction(intent.getAction());
            notificationService.setType(intent.getType());
            context.startService(notificationService);
        } else {
            Logger.i(BootBroadcastReceiver.class, "Not Set Noti or cancel noti");
        }
    }

    private void restartAlarmReminder() {
        /* loop total tab */
        for (int i = 0; i < TabHelper.TOTAL_TAB; i++) {
            /* get list items from share preference */
            ArrayList<Item> listItems = new ArrayList<>(SharePrefUtil.get(i));

            /* loop total item in each tab */
            for (int j = 0; j < listItems.size(); j++) {

                /* if item has reminder */
                if (listItems.get(j).getNotificationId() != 0) {

                    /* get date from string */
                    final Date date = listItems.get(j).getDateFromString();

                    /* get trimmed html and time */
                    String trimDescription = listItems.get(j).getTrimHtmlTime();

                    /* send notification if notification date is not come yet */
                    if (listItems.get(j).isShouldNotify()) {
                        NotificationService.broadcastNotificationIntent(TabHelper.getNameTab(i), trimDescription, TabHelper.imageResId[i],
                            date != null ? date.getTime() : 0, listItems.get(j).getNotificationId(), i);
                    }
                }
            }
        }
    }
}
