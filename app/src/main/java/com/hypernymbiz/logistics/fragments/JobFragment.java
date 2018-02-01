package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 22-Jan-18.
 */

public class JobFragment extends Fragment implements View.OnClickListener, ToolbarListener {
    private HomeFragment.ViewHolder mHolder;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private List<JobInfo_> jobInfo_s;
    private SwipeRefreshLayout swipelayout;
    private String getUserAssociatedEntity;
    private ProgressBar progressBar;
    View mview;
    private ConstraintLayout constraintLayout;

    public TextView compltd_job, faild_job;


    private ViewPager mViewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Job Details");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        compltd_job = (TextView) view.findViewById(R.id.txt_no_completed_job);
        faild_job = (TextView) view.findViewById(R.id.txt_no_failed_job);
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.layout_contraint);

        swipelayout.setColorSchemeColors(Color.BLUE);
        swipelayout();

        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 55).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {

                if (response.isSuccessful()) {
                    if (response.body().status) {
                        jobInfo_s = response.body().response.job_info;
                        compltd_job.setText(Integer.toString(jobInfo_s.size()));
                        //  faild_job.setText(Integer.toString(jobInfo_s.size()));
                    }
                }
                 else {

                    AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(),2));
               }

            }

            @Override
            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {

                AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
//                Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
//                View sbView = snackbar.getView();
//                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDialogToolbarText));
//                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                snackbar.show();

            }
        });
        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 54).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {

                if (response.body().status!=null) {
                    jobInfo_s = response.body().response.job_info;
                      faild_job.setText(Integer.toString(jobInfo_s.size()));

                }
//                else{
//
//                    AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
//
//
//
//                }
            }

            @Override
            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {
//                AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));


            }
        });
        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    CompleteJobFragment tab1 = new CompleteJobFragment();
                    return tab1;
                case 1:
                    FailedJobFragment tab2 = new FailedJobFragment();
                    return tab2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    String var = "Completed Job";

                    return var;
                case 1:
                    return "Failed Job";
            }
            return null;
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new HomeFragment.ViewHolder(view);
   //     EventBus.getDefault().post(new DrawerItemSelectedEvent(getString(R.string.drawer_job)));
        //       mHolder.button.setOnClickListener(this);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ActivityUtils.startActivity(getActivity(), FrameActivity.class, HomeFragment.class.getName(), null);

    }

    @Override
    public void setTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }

//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//
//        MenuItem itemCart = (MenuItem) menu.findItem(R.id.notification_bell);
//        itemCart.setVisible(true);
//        super.onPrepareOptionsMenu(menu);
//    }


    @Override
    public void onResume() {
        super.onResume();
     //   EventBus.getDefault().post(new DrawerItemSelectedEvent(getString(R.string.drawer_job)));
    }

    public static class ViewHolder {


        Button button;

        public ViewHolder(View view) {
            button = (Button) view.findViewById(R.id.sign_in);

        }

    }

    public void swipelayout() {
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipelayout.setRefreshing(true);


                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipelayout.setRefreshing(false);

                        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
                        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 55).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
                            @Override
                            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {
                                if (response.body().status) {
                                    jobInfo_s = response.body().response.job_info;
                                    compltd_job.setText(Integer.toString(jobInfo_s.size()));
                                 //   faild_job.setText(Integer.toString(jobInfo_s.size()));

                                }
                            }

                            @Override
                            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {
                                AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                            }
                        });
                        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 54).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
                            @Override
                            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {
                                if (response.body().status) {
                                    jobInfo_s = response.body().response.job_info;
                                    // compltd_job.setText(Integer.toString(jobInfo_s.size()));
                                       faild_job.setText(Integer.toString(jobInfo_s.size()));

                                }
                            }

                            @Override
                            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {
//                                AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                            }
                        });


                    }
                }, 3000);
            }
        });


    }

}