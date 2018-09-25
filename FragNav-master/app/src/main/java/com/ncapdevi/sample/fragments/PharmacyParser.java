package com.ncapdevi.sample.fragments;

/**
 * Created by SOOKMYUNG on 2018-02-23.
 */
import android.util.Log;

import java.io.BufferedInputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class PharmacyParser {

    public final static String PHARMACY_URL = "http://apis.data.go.kr/B551182/pharmacyInfoService/getParmacyBasisList?ServiceKey=M3GNtLeBo0fz5J%2FUvhk7qwdy4JOI3qBRuMl%2BhEMCtEOYWshLtlbkbo%2FCe3jcQctjXz5nv1XZPgDzBTB39%2BF%2F8Q%3D%3D&numOfRows=100&pageSize=100&clCd=21&dgsbjtCd=10";
    public PharmacyParser() {
        try {
            apiParserSearch();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     *
     * @throws Exception
     */
    public ArrayList<Pharmacy_DTO> apiParserSearch() throws Exception {
        URL pharmacy_url = new URL(getURLParam(null));

        XmlPullParserFactory pharmacy_factory = XmlPullParserFactory.newInstance();
        pharmacy_factory.setNamespaceAware(true);
        XmlPullParser pharmacy_xpp = pharmacy_factory.newPullParser();
        BufferedInputStream pharmacy_bis = new BufferedInputStream(pharmacy_url.openStream());
        pharmacy_xpp.setInput(pharmacy_bis, "utf-8");

        String tag = null;
        int event_type = pharmacy_xpp.getEventType();

        ArrayList<Pharmacy_DTO> pharmacy_list = new ArrayList<Pharmacy_DTO>();
        String pharmacy_wgs84Lat = null, pharmacy_wgs84Lon= null, pharmacy_name=null, pharmacy_phone=null;

        while (event_type != XmlPullParser.END_DOCUMENT) {
            if (event_type == XmlPullParser.START_TAG) {
                tag = pharmacy_xpp.getName();
            } else if (event_type == XmlPullParser.TEXT) {

                if(tag.equals("YPos")){
                    pharmacy_wgs84Lat = pharmacy_xpp.getText();
                }else if(tag.equals("XPos")){
                    pharmacy_wgs84Lon = pharmacy_xpp.getText();
                }else if(tag.equals("yadmNm")){
                    pharmacy_name = pharmacy_xpp.getText();
                }else if(tag.equals("telno")){
                    pharmacy_phone = pharmacy_xpp.getText();
                }
            } else if (event_type == XmlPullParser.END_TAG) {
                tag = pharmacy_xpp.getName();
                if (tag.equals("item")) {
                    Pharmacy_DTO pharmacy = new Pharmacy_DTO();
                    pharmacy.set_wgs84Lat(Double.valueOf(pharmacy_wgs84Lat));
                    pharmacy.set_wgs84Lon(Double.valueOf(pharmacy_wgs84Lon));
                    pharmacy.setName(pharmacy_name);
                    pharmacy.setTel(pharmacy_phone);
                    pharmacy_list.add(pharmacy);
                }
            }
            event_type = pharmacy_xpp.next();
        }
        System.out.println(pharmacy_list.size());

        return pharmacy_list;
    }




    private String getURLParam(String search){
        String url = PHARMACY_URL;
        if(search != null){
            url = url + "&dutyName" + search;
        }
        return url;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new PharmacyParser();
    }

}
