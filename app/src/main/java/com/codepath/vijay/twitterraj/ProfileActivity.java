package com.codepath.vijay.twitterraj;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.vijay.twitterraj.fragments.UserTimeLineFragment;
import com.codepath.vijay.twitterraj.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getName();
    private TwitterClient client;
    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(getIntent().getStringExtra("screen_name"));

        client = TwitterApplication.getRestClient();
        getUserInfo();
        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimeLineFragment);
            ft.commit();
        }
    }

    private void loadViews() {
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        TextView tvGivenName = (TextView) findViewById(R.id.tvGivenName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        Picasso.with(this).load(curUser.getProfileImageUrl()).into(ivProfileImage);
        tvGivenName.setText(curUser.getName());
        tvTagLine.setText(curUser.getTagLine());
        tvFollowers.setText(curUser.getFollowersCount()+" Followers");
        tvFollowing.setText(curUser.getFollowingCount() + " Following");

    }

    public void getUserInfo() {
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d(TAG, "onSuccess - loaded Current User Info");
                curUser = User.fromJSON(jsonObject);
                getSupportActionBar().setTitle("@" + curUser.getScreenName());
                loadViews();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "Failed to get current user info " + throwable);
                Toast.makeText(getApplicationContext(), "Failed to get current user info ", Toast.LENGTH_SHORT).show();
            }
        }, getIntent().getStringExtra("screen_name"));
    }
}
