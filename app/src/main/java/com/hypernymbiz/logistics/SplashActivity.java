package com.hypernymbiz.logistics;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.ScheduleUtils;
import com.onesignal.OneSignal;


/**
 * Created by Bilal Rashid on 10/8/2017.
 */

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ActivityUtils.startActivity(SplashActivity.this, LoginActivity.class, true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 1000);
        initOneSignal();

    }

    @Override
    public void onBackPressed() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onBackPressed();
    }
    public void initOneSignal() {
        // Logging set to help debug issues, remove before releasing your app.
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
//
        OneSignal.startInit(this)
                .init();
       // OneSignal.sendTag("email","driver1@kotal.com");

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
//                Toast.makeText(SplashActivity.this, userId, Toast.LENGTH_SHORT).show();
                ScheduleUtils.saveUserOneSignalId(getApplicationContext(), userId);
                Log.e("debug", "User:" + userId);
                if (registrationId != null)
                    Log.e("debug", "registrationId:" + registrationId);
            }
        });
    }

}
