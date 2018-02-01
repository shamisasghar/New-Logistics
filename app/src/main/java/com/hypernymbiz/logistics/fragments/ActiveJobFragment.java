package com.hypernymbiz.logistics.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hypernymbiz.logistics.ActiveJobActivity;
import com.hypernymbiz.logistics.FrameActivity;
import com.hypernymbiz.logistics.HomeActivity;
import com.hypernymbiz.logistics.R;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.models.DirectionsJSONParser;
import com.hypernymbiz.logistics.models.ExpandableCategoryParent;
import com.hypernymbiz.logistics.models.ExpandableSubCategoryChild;
import com.hypernymbiz.logistics.models.JobEnd;
import com.hypernymbiz.logistics.models.PayloadNotification;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.toolbox.ToolbarListener;
import com.hypernymbiz.logistics.utils.ActivityUtils;
import com.hypernymbiz.logistics.utils.LoginUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;
import in.shadowfax.proswipebutton.ProSwipeButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Metis on 01-Feb-18.
 */

public class ActiveJobFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    ImageView info_img;
    Dialog summary,info;
    ProSwipeButton swipeButton;
    Button btn_okk,btn_cls;
    MapView mMapView;
    SharedPreferences pref;
    private GoogleMap googleMap;
    LocationManager locationManager;
    Double slat, slng, elat, elng;
    DigitSpeedView digitSpeedView;
    String getUserAssociatedEntity ,actual_end_time;
    private SimpleDialog mSimpleDialog;
    TextView strttime,endtime,actual_start,dig_strt,dig_end,dig_actstrt,dig_actend,dig_dis,dig_vol;
    SharedPreferences.Editor editor;
    private SwipeRefreshLayout swipelayout;
    Dialog dialog_summary;
    String Jobstart,Jobend,JobActualstart,JobActualend;
    PayloadNotification payloadNotification;
    View view;

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
            ((ToolbarListener) context).setTitle("Active Job");
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_active_job, menu);
        View view = menu.findItem(R.id.info_truck).getActionView();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = new Dialog(getContext());
                info.setContentView(R.layout.dialog_truck_info);
                info.show();
                info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btn_cls = (Button) info.findViewById(R.id.btn_close);
                btn_cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.hide();
                    }
                });


            }
        });
//        ImageView cartImage = (ImageView) view.findViewById(R.id.image_cart);
//        cartImage.setColorFilter(ContextCompat.getColor(this, R.color.colorToolbarIcon));

    }







    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_active_job, container, false);
        pref = getActivity().getSharedPreferences("TAG", MODE_PRIVATE);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        digitSpeedView = (DigitSpeedView) view.findViewById(R.id.digit_speed_view1);
        dig_dis=(TextView)view.findViewById(R.id.txt_dialog_actualend);
        dig_vol=(TextView)view.findViewById(R.id.txt_dialog_actualend);
        swipeButton = (ProSwipeButton) view.findViewById(R.id.btn_slide);
        info_img = (ImageView) view.findViewById(R.id.info);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getContext());

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        Expandable();
        map();
//        info_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                info = new Dialog(getContext());
//                info.setContentView(R.layout.dialog_truck_info);
//                info.show();
//                info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                btn_cls = (Button) info.findViewById(R.id.btn_close);
//                btn_cls.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        info.hide();
//                    }
//                });
//            }
//        });

        swipeButton.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            swipeButton.showResultIcon(true);}
                        catch (Exception ex){}
                    }
                }, 2000);

                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; " + c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                actual_end_time = df.format(c.getTime());
                Intent getintent=getActivity().getIntent();
                String  id = getintent.getStringExtra("jobid");
                HashMap<String, Object> body = new HashMap<>();

                body.put("job_id",id);
                body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
                body.put("actual_end_time", actual_end_time);
                ApiInterface.retrofit.endjob(body).enqueue(new Callback<WebAPIResponse<JobEnd>>() {
                    @Override
                    public void onResponse(Call<WebAPIResponse<JobEnd>> call, Response<WebAPIResponse<JobEnd>> response) {
//                        if (response.body().status) {
//                    }
                    }
                    @Override
                    public void onFailure(Call<WebAPIResponse<JobEnd>> call, Throwable t) {

//                        Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
//                        View sbView = snackbar.getView();
//                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
//                        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDialogToolbarText));
//                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                        snackbar.show();
                    }
                });

                summary = new Dialog(getContext());
                summary.setContentView(R.layout.dialog_summary_detail);
                summary.setCanceledOnTouchOutside(false);
                dig_actend=(TextView)summary.findViewById(R.id.txt_dialog_actualend);
                btn_okk = (Button) summary.findViewById(R.id.btn_ok);
                dig_actstrt=(TextView)summary.findViewById(R.id.txt_dialog_actualstart);
                dig_strt=(TextView)summary.findViewById(R.id.txt_dialog_starttime);
                dig_end=(TextView)summary.findViewById(R.id.txt_dialog_endtime);

                summary.show();
                summary.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dig_actend.setText(actual_end_time);
                dig_actstrt.setText(pref.getString("Actualstart", ""));
                dig_strt.setText(pref.getString("Startjob", ""));
                dig_end.setText(pref.getString("Startend", ""));

                btn_okk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        summary.hide();
//                        finish();

                        ActivityUtils.startActivity(getActivity(),HomeActivity.class,true);
                    }
                });


            }
        });




        return view;
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

            this.googleMap = googleMap;

    }

    public void map() {
        slat = Double.parseDouble(pref.getString("Startlat", ""));
        slng = Double.parseDouble(pref.getString("Startlng", ""));
        elat = Double.parseDouble(pref.getString("Endlat", ""));
        elng = Double.parseDouble(pref.getString("Endlng", ""));


        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng pos;

                int currentspeed = (int) ((location.getSpeed() * 3600) / 1000);


                if (currentspeed <= 50) {
                    digitSpeedView.updateSpeed(currentspeed);

                } else {

                    digitSpeedView.updateSpeed(currentspeed);
                    //   digit.hideUnit();

                }

                Location l = (Location) location;
                pos = new LatLng(l.getLatitude(), l.getLongitude());
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    //  googleMap.setTrafficEnabled(true);
                    LatLng start = new LatLng(slat, slng);
                    googleMap.addMarker(new MarkerOptions().position(start).title("Start Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))).showInfoWindow();
                    //googleMap.animateCamera(CameraUpdateFactory.newLatLng(start));
                    LatLng dest = new LatLng(elat, elng);
                    googleMap.addMarker(new MarkerOptions().position(dest).title("Destination Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))).showInfoWindow();
                    String url = getDirectionsUrl(start, dest);
                    FetchUrl FetchUrl = new  FetchUrl();
                    FetchUrl.execute(url);


                }
//                 googleMap.addMarker(new MarkerOptions().position(pos).title("Driver Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.maptruck)));

            }

            private String getDirectionsUrl(LatLng start, LatLng dest) {

                // Origin of route
                String str_origin = "origin=" + start.latitude + "," + start.longitude;

                // Destination of route
                String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

                // Sensor enabled
                String sensor = "sensor=false";

                // Building the parameters to the web service
                String parameters = str_origin + "&" + str_dest + "&" + sensor;

                // Output format
                String output = "json";

                // Building the url to the web service
                String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

                return url;
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

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {

            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ActiveJobFragment.ParserTask parserTask = new ActiveJobFragment.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(R.color.colorPrimary);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                googleMap.addPolyline(lineOptions);
            } else {
            }
        }
    }

    public void Expandable()
    {
        ExpandableLayout sectionLinearLayout = (ExpandableLayout) view.findViewById(R.id.layout_expandable);

        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<ExpandableCategoryParent,ExpandableSubCategoryChild>()
        {
            @Override
            public void renderParent(View view, ExpandableCategoryParent model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.up_arrow : R.drawable.up_arrow);
            }

            @Override
            public void renderChild(View view, ExpandableSubCategoryChild model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.label)).setText(model.getName());
                ((TextView)view.findViewById(R.id.tvChild)).setText(model.getTime());

            }
        });
        Jobstart=pref.getString("Startjob", "");
        Jobend=pref.getString("Startend", "");
        JobActualstart=pref.getString("Actualstart", "");
        JobActualend=actual_end_time;

        sectionLinearLayout.addSection(getsection("Assigned Time ",Jobstart,Jobend));
        sectionLinearLayout.addSection(getsection("Driver Time ",JobActualstart,JobActualend));

    }

    public Section<ExpandableCategoryParent, ExpandableSubCategoryChild> getsection(String ParentTitle, String StartTime, String EndTime) {
        Section<ExpandableCategoryParent, ExpandableSubCategoryChild> section = new Section<>();
        ExpandableCategoryParent phoneCategory = new ExpandableCategoryParent(ParentTitle);
        List<ExpandableSubCategoryChild> list = new ArrayList<ExpandableSubCategoryChild>();
        {

            list.add(new ExpandableSubCategoryChild("Start Time:",StartTime));
            list.add(new ExpandableSubCategoryChild("End Time:",EndTime));
            section.parent = phoneCategory;
            section.children.addAll(list);

        }
        return section;
    }
    @Override
    public void onResume() {
        mMapView.onResume();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
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
