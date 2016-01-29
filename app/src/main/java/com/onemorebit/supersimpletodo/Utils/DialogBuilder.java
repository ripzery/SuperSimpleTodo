package com.onemorebit.supersimpletodo.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
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
            .negativeColor(ContextCompat.getColor(context, R.color.colorDialogNegative))
            .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .onNegative(cancelCb)
            .onPositive(submitCb)
            .theme(Theme.LIGHT)
            .build();
    }
}
