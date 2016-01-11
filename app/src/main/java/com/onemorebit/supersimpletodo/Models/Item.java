package com.onemorebit.supersimpletodo.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Html;
import com.onemorebit.supersimpletodo.BR;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Euro on 1/2/16 AD.
 */
public class Item extends BaseObservable {
    private boolean isChecked;
    private String description;
    private long notificationId;

    public Item(boolean isChecked, String description, long notificationId) {
        this.isChecked = isChecked;
        this.description = description;
        this.notificationId = notificationId;
    }

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    @Bindable public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        notifyPropertyChanged(BR.checked);
    }

    @Bindable public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    public String getTrimHtmlTime() {
        String trimDate;
        if (notificationId != 0) {
            String trimHtml = Html.fromHtml(description).toString();
            int startIndex = trimHtml.lastIndexOf("@");
            trimDate = trimHtml.substring(0, startIndex);
        } else {
            trimDate = description;
        }
        return trimDate;
    }

    public Date getDateFromString() {
        if (notificationId == 0) return null;

        String trimHtml = Html.fromHtml(description).toString();
        int startIndex = trimHtml.lastIndexOf("@") + 1;
        String dateTime = trimHtml.substring(startIndex, trimHtml.length());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
        try {
            return format.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDateString() {
        if (notificationId == 0) return "Not set";

        String trimHtml = Html.fromHtml(description).toString();
        int startIndex = trimHtml.lastIndexOf("@") + 1;
        String dateTime = trimHtml.substring(startIndex, trimHtml.length());

        return dateTime;
    }

    public boolean isShouldNotify() {
        return new Date().compareTo(getDateFromString()) <= 0;
    }
}
