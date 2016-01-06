package com.onemorebit.supersimpletodo.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.onemorebit.supersimpletodo.Utils.NotificationHelper;

/**
 * Created by Euro on 1/6/16 AD.
 */
public class NotificationReceiver extends BroadcastReceiver{
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_IC = "ic";
    public static final String ACTION_SET_NOTIFICATION = "SET_NOTI";

    @Override public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_SET_NOTIFICATION)){
            String title = intent.getStringExtra(EXTRA_TITLE);
            String content = intent.getStringExtra(EXTRA_CONTENT);
            int ic = intent.getIntExtra(EXTRA_IC, 0);
            NotificationHelper.createNotification(title, content, ic);
        }
    }
}
