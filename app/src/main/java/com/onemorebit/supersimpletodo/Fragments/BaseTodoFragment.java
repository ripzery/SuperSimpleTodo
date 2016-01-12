package com.onemorebit.supersimpletodo.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.Adapters.RecyclerAdapter;
import com.onemorebit.supersimpletodo.Listeners.DateTimePickerDialogListener;
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.MainActivity;
import com.onemorebit.supersimpletodo.Models.DateTime;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.Models.OttoCheckedCount;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Services.NotificationService;
import com.onemorebit.supersimpletodo.Utils.AlarmManagerUtil;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.Command;
import com.onemorebit.supersimpletodo.Utils.DialogBuilder;
import com.onemorebit.supersimpletodo.Utils.Logger;
import com.onemorebit.supersimpletodo.Utils.MyAutoCompleteTokenizer;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.databinding.LayoutDialogEditBinding;
import com.onemorebit.supersimpletodo.databinding.TodoBinding;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private PagerAdapter pagerAdapter;
    private MenuItem removeMenu;

    protected void action(final String option, final String description, final int tabNumber) {
        switch (option) {
            case Command.OPTION_REMIND:
                DateTimePickerDialogListener dateTimePickerDialogListener = new DateTimePickerDialogListener(getActivity(), tabNumber) {
                    @Override public void onDateTimeSet(DateTime dateTime) {
                        /* Trim out command option */
                        String trimDescription = Command.trimOptions(description);

                        /* Formatted dateTime */
                        final String dateTimeString = dateTime.getFormattedTime();

                        String htmlDescription = trimDescription + "<b> @<u>" + dateTimeString + "</u></b>";

                        /* Set calendar value */
                        Calendar calendar = dateTime.getCalendar();

                        /* Get notification id */
                        long notificationId = dateTime.getNotificationId();

                        /* Broadcast Notification at specific time*/
                        NotificationService.broadcastNotificationIntent(pagerAdapter.getPageTitle(tabNumber).toString(), trimDescription,
                            pagerAdapter.getTabIcon(tabNumber), calendar.getTimeInMillis(), notificationId, tabNumber);

                        /* Show snackbar to tell user */
                        Snackbar.make(binding.coordinateLayout, "Set reminder at " + dateTimeString + " !", Snackbar.LENGTH_LONG).show();

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

        /* get command option */
        String option = Command.process(description);

        /* send action base on command option */
        action(option, description, tabNumber);
    }

    protected void handleEmptyState() {
        binding.setIsEmpty(todoItems.isEmpty());
    }

    /* initialize to do items from share preference */
    protected void initData(int tabNumber) {
        todoItems = (ArrayList<Item>) SharePrefUtil.get(tabNumber);

        /* handle empty state */
        handleEmptyState();
    }

    protected void initEditTextAttr() {
        binding.layoutEnterNewItem.etEnterDesc.setMaxLines(3);
        binding.layoutEnterNewItem.etEnterDesc.setHorizontallyScrolling(false);

        /* add ability of auto complete */
        String[] COMMANDS = new String[]{"remind"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, COMMANDS);
        binding.layoutEnterNewItem.etEnterDesc.setAdapter(adapter);
        binding.layoutEnterNewItem.etEnterDesc.setThreshold(1); //Set number of characters before the dropdown should be shown

        //Create a new Tokenizer which will get text after '.' and terminate on ' '
        binding.layoutEnterNewItem.etEnterDesc.setTokenizer(new MyAutoCompleteTokenizer());
    }


    protected void initListener(final int tabNumber) {

        /* handle whenever checked event is happen */
        adapter.setTodoInteractionListener(new TodoInteractionListener() {
            @Override public void onCheckedChange(boolean isChecked, TextView tvChecked) {
                SharePrefUtil.update(tabNumber, todoItems);
                updateCheckedCount(todoItems);
                if (isChecked) {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }

            @Override public void onItemLongClick(int index, final Item item) {
                // show dialog or open new fragment?

                MaterialDialog editDialog = initEditDialog(item);

                editDialog.show();
            }
        });

        /* handle when delete item is clicked  */
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                final HashMap<Integer, Item> removedItem = removeItemChecked();
                Snackbar.make(binding.coordinateLayout, R.string.snack_remove_todo, Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo), new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            onUndoItem(removedItem);
                            SharePrefUtil.update(tabNumber, todoItems);
                        }
                    })
                    .show();
                SharePrefUtil.update(tabNumber, todoItems);
            }
        });

        /* handle when press added */
        binding.layoutEnterNewItem.ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                addCommand(binding.layoutEnterNewItem.etEnterDesc.getText().toString(), tabNumber);
            }
        });

        /* handle when press done in the keyboard*/
        binding.layoutEnterNewItem.etEnterDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addCommand(v.getText().toString(), tabNumber);
                }
                return true;
            }
        });
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

        /* set adapter and item animator */
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setItemAnimator(new LandingAnimator());
    }

    // items = todoItems
    protected void onUndoItem(HashMap<Integer, Item> removedItem) {
        /* get all keys in keyset */
        ArrayList<Integer> keys = new ArrayList<>(removedItem.keySet());

        /* sort all keys in ascending order */
        Collections.sort(keys);

        /* insert each item in adapter in ascending order */
        for (int i = 0; i < removedItem.size(); i++) {
            final Item item = removedItem.get(keys.get(i));
            adapter.insertItem(keys.get(i), item);

            /* reset reminder */
            /* Broadcast Notification at specific time*/
            if (item.getNotificationId() != 0 && item.isShouldNotify()) {
                NotificationService.broadcastNotificationIntent(pagerAdapter.getPageTitle(tabNumber).toString(), item.getTrimHtmlTime(),
                    pagerAdapter.getTabIcon(tabNumber), item.getDateFromString().getTime(), item.getNotificationId(), tabNumber);
            }
        }

        /* Handle empty state */
        handleEmptyState();

        /* set checked count = number item removed */
        //checkedCount.set(removedItem.size());
        updateCheckedCount(todoItems);
    }

    protected HashMap<Integer, Item> removeItemChecked() {
        /* backup removed items for undo purpose */
        HashMap<Integer, Item> removedItem = new HashMap<>();

        /* loop check which items is checked, so remove them */
        for (int i = todoItems.size() - 1; i >= 0; i--) {
            if (todoItems.get(i).isChecked()) {

                /* cancel reminder if item check has set */
                if (todoItems.get(i).getNotificationId() != 0) {
                    AlarmManagerUtil.cancelReminder(todoItems.get(i).getNotificationId());
                }

                removedItem.put(i, todoItems.get(i));
                adapter.removeItem(i);
            }
        }

        /* after remove all checked item then checkedCount must be zero */
        updateCheckedCount(todoItems);

        /* decide to show empty state */
        handleEmptyState();

        /* update to do count in tab */
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size(), tabNumber));
        return removedItem;
    }

    protected void updateCheckedCount(ArrayList<Item> todoItems) {
        int count = 0;
        for (Item item : todoItems) {
            if (item.isChecked()) count++;
        }
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size() - count, tabNumber));
        checkedCount.set(count);
        updateRemoveMenuView(count);
    }

    private void addItem(String description, int tabNumber, long notificationId) {
        Item item = new Item(false, description, notificationId);
        adapter.addItem(item);

        /* reset string in input field */
        binding.layoutEnterNewItem.etEnterDesc.setText("");

        /* handle empty state */
        handleEmptyState();

        /* scroll to last item when finish add item*/
        binding.recyclerView.smoothScrollToPosition(todoItems.size());

        /* update count by send data to main activity*/
        BusProvider.getInstance().post(new OttoCheckedCount(todoItems.size(), tabNumber));

        /* update share preference*/
        SharePrefUtil.update(tabNumber, todoItems);
    }

    private MaterialDialog initEditDialog(final Item item) {

        /* Initialize new dateTime object */
        final DateTime newDateTime = new DateTime(0, 0, 0, 0, 0);

        /* Inflate edit dialog layout */
        final LayoutDialogEditBinding inflate = DataBindingUtil.inflate(getLayoutInflater(null), R.layout.layout_dialog_edit, null, false);

        /* Initialize EditText */
        inflate.etEnterDesc.setHorizontallyScrolling(false);
        inflate.etEnterDesc.setMaxLines(3);

        /* Databinding : setIsNotify to set visibility of ivClose and alpha */
        inflate.setIsNotify(item.getNotificationId() != 0);

        /* Databinding : setItem to set description and time text*/
        inflate.setItem(item);


        /* set listener when click textview time */
        inflate.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DateTimePickerDialogListener dateTimePickerDialogListener = new DateTimePickerDialogListener(getActivity(), tabNumber) {
                    @Override public void onDateTimeSet(DateTime dateTime) {
                        newDateTime.setDateTime(dateTime);

                        /* Formatted dateTime */
                        final String dateTimeString = dateTime.getFormattedTime();

                        /* Set tvTime to new selected dateTime */
                        inflate.tvTime.setText(dateTimeString);

                        inflate.setIsNotify(dateTime.getNotificationId() > 0);
                    }
                };

                /* Show date time picker*/
                dateTimePickerDialogListener.show();
            }
        });

        inflate.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                /* Reset notification id */
                inflate.setItem(new Item(item.isChecked(), item.getTrimHtmlTime(), 0));
            }
        });

        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        final DialogInterface.OnShowListener showListener = new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialog) {

                /* set caret to last position of edittext enter */
                inflate.etEnterDesc.setSelection(inflate.etEnterDesc.getText().length());

                /* show keyboard */
                imm.showSoftInput(inflate.etEnterDesc, InputMethodManager.SHOW_IMPLICIT);
            }
        };

        final MaterialDialog.SingleButtonCallback cancelCallback = new MaterialDialog.SingleButtonCallback() {
            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                /* hide keyboard */
                imm.hideSoftInputFromWindow(inflate.etEnterDesc.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        };

        final MaterialDialog.SingleButtonCallback submitCallback = new MaterialDialog.SingleButtonCallback() {
            @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                if (inflate.tvTime.getText().toString().equals("Not set")) {

                    /* cancel notification */
                    AlarmManagerUtil.cancelReminder(item.getNotificationId());

                    /* set new description */
                    item.setDescription(inflate.etEnterDesc.getText().toString());

                    /* reset notification */
                    item.setNotificationId(0);

                } else if (item.getDateString().equals(inflate.tvTime.getText().toString())) {
                    /* if date time is not changed */

                    final String description = inflate.etEnterDesc.getText().toString();

                    /* set html description*/
                    String htmlDescription = description + " <b> @<u>" + inflate.tvTime.getText().toString() + "</u></b>";

                    /* update description */
                    item.setDescription(htmlDescription);
                } else {
                    /* if date time has been edited */

                    final String description = inflate.etEnterDesc.getText().toString();

                    /* set html description*/
                    String htmlDescription = description + " <b> @<u>" + newDateTime.getFormattedTime() + "</u></b>";

                    /* Set calendar value */
                    Calendar calendar = newDateTime.getCalendar();

                    /* Broadcast Notification at specific time */
                    NotificationService.broadcastNotificationIntent(pagerAdapter.getPageTitle(tabNumber).toString(), description,
                        pagerAdapter.getTabIcon(tabNumber), calendar.getTimeInMillis(), newDateTime.getNotificationId(), tabNumber);

                    /* Show snackbar to tell user */
                    Snackbar.make(binding.coordinateLayout, "Set reminder at " + newDateTime.getFormattedTime() + " !", Snackbar.LENGTH_LONG).show();

                    /* remove old notification Id */
                    AlarmManagerUtil.cancelReminder(item.getNotificationId());

                    /* set new notification Id */
                    item.setNotificationId(newDateTime.getNotificationId());

                    /* set new html description*/
                    item.setDescription(htmlDescription);
                }

                SharePrefUtil.update(tabNumber, todoItems);
                imm.hideSoftInputFromWindow(inflate.etEnterDesc.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        };

        final MaterialDialog editDialog = DialogBuilder.createEditDialog(BaseTodoFragment.this.getActivity(), inflate.getRoot(), cancelCallback,
            submitCallback);

        editDialog.setOnShowListener(showListener);

        return editDialog;
    }

    private void updateRemoveMenuView(int count) {
        try {
            removeMenu.setVisible(count > 0);
        } catch (NullPointerException e) {
            Logger.i(BaseTodoFragment.class, "removeMenu has not been initialized yet");
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override public void onResume() {
        super.onResume();
        pagerAdapter = ((MainActivity) getActivity()).adapter;
    }

    @Override public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override public void onPrepareOptionsMenu(Menu menu) {
        removeMenu = menu.findItem(R.id.remove);

        removeMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                final HashMap<Integer, Item> removedItem = removeItemChecked();
                String snackMsg = removedItem.size() + (removedItem.size() > 1 ? " items" : " item") + " has been removed";
                Snackbar.make(binding.coordinateLayout, snackMsg, Snackbar.LENGTH_SHORT).setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        onUndoItem(removedItem);
                        SharePrefUtil.update(tabNumber, todoItems);
                    }
                }).show();
                SharePrefUtil.update(tabNumber, todoItems);
                return true;
            }
        });

        updateCheckedCount(todoItems);
    }
}
