package com.android.teacher;

import android.app.Application;
import android.os.StrictMode;

import com.android.teacher.newwork.HttpCenter;

/**
 * Created by wukanghui on 2017/8/9.
 */

public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());

        HttpCenter.InstancesOkhttp();
        HttpCenter.context =this;
    }
}
