package com.onemorebit.supersimpletodo.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class Contextor {
    public static Contextor contextor;
    private Context context;
    private SharedPreferences sharedPreferences;

    public static Contextor getInstance(){
        if (contextor == null) {
            contextor = new Contextor();
        }

        return contextor;
    }

    public void init(Context context){
        this.context = context;
    }

    public Context getContext(){
        if(context == null) throw new RuntimeException("Context is null");
        return context;
    }

    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            sharedPreferences = getContext().getSharedPreferences("SuperSimpleTodo", Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }
}
