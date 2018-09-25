package com.ncapdevi.sample.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.ncapdevi.sample.R;
import com.ncapdevi.sample.activities.BottomTabsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class GoogleFragment extends BaseFragment
        implements OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener{
    View rootView;
    MapView mapView;
    private GoogleMap mMap;
    String phoneNum;
    String girl_data = "";
    String moon_data = "";
    String girlName = "";
    String moonName = "";
    String girlPhone = "";
    String moonPhone = "";
    Double girlLongitude;
    Double moonLongitude;
    Double girlLatitude;
    Double moonLatitude;
    Button button;
    private IconGenerator mClusterIconGenerator;
    private IconGenerator hospital_ClusterIconGenerator;
    private Location lastKnownLocation = null;

    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    RelativeLayout backgroundLayout;
    View bottomSheetView;


    public static GoogleFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        GoogleFragment fragment = new GoogleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_whole, container, false);
        mapView = (MapView) rootView.findViewById(R.id.googlemap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }

        MapsInitializer.initialize(this.getActivity());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.537523, 126.96558), 14);
        googleMap.animateCamera(cameraUpdate);
        setUpMap(googleMap);
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });
        button = getView().findViewById(R.id.questionbutton);
        backgroundLayout = (RelativeLayout) getView().findViewById(R.id.backgroundlayout);
        bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_what, null);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.show();
            }
        });
    }


    private void setUpMap(GoogleMap googleMap) {
        mMap = googleMap;
        final ClusterManager<MarkerInformation> hospitalClusterManager = new ClusterManager(getActivity(), mMap);
        final ClusterManager<MarkerInformation> pharmacyClusterManager = new ClusterManager(getActivity(), mMap);
        final ClusterManager<MarkerInformation> girlClusterManager = new ClusterManager(getActivity(), mMap);
        final ClusterManager<MarkerInformation> moonClusterManager = new ClusterManager(getActivity(), mMap);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                phoneNum = marker.getSnippet();
                String tel = "tel:" + phoneNum;
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse(tel));
                startActivity(dialIntent);
            }
        });


        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                hospitalClusterManager.onCameraChange(cameraPosition);
                pharmacyClusterManager.onCameraChange(cameraPosition);
                girlClusterManager.onCameraChange(cameraPosition);
                moonClusterManager.onCameraChange(cameraPosition);
            }
        });

        HospitalParser hospital_parser = new HospitalParser();
        ArrayList<Pharmacy_DTO> hospital_list = null;
        try {
            hospital_list = hospital_parser.apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Pharmacy_DTO hospital : hospital_list) {
            LatLng hospital_LL = new LatLng(hospital.get_wgs84Lat(), hospital.get_wgs84Lon());
            String hospital_tel = new String(hospital.getTel());
            String hospital_name = new String(hospital.getName());
            MarkerInformation offsetItem = new MarkerInformation(hospital_LL, hospital_tel, hospital_name);
            hospitalClusterManager.addItem(offsetItem);
        }
        final Hospital_CustomIconRender hospital_renderer = new Hospital_CustomIconRender(getActivity(), mMap, hospitalClusterManager);
        hospitalClusterManager.setRenderer(hospital_renderer);  //아이콘 설정 부분



        PharmacyParser pharmacy_parser = new PharmacyParser();
        ArrayList<Pharmacy_DTO> pharmacy_list = null;
        try {
            pharmacy_list = pharmacy_parser.apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Pharmacy_DTO pharmacy : pharmacy_list) {
            LatLng pharmacy_LL = new LatLng(pharmacy.get_wgs84Lat(), pharmacy.get_wgs84Lon());
            String pharmacy_tel = new String(pharmacy.getTel());
            String pharmacy_name = new String(pharmacy.getName());
            MarkerInformation pharmacy_item = new MarkerInformation(pharmacy_LL, pharmacy_tel, pharmacy_name);
            pharmacyClusterManager.addItem(pharmacy_item);
        }
        final Pharmacy_CustomIconRender pharmacy_renderer = new Pharmacy_CustomIconRender(getActivity(), mMap, pharmacyClusterManager);
        pharmacyClusterManager.setRenderer(pharmacy_renderer);  //아이콘 설정 부분



        try {
            URL url = new URL("http://203.252.195.136/pharmacy_find.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                girl_data = girl_data + line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray JA = null;
        try {
            JA = new JSONObject(girl_data).getJSONArray("pharmacy");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < JA.length(); i++) {
            JSONObject JO = null;
            try {
                JO = (JSONObject) JA.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                girlPhone = "02-" + JO.get("girlPhone") + "";

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                girlName = JO.get("girlName") + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                girlLatitude = Double.valueOf(JO.get("girlLatitude") + "").doubleValue();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                girlLongitude = Double.valueOf(JO.get("girlLongitude") + "").doubleValue();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            girlClusterManager.addItem(new MarkerInformation(new LatLng(girlLatitude, girlLongitude), girlName, girlPhone));
        }


        final Girl_CustomIconRender girl_renderer = new Girl_CustomIconRender(getActivity(), mMap, girlClusterManager);
        girlClusterManager.setRenderer(girl_renderer);  //아이콘 설정 부분


        try {
            URL url = new URL("http://203.252.195.136/moon_find.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                moon_data = moon_data + line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray JA_moon = null;
        try {
            JA_moon = new JSONObject(moon_data).getJSONArray("moon");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < JA_moon.length(); i++) {
            JSONObject JO = null;
            try {
                JO = (JSONObject) JA_moon.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                moonPhone = JO.get("moonPhone") + "";

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                moonName = JO.get("moonName") + "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                moonLatitude = Double.valueOf(JO.get("moonLatitude") + "").doubleValue();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                moonLongitude = Double.valueOf(JO.get("moonLongitude") + "").doubleValue();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            moonClusterManager.addItem(new MarkerInformation(new LatLng(moonLatitude, moonLongitude), moonName, moonPhone));
        }


        final Moon_CustomIconRender moon_renderer = new Moon_CustomIconRender(getActivity(), mMap, moonClusterManager);
        moonClusterManager.setRenderer(moon_renderer);


    }

    public class Hospital_CustomIconRender extends DefaultClusterRenderer<MarkerInformation> {

        private final Context hospital_Context;

        public Hospital_CustomIconRender(Context context, GoogleMap map, ClusterManager<MarkerInformation> clusterManager) {
            super(context, map, clusterManager);
            hospital_Context = context;
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerInformation Item, MarkerOptions markerOptions) {
            final BitmapDescriptor hospital_icon = BitmapDescriptorFactory.fromResource(R.drawable.hospital);
            markerOptions.icon(hospital_icon).title(Item.getSnippet()).snippet(Item.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerInformation> cluster, MarkerOptions markerOptions) {
            hospital_ClusterIconGenerator = new IconGenerator(hospital_Context.getApplicationContext());
            hospital_ClusterIconGenerator.setBackground(ContextCompat.getDrawable(hospital_Context, R.drawable.hospital_background));
            hospital_ClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = hospital_ClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

    }

    public class Pharmacy_CustomIconRender extends DefaultClusterRenderer<MarkerInformation> {

        private final Context pharmacy_Context;

        public Pharmacy_CustomIconRender(Context context, GoogleMap map, ClusterManager<MarkerInformation> clusterManager) {
            super(context, map, clusterManager);
            pharmacy_Context = context;
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerInformation Item, MarkerOptions markerOptions) {
            final BitmapDescriptor pill_icon = BitmapDescriptorFactory.fromResource(R.drawable.pill);
            markerOptions.icon(pill_icon).title(Item.getSnippet()).snippet(Item.getTitle());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerInformation> cluster, MarkerOptions markerOptions) {
            hospital_ClusterIconGenerator = new IconGenerator(pharmacy_Context.getApplicationContext());
            hospital_ClusterIconGenerator.setBackground(ContextCompat.getDrawable(pharmacy_Context, R.drawable.pharmacy_background));
            hospital_ClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = hospital_ClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

    }

    public class Girl_CustomIconRender extends DefaultClusterRenderer<MarkerInformation> {

        private final Context mContext;

        public Girl_CustomIconRender(Context context, GoogleMap map, ClusterManager<MarkerInformation> clusterManager) {
            super(context, map, clusterManager);
            mContext = context;
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerInformation Item, MarkerOptions markerOptions) {
            final BitmapDescriptor girl_icon = BitmapDescriptorFactory.fromResource(R.drawable.girl);
            markerOptions.icon(girl_icon).title(Item.getTitle()).snippet(Item.getSnippet());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerInformation> cluster, MarkerOptions markerOptions) {
            mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
            mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.girl_background));
            mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        }

    }

    public class Moon_CustomIconRender extends DefaultClusterRenderer<MarkerInformation> {

        private final Context mContext;

        public Moon_CustomIconRender(Context context, GoogleMap map, ClusterManager<MarkerInformation> clusterManager) {
            super(context, map, clusterManager);
            mContext = context;
        }

        @Override
        protected void onBeforeClusterItemRendered(MarkerInformation Item, MarkerOptions markerOptions) {
            final BitmapDescriptor moon_icon = BitmapDescriptorFactory.fromResource(R.drawable.moon);
            markerOptions.icon(moon_icon).title(Item.getTitle()).snippet(Item.getSnippet());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MarkerInformation> cluster, MarkerOptions markerOptions) {
            mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
            mClusterIconGenerator.setBackground(ContextCompat.getDrawable(mContext, R.drawable.moon_background));
            mClusterIconGenerator.setTextAppearance(R.style.AppTheme_WhiteTextAppearance);
            final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        }

    }


    public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {
        private final LayoutInflater mInflater;

        public CustomInfoViewAdapter(LayoutInflater inflater) {
            this.mInflater = inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            final View popup = mInflater.inflate(R.layout.info_window_layout, null);
            ((TextView) popup.findViewById(R.id.txtTitle)).setText(marker.getTitle());
            ((TextView) popup.findViewById(R.id.txtSnippet)).setText(marker.getSnippet());
            return popup;
        }

        @Override
        public View getInfoContents(Marker marker) {
            final View popup = mInflater.inflate(R.layout.info_window_layout, null);
            ((TextView) popup.findViewById(R.id.txtTitle)).setText(marker.getTitle());
            ((TextView) popup.findViewById(R.id.txtSnippet)).setText(marker.getSnippet());
            return popup;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMyLocationChange(Location location) {
        Log.d("myLog", "onLocationChanged: !!" + "onLocationChanged!!");
        Marker mMarker;
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        mMarker = mMap.addMarker(new MarkerOptions().position(loc));
    }

}