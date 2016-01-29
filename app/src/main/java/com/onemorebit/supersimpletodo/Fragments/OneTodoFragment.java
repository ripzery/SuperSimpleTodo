package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.onemorebit.supersimpletodo.R;

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
