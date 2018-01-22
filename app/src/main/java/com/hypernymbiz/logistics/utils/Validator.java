package com.hypernymbiz.logistics.utils;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;


import com.hypernymbiz.logistics.R;

import java.util.regex.Pattern;

/**
 * Created by Square on 02/18/2016.
 */
public abstract class Validator {
    // For Testing
//    public static final String regexFirstName = "^[\\p{L} .'-]+$";
//    public static final String regexLastName = "^[\\p{L} .'-]+$";
//    public static final String regexUserName = ".+";// for testing
//    public static final String regexPassword = ".+";//  for testing

    // Actual
    public static final String regexFirstName = "^[a-zA-Z]+(\\s[a-zA-Z]+){0,24}$";
    public static final String regexLastName = "^[a-zA-Z]{1,25}$";
    public static final String regexUserName = "^[a-z]([a-z0-9_]){1,24}$";// from http://www.mkyong.com
    public static final String regexPassword = ".{6,25}";
    //    public static final String regexPassword = "^[a-z0-9_-]{6,18}$";  // from http://code.tutsplus.com/
//    public static final String regexPassword = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";// from http://www.mkyong.com
//    public static final String regexEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";// from http://www.mkyong.com
    public static final String regexEmail = "^([a-z0-9_\\.-]{1,30})@([\\da-z\\.-]{1,25})\\.([a-z\\.]{2,6})$";  // from http://code.tutsplus.com/
    public static final String regexAlphabetsSpaceNumbersLargeExtra = "^.{1,200}$";
    public static final String regexContactNo = "^\\d{5,15}$";
    public static final String regexAddress = "^[a-zA-Z][a-zA-Z0-9\\s,/#.&'-\\\\]{1,199}$";

    public static boolean isValidFirstName(Activity activity, TextInputLayout textInputLayout, EditText editText) {
        if (!Pattern.matches(regexFirstName, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(activity.getString(R.string.err_first_name));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isValidLastName(Activity activity, TextInputLayout textInputLayout, EditText editText) {
        if (!Pattern.matches(regexLastName, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(activity.getString(R.string.err_last_name));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isValidUserName(Activity activity, TextInputLayout textInputLayout, EditText editText) {
        if (!Pattern.matches(regexUserName, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(activity.getString(R.string.err_user_name));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isValidPassword(Activity activity, TextInputLayout textInputLayout, EditText editText) {
        if (!Pattern.matches(regexPassword, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(activity.getString(R.string.err_password));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isValidEmail(Activity activity, TextInputLayout textInputLayout, EditText editText) {
        if (!Pattern.matches(regexEmail, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(activity.getString(R.string.err_email));
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isContactNo(TextInputLayout textInputLayout, EditText editText, String error) {
        if (!Pattern.matches(regexContactNo, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(error);
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isAlphabetsSpaceNumbersLargeExtra(TextInputLayout textInputLayout, EditText editText, String error) {
        if (!Pattern.matches(regexAlphabetsSpaceNumbersLargeExtra, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(error);
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public static boolean isValidAddress(TextInputLayout textInputLayout, EditText editText, String error) {
        if (!Pattern.matches(regexAddress, editText.getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(error);
            return false;
        } else {
            textInputLayout.setError(null);
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
}
