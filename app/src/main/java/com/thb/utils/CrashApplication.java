package com.thb.utils;

import android.app.Application;

/**
 * Created by sea79 on 2017/10/30.
 */

public class CrashApplication extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        CrashHandler crashHandler= CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }

}
