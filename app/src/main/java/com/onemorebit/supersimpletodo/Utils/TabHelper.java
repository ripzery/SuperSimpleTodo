package com.onemorebit.supersimpletodo.Utils;

import com.onemorebit.supersimpletodo.Adapters.PagerAdapter;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/7/16 AD.
 */
public class TabHelper {
    public static int[] imageResId = new int[]{ R.mipmap.ic_image_wb_incandescent, R.mipmap.ic_action_done};
    public static final int TOTAL_TAB = 2;

    public static String getNameTab(int tabNumber) {
        switch (tabNumber) {
            case 0:
                return Contextor.getInstance().getContext().getString(R.string.tab_one_title);
            case 1:
                return Contextor.getInstance().getContext().getString(R.string.tab_one_title);
            default:
                Logger.i(TabHelper.class, "getNameTab_17: " + "Unknown Tab");
                return "Unknown";
        }
    }
}
