package com.yixia.videoeditor.resourcerecord.recordvoice;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopRecordRemind extends PopupWindow {

    public PopRecordRemind(Context mContext, int layoutID) {
        super(mContext);
        View view = LayoutInflater.from(mContext).inflate(layoutID, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(false);
        setTouchable(false);
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public PopRecordRemind showCenter(Context context, int layoutId) {
        if (!isShowing()) {
            View rootView = LayoutInflater.from(context).inflate(layoutId, null);
            showAtLocation(rootView, Gravity.CENTER, 0, 0);
        }
        return this;
    }

}
