package com.hypernymbiz.logistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypernymbiz.logistics.LoginActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.Profile;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.LoginUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 22-Jan-18.
 */

public class Profile_Fragment extends Fragment implements View.OnClickListener, ToolbarListener {
    private ViewHolder mHolder;
    TextView email, drivername, drivercnic,martialstatus,dof;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private SwipeRefreshLayout swipelayout;
    SharedPreferences pref;
    String getUserAssociatedEntity,Email,Driver_name,Driver_id,Driver_photo;
    CircleImageView img_profile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Profile");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        email = (TextView) view.findViewById(R.id.txt_Email);
        drivername = (TextView) view.findViewById(R.id.txt_drivername);
        drivercnic = (TextView) view.findViewById(R.id.txt_driverid);
        martialstatus = (TextView) view.findViewById(R.id.txt_gender);
        dof = (TextView) view.findViewById(R.id.txt_dateofjoin);
        img_profile=(CircleImageView) view.findViewById(R.id.img_driver_profile);
        swipelayout = (SwipeRefreshLayout) view.findViewById(R.id.layout_swipe);
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);

        Email = pref.getString("Email", "");
//        Driver_photo = pref.getString("Url", "");
//        Driver_name = pref.getString("Name", "");
//        Driver_id = pref.getString("Id", "");
        email.setText(Email);
//        Glide.with(getContext()).load(Driver_photo).into(img_profile);
//        drivername.setText(Driver_name);
//        drivercnic.setText(Driver_id);
        swipelayout();
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
        ApiInterface.retrofit.getprofile(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<Profile>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<Profile>> call, Response<WebAPIResponse<Profile>> response) {
                if (response.body().status!=null) {

                    String driverName, driverCnic,drivergender,driverdof;
                    String url=response.body().response.getPhoto();
                    driverName = response.body().response.getName();
                    driverCnic = String.valueOf(response.body().response.getCnic());
                    drivergender=response.body().response.getMaritalStatus();
                    driverdof=response.body().response.getDateOfJoining();
                    drivername.setText(driverName);
                    drivercnic.setText(driverCnic);
                    martialstatus.setText(drivergender);
                    dof.setText(driverdof);
                    Glide.with(getContext()).load(url).into(img_profile);
                }

            }

            @Override
            public void onFailure(Call<WebAPIResponse<Profile>> call, Throwable t) {

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
     //   EventBus.getDefault().post(new DrawerItemSelectedEvent(getString(R.string.drawer_profile)));
        mHolder.btn_sgnout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signout:
                LoginUtils.clearUser(getContext());
                startActivity(new Intent(this.getContext(), LoginActivity.class));
                getActivity().finish();
                break;
        }

    }

    @Override
    public void setTitle(String title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
      //  EventBus.getDefault().post(new DrawerItemSelectedEvent(getString(R.string.drawer_profile)));
    }

    public static class ViewHolder {

        Button btn_sgnout;

        public ViewHolder(View view) {
            btn_sgnout = (Button) view.findViewById(R.id.btn_signout);

        }
    }

    public void swipelayout() {
        swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipelayout.setRefreshing(true);


                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipelayout.setRefreshing(false);

                        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());
                        ApiInterface.retrofit.getprofile(Integer.parseInt(getUserAssociatedEntity)).enqueue(new Callback<WebAPIResponse<Profile>>() {
                            @Override
                            public void onResponse(Call<WebAPIResponse<Profile>> call, Response<WebAPIResponse<Profile>> response) {
                                if (response.body().status) {

                                    String driverName,driverId;

                                    driverName = response.body().response.getName();
                                    driverId = Integer.toString(response.body().response.getId());
                                    String url=response.body().response.getPhoto();
                                    drivername.setText(driverName);
//                                    driverid.setText(driverId);
//                                    Glide.with(getContext()).load(url).into(img_profile);

                                }
                            }

                            @Override
                            public void onFailure(Call<WebAPIResponse<Profile>> call, Throwable t) {

                                Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                sbView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorDialogToolbarText));
                                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                snackbar.show();
                            }
                        });
                    }
                }, 3000);
            }
        });


    }


}

