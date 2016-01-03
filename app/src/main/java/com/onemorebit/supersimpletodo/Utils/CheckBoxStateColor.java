package com.onemorebit.supersimpletodo.Utils;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/3/16 AD.
 */
public class CheckBoxStateColor {
    @BindingAdapter({"app:buttonTint"})
    public static void setStateColor(AppCompatCheckBox checkBox, boolean isTodo){
        checkBox.setSupportButtonTintList(ContextCompat.getColorStateList(Contextor.getInstance().getContext(), isTodo ? R.color.colorAccent : R.color.colorDeleteItem));
    }
}
