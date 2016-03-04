package com.codepath.vijay.twitterraj.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.vijay.twitterraj.ComposeTweet;
import com.codepath.vijay.twitterraj.ReplyTweet;
import com.codepath.vijay.twitterraj.TwitterApplication;
import com.codepath.vijay.twitterraj.TwitterClient;
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
public class MentionTimeLineFragment extends TweetsListFragment{

    private static final String TAG = MentionTimeLineFragment.class.getName();
    private TwitterClient client;
    private ComposeTweet composeTweet;
    private ReplyTweet replyTweet;

    public MentionTimeLineFragment() {
        setTitle("@Mention");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
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
            populateTimeLine();
        }
        return super.onCreateView(inflater, parent, savedInstanceState);
    }

    @Override
    public boolean loadMore(int page, int totalItemsCount) {
        populateTimeLine();
        return true;
    }

    @Override
    public void refreshTimeLine() {
        Tweet.deleteAll();
        User.deleteAll();
        clearAll();
        populateTimeLine();
        Log.d(TAG, "Refreshed the list");
    }

    @Override
    public void populateTimeLine() {
        client.getMentionTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                super.onSuccess(statusCode, headers, jsonArray);
                Log.d(TAG, "onSuccess - received a JsonArray");
                List<Tweet> newList = Tweet.fromJSONArray(jsonArray);
                addAll(newList);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure while fetching timeline " + throwable.toString());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
