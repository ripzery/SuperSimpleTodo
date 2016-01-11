package com.onemorebit.supersimpletodo.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.onemorebit.supersimpletodo.R;

/**
 * Created by Euro on 1/8/16 AD.
 */
public class DialogBuilder {
    public static MaterialDialog createEditDialog(Context context, View view, MaterialDialog.SingleButtonCallback cancelCb, MaterialDialog.SingleButtonCallback submitCb) {

        return new MaterialDialog.Builder(context).title(R.string.dialog_edit_title)
            .customView(view, true)
            .cancelable(true)
            .positiveText("Set")
            .negativeText("Cancel")
            .onNegative(cancelCb)
            .onPositive(submitCb)
            .build();
    }
}
