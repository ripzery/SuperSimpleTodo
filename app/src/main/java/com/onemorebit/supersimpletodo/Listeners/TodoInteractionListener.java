package com.onemorebit.supersimpletodo.Listeners;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.onemorebit.supersimpletodo.Models.Item;

/**
 * Created by Euro on 1/2/16 AD.
 */
public interface TodoInteractionListener {
    void onCheckedChangeListener(boolean isChecked, EditText etChecked);
    void onAddNewItem(Item item);
}
