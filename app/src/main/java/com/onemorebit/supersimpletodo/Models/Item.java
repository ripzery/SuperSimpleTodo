package com.onemorebit.supersimpletodo.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import com.onemorebit.supersimpletodo.BR;


/**
 * Created by Euro on 1/2/16 AD.
 */
public class Item extends BaseObservable{
    private boolean isChecked;
    private String description;

    public Item(boolean isChecked, String description) {
        this.isChecked = isChecked;
        this.description = description;
    }

    @Bindable
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        notifyPropertyChanged(BR.checked);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }
}
