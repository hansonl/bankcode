package com.weidou.tools;

import android.app.Application;
import android.content.Context;

public class MingshengApp  extends Application{
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }
}
