package com.hypernymbiz.logistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.fragments.HomeFragment;
import com.hypernymbiz.logistics.fragments.JobFragment;
import com.hypernymbiz.logistics.fragments.JobNotificationFragment;
import com.hypernymbiz.logistics.fragments.Profile_Fragment;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeActivity extends AppCompatActivity implements ToolbarListener, NavigationView.OnNavigationItemSelectedListener {
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




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)

    {
        int id = item.getItemId();

        if (id == R.id.home) {

            if (!AppUtils.isInternetAvailable(this)) {
                mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                ActivityUtils.startWifiSettings(HomeActivity.this);
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            }
            addFragment(new HomeFragment());
            // Handle the camera action
        } else if (id == R.id.jobs) {

            if (!AppUtils.isInternetAvailable(this)) {
                mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                ActivityUtils.startWifiSettings(HomeActivity.this);
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            }
            addFragment(new JobFragment());
        } else if (id == R.id.distance) {

        } else if (id == R.id.profile) {
            if (!AppUtils.isInternetAvailable(this)) {
                mSimpleDialog = new SimpleDialog(this, getString(R.string.title_internet), getString(R.string.msg_internet),
                        getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                ActivityUtils.startWifiSettings(HomeActivity.this);
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();
                return true;
            }
            addFragment(new Profile_Fragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
