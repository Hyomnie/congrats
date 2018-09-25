package com.ncapdevi.sample.fragments;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.DragAndDropPermissions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncapdevi.sample.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class RecentsFragment extends BaseFragment {

    CharSequence[] items = {"생리 시작", "생리 끝", "진통제 복용", "아랫배통증", "소화불량", "식욕상승",
            "가슴통증", "요통(허리통증)", "두통", "스트레스"};
    boolean[] itemsChecked = new boolean[items.length];
    CalendarDay start=new CalendarDay();
    String p_start[]={"0","0","0"};
    CalendarDay end=new CalendarDay();
    int c_sum=0, p_sum=0;
    int cycle=0;
    int period=0;
    ArrayList<String> sd = new ArrayList<>();
    ArrayList<String> ed = new ArrayList<>();
    ArrayList<String> cy=new ArrayList<>();
    ArrayList<String> pe=new ArrayList<>();
    ArrayList<ListItem> list=new ArrayList<>();
    String state[]=new String[9];
    public static RecentsFragment newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt(ARGS_INSTANCE, instance);
        RecentsFragment fragment = new RecentsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.activity_calendarpart, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MaterialCalendarView materialCalendarView = (MaterialCalendarView) getView().findViewById(R.id.calendarView);
        NetworkUtil.setNetworkPolicy();
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1))
                .setMaximumDate(CalendarDay.from(2019, 11, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();


        TextView textView3 = (TextView) getView().findViewById(R.id.textView3);
        String string = "● : 생리중  ● : 가임기  ● : 생리예정일";
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 64, 129)), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.rgb(205, 194, 211)), 9, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(Color.rgb(190, 211, 142)), 18, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(builder);

        materialCalendarView.addDecorators(new OneDayDecorator());
        materialCalendarView.setDateSelected(CalendarDay.today(), true);
        CalendarDay data1=materialCalendarView.getSelectedDate();
        TextView textView1 = (TextView) getView().findViewById(R.id.textView) ;
        textView1.setText("  "+(data1.getMonth()+1)+"월 "+data1.getDay()+"일") ;

        String d="0";
        String str=String.valueOf(materialCalendarView.getSelectedDate().hashCode());
        try {
            PHPRequest request=new PHPRequest("http://203.252.195.136/state.php");
            d=request.PhPtest(str, str, str, str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(!d.equals(0)) {
            try {
                JSONObject root = new JSONObject(d);
                JSONArray ja = root.getJSONArray("result");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    state[0] = jo.getString("Date");
                    state[1] = jo.getString("Medicine");
                    state[2] = jo.getString("Stomach");
                    state[3] = jo.getString("Indigestion");
                    state[4] = jo.getString("Appetite");
                    state[5] = jo.getString("Chest");
                    state[6] = jo.getString("Waist");
                    state[7] = jo.getString("Head");
                    state[8] = jo.getString("Stress");
                    list.add(new ListItem(state[0], state[1], state[2], state[3], state[4], state[5], state[6], state[7], state[8]));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        TextView textView2 = (TextView) getView().findViewById(R.id.textView2) ;
        textView2.setText(" 입력한 증상이 없습니다.\n 추가하려면 버튼을 누르세요.") ;
        int index=999;
        String temp="";
        for(int i=0; i<list.size(); i++){
            if(data1.getMonth()<10 && data1.getDay()<10)
                temp=data1.getYear()+"-0"+data1.getMonth()+"-0"+data1.getDay();
            else if(data1.getMonth()>=10 && data1.getDay()<10)
                temp=data1.getYear()+"-"+data1.getMonth()+"-0"+data1.getDay();
            else if(data1.getMonth()<10 && data1.getDay()>=10)
                temp=data1.getYear()+"-0"+data1.getMonth()+"-"+data1.getDay();
            else temp=data1.getYear()+"-"+data1.getMonth()+"-"+data1.getDay();
            if(list.get(i).getData(0).equals(temp)){
                index=i;
                break;
            }
        }

        if(index!=999){
            textView2.setText("  ");
            for(int i=1; i<9; i++){
                int k=i+1;
                if("1".equals(list.get(index).getData(i))){
                    textView2.append(items[k]+",  ");
                    continue;
                }
            }
        }

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                TextView textView1 = (TextView) getView().findViewById(R.id.textView) ;
                textView1.setText("  "+(date.getMonth()+1)+"월 "+date.getDay()+"일") ;
                TextView textView2 = (TextView) getView().findViewById(R.id.textView2) ;
                textView2.setText(" 입력한 증상이 없습니다.\n 추가하려면 버튼을 누르세요.") ;
                int index=999;
                String temp="";
                for(int i= 0; i<list.size(); i++){
                    if(date.getMonth()<10 && date.getDay()<10)
                        temp=date.getYear()+"-0"+date.getMonth()+"-0"+date.getDay();
                    else if(date.getMonth()>=10 && date.getDay()<10)
                        temp=date.getYear()+"-"+date.getMonth()+"-0"+date.getDay();
                    else if(date.getMonth()<10 && date.getDay()>=10)
                        temp=date.getYear()+"-0"+date.getMonth()+"-"+date.getDay();
                    else temp=date.getYear()+"-"+date.getMonth()+"-"+date.getDay();
                    if(list.get(i).getData(0).equals(temp)){
                        index=i;
                        break;
                    }
                }

                if(index!=999){
                    textView2.setText("  ");
                    for(int i=1; i<9; i++){
                        int k=i+1;
                        if("1".equals(list.get(index).getData(i))){
                            textView2.append(items[k]+",  ");
                            continue;
                        }
                    }
                }

            }
        });


        String da="0";
        try {
            PHPRequest request=new PHPRequest("http://203.252.195.136/enddate.php");
            da=request.PhPtest(str, str, str, str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
if(!da.equals(0)) {

    try {
        JSONObject root = new JSONObject(da);
        JSONArray ja = root.getJSONArray("result");
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            sd.add(jo.getString("Date"));
            ed.add(jo.getString("Date2"));
            cy.add(jo.getString("Cycle"));
            pe.add(jo.getString("Period"));
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    Calendar ca = Calendar.getInstance();
    Calendar ca2 = Calendar.getInstance();
    ArrayList<CalendarDay> dates = new ArrayList<>();

    for (int i = 0; i < sd.size(); i++) {

        String tmp[] = sd.get(i).split("-");
        String tmp2[] = ed.get(i).split("-");
        ca.set(Calendar.YEAR, Integer.parseInt(tmp[0]));
        ca.set(Calendar.MONTH, Integer.parseInt(tmp[1]));
        ca.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tmp[2]));
        ca2.set(Calendar.YEAR, Integer.parseInt(tmp2[0]));
        ca2.set(Calendar.MONTH, Integer.parseInt(tmp2[1]));
        ca2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tmp2[2]));

        CalendarDay day = new CalendarDay();
        long today = ca.getTimeInMillis() / 86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
        long dday = ca2.getTimeInMillis() / 86400000;
        long count = dday - today;

        if(i==sd.size()-1){
            p_start=sd.get(i).split("-");
        }
        for (int j = 0; j <= (int) count; j++) {
            day = CalendarDay.from(ca);
            dates.add(day);
            ca.add(Calendar.DAY_OF_MONTH, 1);
        }
        materialCalendarView.addDecorators(new EventDecorator(Color.parseColor("#FF4081"), dates));
        ca2.set(Calendar.YEAR, Integer.parseInt(tmp[0]));
        ca2.set(Calendar.MONTH, Integer.parseInt(tmp[1]));
        ca2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tmp[2])+14);
        ArrayList<CalendarDay>dates2 = new ArrayList<>();
        for (int j = 0; j < 9; j++) {
            day=CalendarDay.from(ca2);
            dates2.add(day);
            ca2.add(Calendar.DAY_OF_MONTH, 1);
        }
        materialCalendarView.addDecorators(new EventDecorator(Color.rgb(205, 194, 211), dates2));
    }
    for(int i=1;i<sd.size(); i++){
        c_sum+=Integer.parseInt(cy.get(i));
        p_sum+=Integer.parseInt(pe.get(i));
    }
    if(sd.size()>1) {
        cycle = c_sum / (sd.size() - 1);
        period = p_sum / (sd.size() - 1);
    }
    //Toast.makeText(getActivity().getBaseContext(),cycle+" "+period,Toast.LENGTH_SHORT).show();

}

        Calendar ca = Calendar.getInstance();
        ArrayList<CalendarDay> dates = new ArrayList<>();
        CalendarDay day=new CalendarDay();
        ca.set(Calendar.YEAR, Integer.parseInt(p_start[0]));
        ca.set(Calendar.MONTH, Integer.parseInt(p_start[1]));
        ca.set(Calendar.DAY_OF_MONTH, Integer.parseInt(p_start[2])+cycle);
        for (int i = 0; i < period; i++) {
            day=CalendarDay.from(ca);
            dates.add(day);
            ca.add(Calendar.DAY_OF_MONTH, 1);
        }
        materialCalendarView.addDecorators(new EventDecorator(Color.parseColor("#BED38E"), dates));

        Button button = (Button) getView().findViewById(R.id.inputPeriod);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // do something
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //builder.setIcon(R.drawable.ic_launcher_foreground);
                builder.setTitle("몸 상태 기록");

                builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    CalendarDay data1=materialCalendarView.getSelectedDate();
                    ArrayList<CalendarDay> dates = new ArrayList<>();
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(itemsChecked[0]==true){
                            //생리시작
                            CalendarDay day = data1;
                            start=data1;
                            dates.add(day);
                            materialCalendarView.addDecorators(new EventDecorator(Color.parseColor("#FF4081"), dates));
                        }
                        else if(itemsChecked[1]==true){
                            //생리끝-디비에 저장
                            String str=String.valueOf(start.hashCode());
                            String str2=String.valueOf(data1.hashCode());

                            //materialCalendarView.removeDecorators();

                            Calendar calendar = Calendar.getInstance();
                            Calendar calendar2 = Calendar.getInstance();
                            Calendar calendar3 = Calendar.getInstance();

                            ArrayList<CalendarDay> dates = new ArrayList<>();
                            CalendarDay day = start;
                            end=data1;
                            calendar.set(Calendar.YEAR, end.getYear());
                            calendar.set(Calendar.MONTH, end.getMonth());
                            calendar.set(Calendar.DAY_OF_MONTH, end.getDay());

                            calendar2.set(Calendar.YEAR, start.getYear());
                            calendar2.set(Calendar.MONTH, start.getMonth());
                            calendar2.set(Calendar.DAY_OF_MONTH, start.getDay());

                            long today = calendar2.getTimeInMillis()/86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
                            long dday = calendar.getTimeInMillis()/86400000;
                            long p_count = dday - today+1;

                            for (int i = 0; i <= (int)p_count; i++) {
                                dates.add(day);
                                day=CalendarDay.from(calendar2);
                                calendar2.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            materialCalendarView.addDecorators(new EventDecorator(Color.parseColor("#FF4081"), dates));

                            calendar3.set(Calendar.YEAR, Integer.parseInt(p_start[0]));
                            calendar3.set(Calendar.MONTH, Integer.parseInt(p_start[1]));
                            calendar3.set(Calendar.DAY_OF_MONTH, Integer.parseInt(p_start[2]));

                            long pday=calendar3.getTimeInMillis()/86400000;
                            long c_count=today-pday;

                            try {
                                PHPRequest request = new PHPRequest("http://203.252.195.136/Data_insert.php");
                                String result = request.PhPtest(str, str2, String.valueOf(c_count),String.valueOf(p_count));

                            }catch (MalformedURLException e){
                                e.printStackTrace();
                            }
                            p_start[0]="0";
                            p_start[1]="0";
                            p_start[2]="0";
                            calendar2.set(Calendar.YEAR, start.getYear());
                            calendar2.set(Calendar.MONTH, start.getMonth());
                            calendar2.set(Calendar.DAY_OF_MONTH, start.getDay()+14);
                            day=start;
                            dates = new ArrayList<>();
                            for (int i = 0; i < 9; i++) {
                                day=CalendarDay.from(calendar2);
                                dates.add(day);
                                calendar2.add(Calendar.DAY_OF_MONTH, 1);
                            }
                            materialCalendarView.addDecorators(new EventDecorator(Color.rgb(205, 194, 211), dates));
                        }
                        else if (itemsChecked[2]==true || itemsChecked[3]==true || itemsChecked[4]==true || itemsChecked[5]==true
                                || itemsChecked[6]==true || itemsChecked[7]==true|| itemsChecked[8]==true || itemsChecked[9]==true){
                            //증상
                            int state[]=new int[items.length];
                            for(int i=2; i<items.length; i++){
                                if(itemsChecked[i]==true)
                                    state[i]=1;
                                else state[i]=0;
                            }
                            try {
                                PHPRequest2 request = new PHPRequest2("http://203.252.195.136/calendar.php");
                                String result = request.PhPtest2(String.valueOf(data1.hashCode()), state);

                            }catch (MalformedURLException e){
                                e.printStackTrace();
                            }
                        }

                        for(int i=0; i<items.length; i++)
                            itemsChecked[i]=false;
                    }}
                );

                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                for(int i=0; i<items.length; i++)
                                    itemsChecked[i]=false;
                            }
                        }
                );

                builder.setMultiChoiceItems(items, itemsChecked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                itemsChecked[which]= isChecked ? true : false;
                            }
                        }
                );
                builder.create().show();
            }
        });
    }
}