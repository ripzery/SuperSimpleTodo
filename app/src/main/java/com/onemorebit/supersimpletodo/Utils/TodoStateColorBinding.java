package com.onemorebit.supersimpletodo.Utils;

import android.databinding.BindingAdapter;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/3/16 AD.
 */
public class TodoStateColorBinding {
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

    //@BindingAdapter({ "android:textCursorDrawable" }) public static void setTextCursorDrawable(EditText editText, int tabNumber) {
    //    editText.setCur
    //}

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
