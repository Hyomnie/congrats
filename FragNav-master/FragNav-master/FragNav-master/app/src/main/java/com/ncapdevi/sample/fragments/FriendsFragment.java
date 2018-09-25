package com.ncapdevi.sample.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ncapdevi.sample.R;
import com.ncapdevi.sample.activities.AppSingleton;
import com.ncapdevi.sample.activities.BottomTabsActivity;
import com.ncapdevi.sample.activities.startActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class FriendsFragment extends BaseFragment implements OnChartValueSelectedListener {

    ArrayList<String> cy = new ArrayList<>();
    ArrayList<String> pe = new ArrayList<>();
    int[] data0 = new int[]{1, 1, 5, 4, 2, 2, 4, 2};
    int[] data2 = new int[]{1, 4, 1, 1, 1, 1, 1, 1};
    int[] data3 = new int[]{5, 6, 9, 4, 3, 4, 5, 4};
    int[] data4 = new int[]{2, 1, 2, 3, 2, 1, 2, 1};
    int[] data7 = new int[]{1, 1, 1, 0, 0, 0, 0, 0};
    int[] data8 = new int[]{0, 0, 0, 0, 1, 1, 1, 1};
    ArrayList<PieEntry> entries0 = new ArrayList<PieEntry>();
    ArrayList<PieEntry> entries2 = new ArrayList<PieEntry>();
    ArrayList<PieEntry> entries3 = new ArrayList<PieEntry>();
    ArrayList<PieEntry> entries4 = new ArrayList<PieEntry>();
    ArrayList<PieEntry> entries7 = new ArrayList<PieEntry>();
    ArrayList<PieEntry> entries8 = new ArrayList<PieEntry>();
    List<Entry> entries5 = new ArrayList<>();
    List<Entry> entries6 = new ArrayList<>();
    String[] values = new String[]{"Oct", "Nov", "Dec", "Jan", "Feb", "Mar", "Apr"};
    String[] symptom = new String[]{"Medicine", "Stomach", "Indigestion", "Appetite", "Chest", "Waist", "Head", "Stress"};
    float month;


    public static FriendsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LineChart lineChart;
    private PieChart pieChart1;
    private PieChart pieChart2;
    private PieChart pieChart3;
    private PieChart pieChart4;
    private PieChart pieChart5;
    private PieChart pieChart6;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lineChart = (LineChart) getView().findViewById(R.id.chart);
        pieChart1 = (PieChart) getView().findViewById(R.id.pieChart1);
        pieChart2 = (PieChart) getView().findViewById(R.id.pieChart2);
        pieChart3 = (PieChart) getView().findViewById(R.id.pieChart1);
        pieChart4 = (PieChart) getView().findViewById(R.id.pieChart2);
        pieChart5 = (PieChart) getView().findViewById(R.id.pieChart1);
        pieChart6 = (PieChart) getView().findViewById(R.id.pieChart2);
        lineChart.setTouchEnabled(true);

        String str = "";
        String da = "0";
        try {
            PHPRequest request = new PHPRequest("http://203.252.195.136/enddate.php");
            da = request.PhPtest(str, str, str, str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (!da.equals(0)) {
            try {
                JSONObject root = new JSONObject(da);
                JSONArray ja = root.getJSONArray("result");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    cy.add(jo.getString("Cycle"));
                    pe.add(jo.getString("Period"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 1; i < cy.size(); i++) {
            entries5.add(new Entry(i, Integer.parseInt(cy.get(i))));
        }
        for (int i = 1; i < pe.size(); i++) {
            entries6.add(new Entry(i, Integer.parseInt(pe.get(i))));
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        LineDataSet lineDataSet = new LineDataSet(entries5, "생리 주기");
        LineDataSet lineDataSet2 = new LineDataSet(entries6, "생리 기간");
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleColor(Color.parseColor("#f6cfe1"));
        lineDataSet.setCircleColorHole(Color.MAGENTA);
        lineDataSet.setColor(Color.parseColor("#f6cfe1"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setDrawHighlightIndicators(false);

        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(6);
        lineDataSet2.setCircleColor(Color.rgb(237, 233, 146));
        lineDataSet2.setCircleColorHole(Color.rgb(190, 211, 144));
        lineDataSet2.setColor(Color.rgb(237, 233, 146));
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);
        lineDataSet2.setDrawHighlightIndicators(false);

        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet2);

        LineData lineData = new LineData(lineDataSets);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.parseColor("#808080"));
        xAxis.setTextSize(10);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setGranularity(1f);
        //xAxis.setValueFormatter(new IndexAxisValueFormatter(values));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] values = new String[]{"9월", "10월", "11월", "12월", "1월", "2월", "3월", "4월", "5월", /*"May", "Jun", "Jul", "Aug"*/};
            private ArrayList<Float> cachedValues = new ArrayList<>();

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (value >= 0) {
                    if (values.length > (int) value) {
                        return values[(int) value];
                    } else return "";
                } else {
                    return "";
                }
            }

            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.parseColor("#9e9e9e"));
        yLAxis.setTextSize(8);
        yLAxis.setAxisMinimum(0f);
        yLAxis.setAxisMaximum(50f);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("");

        LimitLine ll = new LimitLine(28f); // set where the line should be drawn
        ll.setLineColor(Color.parseColor("#FF4081"));
        ll.setLineWidth(1f);

        LimitLine ll2 = new LimitLine(5f); // set where the line should be drawn
        ll2.setLineColor(Color.parseColor("#bed38e"));
        ll2.setLineWidth(1f);

        yLAxis.addLimitLine(ll);
        yLAxis.addLimitLine(ll2);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.invalidate();
        lineChart.setOnChartValueSelectedListener(this);


        pieChart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                TextView tv = (TextView) getView().findViewById(R.id.textView3);
                int selectvalue = (int) h.getX();
                if (selectvalue == 0) {
                    String string = "● : 진통제 복용";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(107, 92, 151)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 1) {
                    String string = "● : 아랫배통증";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(190, 211, 142)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 2) {
                    String string = "● : 소화불량";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(210, 115, 143)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 3) {
                    String string = "● : 식욕상승";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(54, 134, 160)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 4) {
                    String string = "● : 가슴통증";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(247, 196, 108)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 5) {
                    String string = "● : 요통(허리통증)";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(175, 108, 103)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 6) {
                    String string = "● : 두통";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(161, 117, 156)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                } else if (selectvalue == 7) {
                    String string = "● : 스트레스";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(205, 184, 160)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                TextView tv2 = (TextView) getView().findViewById(R.id.textView4);
                int selectvalue = (int) h.getX();
                if (selectvalue == 0) {
                    String string = "● : 진통제 복용";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(107, 92, 151)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 1) {
                    String string = "● : 아랫배통증";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(190, 211, 142)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 2) {
                    String string = "● : 소화불량";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(210, 115, 143)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 3) {
                    String string = "● : 식욕상승";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(54, 134, 160)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 4) {
                    String string = "● : 가슴통증";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(247, 196, 108)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 5) {
                    String string = "● : 요통";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(175, 108, 103)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 6) {
                    String string = "● : 두통";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(161, 117, 156)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                } else if (selectvalue == 7) {
                    String string = "● : 스트레스";
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new ForegroundColorSpan(Color.rgb(205, 184, 160)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv2.setText(builder);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        pieChart1.setTouchEnabled(true);
        pieChart2.setTouchEnabled(true);
        Paint p1 = pieChart1.getPaint(Chart.PAINT_INFO);
        Paint p2 = pieChart2.getPaint(Chart.PAINT_INFO);
        p1.setColor(Color.parseColor("#000000"));
        p2.setColor(Color.parseColor("#000000"));
        p1.setTextSize(20);
        p2.setTextSize(20);
        pieChart1.setNoDataText("열람할 기간을 선택해주세요");
        pieChart2.setNoDataText("열람할 기간을 선택해주세요");
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("TAG", "OnValueSelected:" + e.getX());
        month = e.getX();
        for (int i = 0; i < 8; i++) {
            entries0.add(new PieEntry(data0[i]));
            entries2.add(new PieEntry(data2[i]));
            entries3.add(new PieEntry(data3[i]));
            entries4.add(new PieEntry(data4[i]));
            entries7.add(new PieEntry(data7[i]));
            entries8.add(new PieEntry(data8[i]));
        }
        if (month == 6.0) {
            pieChart1.clear();
            entries0.clear();
            pieChart2.clear();
            entries2.clear();
            pieChart3.clear();
            entries3.clear();
            pieChart4.clear();
            entries4.clear();
            pieChart5.clear();
            entries7.clear();
            pieChart6.clear();
            entries8.clear();
            for (int i = 0; i < 8; i++) {
                entries7.add(new PieEntry(data7[i]));
                entries8.add(new PieEntry(data8[i]));
            }
            pieChart6.getDescription().setEnabled(false);
            pieChart6.setCenterText(generateCenterText2());
            pieChart6.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart6.setHoleRadius(45f);
            pieChart6.setTransparentCircleRadius(50f);
            Legend lI = pieChart6.getLegend();
            lI.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            lI.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            lI.setOrientation(Legend.LegendOrientation.VERTICAL);
            lI.setDrawInside(false);
            pieChart6.setData(generatePieData(entries8));

            pieChart5.getDescription().setEnabled(false);
            pieChart5.setCenterText(generateCenterText());
            pieChart5.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart5.setHoleRadius(45f);
            pieChart5.setTransparentCircleRadius(50f);
            Legend l = pieChart5.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            pieChart5.setData(generatePieData(entries7));
            pieChart5.getLegend().setEnabled(false);
            pieChart5.setDrawSliceText(false);
            pieChart5.setNoDataText("상단 그래프에서 해당하는 날짜를 선택해주세요");
            pieChart6.getLegend().setEnabled(false);
            pieChart6.setDrawSliceText(false);
            pieChart6.setNoDataText("상단 그래프에서 해당하는 날짜를 선택해주세요");
            pieChart5.invalidate();
            pieChart6.invalidate();
        }

        if (month == 5.0) {
            pieChart1.clear();
            entries0.clear();
            pieChart2.clear();
            entries2.clear();
            pieChart3.clear();
            entries3.clear();
            pieChart4.clear();
            entries4.clear();
            pieChart5.clear();
            entries7.clear();
            pieChart6.clear();
            entries8.clear();
            for (int i = 0; i < 8; i++) {
                entries3.add(new PieEntry(data3[i]));
                entries4.add(new PieEntry(data4[i]));
            }
            pieChart4.clear();
            pieChart4.getDescription().setEnabled(false);
            pieChart4.setCenterText(generateCenterText2());
            pieChart4.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart4.setHoleRadius(45f);
            pieChart4.setTransparentCircleRadius(50f);
            Legend lI = pieChart4.getLegend();
            lI.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            lI.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            lI.setOrientation(Legend.LegendOrientation.VERTICAL);
            lI.setDrawInside(false);
            pieChart4.setData(generatePieData(entries4));

            pieChart3.getDescription().setEnabled(false);
            pieChart3.setCenterText(generateCenterText());
            pieChart3.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart3.setHoleRadius(45f);
            pieChart3.setTransparentCircleRadius(50f);
            Legend l = pieChart3.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            pieChart3.setData(generatePieData(entries3));
            pieChart3.getLegend().setEnabled(false);
            pieChart3.setDrawSliceText(false);
            pieChart3.setNoDataText("상단 그래프에서 해당하는 날짜를 선택해주세요");
            pieChart4.getLegend().setEnabled(false);
            pieChart4.setDrawSliceText(false);
            pieChart4.setNoDataText("상단 그래프에서 해당하는 날짜를 선택해주세요");
            pieChart3.invalidate();
            pieChart4.invalidate();
        }

        if (month == 4.0) {
            pieChart1.clear();
            entries0.clear();
            pieChart2.clear();
            entries2.clear();
            pieChart3.clear();
            entries3.clear();
            pieChart4.clear();
            entries4.clear();
            pieChart5.clear();
            entries7.clear();
            pieChart6.clear();
            entries8.clear();
            for (int i = 0; i < 8; i++) {
                entries0.add(new PieEntry(data0[i]));
                entries2.add(new PieEntry(data2[i]));
            }
            pieChart2.getDescription().setEnabled(false);
            pieChart2.setCenterText(generateCenterText2());
            pieChart2.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart2.setHoleRadius(45f);
            pieChart2.setTransparentCircleRadius(50f);
            Legend lI = pieChart2.getLegend();
            lI.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            lI.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            lI.setOrientation(Legend.LegendOrientation.VERTICAL);
            lI.setDrawInside(false);
            pieChart2.setData(generatePieData(entries2));

            pieChart1.getDescription().setEnabled(false);
            pieChart1.setCenterText(generateCenterText());
            pieChart1.setCenterTextSize(10f);
            // radius of the center hole in percent of maximum radius
            pieChart1.setHoleRadius(45f);
            pieChart1.setTransparentCircleRadius(50f);
            Legend l = pieChart1.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            pieChart1.setData(generatePieData(entries0));
            pieChart1.getLegend().setEnabled(false);
            pieChart1.setDrawSliceText(false);
            pieChart1.setNoDataText("상단 그래프에서 해당하는 날짜를 선택해주세요");
            pieChart2.getLegend().setEnabled(false);
            pieChart2.setDrawSliceText(false);
            pieChart2.setNoDataText("상단 그래프에서 해당하는 날짜를 선택해주세요");
            pieChart1.invalidate();
            pieChart2.invalidate();
        }

    }


    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("생리전");
        return s;
    }

    private SpannableString generateCenterText2() {
        SpannableString s = new SpannableString("생리중");
        return s;
    }

    @Override
    public void onNothingSelected() {
        Log.i("Tag", "nothing selected");
    }

    @Override
    public void onStop() {
        super.onStop();
        lineChart.clear();
        entries5.clear();
        entries6.clear();
        cy.clear();
        pe.clear();
    }
}
