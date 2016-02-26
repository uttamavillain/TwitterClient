package com.codepath.apps.twitterraj;

import android.app.Application;
import android.util.Log;

/**
 * Created by uttamavillain on 2/19/16.
 */
public class Test extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Test", "OnCreate inside test");
    }
}
