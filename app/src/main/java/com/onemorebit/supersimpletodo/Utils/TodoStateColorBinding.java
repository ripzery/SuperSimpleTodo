package com.onemorebit.supersimpletodo.Utils;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
        //editText.getBackground()
        //    .setColorFilter(ContextCompat.getColor(Contextor.getInstance().getContext(), getColorView(tabNumber)), PorterDuff.Mode.SRC_ATOP);
        editText.setBackground(ContextCompat.getDrawable(Contextor.getInstance().getContext(), getDrawableEditText(tabNumber)));
    }

    @BindingAdapter({ "app:mrl_rippleColor" }) public static void setRippleColor(MaterialRippleLayout materialRippleLayout, int tabNumber) {
        materialRippleLayout.setRippleColor(ContextCompat.getColor(Contextor.getInstance().getContext(), getColorView(tabNumber)));
    }

    @BindingAdapter({ "app:animateVisibility" }) public static void setAnimateVisibility(final View view, final int checkCount) {
        final boolean isNotAnimateRepeat = view.getAlpha() < 1 || checkCount < 1;
        final boolean isShouldAnimate = (isNotAnimateRepeat) && (btnDeleteAnimation == null || !(btnDeleteAnimation.isRunning() && checkCount > 0));
        if (isShouldAnimate) {
            btnDeleteAnimation = YoYo.with(checkCount > 0 ? Techniques.Landing : Techniques.TakingOff).duration(300).playOn(view);
        }
    }

    @BindingAdapter({ "android:src" }) public static void setImageDrawable(ImageView imageView, Drawable drawable) {
        imageView.setImageDrawable(drawable);
    }

    @BindingAdapter({ "app:tabTextColor" }) public static void setTabTextColor(TextView textView, int tabNumber) {
        textView.setTextColor(getTabTextColor(tabNumber));
    }

    @BindingAdapter({ "app:htmlText"})
    public static void setHtmlText(TextView textView, String htmlText){
        textView.setText(Html.fromHtml(htmlText));
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

    private static ColorStateList getTabTextColor(int tabNumber) {
        switch (tabNumber) {
            case BaseTodoFragment.ONE:
                return ContextCompat.getColorStateList(Contextor.getInstance().getContext(), R.color.selector_tab_color_blue);
            case BaseTodoFragment.TWO:
                return ContextCompat.getColorStateList(Contextor.getInstance().getContext(), R.color.selector_tab_color_red);
            default:
                return ContextCompat.getColorStateList(Contextor.getInstance().getContext(), R.color.selector_tab_color_blue);
        }
    }

    private static int getDrawableEditText(int tabNumber) {
        switch (tabNumber) {
            case BaseTodoFragment.ONE:
                return R.drawable.todo_et_bg_blue2;
            case BaseTodoFragment.TWO:
                return R.drawable.todo_et_bg_red2;
            default:
                return R.drawable.todo_et_bg_blue2;
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
