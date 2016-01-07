package com.onemorebit.supersimpletodo.Adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.onemorebit.supersimpletodo.Fragments.TwoTodoFragment;
import com.onemorebit.supersimpletodo.Fragments.OneTodoFragment;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.databinding.LayoutTabBinding;
import com.onemorebit.supersimpletodo.databinding.LayoutTabColumnBinding;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    Context context;
    private final int TAB_ONE = 0;
    private final int TAB_TWO = 1;
    private final int TAB_THREE = 2;
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {};
    private int[] imageResId = new int[]{ R.mipmap.ic_image_wb_incandescent, R.mipmap.ic_action_done};
    private LayoutTabColumnBinding tabBinding;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{ context.getString(R.string.tab_one_title), context.getString(R.string.tab_two_title), "Work" };
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        switch (position) {
            case TAB_ONE:
                return OneTodoFragment.newInstance();
            case TAB_TWO:
                return TwoTodoFragment.newInstance();
            case TAB_THREE:
                return OneTodoFragment.newInstance();
            default:
                return OneTodoFragment.newInstance();
        }
    }

    public LayoutTabColumnBinding getTabView(int position) {
        // Given you have a custom layout in `res/layout/layout_tab.xml` with a TextView and ImageView

        tabBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_tab_column, null, false);
        tabBinding.setTabNumber(position);
        tabBinding.setTabDrawable(ContextCompat.getDrawable(context, imageResId[position]));
        tabBinding.setTabTitle(tabTitles[position]);
        tabBinding.setTabTodoCount("");
        return tabBinding;
    }

    public int getTabIcon(int position){
        return imageResId[position];
    }

    @Override public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
