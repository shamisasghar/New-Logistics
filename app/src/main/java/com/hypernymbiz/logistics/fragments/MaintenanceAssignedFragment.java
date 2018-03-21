package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.LoadingDialog;
import com.hypernymbiz.logistics.models.Maintenance;
import com.hypernymbiz.logistics.models.PayloadNotification;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.GsonUtils;
import com.hypernymbiz.logistics.utils.LoginUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 07-Mar-18.
 */

public class MaintenanceAssignedFragment extends Fragment {
    Context fContext;
    View view;
    String getUserAssociatedEntity;
    SharedPreferences pref;
    LoadingDialog dialog;
    PayloadNotification payloadNotification;
    TextView main_name, main_type, main_truck, main_time, main_status;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext = context;
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Maintenance Assigned");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maintenance_assigned, container, false);

        main_name = (TextView) view.findViewById(R.id.txt_maintenance_name);
        main_type = (TextView) view.findViewById(R.id.txt_maintenance_type);
        main_truck = (TextView) view.findViewById(R.id.txt_assigned_truck);
        main_time = (TextView) view.findViewById(R.id.txt_due_date);
        main_status = (TextView) view.findViewById(R.id.txt_status);


        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getActivity());
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);
        dialog = new LoadingDialog(getActivity(), getString(R.string.msg_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Bundle extras = getActivity().getIntent().getExtras();
        Bundle value = null;
        if (extras != null) {
            value = extras.getBundle(Constants.DATA);
            if (value != null) {
                payloadNotification = GsonUtils.fromJson(value.getString(Constants.PAYLOAD), PayloadNotification.class);
                ApiInterface.retrofit.getmaintenancedata(Integer.parseInt(getUserAssociatedEntity), payloadNotification.job_id).enqueue(new Callback<WebAPIResponse<Maintenance>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<Maintenance>> call, Response<WebAPIResponse<Maintenance>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().status) {

                                    main_name.setText(response.body().response.getMaintenanceName());
                                    main_type.setText(response.body().response.getMaintenanceType());
                                    main_truck.setText(response.body().response.getAssignedTruck());
                                    main_time.setText(AppUtils.getFormattedDate(response.body().response.getDueDate()) + " " + AppUtils.getTime(response.body().response.getDueDate()));
                                    main_status.setText(response.body().response.getStatus());
//                            String status = response.body().response.getJobStatus();
//                            if (status != null) {
//                                if (status.equals("Pending")||status.equals("Accepted")) {
//                                    btn_start.setVisibility(View.VISIBLE);
//                                    btn_cancel.setVisibility(View.VISIBLE);
//                                }
////                                    else if (status.equals("Failed")) {
////                                        btn_start.setVisibility(View.GONE);
////                                        btn_cancel.setVisibility(View.GONE);
////
////                                    }
//                            } else {
//                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                                } else {
                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                                }
                            } catch (Exception ex) {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));


                            }
                        } else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }
                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<Maintenance>> call, Throwable t) {
                        dialog.dismiss();
                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                    }
                });


            } else {
                Intent getintent = getActivity().getIntent();
                String id = getintent.getStringExtra("jobid");

                ApiInterface.retrofit.getmaintenancedata(Integer.parseInt(getUserAssociatedEntity), Integer.parseInt(id)).enqueue(new Callback<WebAPIResponse<Maintenance>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<Maintenance>> call, Response<WebAPIResponse<Maintenance>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().status) {

                                    main_name.setText(response.body().response.getMaintenanceName());
                                    main_type.setText(response.body().response.getMaintenanceType());
//                            main_truck.setText(response.body().response.get());
                                    main_time.setText(AppUtils.getFormattedDate(response.body().response.getDueDate()) + " " + AppUtils.getTime(response.body().response.getDueDate()));
                                    main_status.setText(response.body().response.getStatus());
//                            String status = response.body().response.getJobStatus();
//                            if (status != null) {
//                                if (status.equals("Pending")||status.equals("Accepted")) {
//                                    btn_start.setVisibility(View.VISIBLE);
//                                    btn_cancel.setVisibility(View.VISIBLE);
//                                }
////                                    else if (status.equals("Failed")) {
////                                        btn_start.setVisibility(View.GONE);
////                                        btn_cancel.setVisibility(View.GONE);
////
////                                    }
//                            } else {
//                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                                } else {
                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                                }
                            } catch (Exception ex) {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                            }
                        } else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }
                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<Maintenance>> call, Throwable t) {
                        dialog.dismiss();
                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                    }
                });

            }
        }
        return view;
    }
}


