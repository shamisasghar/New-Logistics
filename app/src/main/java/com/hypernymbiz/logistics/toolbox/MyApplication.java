package com.hypernymbiz.logistics.toolbox;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.hypernymbiz.logistics.utils.PrefUtils;
import com.hypernymbiz.logistics.utils.ScheduleUtils;

/**
 * Created by Metis on 28-Feb-18.
 */

public class MyApplication extends Application

{
    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

    }
    public static Context getAppContext() {
        return MyApplication.context;
    }
}
