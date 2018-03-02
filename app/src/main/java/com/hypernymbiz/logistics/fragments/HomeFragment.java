package com.hypernymbiz.logistics.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.adapter.ExpandableAdapter;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.LoadingDialog;
import com.hypernymbiz.logistics.models.ItemModel;
import com.hypernymbiz.logistics.models.JobCount;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.AppUtils;
import com.hypernymbiz.logistics.utils.Constants;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shamis on 10/12/2017.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    private ViewHolder mHolder;
    private Toolbar mToolbar;
    private Context mContext;
    SharedPreferences pref;
    CameraUpdate update;
    Location l;

    private GoogleMap googleMap;
    LocationManager locationManager;
    MapView mMapView;
    LatLng pos;
    String size;
    TextView mNumberOfCartItemsText;
    String Jobstart, Jobend, JobActualstart, JobActualend;
    LinearLayout linearLayout;
    ScrollView nestedScrollView;
    ExpandableLayout sectionLinearLayout;
    boolean status = true;
    LoadingDialog dialog;
    RecyclerView recyclerView;
    CardView cardView;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HomeFragment.class));
    }

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
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        linearLayout = (LinearLayout) view.findViewById(R.id.linear_error);
        nestedScrollView = (ScrollView) view.findViewById(R.id.layout_nestedview);
        cardView=(CardView)view.findViewById(R.id.cardviewhome_map);

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        };
        nestedScrollView.post(runnable);


//        nestedScrollView.post(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),0));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<ItemModel> data = new ArrayList<>();
        data.add(new ItemModel(true,"Assigned Time", Jobstart, Jobend, R.color.colorwhite, R.color.material_grey_300, Utils.createInterpolator(Utils.BOUNCE_INTERPOLATOR)));
//        data.add(new ItemModel("Driver Time", R.color.colorwhite, R.color.material_grey_300, Utils.createInterpolator(Utils.BOUNCE_INTERPOLATOR)));
        recyclerView.setAdapter(new ExpandableAdapter(data));


//        sectionLinearLayout = (ExpandableLayout) view.findViewById(R.id.layout_expandable);


        dialog = new LoadingDialog(getActivity(), getString(R.string.msg_loading));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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

//                        Log.d("TAAAG", "on location change");
                        l = (Location) location;
                        pos = new LatLng(l.getLatitude(), l.getLongitude());
//                        update = CameraUpdateFactory.newLatLngZoom(pos, 15.4f);
//                        googleMap.animateCamera(update);
                        // googleMap.addMarker(new MarkerOptions().position(pos).title("Driver Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15.4f));
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

//        sectionLinearLayout.animate();
//        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<ExpandableCategoryParent, ExpandableSubCategoryChild>() {
//            @Override
//            public void renderParent(View view, ExpandableCategoryParent model, boolean isExpanded, int parentPosition) {
//                ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
//
//                if (isExpanded) {
//
//                    view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.down_arrow);
//                } else
//                    view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.ic_up);
//
//
//            }
//
//            @Override
//            public void renderChild(View view, ExpandableSubCategoryChild model, int parentPosition, int childPosition) {
//                ((TextView) view.findViewById(R.id.label)).setText(model.getName());
//                ((TextView) view.findViewById(R.id.tvChild)).setText(model.getTime());
//
//            }
//        });

        return view;


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHolder = new ViewHolder(view);
//        sectionLinearLayout.setExpandListener(new ExpandCollapseListener.ExpandListener<Object>() {
//
//            @Override
//            public void onExpanded(int i, Object o, View view) {
//                view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.down_arrow);
//            }
//        });
//        sectionLinearLayout.setCollapseListener(new ExpandCollapseListener.CollapseListener<Object>() {
//
//            @Override
//            public void onCollapsed(int i, Object o, View view) {
//                view.findViewById(R.id.arrow).setBackgroundResource(R.drawable.ic_up);
//
//            }
//        });


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
        MapStyleOptions mapStyleOptions=MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.map);
        googleMap.setMapStyle(mapStyleOptions);

    }

    public static class ViewHolder {

        Button button;

        public ViewHolder(View view) {
            //   button = (Button) view.findViewById(R.id.button);

        }
    }

//    public Section<ExpandableCategoryParent, ExpandableSubCategoryChild> getsection(String ParentTitle, String StartTime, String EndTime) {
//        Section<ExpandableCategoryParent, ExpandableSubCategoryChild> section = new Section<>();
//        ExpandableCategoryParent phoneCategory = new ExpandableCategoryParent(ParentTitle);
//        List<ExpandableSubCategoryChild> list = new ArrayList<ExpandableSubCategoryChild>();
//        {
//            list.add(new ExpandableSubCategoryChild("Start Time:", StartTime));
//            list.add(new ExpandableSubCategoryChild("End Time: ", EndTime));
//            section.parent = phoneCategory;
//            section.children.addAll(list);
//
//        }
//        return section;
//    }

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
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().status) {
                        size = String.valueOf(response.body().response.get(0).getCount());
                        if (size != null) {
                            try {
                                mNumberOfCartItemsText.setText(size);
                            } catch (Exception ex) {

                            }
                        }

                    }

                } else {
                    dialog.dismiss();
                    AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), 2));

                }
                //   Toast.makeText(getActivity(),response.body().response.get(0).getCount(), Toast.LENGTH_SHORT).show();
//                mNumberOfCartItemsText.setText("000");
            }

            @Override
            public void onFailure(Call<WebAPIResponse<List<JobCount>>> call, Throwable t) {
                dialog.dismiss();
                AppUtils.showSnackBar(getView(), AppUtils.getErrorMessage(getContext(), Constants.NETWORK_ERROR));

            }
        });
        Jobstart = pref.getString("jobstart", "");
        Jobend = pref.getString("jobend", "");
        JobActualstart = pref.getString("actalstart", "");
        JobActualend = pref.getString("driverend", "");
        if (Jobstart.equals("")) {
            nestedScrollView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            if (status == true) {

                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),0));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                final List<ItemModel> data = new ArrayList<>();
                data.add(new ItemModel(true,"Assigned Time", Jobstart, Jobend, R.color.colorwhite, R.color.material_grey_300, Utils.createInterpolator(Utils.BOUNCE_INTERPOLATOR)));
                data.add(new ItemModel(false,"Driver Time", JobActualstart, "", R.color.colorwhite, R.color.material_grey_300, Utils.createInterpolator(Utils.BOUNCE_INTERPOLATOR)));
                recyclerView.setAdapter(new ExpandableAdapter(data));

//                sectionLinearLayout.addSection(getsection("Assigned Time ", Jobstart, Jobend));
//                sectionLinearLayout.addSection(getsection("Driver Time ", JobActualstart, JobActualend));
                status = false;
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
