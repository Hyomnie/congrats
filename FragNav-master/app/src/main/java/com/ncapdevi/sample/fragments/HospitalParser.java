package com.ncapdevi.sample.fragments;

/**
 * Created by SOOKMYUNG on 2018-02-23.
 */
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class HospitalParser {

    public final static String HOSPITAL_URL = "http://apis.data.go.kr/B551182/hospInfoService/getHospBasisList?serviceKey=M3GNtLeBo0fz5J%2FUvhk7qwdy4JOI3qBRuMl%2BhEMCtEOYWshLtlbkbo%2FCe3jcQctjXz5nv1XZPgDzBTB39%2BF%2F8Q%3D%3D&numOfRows=100&pageSize=100&clCd=21&dgsbjtCd=10";
    public HospitalParser() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @throws Exception
     */
    public ArrayList<Pharmacy_DTO> apiParserSearch() throws Exception {
        URL url = new URL(getURLParam(null));

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        xpp.setInput(bis, "utf-8");

        String tag = null;
        int event_type = xpp.getEventType();

        ArrayList<Pharmacy_DTO> list = new ArrayList<Pharmacy_DTO>();

        String wgs84Lat = null, wgs84Lon= null, name=null, phone=null;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = xpp.getName();
            } else if (event_type == XmlPullParser.TEXT) {
                /**
                 * 약국의 주소만 가져와본다.
                 */
                if(tag.equals("YPos")){
                    wgs84Lat = xpp.getText();
                }else if(tag.equals("XPos")){
                    wgs84Lon = xpp.getText();
                }else if(tag.equals("yadmNm")){
                    name = xpp.getText();
                }else if(tag.equals("telno")){
                    phone = xpp.getText();
                }
            } else if (event_type == XmlPullParser.END_TAG) {
                tag = xpp.getName();
                if (tag.equals("item")) {
                    Pharmacy_DTO hospital = new Pharmacy_DTO();
                    hospital.set_wgs84Lat(Double.valueOf(wgs84Lat));
                    hospital.set_wgs84Lon(Double.valueOf(wgs84Lon));
                    hospital.setName(name);
                    hospital.setTel(phone);
                    list.add(hospital);
                }
            }
            event_type = xpp.next();
        }
        System.out.println(list.size());

        return list;
    }




    private String getURLParam(String search){
        String url = HOSPITAL_URL;
        if(search != null){
            url = url + "&dutyName" + search;
        }
        return url;
    }

    public static void main(String[] args) {
        new PharmacyParser();
    }

}
