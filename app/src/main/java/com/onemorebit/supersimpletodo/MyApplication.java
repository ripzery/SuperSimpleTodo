package com.onemorebit.supersimpletodo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.onemorebit.supersimpletodo.Utils.Contextor;
import com.onemorebit.supersimpletodo.Utils.Logger;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class MyApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Logger.i(MyApplication.class, "onCreate_15: ");
        Contextor.getInstance().init(getApplicationContext());
    }
}
