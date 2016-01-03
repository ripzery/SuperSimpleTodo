package com.onemorebit.supersimpletodo.Utils;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/3/16 AD.
 */
public class CheckBoxStateColor {
    @BindingAdapter({"app:buttonTint"})
    public static void setStateColor(AppCompatCheckBox checkBox, int tabNumber){
        checkBox.setSupportButtonTintList(ContextCompat.getColorStateList(Contextor.getInstance().getContext(), getColorStateList(tabNumber)));
    }

    private static int getColorStateList(int tabNumber){
        switch (tabNumber){
            case BaseTodoFragment.ONE:
                return R.color.colorAccent;
            case BaseTodoFragment.TWO:
                return R.color.colorRed;
            default:
                return R.color.colorAccent;
        }
    }
}
