package com.ahmed.m.hassaan.prayerstimesapp.base;

import android.app.Application;


public class App extends Application {


    public static App mACTIVITY;
    public static String APP_TAG = "APP_TAG";


    @Override
    public void onCreate() {
        super.onCreate();
        mACTIVITY = this;
    }

    @Override
    public void onTerminate() {
        mACTIVITY = null;
        super.onTerminate();
    }
}
