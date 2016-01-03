package com.onemorebit.supersimpletodo.Fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.onemorebit.supersimpletodo.Adapters.RecyclerAdapter;
import com.onemorebit.supersimpletodo.Listeners.TodoInteractionListener;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.SharePrefUtil;
import com.onemorebit.supersimpletodo.databinding.DoneBinding;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

public class DoneFragment extends Fragment {
    private ArrayList<Item> doneItems;
    private ObservableInt checkedCount = new ObservableInt(0);
    private DoneBinding doneBinder;
    private RecyclerAdapter adapter;

    public DoneFragment() {
        // Required empty public constructor
    }

    public static DoneFragment newInstance() {
        DoneFragment fragment = new DoneFragment();
        return fragment;
    }

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

    @Subscribe public void onMovedItem(ArrayList<Item> items) {
        doneItems.addAll(items);
        adapter.setListItems(doneItems);
        checkedCount.set(doneItems.size());
    }

    private void initData() {
        doneItems = new ArrayList<>();
        doneItems = (ArrayList<Item>) SharePrefUtil.get(false);
    }

    private void initListener() {

        adapter.setTodoInteractionListener(new TodoInteractionListener() {
            @Override public void onCheckedChangeListener(boolean isChecked, TextView tvChecked) {
                SharePrefUtil.update(false, doneItems);
                updateCheckedCount();
                if (isChecked) {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    tvChecked.setPaintFlags(tvChecked.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }

            @Override public void onAddNewItem(Item item) {
                // Nothing here
            }
        });

        doneBinder.btnMove.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                removeItemChecked();
                SharePrefUtil.update(false, doneItems);
            }
        });
    }

    private void removeItemChecked() {
        for (int i = doneItems.size() - 1; i >= 0; i--) {
            if (doneItems.get(i).isChecked()) {
                adapter.removeItem(i);
            }
        }
        checkedCount.set(0);
    }

    private void initRecyclerAdapter() {
        doneBinder.recyclerView.setHasFixedSize(true);
        doneBinder.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerAdapter(doneItems);
        doneBinder.recyclerView.setAdapter(adapter);
        doneBinder.recyclerView.setItemAnimator(new SlideInRightAnimator());
    }

    private void updateCheckedCount() {
        int count = 0;
        for (Item item : doneItems) {
            if (item.isChecked()) count++;
        }
        checkedCount.set(count);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        doneBinder = DataBindingUtil.inflate(inflater, R.layout.fragment_done, container, false);
        doneBinder.setCheckedCount(checkedCount);
        initData();
        initRecyclerAdapter();
        initListener();
        return doneBinder.getRoot();
    }
}
