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
import java.util.HashMap;

public class OneTodoFragment extends BaseTodoFragment {

    public OneTodoFragment() {
        // Required empty public constructor
    }

    public static OneTodoFragment newInstance() {
        OneTodoFragment fragment = new OneTodoFragment();
        return fragment;
    }



    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_base_todo, container, false);
        binding.setCheckedCount(checkedCount);
        binding.setTabNumber(ONE);
        tabNumber = ONE;
        initEditTextAttr();
        initData(ONE);
        initRecyclerAdapter(ONE);
        initListener(ONE);
        return binding.getRoot();
    }
}
