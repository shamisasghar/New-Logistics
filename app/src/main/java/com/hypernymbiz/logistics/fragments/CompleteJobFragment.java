package com.hypernymbiz.logistics.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.adapter.CompleteJobAdapter;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.WebAPIResponse;
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

public class CompleteJobFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CompleteJobAdapter completeJobAdapter;
    private List<JobInfo_> jobInfo_s;
    String getUserAssociatedEntity;
    ConstraintLayout asd;
    ConstraintLayout rootlayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_pager_compltd, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_complete);

        rootlayout = (ConstraintLayout) view.findViewById(R.id.layout_contraint);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        if (getUserAssociatedEntity != null) {

            ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 55).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
                @Override
                public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {
                    if (response.isSuccessful()) {
                        if (response.body().status) {

                            // Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
                            jobInfo_s = response.body().response.job_info;
                            completeJobAdapter = new CompleteJobAdapter(jobInfo_s);
                            recyclerView.setAdapter(completeJobAdapter);
                        }
                    } else {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                    }
                }
                @Override
                public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {

                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                }
            });
        } else {
            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

        }

        return view;
    }

//    public void compled_swipe()
//    {
//
//
//        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity), 55).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
//            @Override
//            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {
//                if (response.body().status) {
//
////                    Toast.makeText(getContext(), "User GGetS", Toast.LENGTH_SHORT).show();
////                    Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
//                    jobInfo_s = response.body().response.job_info;
//                    completeJobAdapter = new CompleteJobAdapter(jobInfo_s);
//                    recyclerView.setAdapter(completeJobAdapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {
//                Toast.makeText(getContext(), "Network failure", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//


}