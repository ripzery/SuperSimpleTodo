package com.onemorebit.supersimpletodo.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import com.onemorebit.supersimpletodo.Fragments.TwoTodoFragment;
import com.onemorebit.supersimpletodo.Fragments.OneTodoFragment;
import com.onemorebit.supersimpletodo.R;
import com.onemorebit.supersimpletodo.Utils.Logger;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    Context context;
    private final int TAB_TODO = 0;
    private final int TAB_DONE = 1;
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] {};
    private int[] imageResId = new int[]{ R.mipmap.ic_image_wb_incandescent, R.mipmap.ic_action_done};

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{ context.getString(R.string.tab_one_title), context.getString(R.string.tab_two_title) };

    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public Fragment getItem(int position) {
        Logger.i(PagerAdapter.class, "getItem_31: " + position);
        switch (position) {
            case TAB_TODO:
                return OneTodoFragment.newInstance();
            case TAB_DONE:
                return TwoTodoFragment.newInstance();
            default:
                return OneTodoFragment.newInstance();
        }
    }

    @Override public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString("   " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BASELINE);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
