package com.hypernymbiz.logistics.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernymbiz.logistics.ActiveJobActivity;
import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 21-Jan-18.
 */

public class JobDetailsFragment extends Fragment {
    View view;
    Context fContext;
    Button btn_start, btn_cancel;
    TextView jbname, jbstatus, jbstart, jbend, decrptin;
    String getUserAssociatedEntity, actual_start_time;
    private SwipeRefreshLayout swipelayout;
    PayloadNotification payloadNotification;
    SharedPreferences.Editor editor;
    SharedPreferences pref;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fContext = context;
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Job Details");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_job_details, container, false);

        btn_start = (Button) view.findViewById(R.id.btn_startjob);
        btn_cancel = (Button) view.findViewById(R.id.btn_canceljob);
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
        jbname = (TextView) view.findViewById(R.id.txt_jobname);
        jbstatus = (TextView) view.findViewById(R.id.txt_status);
        jbstart = (TextView) view.findViewById(R.id.txt_starttime);
        jbend = (TextView) view.findViewById(R.id.txt_endtime);
        decrptin = (TextView) view.findViewById(R.id.txt_description);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getActivity());
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actual_start_time = df.format(c.getTime());

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getintent = getActivity().getIntent();
                String id = getintent.getStringExtra("jobid");
                HashMap<String, Object> body = new HashMap<>();
                if (id != null) {
                    body.put("job_id", Integer.parseInt(id));
                    body.put("actual_start_time", actual_start_time);
                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                } else {
                    body.put("job_id", payloadNotification.job_id);
                    body.put("actual_start_time", actual_start_time);
                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                }

                ApiInterface.retrofit.startjob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().status) {
                                String strlat, strlng, endlat, endlng;

                                strlat = String.valueOf(response.body().response.getJobStartLat());
                                strlng = String.valueOf(response.body().response.getJobStartLng());
                                endlat = String.valueOf(response.body().response.getJobEndLat());
                                endlng = String.valueOf(response.body().response.getJobEndLng());

                                if (!strlat.equals("") && !strlng.equals("") && !endlat.equals("") && !endlng.equals("")) {
                                    editor.putString("Startlat", strlat);
                                    editor.putString("Startlng", strlng);
                                    editor.putString("Endlat", endlat);
                                    editor.putString("Endlng", endlng);
                                    editor.putString("Actualstart", actual_start_time);
                                    editor.commit();
                                } else {

                                    Snackbar snackbar = Snackbar.make(swipelayout, "Missing Route Parameters", Snackbar.LENGTH_SHORT);
                                    View sbView = snackbar.getView();
                                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                    sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDialogToolbarText));
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    snackbar.show();

                                }
                                // Toast.makeText(fContext, "hhh", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), ActiveJobFragment.class);
                                Intent getintent = getActivity().getIntent();
                                String id = getintent.getStringExtra("jobid");
                                if (id != null) {
                                    editor = pref.edit();
                                    editor.putString("jobid", id);
                                    editor.commit();

//                                        intent.putExtra("jobid",id);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("jobid",id);
//                                        ActivityUtils.startActivity(getActivity(),ActiveJobFragment.class,true);
                                    ActivityUtils.startActivity(getActivity(), FrameActivity.class, ActiveJobFragment.class.getName(), null);
                                    getActivity().finish();
                                } else {
                                    intent.putExtra("jobid", "" + payloadNotification.job_id);
                                    Toast.makeText(getContext(), String.valueOf(payloadNotification.job_id), Toast.LENGTH_SHORT).show();
                                    ActivityUtils.startActivity(getActivity(), FrameActivity.class, ActiveJobFragment.class.getName(), null);
                                    getActivity().finish();
                                }
                            }
                        } else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }
                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {

                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                    }
                });


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getintent = getActivity().getIntent();
                String id = getintent.getStringExtra("jobid");
                HashMap<String, Object> body = new HashMap<>();
                if (id != null) {
                    body.put("job_id", Integer.parseInt(id));
                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                    body.put("flag", 54);
                }
                ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                        if (response.isSuccessful()) {
                            if (response.body().status) {

                            }

                        } else {

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
                            if (response.body().status) {
                                jbname.setText(response.body().response.getName());
                                jbstatus.setText(response.body().response.getJobStatus());
                                jbstart.setText(AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime()));
                                jbend.setText(AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime()));
                                decrptin.setText(response.body().response.getDescription());
                                String status = response.body().response.getJobStatus();
                                if (status.equals("Accomplished")) {
                                    btn_start.setVisibility(View.GONE);
                                    btn_cancel.setVisibility(View.GONE);
                                } else if (status.equals("Failed")) {
                                    btn_start.setVisibility(View.GONE);
                                    btn_cancel.setVisibility(View.GONE);

                                }
                                String strttime, endtime;

                                strttime = AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime());
                                endtime = AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime());
                                editor = pref.edit();
                                editor.putString("Startjob", strttime);
                                editor.putString("Startend", endtime);
                                editor.commit();
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

            } else {
                Intent getintent = getActivity().getIntent();
                String id = getintent.getStringExtra("jobid");
                ApiInterface.retrofit.getalldata(Integer.parseInt(id)).enqueue(new Callback<WebAPIResponse<JobDetail>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<JobDetail>> call, Response<WebAPIResponse<JobDetail>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().status) {
                                jbname.setText(response.body().response.getName());
                                jbstatus.setText(response.body().response.getJobStatus());
                                jbstart.setText(AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime()));
                                jbend.setText(AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime()));
                                decrptin.setText(response.body().response.getDescription());
                                String status = response.body().response.getJobStatus();
                                if (status != null) {
                                    if (status.equals("Accomplished")) {
                                        btn_start.setVisibility(View.GONE);
                                        btn_cancel.setVisibility(View.GONE);
                                    } else if (status.equals("Failed")) {
                                        btn_start.setVisibility(View.GONE);
                                        btn_cancel.setVisibility(View.GONE);

                                    }
                                } else {
                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                                }
                                String strttime, endtime;

                                strttime = AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime());
                                endtime = AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime());
                                editor = pref.edit();
                                editor.putString("Startjob", strttime);
                                editor.putString("Startend", endtime);
                                editor.commit();
                            } else {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

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
