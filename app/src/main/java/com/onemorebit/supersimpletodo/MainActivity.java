package com.onemorebit.supersimpletodo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.Fragments.OneTodoFragment;
import com.onemorebit.supersimpletodo.Models.OttoCheckedCount;
import com.onemorebit.supersimpletodo.Utils.Contextor;
import com.onemorebit.supersimpletodo.databinding.LayoutTabColumnBinding;
import com.onemorebit.supersimpletodo.databinding.MainBinder;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public MainBinder mainBinder;
    public PagerAdapter adapter;
    private ArrayList<LayoutTabColumnBinding> tabColumnBindings = new ArrayList<>();
    private OneTodoFragment oneFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Contextor.getInstance().getContext() == null) {
            Contextor.getInstance().init(this);
        }

        initDataBinding();

        /* Init pager adapter */
        //initInstance();

        adapter = new PagerAdapter(getSupportFragmentManager(), this);

        initFragment();
    }

    private void initFragment(){
        oneFragment = new OneTodoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.rootLayout, oneFragment).commit();
    }

    @Override protected void onStop() {
        //BusProvider.getInstance().unregister(this);
        super.onStop();
    }

    @Override protected void onStart() {
        super.onStart();
        //BusProvider.getInstance().register(this);
    }

    private void initDataBinding() {
        mainBinder = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mainBinder.layoutToolbar.toolbar);

        ActionBar actionBar = getSupportActionBar();

        /* set toolbar title */
        actionBar.setTitle(getString(R.string.app_name) + " - To do list");
    }

    private void initInstance() {

        /* init instance adapter */
        adapter = new PagerAdapter(getSupportFragmentManager(), this);

        /* set view pager adapter */
        mainBinder.pager.setAdapter(adapter);

        /* bind view pager with tab layout */
        mainBinder.layoutTab.tabLayout.setupWithViewPager(mainBinder.pager);

        /* init tab indicator color */
        mainBinder.layoutTab.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(MainActivity.this, R.color.itemTextColor));


        /* set all tab's custom view */
        for (int i = 0; i < mainBinder.layoutTab.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mainBinder.layoutTab.tabLayout.getTabAt(i);
            final LayoutTabColumnBinding tabView = adapter.getTabView(i);
            tabColumnBindings.add(tabView);
            tab.setCustomView(tabView.getRoot());
        }

        /* add page change listener */
        mainBinder.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainBinder.layoutTab.tabLayout));

        /* add tab selected listener */
        mainBinder.layoutTab.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                mainBinder.layoutTab.tabLayout.setSelectedTabIndicatorColor(
                    ContextCompat.getColor(MainActivity.this, tab.getPosition() == BaseTodoFragment.ONE ? R.color.itemTextColor : R.color.colorRed ));
                mainBinder.pager.setCurrentItem(tab.getPosition());
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /* Initial selected color state */
        mainBinder.layoutTab.tabLayout.getTabAt(0).getCustomView().setSelected(true);

        /* selected first tab */
        mainBinder.pager.setCurrentItem(BaseTodoFragment.ONE);
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
