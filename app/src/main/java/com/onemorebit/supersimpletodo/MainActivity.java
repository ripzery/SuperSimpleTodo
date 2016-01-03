package com.onemorebit.supersimpletodo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.Models.Item;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.databinding.MainBinder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MainBinder mainBinder;
    private ArrayList<Item> todoItems;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init data binding
        initDataBinding();

        initInstance();
    }

    @Override protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override protected void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();

    }

    private void initInstance() {
        mainBinder.pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), this));
        mainBinder.layoutTab.tabLayout.setupWithViewPager(mainBinder.pager);
        mainBinder.pager.setCurrentItem(0);
    }


    private void initDataBinding() {
        mainBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //setSupportActionBar(mainBinder.layoutToolbar.toolbar);
        //mainBinder.layoutToolbar.toolbar.setTitle(getString(R.string.app_name));
    }
}
