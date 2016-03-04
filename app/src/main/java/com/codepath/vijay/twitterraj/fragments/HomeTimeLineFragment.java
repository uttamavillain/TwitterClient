package com.codepath.vijay.twitterraj.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.vijay.twitterraj.ComposeTweet;
import com.codepath.vijay.twitterraj.models.Tweet;
import com.codepath.vijay.twitterraj.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by uttamavillain on 2/28/16.
 */
public class HomeTimeLineFragment extends TweetsListFragment implements ComposeTweet.TweetSenderListener{

    private static final String TAG = HomeTimeLineFragment.class.getName();

    private long max_id;

    public HomeTimeLineFragment() {
        setTitle("Home");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        max_id = 0;
        setCallBackListener(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if(!isNetworkAvailable()){
            Log.d(TAG, "Network not available, loading from persistant storage");
            Toast.makeText(getActivity(), "Network not available, loading from persistant storage", Toast.LENGTH_SHORT).show();
            addAll(Tweet.getAll());
        }
        else {
            getUserInfo();
            populateTimeLine();
        }
        return super.onCreateView(inflater, parent, savedInstanceState);
    }

    public void populateTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                super.onSuccess(statusCode, headers, jsonArray);
                Log.d(TAG, "onSuccess - received a JsonArray");
                List<Tweet> newList = Tweet.fromJSONArray(jsonArray);
                addAll(newList);
                max_id = newList.get(newList.size() - 1).getUid();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure while fetching timeline " + throwable.toString());
                swipeContainer.setRefreshing(false);
            }
        }, max_id);
    }

    public void refreshTimeLine() {
        Tweet.deleteAll();
        User.deleteAll();
        clearAll();
        max_id = 0;
        populateTimeLine();
        Log.d(TAG, "Refreshed the list");
    }

    @Override
    public boolean loadMore(int page, int totalItemsCount) {
        populateTimeLine();
        return true;
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void showComposeTweet() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ComposeTweet composeTweet = ComposeTweet.newInstance(curUser);
        composeTweet.setTargetFragment(HomeTimeLineFragment.this, 300);
        composeTweet.show(fm, "compose_tweet");
    }

    @Override
    public void postTweet(String tweet) {
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "onSuccess - Posted the tweet");
                refreshTimeLine();
                Toast.makeText(getContext(), "Successfull Tweeted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to tweet");
                Toast.makeText(getContext(), "Failed to Tweet", Toast.LENGTH_SHORT).show();
            }
        }, tweet);
    }
}
