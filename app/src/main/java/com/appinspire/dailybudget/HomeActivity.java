package com.appinspire.dailybudget;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appinspire.dailybudget.dialog.SimpleDialog;
import com.appinspire.dailybudget.fragments.HomeFragment;
import com.appinspire.dailybudget.fragments.JobNotificationFragment;
import com.appinspire.dailybudget.models.JobInfo_;
import com.appinspire.dailybudget.toolbox.ToolbarListener;
import com.appinspire.dailybudget.utils.ActivityUtils;
import com.appinspire.dailybudget.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeActivity extends AppCompatActivity implements ToolbarListener {
    private Toolbar mToolbar;
    private SimpleDialog mSimpleDialog;
    SharedPreferences sharedPreferences;
    ArrayList<JobInfo_> infoList;
    private TextView mNumberOfCartItemsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbarSetup();
        String fragmentName = getIntent().getStringExtra(Constants.FRAGMENT_NAME);
        Bundle bundle = getIntent().getBundleExtra(Constants.DATA);
        if (!TextUtils.isEmpty(fragmentName)) {
            Fragment fragment = Fragment.instantiate(this, fragmentName);
            if (bundle != null)
                fragment.setArguments(bundle);
            addFragment(fragment);
        } else {
            addFragment(new HomeFragment());
        }
        sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<JobInfo_>>() {}.getType();
        infoList = gson.fromJson(json, type);

//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    public void addFragment(final Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void toolbarSetup() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(" ");
        ActivityUtils.centerToolbarTitle(mToolbar,false);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void onBackPressed() {

        mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_exit),
                getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_positive:
                        mSimpleDialog.dismiss();
                        HomeActivity.this.finish();
                        break;
                    case R.id.button_negative:
                        mSimpleDialog.dismiss();
                        break;
                }
            }
        });
        mSimpleDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        View view = menu.findItem(R.id.notification_bell).getActionView();
        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);

        if ( infoList== null) {
            mNumberOfCartItemsText.setText("0");
        } else {
            mNumberOfCartItemsText.setText(String.valueOf(infoList.size()));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(),JobNotifyActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//                finish();
//                Toast.makeText(HomeActivity.this, "bell", Toast.LENGTH_SHORT).show();
               ActivityUtils.startActivity(getApplicationContext(), FrameActivity.class,JobNotificationFragment.class.getName(),null);

            }
        });
//        ImageView cartImage = (ImageView) view.findViewById(R.id.image_cart);
//        cartImage.setColorFilter(ContextCompat.getColor(this, R.color.colorToolbarIcon));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
//            case R.id.image_cart:
//                break;
//                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                return true;
//            case R.id.action_cart:
//                break;
//            case R.id.action_settings:
//                AppUtils.makeToast(this, getString(R.string.msg_settings_not_available));
//                break;
//            default:
            // ...
        }
        return super.onOptionsItemSelected(item);
    }


}
