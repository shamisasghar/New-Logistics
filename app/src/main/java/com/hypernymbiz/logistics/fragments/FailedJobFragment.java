package com.hypernymbiz.logistics.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.adapter.FailedJobAdapter;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 22-Jan-18.
 */

public class FailedJobFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FailedJobAdapter failedJobAdapter;
    private List<JobInfo_> jobInfo_s;
    String getUserAssociatedEntity;
    ConstraintLayout asd;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_view_pager_failed,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_complete);
        asd=(ConstraintLayout)view.findViewById(R.id.layout_contraint_failed);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getUserAssociatedEntity= LoginUtils.getUserAssociatedEntity(getContext());
        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity),54).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {

                if(response.body().response.job_count==0)
                {
                    asd.setBackgroundColor(Color.parseColor("#63a4ff"));

                }
                else if (response.body().status){

                    Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
                    jobInfo_s=response.body().response.job_info;
                    failedJobAdapter=new FailedJobAdapter(jobInfo_s);
                    recyclerView.setAdapter(failedJobAdapter);
                }
            }

            @Override
            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t) {
            }
        });

        return view;
    }

}