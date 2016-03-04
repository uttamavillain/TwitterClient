package com.codepath.vijay.twitterraj;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.vijay.twitterraj.fragments.SearchFragment;
import com.codepath.vijay.twitterraj.models.User;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = SearchActivity.class.getName();
    private TwitterClient client;
    User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String search = getIntent().getStringExtra("search");
        getSupportActionBar().setTitle(search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, SearchFragment.getNewInstance(search));
            ft.commit();
        }
    }
}
