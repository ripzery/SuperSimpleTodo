package com.onemorebit.supersimpletodo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.Models.OttoCheckedCount;
import com.onemorebit.supersimpletodo.Utils.BusProvider;
import com.onemorebit.supersimpletodo.Utils.Contextor;
import com.onemorebit.supersimpletodo.databinding.LayoutTabColumnBinding;
import com.onemorebit.supersimpletodo.databinding.MainBinder;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public MainBinder mainBinder;
    public PagerAdapter adapter;
    public MenuItem removeItem;
    private ArrayList<LayoutTabColumnBinding> tabColumnBindings = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Contextor.getInstance().getContext() == null) {
            Contextor.getInstance().init(this);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initDataBinding();
        initInstance();
    }

    @Override protected void onStop() {
        BusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    private void initDataBinding() {
        mainBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mainBinder.layoutToolbar.toolbar);
        mainBinder.layoutToolbar.toolbar.setTitle(getString(R.string.app_name));
    }

    private void initInstance() {
        adapter = new PagerAdapter(getSupportFragmentManager(), this);
        mainBinder.pager.setAdapter(adapter);
        mainBinder.layoutTab.tabLayout.setupWithViewPager(mainBinder.pager);

        for (int i = 0; i < mainBinder.layoutTab.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mainBinder.layoutTab.tabLayout.getTabAt(i);
            final LayoutTabColumnBinding tabView = adapter.getTabView(i);
            tabColumnBindings.add(tabView);
            tab.setCustomView(tabView.getRoot());
        }

        mainBinder.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainBinder.layoutTab.tabLayout));
        mainBinder.layoutTab.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                mainBinder.layoutTab.tabLayout.setSelectedTabIndicatorColor(
                    ContextCompat.getColor(MainActivity.this, tab.getPosition() == 0 ? R.color.colorAccent : R.color.colorRed));
                mainBinder.pager.setCurrentItem(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mainBinder.pager.setCurrentItem(0);
    }

    @Subscribe public void onCheckedUpdate(OttoCheckedCount ottoCheckedCount) {
        String todoCount = ottoCheckedCount.count == 0 ? "<b>All done</b>" : "<b>" + ottoCheckedCount.count + "</b> items";
        tabColumnBindings.get(ottoCheckedCount.tabNumber).setTabTodoCount(todoCount);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //removeItem = menu.findItem(R.id.remove);
        return false;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                // remove checked item
                break;
            default:
        }

        return true;
    }
}
