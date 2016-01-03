package com.onemorebit.supersimpletodo.Utils;

import android.util.Log;

/**
 * Created by Euro on 12/30/15 AD.
 */
public class Logger {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFORMATION = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    private static final boolean ENABLED = false;

    public static void e(Class x, String msg) {
        log(ERROR, x, msg);
    }

    public static void d(Class x, String msg) {
        log(DEBUG, x, msg);
    }

    public static void i(Class x, String msg) {
        log(INFORMATION, x, msg);
    }

    public static void w(Class x, String msg) {
        log(WARN, x, msg);
    }

    public static void v(Class x, String msg) {
        log(VERBOSE, x, msg);
    }

    public static void wtf(Class x, String msg) {
        log(6, x, msg);
    }


    private static void log(int type, Class x, String msg) {
        if(ENABLED) {
            switch (type) {
                case VERBOSE:
                    Log.v(x.getSimpleName(), msg);
                    break;
                case DEBUG:
                    Log.d(x.getSimpleName(), msg);
                    break;
                case INFORMATION:
                    Log.i(x.getSimpleName(), msg);
                    break;
                case WARN:
                    Log.w(x.getSimpleName(), msg);
                    break;
                case ERROR:
                    Log.e(x.getSimpleName(), msg);
                    break;
                default:
                    Log.wtf(x.getSimpleName(), msg);
            }
        }
    }
}
