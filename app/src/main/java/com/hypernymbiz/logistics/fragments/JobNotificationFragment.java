package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.adapter.JobNotifiyAdapter;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
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
    private List<JobInfo_> jobInfo_s;
    SharedPreferences sharedPreferences;
    String getUserAssociatedEntity;
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipelayout;
    Context fContext;
    View view;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext=context;
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Job Notification");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_notification, container, false);


       sharedPreferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getActivity());
        recyclerView= (RecyclerView) view.findViewById(R.id.notification_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipelayout = (SwipeRefreshLayout)view.findViewById(R.id.layout_swipe);

        ApiInterface.retrofit.getalldata(Integer.parseInt(getUserAssociatedEntity),53).enqueue(new Callback<WebAPIResponse<Respone_Completed_job>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<Respone_Completed_job>> call, Response<WebAPIResponse<Respone_Completed_job>> response) {
                if (response.body().status) {

                    // Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
                    jobInfo_s = response.body().response.job_info;
                    adapter=new JobNotifiyAdapter(jobInfo_s,getActivity());
                    recyclerView.setAdapter(adapter);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(jobInfo_s);
                    editor.putString("list", json);
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<WebAPIResponse<Respone_Completed_job>> call, Throwable t)
            {

                Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDialogToolbarText));
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.show();
            }
        });
        return view;
    }







}
