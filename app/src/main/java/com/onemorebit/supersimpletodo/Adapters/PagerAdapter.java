package com.onemorebit.supersimpletodo.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.onemorebit.supersimpletodo.Fragments.DoneFragment;
import com.onemorebit.supersimpletodo.Fragments.TodoFragment;
import com.onemorebit.supersimpletodo.Utils.Logger;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    Context context;
    private final int TAB_TODO = 0;
    private final int TAB_DONE = 1;
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Todo", "Done" };

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        Logger.i(PagerAdapter.class, "getItem_31: " + position);
        switch (position) {
            case TAB_TODO:
                return TodoFragment.newInstance();
            case TAB_DONE:
                return DoneFragment.newInstance();
            default:
                return TodoFragment.newInstance();
        }
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
