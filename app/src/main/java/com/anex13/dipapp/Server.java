package com.anex13.dipapp;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by namel on 16.05.2016.
 */
public class Server {
    String name;
    String url;
    String chkurl;
    String time;

    public Server(String name,String url,String chkurl,String time) {
        this.name = name;
        this.url = url;
        this.chkurl = chkurl;
        this.time = time;
    }

    public Server(Cursor cursor) {
        name = cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_NAME));
        url = cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_URL));
        chkurl = cursor.getString(cursor.getColumnIndex(SRVContentProvider.SERVER_CHKURL));
        time = cursor.getString(cursor.getColumnIndex(SRVContentProvider.UPDATE_TIME));
    }

    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(SRVContentProvider.SERVER_NAME, name);
        cv.put(SRVContentProvider.SERVER_URL, url);
        cv.put(SRVContentProvider.SERVER_CHKURL, chkurl);
        cv.put(SRVContentProvider.UPDATE_TIME, time);
        return cv;
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

    public String getUrl() {

        return url;
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
