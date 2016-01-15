package com.onemorebit.supersimpletodo.Utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Euro on 1/4/16 AD.
 */
public class MySnackbarBehavior extends CoordinatorLayout.Behavior<View>  {

    public MySnackbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // we only want to trigger the change
        // only when the changes is from a snackbar
        return dependency instanceof Snackbar.SnackbarLayout;
    }


}
