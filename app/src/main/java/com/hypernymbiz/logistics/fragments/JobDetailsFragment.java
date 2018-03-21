package com.hypernymbiz.logistics.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 21-Jan-18.
 */

public class JobDetailsFragment extends Fragment implements com.google.android.gms.location.LocationListener {
    View view;
    Context fContext;
    Button btn_start, btn_cancel;
    TextView jbname, jbstatus, jbstart, jbend, decrptin;
    String getUserAssociatedEntity, actual_start_time;
    private SwipeRefreshLayout swipelayout;
    PayloadNotification payloadNotification;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    LoadingDialog dialog;
    Calendar c;
    private SimpleDialog mSimpleDialog;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 1000;  /* 1 sec */
    private long FASTEST_INTERVAL = 500; /* 1/2 sec */
    public static int counter = 0;

    String driverlocation;
    LatLng ll;

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
        dialog = new LoadingDialog(getActivity(), getString(R.string.msg_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        startLocationUpdates();

        btn_start.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);

        c = Calendar.getInstance();
//        System.out.println("Current time =&gt; " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actual_start_time = df.format(c.getTime());


        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("gmt"));
//        String ss=


        actual_start_time = outputFmt.format(new Date());


        Log.d("TAAg",actual_start_time);

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
                    body.put("start_lat_long", driverlocation);
                } else {
                    body.put("job_id", payloadNotification.job_id);
                    body.put("actual_start_time", actual_start_time);
                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                    body.put("start_lat_long",driverlocation);
                }
                ApiInterface.retrofit.startjob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            try {

                                if (response.body().status) {
                                    String strlat, strlng, endlat, endlng;

                                    strlat = String.valueOf(response.body().response.getJobStartLat());
                                    strlng = String.valueOf(response.body().response.getJobStartLng());
                                    endlat = String.valueOf(response.body().response.getJobEndLat());
                                    endlng = String.valueOf(response.body().response.getJobEndLng());

                                    if (!strlat.equals("null") && !strlng.equals("null") && !endlat.equals("null") && !endlng.equals("null")) {
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
                                    String date = DateFormat.getDateTimeInstance().format(c.getTime());
                                    if (id != null) {
                                        editor = pref.edit();
                                        editor.putString("jobid", id);
                                        editor.putString("drivertime", actual_start_time);
                                        editor.commit();

//                                        intent.putExtra("jobid",id);
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("jobid",id);
//                                        ActivityUtils.startActivity(getActivity(),ActiveJobFragment.class,true);
                                        ActivityUtils.startActivity(getActivity(), FrameActivity.class, ActiveJobFragment.class.getName(), null);
                                        getActivity().finish();
                                    }
                                    else {
//                                    intent.putExtra("jobid", "" + payloadNotification.job_id);
                                        editor = pref.edit();
                                        editor.putString("jobid", "" + payloadNotification.job_id);
                                        editor.putString("drivertime", actual_start_time);
                                        editor.commit();
                                        Toast.makeText(getContext(), String.valueOf(payloadNotification.job_id), Toast.LENGTH_SHORT).show();
                                        ActivityUtils.startActivity(getActivity(), FrameActivity.class, ActiveJobFragment.class.getName(), null);
                                        getActivity().finish();
                                    }
                                }
                            }
                            catch (Exception ex)
                            {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));


                            }
                        }


                        else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }
                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<StartJob>> call, Throwable t) {
                        dialog.dismiss();
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
                    body.put("start_lat_long", ll);

                } else {
                    body.put("job_id", payloadNotification.job_id);
                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                    body.put("flag", 54);
                    body.put("start_lat_long", ll);
                }


                ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                        if (response.isSuccessful()) {
                            try {

                            } catch (Exception ex) {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

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
                        dialog.dismiss();
                        if (response.isSuccessful()) {

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
                                        btn_start.setText("Accept");
                                        btn_cancel.setText("Reject");
                                        btn_start.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent getintent = getActivity().getIntent();
                                                String id = getintent.getStringExtra("jobid");
                                                HashMap<String, Object> body = new HashMap<>();
                                                if (id != null) {
                                                    body.put("job_id", Integer.parseInt(id));
                                                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                    body.put("flag", 74);
                                                } else {
                                                    body.put("job_id", payloadNotification.job_id);
                                                    body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                    body.put("flag", 74);
                                                }
                                                ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                                                    @Override
                                                    public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                                                        if (response.isSuccessful()) {
                                                            try {

                                                            } catch (Exception ex) {
                                                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

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

                                                } else {
                                                    Intent getintent = getActivity().getIntent();
                                                    String id = getintent.getStringExtra("jobid");
                                                    HashMap<String, Object> body = new HashMap<>();

                                                    if (id != null) {
                                                        body.put("job_id", Integer.parseInt(id));
                                                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                        body.put("flag", 75);
                                                    } else {
                                                        body.put("job_id", payloadNotification.job_id);
                                                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                        body.put("flag", 75);

                                                    }
                                                    ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                                                        @Override
                                                        public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                                                            if (response.isSuccessful()) {
                                                                try {

                                                                } catch (Exception ex) {
                                                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

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
                                            }
                                        });

                                    } else if (status.equals("Accepted")) {
                                        btn_start.setVisibility(View.VISIBLE);
                                        btn_cancel.setVisibility(View.VISIBLE);
                                        btn_start.setText("Start Job");
                                        btn_cancel.setText("Cancel Job");
                                    }


//                                else if (status.equals("Failed")) {
//                                    btn_start.setVisibility(View.GONE);
//                                    btn_cancel.setVisibility(View.GONE);
//
//                                }
                                    String strttime, endtime;

                                    strttime = AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime());
                                    endtime = AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime());
                                    editor = pref.edit();
                                    editor.putString("Startjob", strttime);
                                    editor.putString("Startend", endtime);
                                    editor.commit();

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

            } else {
                Intent getintent = getActivity().getIntent();
                String id = getintent.getStringExtra("jobid");
                ApiInterface.retrofit.getalldata(Integer.parseInt(id)).enqueue(new Callback<WebAPIResponse<JobDetail>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<JobDetail>> call, Response<WebAPIResponse<JobDetail>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().status) {
                                    jbname.setText(response.body().response.getName());
                                    jbstatus.setText(response.body().response.getJobStatus());
                                    jbstart.setText(AppUtils.getFormattedDate(response.body().response.getJobStartDatetime()) + " " + AppUtils.getTime(response.body().response.getJobStartDatetime()));
                                    jbend.setText(AppUtils.getFormattedDate(response.body().response.getJobEndDatetime()) + " " + AppUtils.getTime(response.body().response.getJobEndDatetime()));
                                    decrptin.setText(response.body().response.getDescription());
                                    String status = response.body().response.getJobStatus();
                                    if (status != null) {
//                                        if (status.equals("Pending")||status.equals("Accepted")) {
//                                            btn_start.setVisibility(View.VISIBLE);
//                                            btn_cancel.setVisibility(View.VISIBLE);
//                                        }
                                        if (status.equals("Pending")) {
                                            btn_start.setVisibility(View.VISIBLE);
                                            btn_cancel.setVisibility(View.VISIBLE);
                                            btn_start.setText("Accept");
                                            btn_cancel.setText("Reject");
                                            btn_start.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent getintent = getActivity().getIntent();
                                                    String id = getintent.getStringExtra("jobid");
                                                    HashMap<String, Object> body = new HashMap<>();
                                                    if (id != null) {
                                                        body.put("job_id", Integer.parseInt(id));
                                                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                        body.put("flag", 74);
                                                    } else {
                                                        body.put("job_id", payloadNotification.job_id);
                                                        body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                        body.put("flag", 74);
                                                    }
                                                    ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                                                        @Override
                                                        public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                                                            if (response.isSuccessful()) {
                                                                try {

                                                                } catch (Exception ex) {
                                                                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

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

                                                    } else {
                                                        Intent getintent = getActivity().getIntent();
                                                        String id = getintent.getStringExtra("jobid");
                                                        HashMap<String, Object> body = new HashMap<>();

                                                        if (id != null) {
                                                            body.put("job_id", Integer.parseInt(id));
                                                            body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                            body.put("flag", 75);
                                                        } else {
                                                            body.put("job_id", payloadNotification.job_id);
                                                            body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                                                            body.put("flag", 75);

                                                        }
                                                        ApiInterface.retrofit.canceljob(body).enqueue(new Callback<WebAPIResponse<StartJob>>() {
                                                            @Override
                                                            public void onResponse(Call<WebAPIResponse<StartJob>> call, Response<WebAPIResponse<StartJob>> response) {

                                                                if (response.isSuccessful()) {
                                                                    try {

                                                                    } catch (Exception ex) {
                                                                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

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
                                                }
                                            });

                                        } else if (status.equals("Accepted")) {
                                            btn_start.setVisibility(View.VISIBLE);
                                            btn_cancel.setVisibility(View.VISIBLE);
                                            btn_start.setText("Start Job");
                                            btn_cancel.setText("Cancel Job");
                                        }


//                                    else if (status.equals("Failed")) {
//                                        btn_start.setVisibility(View.GONE);
//                                        btn_cancel.setVisibility(View.GONE);
//
//                                    }
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
                            } catch (Exception ex) {
                                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));


                            }
                        } else {

                            AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));
                        }
                    }

                    @Override
                    public void onFailure(Call<WebAPIResponse<JobDetail>> call, Throwable t) {
                        dialog.dismiss();
                        AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onLocationChanged(Location location) {
        ll = new LatLng(location.getLatitude(), location.getLongitude());
        Double lat,lng;

        lat=location.getLatitude();
        lng=location.getLongitude();
        driverlocation=lat.toString()+","+lng.toString();


//        longi = location.getLongitude();

//        Toast.makeText(getContext(), ""+driverlocation, Toast.LENGTH_SHORT).show();

    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        final LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        counter++;
                        try {
                            if (counter > 1) {
                                onLocationChanged(locationResult.getLastLocation());
                                LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                            }
                        } catch (Exception ex)
                        {

                        }
                    }
                },
                Looper.myLooper());
    }

}


