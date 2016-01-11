package com.onemorebit.supersimpletodo.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class SharePrefUtil {

    public static List<Item> get(int tabNumber) {
        ArrayList<Item> todoItems;

        try {
            Gson gson = new Gson();
            String key = getKeySharePrefByTabNumber(tabNumber);
            String json = Contextor.getInstance()
                .getSharedPreferences()
                .getString(key, "");
            Type listType = new TypeToken<List<Item>>() {}.getType();
            todoItems = gson.fromJson(json, listType);

            if (todoItems == null) {
                todoItems = new ArrayList<>();
            }
        } catch (Exception e) {
            // TODO : Handle
            e.printStackTrace();
            todoItems = new ArrayList<>();
        }
        return todoItems;
    }

    public static void update(int tabNumber, ArrayList<Item> item) {
        Gson gson = new Gson();
        try {
            String json = gson.toJson(item);
            String key = getKeySharePrefByTabNumber(tabNumber);
                Contextor.getInstance()
                .getSharedPreferences()
                .edit()
                .putString(key, json)
                .apply();
        } catch (Exception e) {
            // TODO: Handle
            e.printStackTrace();
        }
    }

    public static String getKeySharePrefByTabNumber(int tabNumber) {
        switch (tabNumber) {
            case BaseTodoFragment.ONE:
                return Contextor.getInstance().getContext().getString(R.string.share_pref_key_one_list);
            case BaseTodoFragment.TWO:
                return Contextor.getInstance().getContext().getString(R.string.share_pref_key_two_list);
            default:
                return "Unknown";
        }
    }
}
