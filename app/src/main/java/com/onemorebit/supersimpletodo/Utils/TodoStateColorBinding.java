package com.onemorebit.supersimpletodo.Utils;

import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/3/16 AD.
 */
public class TodoStateColorBinding {
    private static YoYo.YoYoString btnDeleteAnimation;

    @BindingAdapter({ "app:buttonTint" }) public static void setStateColor(AppCompatCheckBox checkBox, int tabNumber) {
        checkBox.setSupportButtonTintList(ContextCompat.getColorStateList(Contextor.getInstance().getContext(), getColorView(tabNumber)));
    }

    @BindingAdapter({ "android:background" }) public static void setBtnDeleteBackground(Button btnDelete, int tabNumber) {
        btnDelete.setBackground(ContextCompat.getDrawable(Contextor.getInstance().getContext(), getColorButtonDelete(tabNumber)));
    }

    @BindingAdapter({ "android:textColor" }) public static void setTextColor(TextView textView, int tabNumber) {
        textView.setTextColor(ContextCompat.getColor(Contextor.getInstance().getContext(), getColorView(tabNumber)));
    }

    @BindingAdapter({ "app:editTextLine" }) public static void setEditTextLineColor(EditText editText, int tabNumber) {
        editText.getBackground()
            .setColorFilter(ContextCompat.getColor(Contextor.getInstance().getContext(), getColorView(tabNumber)), PorterDuff.Mode.SRC_ATOP);
    }

    @BindingAdapter({ "app:mrl_rippleColor" }) public static void setRippleColor(MaterialRippleLayout materialRippleLayout, int tabNumber) {
        materialRippleLayout.setRippleColor(ContextCompat.getColor(Contextor.getInstance().getContext(), getColorView(tabNumber)));
    }

    @BindingAdapter({ "app:animateVisibility" }) public static void setAnimateVisibility(final View view, final int checkCount) {
        final boolean isNotAnimateRepeat = view.getAlpha() < 1 || checkCount < 1;
        final boolean isShouldAnimate = (isNotAnimateRepeat) && (btnDeleteAnimation == null || !(btnDeleteAnimation.isRunning() && checkCount > 0));
        if(isShouldAnimate) {
            btnDeleteAnimation = YoYo.with(checkCount > 0 ? Techniques.Landing : Techniques.TakingOff).duration(300).playOn(view);
        }
    }

    private static int getColorView(int tabNumber) {
        switch (tabNumber) {
            case BaseTodoFragment.ONE:
                return R.color.colorAccent;
            case BaseTodoFragment.TWO:
                return R.color.colorRed;
            default:
                return R.color.colorAccent;
        }
    }

    private static int getDrawableCursor(int tabNumber) {
        switch (tabNumber) {
            case BaseTodoFragment.ONE:
                return R.drawable.color_cursor_blue;
            case BaseTodoFragment.TWO:
                return R.drawable.color_cursor_red;
            default:
                return R.drawable.color_cursor_blue;
        }
    }

    private static int getColorButtonDelete(int tabNumber) {
        switch (tabNumber) {
            case BaseTodoFragment.ONE:
                return R.drawable.btn_ripple_move;
            case BaseTodoFragment.TWO:
                return R.drawable.btn_ripple_delete;
            default:
                return R.drawable.btn_ripple_move;
        }
    }
}
