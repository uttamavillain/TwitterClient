package com.codepath.apps.twitterraj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.twitterraj.models.Tweet;
import com.codepath.apps.twitterraj.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeLineActivity extends AppCompatActivity implements ComposeTweet.TweetSender{

    private static final String TAG = TimeLineActivity.class.getName();
    private TwitterClient client;
    private ArrayList<Tweet> mtweets;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;
    private ComposeTweet composeTweet;
    private User curUser;

    private long max_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        max_id = 0;
        setContentView(R.layout.activity_time_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.twitter_icon);
        client = TwitterApplication.getRestClient();
        setupViews();
        loadCurrentUserInfo();
        populateTimeLine();
    }

    private void setupViews() {
        setupListViewAndAdapter();
        setupOnScrollListener();
        setupSwipeRefresh();
    }

    private void setupListViewAndAdapter() {
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        mtweets = new ArrayList<Tweet>();
        tweetsArrayAdapter = new TweetsArrayAdapter(this, mtweets);
        lvTweets.setAdapter(tweetsArrayAdapter);
    }

    private void setupOnScrollListener() {
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                populateTimeLine();
                return true;
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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

    public void refreshTimeLine() {
        mtweets.clear();
        max_id = 0;
        populateTimeLine();
        Log.d(TAG, "Refreshed the list");
    }

    private void populateTimeLine() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                super.onSuccess(statusCode, headers, jsonArray);
                Log.d(TAG, "onSuccess - received a JsonArray");
                mtweets.addAll(Tweet.fromJSONArray(jsonArray));
                max_id = mtweets.get(mtweets.size() - 1).getUid();
                tweetsArrayAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "onFailure while fetching timeline");
                swipeContainer.setRefreshing(false);
            }
        }, max_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hometimeline, menu);
        MenuItem item = menu.findItem(R.id.action_tweet);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_tweet) {
            showComposeTweet();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showComposeTweet() {
        FragmentManager fm = getSupportFragmentManager();
        composeTweet = ComposeTweet.newInstance(curUser);
        composeTweet.show(fm, "compose_tweet");
    }

    @Override
    public void sendTweet(String tweet) {
        client.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "onSuccess - Posted the tweet");
                refreshTimeLine();
                Toast.makeText(getBaseContext(), "Successfull Tweeted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to tweet");
                Toast.makeText(getBaseContext(), "Failed to Tweet", Toast.LENGTH_SHORT).show();
            }
        }, tweet);
    }

    public void loadCurrentUserInfo() {
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "onSuccess - loaded Current User Info");
                curUser = User.fromJSON(jsonObject);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to get current user info "+throwable);
                Toast.makeText(getBaseContext(), "Failed to get current user info", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
