package com.hypernymbiz.logistics;

import android.app.Dialog;
import android.content.Context;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hypernymbiz.logistics.api.ApiInterface;
import com.hypernymbiz.logistics.dialog.SimpleDialog;
import com.hypernymbiz.logistics.models.AssignedTime;
import com.hypernymbiz.logistics.models.DirectionsJSONParser;
import com.hypernymbiz.logistics.models.JobEnd;
import com.hypernymbiz.logistics.models.Time;
import com.hypernymbiz.logistics.models.WebAPIResponse;
import com.hypernymbiz.logistics.utils.LoginUtils;
import com.shitij.goyal.slidebutton.SwipeButton;

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

/**
 * Created by Metis on 23-Jan-18.
 */

public class ActiveJobActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    //    private List<AssestData> assestData = new ArrayList<>();
//    private RecyclerView.Adapter adapter;
//    RecyclerView recyclerView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_job);
        pref = getApplicationContext().getSharedPreferences("TAG", MODE_PRIVATE);
        mMapView = (MapView) findViewById(R.id.mapView);
        digitSpeedView = (DigitSpeedView) findViewById(R.id.digit_speed_view1);
        //  strttime=(TextView)findViewById(R.id.txt_starttime);
//        endtime=(TextView)findViewById(R.id.txt_jobendtime);
//        actual_start=(TextView)findViewById(R.id.txt_actual_time);
       // actual_start.setText(pref.getString("Actualstart", ""));
        //strttime.setText(pref.getString("Startjob", ""));
        //endtime.setText(pref.getString("Startend", ""));

        dig_dis=(TextView)findViewById(R.id.txt_dialog_actualend);
        dig_vol=(TextView)findViewById(R.id.txt_dialog_actualend);

        swipelayout = (SwipeRefreshLayout)findViewById(R.id.layout_swipe);
        swipeButton = (ProSwipeButton) findViewById(R.id.btn_slide);
        info_img = (ImageView) findViewById(R.id.info);
        getUserAssociatedEntity = LoginUtils.getUserAssociatedEntity(getApplicationContext());
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        actual_end_time = df.format(c.getTime());

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        Expandable();

        map();

//        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setNestedScrollingEnabled(false);
//
//        adapter=new AssetsAdapter(assestData,this);
//        adapter.notifyItemInserted(0);
//        recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount());
//        recyclerView.setAdapter(adapter);  //


        info_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info = new Dialog(ActiveJobActivity.this);
                info.setContentView(R.layout.dialog_truck_info);
                info.show();
                info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//                btn_cls.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(ActiveJobActivity.this, "hllo", Toast.LENGTH_SHORT).show();
//                    }
//                });
                btn_cls = (Button) info.findViewById(R.id.btn_close);
                btn_cls.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info.hide();
                    }
                });
            }
        });

        swipeButton.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeButton.showResultIcon(true);
                    }
                }, 2000);

                HashMap<String, Object> body = new HashMap<>();
                body.put("job_id",13);
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

                summary = new Dialog(ActiveJobActivity.this);
                summary.setContentView(R.layout.dialog_summary_detail);

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
                        finish();
                    }
                });


            }
        });

//        swipeButton.addOnSwipeCallback(new SwipeButton.Swipe() {
//            @Override
//            public void onButtonPress() {
//
//            }
//
//            @Override
//            public void onSwipeCancel() {
//            }
//
//            @Override
//            public void onSwipeConfirm() {
//
//
//                HashMap<String, Object> body = new HashMap<>();
//                body.put("job_id",13);
//                body.put("driver_id", Integer.parseInt(getUserAssociatedEntity));
//                body.put("actual_end_time", actual_end_time);
//
//
//
//
//                ApiInterface.retrofit.endjob(body).enqueue(new Callback<WebAPIResponse<JobEnd>>() {
//                    @Override
//                    public void onResponse(Call<WebAPIResponse<JobEnd>> call, Response<WebAPIResponse<JobEnd>> response) {
////                        if (response.body().status) {
//
//
////                    }
//                    }
//                    @Override
//                    public void onFailure(Call<WebAPIResponse<JobEnd>> call, Throwable t) {
//
////                        Snackbar snackbar = Snackbar.make(swipelayout, "Establish Network Connection!", Snackbar.LENGTH_SHORT);
////                        View sbView = snackbar.getView();
////                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
////                        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
////                        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDialogToolbarText));
////                        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
////                        snackbar.show();
//                    }
//                });
//
//
//                summary = new Dialog(ActiveJobActivity.this);
//                summary.setContentView(R.layout.dialog_summary_detail);
//
//                dig_actend=(TextView)summary.findViewById(R.id.txt_dialog_actualend);
//                btn_okk = (Button) summary.findViewById(R.id.btn_ok);
//                dig_actstrt=(TextView)summary.findViewById(R.id.txt_dialog_actualstart);
//                dig_strt=(TextView)summary.findViewById(R.id.txt_dialog_starttime);
//                dig_end=(TextView)summary.findViewById(R.id.txt_dialog_endtime);
//
//                summary.show();
//                summary.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//                dig_actend.setText(actual_end_time);
//                dig_actstrt.setText(pref.getString("Actualstart", ""));
//                dig_strt.setText(pref.getString("Startjob", ""));
//                dig_end.setText(pref.getString("Startend", ""));
//
//                btn_okk.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        summary.hide();
//                        finish();
//                    }
//                });
//
//            }
//        });
        //    add();

    }

    @Override
    public void onBackPressed() {
        mSimpleDialog = new SimpleDialog(this, null, getString(R.string.msg_end_job),getString(R.string.button_cancel), getString(R.string.button_ok), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button_positive:
                        mSimpleDialog.dismiss();
                        finish();
                        break;
                    case R.id.button_negative:
                        mSimpleDialog.dismiss();
                        break;
                }
            }
        });
        mSimpleDialog.show();

    }

//    public void add()
//    {
//        AssestData listitem=new AssestData("abc","Vehicle");
//        assestData.add(listitem);
//        AssestData listitem1=new AssestData("abc","Vehicle");
//        assestData.add(listitem1);
//        AssestData listite2=new AssestData("abc","Vehicle");
//        assestData.add(listite2);
//
//    }

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


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng pos;

                    int currentspeed = (int) ((location.getSpeed() * 3600) / 1000);


                    if (currentspeed <= 50) {
                        digitSpeedView.updateSpeed(currentspeed);
                        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                    } else {

                        digitSpeedView.updateSpeed(currentspeed);
                        //   digit.hideUnit();
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                    }

                    Location l = (Location) location;
                    pos = new LatLng(l.getLatitude(), l.getLongitude());
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (googleMap != null) {
                        googleMap.setMyLocationEnabled(true);
                        //  googleMap.setTrafficEnabled(true);
                        LatLng start = new LatLng(slat, slng);
                        googleMap.addMarker(new MarkerOptions().position(start).title("Start Location")).showInfoWindow();
                        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(start));
                        LatLng dest = new LatLng(elat, elng);
                        googleMap.addMarker(new MarkerOptions().position(dest).title("Destination Location")).showInfoWindow();
                        String url = getDirectionsUrl(start, dest);
                        FetchUrl FetchUrl = new FetchUrl();
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

            ParserTask parserTask = new ParserTask();

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
                lineOptions.color(Color.RED);
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
        ExpandableLayout sectionLinearLayout = (ExpandableLayout) findViewById(R.id.layout_expandable);

        sectionLinearLayout.setRenderer(new ExpandableLayout.Renderer<AssignedTime,Time>()
        {
            @Override
            public void renderParent(View view, AssignedTime model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tvParent)).setText(model.name);
                view.findViewById(R.id.arrow).setBackgroundResource(isExpanded ? R.drawable.up_arrow : R.drawable.up_arrow);
            }

            @Override
            public void renderChild(View view, Time model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tvChild)).setText(model.name);

            }
        });

        sectionLinearLayout.addSection(getsection());
        sectionLinearLayout.addSection(getsection());

    }

    public Section<AssignedTime,Time> getsection() {
        Section<AssignedTime, Time> section = new Section<>();
        AssignedTime phoneCategory=new AssignedTime("Assigned Time");
        List<Time> list=new ArrayList<Time>();
        {
            for (int i=0;i<=5;i++)

                list.add(new Time("21313"+i));
            section.parent=phoneCategory;
            section.children.addAll(list);

        }
        return section;
    }

}