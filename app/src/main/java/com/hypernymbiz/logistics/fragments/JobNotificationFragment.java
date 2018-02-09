package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.adapter.JobNotifiyAdapter;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.JobCount;
import com.hypernymbiz.logistics.models.JobCountPatch;
import com.hypernymbiz.logistics.models.JobInfo;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.Profile;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.LoginUtils;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 21-Jan-18.
 */

public class JobNotificationFragment extends Fragment {

    private JobNotifiyAdapter adapter;
    List<JobInfo_> jobInfo_s;
    SharedPreferences sharedPreferences;
    String getUserAssociatedEntity;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipelayout;
    ConstraintLayout constraintLayout;
    Context fContext;
    ImageView imageView;
    View view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext = context;
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Job Notification");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_notification, container, false);

        sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.notification_recycler);
        imageView = (ImageView) view.findViewById(R.id.img_job_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.layout_constraint1);
      //  swipe();


//        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity),53).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
//            @Override
//            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {
//                if (response.body().status!=null) {
//
//
//
//                    // Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
//                    jobInfo_s = response.body().response.job_info;
//                    adapter=new JobNotifiyAdapter(jobInfo_s,getActivity());
//                    recyclerView.setAdapter(adapter);
//                    size=String.valueOf(jobInfo_s.size());
//                    if (size==null) {
//                        imageView.setVisibility(View.VISIBLE);
//                    }
//                    else
//                        imageView.setVisibility(View.GONE);
//
////                        Toast.makeText(getActivity(), size, Toast.LENGTH_SHORT).show();
//
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    Gson gson = new Gson();
//                    String json = gson.toJson(jobInfo_s);
//                    editor.putString("list", json);
//                    editor.commit();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t)
//            {
//
//                Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
//                View sbView = snackbar.getView();
//                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDialogToolbarText));
//                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                snackbar.show();
//            }
//        });
        if(getUserAssociatedEntity!=null) {
            ApiInterface.retrofit.getallpendingdata(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<List<JobInfo>>>() {
                @Override
                public void onResponse(Call<WebAPIResponse<List<JobInfo>>> call, Response<WebAPIResponse<List<JobInfo>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().status) {
                            List<JobInfo> jobInfo = response.body().response;
                            adapter = new JobNotifiyAdapter(jobInfo, getActivity());
                            recyclerView.setAdapter(adapter);
                            String size;
                            size = String.valueOf(jobInfo.size());
                            if (size.equals("0")) {
                                imageView.setVisibility(View.VISIBLE);
                            } else
                                imageView.setVisibility(View.GONE);
                        }
                    } else {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                    }
                }

                @Override
                public void onFailure(Call<WebAPIResponse<List<JobInfo>>> call, Throwable t) {
                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                }
            });
        }
        else
        {
            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

        }

//        ApiInterface.retrofit.getcountpatch().enqueue(new Callback<WebAPIResponse<JobCountPatch>>() {
//            @Override
//            public void onResponse(Call<WebAPIResponse<JobCountPatch>> call, Response<WebAPIResponse<JobCountPatch>> response) {
//                if (response.isSuccessful()) {
//                    if (response.body().status) {
//                        Log.d("TAAAG", "" + response);
//
//                    }
//
//                } else {
//
//                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
//
//                }
//
//            }
//
//
//            @Override
//            public void onFailure(Call<WebAPIResponse<JobCountPatch>> call, Throwable t) {
//                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
//
//            }
//        });


        return view;
    }

//    public void swipe() {
//        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipelayout.setRefreshing(true);
//                (new Handler()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        swipelayout.setRefreshing(false);
//                        ApiInterface.retrofit.getallpendingdata(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<List<JobInfo>>>() {
//                            @Override
//                            public void onResponse(Call<WebAPIResponse<List<JobInfo>>> call, Response<WebAPIResponse<List<JobInfo>>> response) {
//                                if (response.isSuccessful()) {
//                                    if (response.body().status) {
//                                        List<JobInfo> jobInfo = response.body().response;
//                                        adapter = new JobNotifiyAdapter(jobInfo, getActivity());
//                                        recyclerView.setAdapter(adapter);
//                                        String size;
//                                        size = String.valueOf(jobInfo.size());
//                                        if (size.equals("0")) {
//                                            imageView.setVisibility(View.VISIBLE);
//                                        } else
//                                            imageView.setVisibility(View.GONE);
//
//                                    }
//                                }else {
//                                    AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), 2));
//                                }
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<WebAPIResponse<List<JobInfo>>> call, Throwable t) {
//                                AppUtils.showSnackBar(getView(),AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
//
//                            }
//                        });
//
//                    }
//                }, 3000);
//            }
//        });
//
//
//    }


}
