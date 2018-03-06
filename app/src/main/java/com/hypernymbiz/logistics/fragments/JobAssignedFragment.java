package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.LoadingDialog;
import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.models.JobDetail;
import com.hypernymbiz.logistics.models.PayloadNotification;
import com.hypernymbiz.logistics.models.StartJob;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;
import com.hypernymbiz.logistics.utils.GsonUtils;
import com.hypernymbiz.logistics.utils.LoginUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 02-Mar-18.
 */

public class JobAssignedFragment  extends Fragment {
    Context fContext;
    View view;
    SharedPreferences pref;
    Button btn_start, btn_cancel;
    String getUserAssociatedEntity;
    PayloadNotification payloadNotification;
    TextView jbname, jbstatus, jbstart, jbend, decrptin;
    private SimpleDialog mSimpleDialog;

    LoadingDialog dialog;
    SharedPreferences.Editor editor;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext = context;
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Job Assigned");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_assigned, container, false);
        btn_start = (Button) view.findViewById(R.id.btn_accept);
        btn_cancel = (Button) view.findViewById(R.id.btn_reject);
        jbname = (TextView) view.findViewById(R.id.txt_jobname);
        jbstatus = (TextView) view.findViewById(R.id.txt_status);
        jbstart = (TextView) view.findViewById(R.id.txt_starttime);
        jbend = (TextView) view.findViewById(R.id.txt_endtime);
        decrptin = (TextView) view.findViewById(R.id.txt_description);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getActivity());
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppUtils.isInternetAvailable(getActivity())) {
                    mSimpleDialog = new SimpleDialog(getContext(), getString(R.string.title_internet), getString(R.string.msg_internet),
                            getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.button_positive:
                                    mSimpleDialog.dismiss();
                                    ActivityUtils.startWifiSettings(getContext());
                                    break;
                                case R.id.button_negative:
                                    mSimpleDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    mSimpleDialog.show();

                } else
                    {
                    Intent getintent = getActivity().getIntent();
                    String id = getintent.getStringExtra("jobid");
                    HashMap<String, Object> body = new HashMap<>();

                    if (id != null) {
                        body.put("job_id", Integer.parseInt(id));
                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                        body.put("flag", 75);
                    }
                    else
                    {
                        body.put("job_id", payloadNotification.job_id);
                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                        body.put("flag", 75);

                    }
                    ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                        @Override
                        public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                            if (response.isSuccessful()) {
                                try {

                                }
                                catch (Exception ex)
                                {
                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                                }
                            }
                            else {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                            }

                        }

                        @Override
                        public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {
                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                        }
                    });

                    FrameActivity frameActivity = (FrameActivity) getActivity();
                    frameActivity.onBackPressed();

                }
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!AppUtils.isInternetAvailable(getActivity())) {
                    mSimpleDialog = new SimpleDialog(getContext(), getString(R.string.title_internet), getString(R.string.msg_internet),
                            getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.button_positive:
                                    mSimpleDialog.dismiss();
                                    ActivityUtils.startWifiSettings(getContext());
                                    break;
                                case R.id.button_negative:
                                    mSimpleDialog.dismiss();
                                    break;
                            }
                        }
                    });
                    mSimpleDialog.show();

                } else {
                    Intent getintent = getActivity().getIntent();
                    String id = getintent.getStringExtra("jobid");
                    HashMap<String, Object> body = new HashMap<>();
                    if (id != null) {
                        body.put("job_id", Integer.parseInt(id));
                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                        body.put("flag", 74);
                    }
                    else {
                        body.put("job_id", payloadNotification.job_id);
                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                        body.put("flag", 74);
                    }
                    ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                        @Override
                        public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                            if (response.isSuccessful()) {
                                try {

                                }
                                catch (Exception ex)
                                {
                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                                }
                            }
                            else {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                            }

                        }

                        @Override
                        public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {
                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                        }
                    });

                    FrameActivity frameActivity = (FrameActivity) getActivity();
                    frameActivity.onBackPressed();

                }
            }
        });

        Bundle extras = getActivity().getIntent().getExtras();
        Bundle value = null;
        if (extras != null) {
            value = extras.getBundle(Constants.DATA);
            if (value != null) {
                payloadNotification = GsonUtils.fromJson(value.getString(Constants.PAYLOAD), PayloadNotification.class);

                ApiInterface.retrofit.getalldata(payloadNotification.job_id).enqueue(new Callback<WebAPIResponse<JobDetail>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<JobDetail>> call, Response<WebAPIResponse<JobDetail>> response) {
                        if (response.isSuccessful()) {
//                            dialog.dismiss();
                            try {
                                if (response.body().status) {
                                    jbname.setText(response.body().response.getName());
                                    jbstatus.setText(response.body().response.getJobStatus());
                                    jbstart.setText(AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime()));
                                    jbend.setText(AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime()));
                                    decrptin.setText(response.body().response.getDescription());
                                    String status = response.body().response.getJobStatus();
                                    if (status.equals("Pending")) {
                                        btn_start.setVisibility(View.VISIBLE);
                                        btn_cancel.setVisibility(View.VISIBLE);
                                    }
//                                else if (status.equals("Failed")) {
//                                    btn_start.setVisibility(View.GONE);
//                                    btn_cancel.setVisibility(View.GONE);
//
//                                }

                                }
                            } catch (Exception ex) {

                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                            }
                        } else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }

                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<JobDetail>> call, Throwable t) {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                    }
                });

            }

        }

        return view;
    }



}
