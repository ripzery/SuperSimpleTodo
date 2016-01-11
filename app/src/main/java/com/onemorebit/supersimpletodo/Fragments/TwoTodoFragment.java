package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import java.util.HashMap;

public class TwoTodoFragment extends BaseTodoFragment {

    public TwoTodoFragment() {
        // Required empty public constructor
    }

    public static TwoTodoFragment newInstance() {
        TwoTodoFragment fragment = new TwoTodoFragment();
        return fragment;
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
        initData(TWO);
        initRecyclerAdapter(TWO);
        initListener(TWO);
        return binding.getRoot();
    }
}
