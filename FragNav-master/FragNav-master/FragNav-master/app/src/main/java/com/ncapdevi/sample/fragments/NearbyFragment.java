package com.ncapdevi.sample.fragments;
import net.daum.android.map.MapViewEventListener;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;
import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ncapdevi.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class NearbyFragment extends BaseFragment {

    public static NearbyFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.youtube, container, false);
        ScrollView layout = (ScrollView) inflater.inflate(R.layout.youtube, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imgView = (ImageView)getView().findViewById(R.id.imgView);
        try {
            URL url = new URL("http://img.youtube.com/vi/cL_ZyIcRyZ0/0.jpg");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgView.setImageBitmap(bm);
        } catch (Exception e) {
        }
        imgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.youtube.com/watch?v=cL_ZyIcRyZ0" ));
                startActivity( intent );
            }
        });

        ImageView imgView2 = (ImageView)getView().findViewById(R.id.i2);
        try {
            URL url = new URL("http://img.youtube.com/vi/ji1pPiuLTH8/0.jpg");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgView2.setImageBitmap(bm);
        } catch (Exception e) {
        }
        imgView2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.youtube.com/watch?v=ji1pPiuLTH8" ));
                startActivity( intent );
            }
        });

        ImageView imgView3 = (ImageView)getView().findViewById(R.id.i3);
        try {
            URL url = new URL("http://img.youtube.com/vi/p9pGotuZakw/0.jpg");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgView3.setImageBitmap(bm);
        } catch (Exception e) {
        }
        imgView3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.youtube.com/watch?v=p9pGotuZakw" ));
                startActivity( intent );
            }
        });

        ImageView imgView4 = (ImageView)getView().findViewById(R.id.i4);
        try {
            URL url = new URL("http://img.youtube.com/vi/mf23OTeb0cA/0.jpg");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgView4.setImageBitmap(bm);
        } catch (Exception e) {
        }
        imgView4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.youtube.com/watch?v=mf23OTeb0cA" ));
                startActivity( intent );
            }
        });

        ImageView imgView5 = (ImageView)getView().findViewById(R.id.i5);
        try {
            URL url = new URL("http://img.youtube.com/vi/HdLQ_u408ss/0.jpg");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgView5.setImageBitmap(bm);
        } catch (Exception e) {
        }
        imgView5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.youtube.com/watch?v=HdLQ_u408ss" ));
                startActivity( intent );
            }
        });

        ImageView imgView6 = (ImageView)getView().findViewById(R.id.i6);
        try {
            URL url = new URL("http://img.youtube.com/vi/neviSPzvWVk/0.jpg");
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            imgView6.setImageBitmap(bm);
        } catch (Exception e) {
        }
        imgView6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse( "https://www.youtube.com/watch?v=neviSPzvWVk" ));
                startActivity( intent );
            }
        });


    }
}