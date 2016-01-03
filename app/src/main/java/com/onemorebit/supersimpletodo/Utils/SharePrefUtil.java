package com.onemorebit.supersimpletodo.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class SharePrefUtil {
    public static List<Item> get(boolean isTodo) {
        ArrayList<Item> todoItems;

        try {
            Gson gson = new Gson();
            String json = Contextor.getInstance()
                .getSharedPreferences()
                .getString(Contextor.getInstance().getContext().getString(isTodo ? R.string.share_pref_key_todo_list : R.string.share_pref_key_done_list), "");
            Type listType = new TypeToken<List<Item>>() {
            }.getType();
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

    public static void update(boolean isTodo, ArrayList<Item> item) {
        Gson gson = new Gson();
        try {
            String json = gson.toJson(item);
            Contextor.getInstance()
                .getSharedPreferences()
                .edit()
                .putString(Contextor.getInstance().getContext().getString(isTodo ? R.string.share_pref_key_todo_list : R.string.share_pref_key_done_list), json)
                .apply();
        } catch (Exception e) {
            // TODO: Handle
            e.printStackTrace();
        }
    }
}
