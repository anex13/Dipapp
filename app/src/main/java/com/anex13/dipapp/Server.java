package com.anex13.dipapp;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by namel on 16.05.2016.
 */
public class Server {
    int id;
    String name;
    String url;
    String chkurl;
    String time;
    String nextchktime;
    int alarm;
    int state;

    public Server(String name,String url,String chkurl,String time,String nextchktime,int alarm,int state) {
        this.name = name;
        this.url = url;
        this.chkurl = chkurl;
        this.time = time;
        this.state=state;
        this.nextchktime=nextchktime;
        this.alarm=alarm;
    }

    public Server(Cursor cursor) {
        id =cursor.getInt(cursor.getColumnIndex(SRVContentProvider.SERVER_ID));
        name = cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_NAME));
        url = cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_URL));
        chkurl = cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_CHKURL));
        time = cursor.getString(cursor.getColumnIndex(SRVContentProvider.UPDATE_TIME));
        state= cursor.getInt(cursor.getColumnIndex(SRVContentProvider.SERVER_STATE));
        nextchktime= cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_NEXT_CHECK));
        alarm= cursor.getInt(cursor.getColumnIndex(SRVContentProvider.FAIL_NOTIFICATION));
    }



    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(SRVContentProvider.SERVER_NAME, name);
        cv.put(SRVContentProvider.SERVER_URL, url);
        cv.put(SRVContentProvider.SERVER_CHKURL, chkurl);
        cv.put(SRVContentProvider.UPDATE_TIME, time);
        cv.put(SRVContentProvider.SERVER_NEXT_CHECK, nextchktime);
        cv.put(SRVContentProvider.FAIL_NOTIFICATION, alarm);
        cv.put(SRVContentProvider.SERVER_STATE, state);
        return cv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNextchktime(String nextchktime) {
        this.nextchktime = nextchktime;
    }
    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setChkurl(String chkurl) {
        this.chkurl = chkurl;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setState(int state) {
        this.state = state;
    }

    public int getAlarm() {
        return alarm;
    }
    public int getState() {
        return state;
    }
    public int getcolor() {
        int color;
        switch (state){
            case 0 :
                color=0xFFFF0000;
                break;
            case 1:
                color=0xFF00FF00;
                break;
            default:
                color=0x80000000;
                break;
        }
        return color;
    }
    public String getUrl() {

        return url;
    }
    public String getNextchktime() {
        return nextchktime;

    }
    public String getChkurl() {
        return chkurl;
    }
    public String getTime() {
        return time;
    }
    public String getName() {
        return name;
    }
}
