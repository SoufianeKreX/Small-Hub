package com.soufianekre.smallhub;

import android.app.Application;

import com.soufianekre.smallhub.helper.ColorsProvider;


public class SmallHubApp extends Application {
    private static Application mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ColorsProvider.load();
    }

    public static Application getInstance() {
        return mInstance;
    }

}
