package com.appinspire.dailybudget.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.appinspire.dailybudget.toolbox.OnAfterTextChange;


public class MyTextWatcher implements TextWatcher {
    private View view;
    private OnAfterTextChange onAfterTextChange;

    public MyTextWatcher(View view, OnAfterTextChange onAfterTextChange) {
        this.view = view;
        this.onAfterTextChange = onAfterTextChange;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    public void afterTextChanged(Editable editable) {
        onAfterTextChange.onAfterTextChange(view, editable);
    }
}
