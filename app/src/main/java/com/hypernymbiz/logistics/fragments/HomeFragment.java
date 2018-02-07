package com.hypernymbiz.logistics.fragments;


import android.Manifest;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.models.ExpandableCategoryParent;
import com.hypernymbiz.logistics.models.ExpandableSubCategoryChild;
import com.hypernymbiz.logistics.models.JobCount;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Bilal Rashid on 10/10/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    private ViewHolder mHolder;
    private Toolbar mToolbar;
    private Context mContext;
    SharedPreferences pref;
    Location l;
    public static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 2;
    private GoogleMap googleMap;
    LocationManager locationManager;
    MapView mMapView;
    LatLng pos;
    String size;
    TextView mNumberOfCartItemsText;
    String Jobstart, Jobend, JobActualstart, JobActualend;
    LinearLayout linearLayout;
    NestedScrollView nestedScrollView;
    ExpandableLayout sectionLinearLayout;
    boolean status=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
//        if (!EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ToolbarListener) {
            ((ToolbarListener) context).setTitle("Home");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        View view = menu.findItem(R.id.notification_bell).getActionView();
        mNumberOfCartItemsText = (TextView) view.findViewById(R.id.text_number_of_cart_items);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(getActivity(), FrameActivity.class, JobNotificationFragment.class.getName(), null);

            }
        });
//        ImageView cartImage = (ImageView) view.findViewById(R.id.image_cart);
//        cartImage.setColorFilter(ContextCompat.getColor(this, R.color.colorToolbarIcon));

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        linearLayout=(LinearLayout) view.findViewById(R.id.linear_error);
        nestedScrollView=(NestedScrollView) view.findViewById(R.id.layout_nestedview);
        sectionLinearLayout = (ExpandableLayout) view.findViewById(R.id.layout_expandable);




//        Jobstart=  pref.getString("jobstart","");
//        Jobend= pref.getString("jobend","");
//        JobActualstart= pref.getString("actalstart","");
//        JobActualend=pref.getString("actalend","");
//
//      if(Jobstart.equals(""))
//      {
//          nestedScrollView.setVisibility(View.GONE);
//          linearLayout.setVisibility(View.VISIBLE);
//
//      }
//      else
//      {
//          nestedScrollView.setVisibility(View.VISIBLE);
//          linearLayout.setVisibility(View.GONE);
//
//      }



        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        Log.d("TAAAG", "on location change");
                        l = (Location) location;
                        pos = new LatLng(l.getLatitude(), l.getLongitude());
                        // googleMap.addMarker(new MarkerOptions().position(pos).title("Driver Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.4f));
//                        googleMap.setTrafficEnabled(true);

                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                });

            }
        }


        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);



        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<ExpandableCategoryParent, ExpandableSubCategoryChild>() {
            @Override
            public void renderParent(View view, ExpandableCategoryParent model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.up_arrow : R.drawable.up_arrow);
            }

            @Override
            public void renderChild(View view, ExpandableSubCategoryChild model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.label)).setText(model.getName());
                ((TextView) view.findViewById(R.id.tvChild)).setText(model.getTime());

            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
        //    mHolder.button.setOnClickListener(this);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ActivityUtils.startActivity(getActivity(), FrameActivity.class, HomeFragment.class.getName(), null);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.googleMap.setMyLocationEnabled(true);


    }

    public static class ViewHolder {

        Button button;

        public ViewHolder(View view) {
            //   button = (Button) view.findViewById(R.id.button);

        }

    }

    public Section<ExpandableCategoryParent, ExpandableSubCategoryChild> getsection(String ParentTitle, String StartTime, String EndTime) {
        Section<ExpandableCategoryParent, ExpandableSubCategoryChild> section = new Section<>();
        ExpandableCategoryParent phoneCategory = new ExpandableCategoryParent(ParentTitle);
        List<ExpandableSubCategoryChild> list = new ArrayList<ExpandableSubCategoryChild>();
        {
            list.add(new ExpandableSubCategoryChild("Start Time:", StartTime));
            list.add(new ExpandableSubCategoryChild("End Time:", EndTime));
            section.parent = phoneCategory;
            section.children.addAll(list);

        }
        return section;
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && googleMap != null) {

            googleMap.setMyLocationEnabled(true);
            googleMap.setTrafficEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        ApiInterface.retrofit.getcount().enqueue(new Callback<WebAPIResponse<List<JobCount>>>() {
            @Override
            public void onResponse(Call<WebAPIResponse<List<JobCount>>> call, Response<WebAPIResponse<List<JobCount>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().status) {


                        size = String.valueOf(response.body().response.get(0).getCount());
                        if(size!=null) {
                            try {
                                mNumberOfCartItemsText.setText(size);
                            }
                            catch (Exception ex) {

                            }
                        }

                    }

                } else {

                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                }
                //   Toast.makeText(getActivity(),response.body().response.get(0).getCount(), Toast.LENGTH_SHORT).show();
//                mNumberOfCartItemsText.setText("000");
            }

            @Override
            public void onFailure(Call<WebAPIResponse<List<JobCount>>> call, Throwable t) {
                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

            }
        });

        Jobstart=  pref.getString("jobstart","");
        Jobend= pref.getString("jobend","");
        JobActualstart= pref.getString("actalstart","");
        JobActualend=pref.getString("actalend","");

        if(Jobstart.equals(""))
        {
            nestedScrollView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }

        else
        {
//
            if(status==true)

            {
                sectionLinearLayout.addSection(getsection("Assigned Time ", Jobstart, Jobend));
                sectionLinearLayout.addSection(getsection("Driver Time ", JobActualstart, JobActualend));
                status=false;
            }
                nestedScrollView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);


        }



        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
