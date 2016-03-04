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

import com.codepath.vijay.twitterraj.models.Tweet;
import com.codepath.vijay.twitterraj.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by uttamavillain on 2/28/16.
 */
public class SearchFragment extends TweetsListFragment{

    private static final String TAG = SearchFragment.class.getName();
    String search;
    private long max_id;

    public static TweetsListFragment getNewInstance(String search) {
        Bundle bundle = new Bundle();
        bundle.putString("search", search);
        SearchFragment fragment = new SearchFragment();
        fragment.setTitle(search);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        max_id = 0;
        setCallBackListener(this);
        setHasOptionsMenu(true);
        search = getArguments().getString("search");
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
        client.getSearch(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                Log.d(TAG, "onSuccess - received a JsonArray");
                List<Tweet> newList = null;
                try {
                    newList = Tweet.fromJSONArray(jsonObject.getJSONArray("statuses"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addAll(newList);
                max_id = newList.get(newList.size() - 1).getUid();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure while fetching timeline " + throwable.toString()+" "+errorResponse);
                swipeContainer.setRefreshing(false);
            }
        }, search);
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
}
