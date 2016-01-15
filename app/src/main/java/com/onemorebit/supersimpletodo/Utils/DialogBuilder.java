package com.onemorebit.supersimpletodo.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import com.onemorebit.supersimpletodo.Fragments.BaseTodoFragment;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/8/16 AD.
 */
public class DialogBuilder {
    public static MaterialDialog createEditDialog(Context context, View view,int tabNumber, MaterialDialog.SingleButtonCallback cancelCb, MaterialDialog.SingleButtonCallback submitCb) {

        return new MaterialDialog.Builder(context).title(R.string.dialog_edit_title)
            .customView(view, true)
            .cancelable(true)
            .positiveText("Set")
            .negativeText("Cancel")
            .positiveColor(ContextCompat.getColor(Contextor.getInstance().getContext(), tabNumber == BaseTodoFragment.ONE ? R.color.colorBlue : R.color.colorRed))
            .negativeColor(ContextCompat.getColor(Contextor.getInstance().getContext(), R.color.colorTextPrimary))
            .onNegative(cancelCb)
            .onPositive(submitCb)
            .build();
    }
}
