package com.codepath.vijay.twitterraj;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TwitterApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends Application {
	private static String TAG = TwitterApplication.class.getName();
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "OnCreate");
		TwitterApplication.context = this;
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApplication.context);
	}
}