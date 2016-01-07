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
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import java.util.ArrayList;

public class TwoTodoFragment extends BaseTodoFragment {

    public TwoTodoFragment() {
        // Required empty public constructor
    }

    public static TwoTodoFragment newInstance() {
        TwoTodoFragment fragment = new TwoTodoFragment();
        return fragment;
    }

    /* initilized to do items from share preference */
    private void initData() {
        todoItems = (ArrayList<Item>) SharePrefUtil.get(TWO);
    }

    private void initListener() {

        adapter.setTodoInteractionListener(new TodoInteractionListener() {
            @Override public void onCheckedChangeListener(boolean isChecked, TextView tvChecked) {
                SharePrefUtil.update(TWO, todoItems);
                updateCheckedCount(todoItems);
                if (isChecked) {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Snackbar.make(binding.coordinateLayout, R.string.snack_remove_todo, Snackbar.LENGTH_SHORT)
                    .setAction(getString(R.string.undo), new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            onUndoItem(todoItems);
                            SharePrefUtil.update(TWO, todoItems);
                        }
                    })
                    .show();
                SharePrefUtil.update(TWO, todoItems);
            }
        });
        binding.layoutEnterNewItem.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                addCommand(binding.layoutEnterNewItem.etEnterDesc.getText().toString(), TWO);
            }
        });

        binding.layoutEnterNewItem.etEnterDesc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addCommand(v.getText().toString(), TWO);
                }
                return true;
            }
        });
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_todo, container, false);
        binding.setCheckedCount(checkedCount);
        binding.setTabNumber(TWO);
        tabNumber = TWO;
        initEditTextAttr();
        initData();
        initRecyclerAdapter(TWO);
        initListener();
        return binding.getRoot();
    }
}
