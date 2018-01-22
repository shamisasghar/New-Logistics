package com.hypernymbiz.logistics.utils;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.enumerations.SnackBarState;


public class SnackBarController {
    View mParent;

    public SnackBarController(View parent) {
        this.mParent = parent;
    }

    public void changeState(SnackBarState state, String message) {
        if (state == SnackBarState.ERROR) {
            Snackbar snackbar = Snackbar
                    .make(mParent, message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundResource(R.color.colorSnackBar);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(ContextCompat.getColor(mParent.getContext(), R.color.colorSnackBarText));
            snackbar.show();
        }
    }
}
