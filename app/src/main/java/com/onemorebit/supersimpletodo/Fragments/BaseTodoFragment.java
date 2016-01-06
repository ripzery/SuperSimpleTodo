package com.onemorebit.supersimpletodo.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.Adapters.RecyclerAdapter;
import com.onemorebit.supersimpletodo.MainActivity;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.Models.OttoCheckedCount;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Services.NotificationReceiver;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.Command;
import com.onemorebit.supersimpletodo.Utils.NotificationHelper;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.Utils.TimeHelper;
import com.onemorebit.supersimpletodo.databinding.TodoBinding;
import java.util.ArrayList;
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
        /* backup removed items for undo purpose */
        HashMap<Integer, Item> removedItem = new HashMap<>();

        /* loop check which items is checked, so remove them */
        for (int i = todoItems.size() - 1; i >= 0; i--) {
            if (todoItems.get(i).isChecked()) {
                removedItem.put(i, todoItems.get(i));
                adapter.removeItem(i);
            }
        }

        /* after remove all checked item then checkedCount must be zero */
        checkedCount.set(0);

        /* update to do count in tab */
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

    protected void addItem(String description, final int tabNumber) {


        /* Check if description is empty or not */
        if (description.isEmpty()) {
            Snackbar.make(binding.coordinateLayout, R.string.snack_bar_et_warn_empty, Snackbar.LENGTH_LONG).show();
            return;
        }

        /* Check for command option */
        final String timeOptionData = Command.process(description);

        if (!timeOptionData.isEmpty()) {
            Snackbar.make(binding.coordinateLayout, "Set reminder at " + timeOptionData + " !", Snackbar.LENGTH_LONG).show();
            description = Command.trimOptions(description);

            int[] times = TimeHelper.getTimeInt(timeOptionData);
            PagerAdapter pagerAdapter = ((MainActivity) getActivity()).adapter;

            AlarmManager alarmMgr = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), NotificationReceiver.class);
            intent.setAction(NotificationReceiver.ACTION_SET_NOTIFICATION);
            intent.putExtra(NotificationReceiver.EXTRA_TITLE, pagerAdapter.getPageTitle(tabNumber).toString());
            intent.putExtra(NotificationReceiver.EXTRA_CONTENT, description);
            intent.putExtra(NotificationReceiver.EXTRA_IC, pagerAdapter.getTabIcon(tabNumber));
            PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + TimeHelper.getTimeDiffFromNow(times[0], times[1]), alarmIntent);

            description = description + "\n <b> @" + timeOptionData + "</b>";
        }

        Item item = new Item(false, description);
        adapter.addItem(item);

        /* reset string in input field */
        binding.layoutEnterNewItem.etEnterDesc.setText("");

        /* scroll to last item when finish add item*/
        binding.recyclerView.smoothScrollToPosition(todoItems.size());

        /* update count by send data to main activity*/
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size(), tabNumber));

        /* update share preference*/
        SharePrefUtil.update(tabNumber, todoItems);
    }

    protected void initEditTextAttr() {
        binding.layoutEnterNewItem.etEnterDesc.setMaxLines(3);
        binding.layoutEnterNewItem.etEnterDesc.setHorizontallyScrolling(false);
    }
}
