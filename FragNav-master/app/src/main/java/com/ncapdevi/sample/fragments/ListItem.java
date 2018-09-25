package com.ncapdevi.sample.fragments;

/**
 * Created by sookmyung on 2018-03-17.
 */

public class ListItem {

    private String[] mData;

    public ListItem(String[] data ){
        mData = data;
    }

    public ListItem(String a, String a2, String a3, String a4, String a5, String a6, String a7, String a8, String a9){

        mData = new String[9];
        mData[0] = a;
        mData[1] = a2;
        mData[2] = a3;
        mData[3] = a4;
        mData[4] = a5;
        mData[5] = a6;
        mData[6] = a7;
        mData[7] = a8;
        mData[8] = a9;

    }

    public String[] getData(){
        return mData;
    }

    public String getData(int index){
        return mData[index];
    }

    public void setData(String[] data) {
        mData = data;
    }

}
