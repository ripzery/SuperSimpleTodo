package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import com.onemorebit.supersimpletodo.Adapters.RecyclerAdapter;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.Models.OttoCheckedCount;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.databinding.TodoBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * Created by Euro on 1/3/16 AD.
 */
public class BaseTodoFragment extends Fragment {
    public static final int ONE = 0;
    public static final int TWO = 1;
    protected int tabNumber;
    protected TodoBinding binding;
    protected RecyclerAdapter adapter;
    protected ArrayList<Item> todoItems;
    protected ObservableInt checkedCount = new ObservableInt(0);

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }

    protected void updateCheckedCount(ArrayList<Item> todoItems) {
        int count = 0;
        for (Item item : todoItems) {
            if (item.isChecked()) count++;
        }
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size() - count, tabNumber));
        checkedCount.set(count);
    }

    protected HashMap<Integer, Item> removeItemChecked() {
        HashMap<Integer, Item> removedItem = new HashMap<>();
        for (int i = todoItems.size() - 1; i >= 0; i--) {
            if (todoItems.get(i).isChecked()) {
                removedItem.put(i, todoItems.get(i));
                adapter.removeItem(i);
            }
        }

        checkedCount.set(0);
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size(), tabNumber));
        return removedItem;
    }

    protected void initRecyclerAdapter(int tabNumber) {
        /* tell recyclerview that every item has a fix size for better performance */
        binding.recyclerView.setHasFixedSize(true);

        /* set simple vertical linear layout */
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /* init current items in share preference*/
        adapter = new RecyclerAdapter(todoItems, tabNumber);

        /* update to do count in tab */
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size(), tabNumber));

        /* set adapter and item animator*/
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setItemAnimator(new LandingAnimator());
    }

    protected void addItem(String description, int tabNumber) {


        /* Check if description is empty or not */
        if (description.isEmpty()) {
            Snackbar.make(binding.coordinateLayout, R.string.snack_bar_et_warn_empty, Snackbar.LENGTH_LONG).show();
            return;
        }

        /* Split to add multiple to do item at once*/
        final String[] split = description.split(getString(R.string.regex_todo_et_split));
        if (split.length > 1) {
            for (String s : split) {
                Item item = new Item(false, s);
                adapter.addItem(item);
            }
        } else {
            Item item = new Item(false, description);
            adapter.addItem(item);
        }

        /* reset string in input field */
        binding.layoutEnterNewItem.etEnterDesc.setText("");

        /* scroll to last item when finish add item*/
        binding.recyclerView.smoothScrollToPosition(todoItems.size());

        /* update count by send data to main activity*/
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size(), tabNumber));

        /* update share preference*/
        SharePrefUtil.update(tabNumber, todoItems);
    }

    protected void initEditTextAttr(){
        binding.layoutEnterNewItem.etEnterDesc.setMaxLines(3);
        binding.layoutEnterNewItem.etEnterDesc.setHorizontallyScrolling(false);
    }

}
