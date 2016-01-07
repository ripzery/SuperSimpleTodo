package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;
import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.Adapters.RecyclerAdapter;
import com.onemorebit.supersimpletodo.Listeners.DateTimePickerDialogListener;
import com.onemorebit.supersimpletodo.MainActivity;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.Models.OttoCheckedCount;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Services.NotificationService;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.Command;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.databinding.TodoBinding;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

    // items = todoItems
    protected void onUndoItem(ArrayList<Item> items){

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

    protected void addCommand(String description, final int tabNumber) {
        /* Check if description is empty or not */
        if (description.isEmpty()) {
            //Snackbar.make(binding.coordinateLayout, R.string.snack_bar_et_warn_empty, Snackbar.LENGTH_LONG).show();
            Toast.makeText(getContext(), R.string.snack_bar_et_warn_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        /* Check for command option */
        //final String timeOptionData = Command.process(description);
        //
        //if (!timeOptionData.isEmpty()) {
        //    Snackbar.make(binding.coordinateLayout, "Set reminder at " + timeOptionData + " !", Snackbar.LENGTH_LONG).show();
        //    description = Command.trimOptions(description);
        //
        //    int[] times = TimeHelper.getTimeInt(timeOptionData);
        //    PagerAdapter pagerAdapter = ((MainActivity) getActivity()).adapter;
        //
        //    NotificationReceiver.broadcastNotificationIntent(tabNumber,
        //        pagerAdapter.getPageTitle(tabNumber).toString(),
        //        description,
        //        pagerAdapter.getTabIcon(tabNumber),
        //        times);
        //
        //    description = description + "\n <b> @" + timeOptionData + "</b>";
        //}

        PagerAdapter pagerAdapter = ((MainActivity) getActivity()).adapter;


        /* Check for command option */
        String option = Command.process(description);
        action(option, description, tabNumber, pagerAdapter);
    }

    private void addItem(String description, int tabNumber, long notificationId) {
        Item item = new Item(false, description, notificationId);
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

    protected void action(final String option, final String description, final int tabNumber, final PagerAdapter pagerAdapter) {
        switch (option) {
            case Command.OPTION_REMIND:
                DateTimePickerDialogListener dateTimePickerDialogListener = new DateTimePickerDialogListener(getActivity(), tabNumber) {
                    @Override public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
                        /* Trim out command option */
                        String trimDescription = Command.trimOptions(description);

                        /* Formatted dateTime */
                        final String dateTime = dayOfMonth + "/" + monthOfYear + 1 + "/" + year + ", " + hourOfDay + ":" + minute;
                        String htmlDescription = trimDescription + "<b> @<u>" + dateTime + "</u></b>";

                        /* Set calendar value */
                        final Calendar tempCal = new GregorianCalendar();
                        final long notificationId = System.currentTimeMillis();
                        tempCal.setTimeInMillis(notificationId);

                        Calendar calendar = new GregorianCalendar();
                        calendar.set(Calendar.DAY_OF_YEAR, tempCal.get(Calendar.DAY_OF_YEAR));
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        calendar.set(Calendar.DATE, tempCal.get(Calendar.DATE));

                        /* Broadcast Notification at specific time*/
                        NotificationService.broadcastNotificationIntent(pagerAdapter.getPageTitle(tabNumber).toString(),
                            trimDescription,
                            pagerAdapter.getTabIcon(tabNumber), calendar, notificationId, tabNumber);

                        /* Show snackbar to tell user */
                        Snackbar.make(binding.coordinateLayout, "Set reminder at " + dateTime + " !", Snackbar.LENGTH_LONG).show();

                        /* Add Item to list*/
                        addItem(htmlDescription, tabNumber, notificationId);
                    }
                };

                /* Show date time picker*/
                dateTimePickerDialogListener.show();
                break;
            default:
                addItem(description, tabNumber, 0);
        }
    }

    protected void initEditTextAttr() {
        binding.layoutEnterNewItem.etEnterDesc.setMaxLines(3);
        binding.layoutEnterNewItem.etEnterDesc.setHorizontallyScrolling(false);
    }
}
