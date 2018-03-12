package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.LoadingDialog;
import com.hypernymbiz.logistics.models.JobDetail;
import com.hypernymbiz.logistics.models.Maintenance;
import com.hypernymbiz.logistics.models.MaintenanceUpdate;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 07-Mar-18.
 */

public class MaintenanceDetailFragment extends Fragment {
    Context fContext;
    View view;
    String getUserAssociatedEntity;
    SharedPreferences pref;
    LoadingDialog dialog;
    Button btn_complte;
    TextView main_name, main_type, main_truck, main_time, main_status;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext = context;
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Maintenance Detials");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_maintenance_detail, container, false);

        main_name = (TextView) view.findViewById(R.id.txt_maintenance_name);
        main_type = (TextView) view.findViewById(R.id.txt_maintenance_type);
        main_truck = (TextView) view.findViewById(R.id.txt_assigned_truck);
        main_time = (TextView) view.findViewById(R.id.txt_due_date);
        main_status = (TextView) view.findViewById(R.id.txt_status);
        btn_complte = (Button) view.findViewById(R.id.btn_complete);
        btn_complte.setVisibility(View.INVISIBLE);


        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getActivity());
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);
        dialog = new LoadingDialog(getActivity(), getString(R.string.msg_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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


                            String status = response.body().response.getStatus();
                            if (status != null) {
                                if (status.equals("due")) {
                                    btn_complte.setVisibility(View.VISIBLE);
                                }
//                                    else if (status.equals("Failed")) {
//                                        btn_start.setVisibility(View.GONE);
//                                        btn_cancel.setVisibility(View.GONE);
//
//                                    }
                            } else {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                            }
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

        btn_complte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> body = new HashMap<>();
                Intent getintent = getActivity().getIntent();
                String id = getintent.getStringExtra("jobid");
                if (id != null) {
                    body.put("m_id", Integer.parseInt(id));
                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                    body.put("flag", 72);
                }
                ApiInterface.retrofit.maintenanceupdate(body).enqueue(new Callback<WebAPIResponse<MaintenanceUpdate>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<MaintenanceUpdate>> call, Response<WebAPIResponse<MaintenanceUpdate>> response) {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().status) {

                                    Log.d("TAAAG", "" + response.body().response);
                                    Log.d("TAAAG", "" + response.body().status);

                                }

                            } catch (Exception ex) {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));


                            }
                        } else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }
                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<MaintenanceUpdate>> call, Throwable t) {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                    }
                });

                FrameActivity frameActivity = (FrameActivity) getActivity();
                frameActivity.onBackPressed();

           }
        });


        return view;
    }
}
