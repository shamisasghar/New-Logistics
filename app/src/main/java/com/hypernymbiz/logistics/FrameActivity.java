package com.hypernymbiz.logistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.enumerations.AnimationEnum;
import com.hypernymbiz.logistics.fragments.ActiveJobFragment;
import com.hypernymbiz.logistics.fragments.HomeFragment;
import com.hypernymbiz.logistics.fragments.JobDetailsFragment;
import com.hypernymbiz.logistics.fragments.JobNotificationFragment;
import com.hypernymbiz.logistics.models.JobDetail;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.StartJob;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActiveJobUtils;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Bilal Rashid on 10/11/2017.
 */

public class FrameActivity extends AppCompatActivity implements ToolbarListener {
    private Toolbar mToolbar;
    String getUserAssociatedEntity;
    private SimpleDialog mSimpleDialog;
    SharedPreferences sharedPreferences;
    ArrayList<JobInfo_> infoList;

    SharedPreferences pref;
    private TextView mNumberOfCartItemsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        pref = getApplication().getSharedPreferences("TAG", MODE_PRIVATE);

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
        Type type = new TypeToken<ArrayList<JobInfo_>>() {
        }.getType();
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
        ActivityUtils.centerToolbarTitle(mToolbar, true);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
//            getActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (isTaskRoot()) {

            if(fragment instanceof ActiveJobFragment&&ActiveJobUtils.isJobResumed(this))
            {
                mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_failed_job), getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.button_positive:
                                mSimpleDialog.dismiss();
                                String id = pref.getString("id", "");
                                String driverid = pref.getString("driver", "");

                                HashMap<String, Object> body = new HashMap<>();
                                if (id != null) {
                                    body.put("job_id", Integer.parseInt(id));
                                    body.put("driver_id", Integer.parseInt(driverid));
                                    body.put("flag", 54);
                                }
                                ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                                    @Override
                                    public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().status) {

                                            } else {
//                                             AppUtils.showSnackBar(ge(),AppUtils.getErrorMessage(getContext(),2));
                                                Toast.makeText(FrameActivity.this, "Error Occur", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {
//                                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
                                        Toast.makeText(FrameActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                finish();
                                ActivityUtils.startHomeActivity(getApplicationContext(), HomeActivity.class, HomeFragment.class.getName());
                                ActiveJobUtils.clearJobResumed(getApplicationContext());
                                break;
                            case R.id.button_negative:
                                mSimpleDialog.dismiss();
                                break;
                        }
                    }
                });
                mSimpleDialog.show();

            }

            else {
                ActivityUtils.startHomeActivity(this, HomeActivity.class, HomeFragment.class.getName());
            }

        }

        else
            {
            if (fragment instanceof JobNotificationFragment) {
                FrameActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (fragment instanceof JobDetailsFragment) {
                FrameActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (fragment instanceof ActiveJobFragment) {

                    mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_failed_job), getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.button_positive:
                                    mSimpleDialog.dismiss();
                                    String id = pref.getString("id", "");
                                    String driverid = pref.getString("driver", "");

                                    HashMap<String, Object> body = new HashMap<>();
                                    if (id != null) {
                                        body.put("job_id", Integer.parseInt(id));
                                        body.put("driver_id", Integer.parseInt(driverid));
                                        body.put("flag", 54);
                                    }
                                    ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                                        @Override
                                        public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {
                                            if (response.isSuccessful()) {
                                                if (response.body().status) {

                                                } else {
//                                             AppUtils.showSnackBar(ge(),AppUtils.getErrorMessage(getContext(),2));
                                                    Toast.makeText(FrameActivity.this, "Error Occur", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {
//                                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
                                            Toast.makeText(FrameActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    finish();
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



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        View view = menu.findItem(R.id.notification_bell).getActionView();
//        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);
//
//        if ( infoList== null) {
//            mNumberOfCartItemsText.setText("0");
//        } else {
//            mNumberOfCartItemsText.setText(String.valueOf(infoList.size()));
//        }
//
////        view.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
//////                Intent intent=new Intent(getApplicationContext(),JobNotifyActivity.class);
//////                startActivity(intent);
//////                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
//////                finish();
//////                Toast.makeText(HomeActivity.this, "bell", Toast.LENGTH_SHORT).show();
////                ActivityUtils.startActivity(getApplicationContext(), FrameActivity.class,JobNotificationFragment.class.getName(),null);
////
////            }
////        });
////        ImageView cartImage = (ImageView) view.findViewById(R.id.image_cart);
////        cartImage.setCol
//
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
//            case R.id.notification_bell:
//                ActivityUtils.startActivity(this,FrameActivity.class,HomeFragment.class.getName(),null, AnimationEnum.VERTICAL);
//                break;
//            default:
            // ...
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
