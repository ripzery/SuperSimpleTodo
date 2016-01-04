package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class OneTodoFragment extends BaseTodoFragment {

    public OneTodoFragment() {
        // Required empty public constructor
    }

    public static OneTodoFragment newInstance() {
        OneTodoFragment fragment = new OneTodoFragment();
        return fragment;
    }

    private void initData() {
        todoItems = new ArrayList<>();
        todoItems = (ArrayList<Item>) SharePrefUtil.get(ONE);
    }

    private void initListener() {

        adapter.setTodoInteractionListener(new TodoInteractionListener() {
            @Override public void onCheckedChangeListener(boolean isChecked, TextView tvChecked) {
                SharePrefUtil.update(ONE, todoItems);
                updateCheckedCount(todoItems);
                if (isChecked) {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }

            @Override public void onAddNewItem(Item item) {
                addItem(item.getDescription(), ONE);
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final HashMap<Integer, Item> removedItem = removeItemChecked();
                Snackbar.make(binding.coordinateLayout, R.string.snack_remove_todo, Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo), new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            // Return Undo Item
                            ArrayList<Integer> keys = new ArrayList<>(removedItem.keySet());
                            Collections.sort(keys);
                            for(int i = 0; i < removedItem.size() ; i++){
                                adapter.insertItem(keys.get(i), removedItem.get(keys.get(i)));
                            }
                            checkedCount.set(removedItem.size());
                            SharePrefUtil.update(ONE, todoItems);
                        }
                    })
                    .show();
                SharePrefUtil.update(ONE, todoItems);
            }
        });

        binding.layoutEnterNewItem.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                addItem(binding.layoutEnterNewItem.etEnterDesc.getText().toString(), ONE);
            }
        });

        binding.layoutEnterNewItem.etEnterDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addItem(v.getText().toString(), ONE);
                }
                return true;
            }
        });
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_todo, container, false);
        binding.setCheckedCount(checkedCount);
        binding.setTabNumber(ONE);
        tabNumber = ONE;
        initEditTextAttr();
        initData();
        initRecyclerAdapter(ONE);
        initListener();
        return binding.getRoot();
    }
}
