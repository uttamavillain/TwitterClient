package com.codepath.vijay.twitterraj.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.vijay.twitterraj.EndlessScrollListener;
import com.codepath.vijay.twitterraj.ProfileActivity;
import com.codepath.vijay.twitterraj.R;
import com.codepath.vijay.twitterraj.ReplyTweet;
import com.codepath.vijay.twitterraj.TweetsArrayAdapter;
import com.codepath.vijay.twitterraj.TwitterApplication;
import com.codepath.vijay.twitterraj.TwitterClient;
import com.codepath.vijay.twitterraj.models.Tweet;
import com.codepath.vijay.twitterraj.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uttamavillain on 2/28/16.
 */
abstract public class TweetsListFragment extends Fragment implements ReplyTweet.ReplySenderListener, TweetsArrayAdapter.CallBackListener{

    private static final String TAG = TweetsListFragment.class.getName();
    private ArrayList<Tweet> mtweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private ListView lvTweets;
    public SwipeRefreshLayout swipeContainer;
    public User curUser;
    public TwitterClient client;
    public String title="yrst";

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweet_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(tweetsArrayAdapter);
        setupOnScrollListener();
        setupSwipeRefresh(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        mtweets = new ArrayList<Tweet>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), mtweets);
        getUserInfo();
    }

    public void setCallBackListener(TweetsArrayAdapter.CallBackListener callBackListener) {
        tweetsArrayAdapter.setCallBackListener(callBackListener);
    }

    public void addAll(List<Tweet> tweets) {
        mtweets.addAll(tweets);
        tweetsArrayAdapter.notifyDataSetChanged();
    }

    public void clearAll() {
        mtweets.clear();
    }

    private void setupOnScrollListener() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                return loadMore(page, totalItemsCount);
            }
        });
    }

    private void setupSwipeRefresh(View v) {
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeLine();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void showReplyTweet(String replyTo, String id) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ReplyTweet replyTweet = ReplyTweet.newInstance(curUser, replyTo, id);
        replyTweet.setTargetFragment(TweetsListFragment.this, 300);
        replyTweet.show(fm, "reply_tweet");
    }

    @Override
    public void postReply(String tweet, String id) {
        client.postReply(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "onSuccess - Replied the tweet");
                refreshTimeLine();
                Toast.makeText(getContext(), "Successfully Replied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to reply");
                Toast.makeText(getContext(), "Failed to reply", Toast.LENGTH_SHORT).show();
            }
        }, tweet, id);
    }

    @Override
    public void postLike(String id) {
        client.postLike(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "Liked the tweet successfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to like the tweet");
            }
        }, id);
    }

    @Override
    public void postRetweet(String id) {
        client.postRetweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "Retweeted successfully");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to Retweet");
            }
        }, id);
    }

    public void getUserInfo() {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "onSuccess - loaded Current User Info");
                curUser = User.fromJSON(jsonObject);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to get current user info " + throwable);
                Toast.makeText(getActivity(), "Failed to get current user info ", Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    @Override
    public void showProfileActivity(String screenName) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("screen_name",screenName);
        Log.d(TAG, screenName);
        startActivity(intent);
    }

    abstract public void populateTimeLine();
    abstract public boolean loadMore(int page, int totalItemsCount);
    abstract public void refreshTimeLine();
}