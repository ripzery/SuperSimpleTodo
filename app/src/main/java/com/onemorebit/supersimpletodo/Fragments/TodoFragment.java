package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.onemorebit.supersimpletodo.Adapters.RecyclerAdapter;
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.databinding.TodoBinding;
import java.util.ArrayList;

public class TodoFragment extends Fragment {
    private TodoBinding todoBinder;
    private RecyclerAdapter adapter;
    private ArrayList<Item> todoItems;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance() {
        TodoFragment fragment = new TodoFragment();
        return fragment;
    }

    private void initData() {
        todoItems = new ArrayList<>();
        todoItems = (ArrayList<Item>) SharePrefUtil.get();
    }

    private void initListener() {

        adapter.setTodoInteractionListener(new TodoInteractionListener() {
            @Override public void onCheckedChangeListener(boolean isChecked, EditText etChecked) {
                if (isChecked) {
                    etChecked.setPaintFlags(etChecked.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    //adapter.removeItem(todoItems.indexOf(item));
                    //SharePrefUtil.update(todoItems);
                }else{
                    etChecked.setPaintFlags(etChecked.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }

            @Override public void onAddNewItem(Item item) {
                addItem(item.getDescription());
            }
        });

        todoBinder.layoutEnterNewItem.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                addItem(todoBinder.layoutEnterNewItem.etEnterDesc.getText().toString());
            }
        });

        todoBinder.layoutEnterNewItem.etEnterDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addItem(v.getText().toString());
                }
                return true;
            }
        });
    }

    private void initRecyclerAdapter() {
        todoBinder.recyclerView.setHasFixedSize(true);
        todoBinder.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(todoItems);
        todoBinder.recyclerView.setAdapter(adapter);
    }

    public void addItem(String description) {

        if (description.isEmpty()) {
            Snackbar.make(todoBinder.getRoot(), R.string.snack_bar_et_warn_empty, Snackbar.LENGTH_LONG).show();
            return;
        }

        final String[] split = description.split("  ");
        if (split.length > 1) {
            for (String s : split) {
                Item item = new Item(false, s);
                adapter.addItem(item);
            }
        } else {
            Item item = new Item(false, description);
            adapter.addItem(item);
        }
        todoBinder.layoutEnterNewItem.etEnterDesc.setText("");
        todoBinder.recyclerView.smoothScrollToPosition(todoItems.size());
        SharePrefUtil.update(todoItems);
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        todoBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_todo, container, false);
        initData();
        initRecyclerAdapter();
        initListener();
        return todoBinder.getRoot();
    }

    @Override public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override public void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }
}
