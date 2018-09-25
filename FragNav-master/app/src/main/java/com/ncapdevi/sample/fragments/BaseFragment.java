package com.ncapdevi.sample.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.ncapdevi.sample.R;

import java.util.ArrayList;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class BaseFragment extends Fragment {
    public static final String ARGS_INSTANCE = "com.ncapdevi.sample.argsInstance";
    private String[] values = new String[]{ "진통제복용", "아랫배통증", "소화불량", "식욕상승", "가슴통증", "요통", "두통", "스트레스"};

    Button btn;
    FragmentNavigation mFragmentNavigation;
    int mInt = 0;
    private View cachedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mInt = args.getInt(ARGS_INSTANCE);
        }
    }


        @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (cachedView == null) {
            cachedView = inflater.inflate(R.layout.fragment_main, container, false);
            btn = cachedView.findViewById(R.id.button);
        }
        return cachedView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            mFragmentNavigation = (FragmentNavigation) context;
        }
    }

    public interface FragmentNavigation {
        void pushFragment(Fragment fragment);
    }

    public interface onFragmentInteractionListener{
        public void onFragmentInteraction(Uri uri);
    }

    protected PieData generatePieData(ArrayList<PieEntry> entries1) {
        int count = 8;
        PieDataSet ds1 = new PieDataSet(entries1, "Quarterly Revenues 2015");
        final int[] MY_COLORS = {Color.rgb(107, 92, 151), Color.rgb(190, 211, 142), Color.rgb(210, 115, 143),
                Color.rgb(54, 134, 160), Color.rgb(247, 196, 108), Color.rgb(175, 108, 103), Color.rgb(161, 117, 156), Color.rgb(205, 184, 160)};
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        ds1.setColors(colors);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);
        PieData d = new PieData(ds1);
        return d;
    }

}

