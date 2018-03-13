package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.adapter.CompleteJobAdapter;
import com.hypernymbiz.logistics.adapter.MaintenanceAdapter;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.JobInfo_;
import com.hypernymbiz.logistics.models.Maintenance;
import com.hypernymbiz.logistics.models.Respone_Completed_job;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Metis on 07-Mar-18.
 */

public class MaintenanceFragment extends Fragment {

    View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaintenanceAdapter maintenanceAdapter;
    private List<Maintenance> maintenances;
    String getUserAssociatedEntity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Maintenance");
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maintenance, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_maintenance);

//        rootlayout = (ConstraintLayout) view.findViewById(R.id.layout_contraint);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        if (getUserAssociatedEntity != null) {

            ApiInterface.retrofit.getallmaintenancedata(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<List<Maintenance>>>() {
                @Override
                public void onResponse(Call<WebAPIResponse<List<Maintenance>>> call, Response<WebAPIResponse<List<Maintenance>>> response) {
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().status) {
                                // Toast.makeText(getContext(), "List Detail"+Integer.toString(response.body().response.job_info.size()), Toast.LENGTH_SHORT).show();
                                maintenances = response.body().response;
                                maintenanceAdapter = new MaintenanceAdapter(maintenances);
                                recyclerView.setAdapter(maintenanceAdapter);
                            }
                        } catch (Exception ex) {
                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));
                        }
                    } else {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                    }
                }

                @Override
                public void onFailure(Call<WebAPIResponse<List<Maintenance>>> call, Throwable t) {

                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                }
            });
        } else {
            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

        }

        return view;
    }

}
