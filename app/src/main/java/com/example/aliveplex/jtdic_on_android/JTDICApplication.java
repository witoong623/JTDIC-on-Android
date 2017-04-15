package com.example.aliveplex.jtdic_on_android;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Aliveplex on 13/4/2560.
 */

public class JTDICApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
