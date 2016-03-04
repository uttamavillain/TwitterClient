package com.codepath.vijay.twitterraj;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.ActiveAndroid;
import com.astuetz.PagerSlidingTabStrip;
import com.codepath.vijay.twitterraj.fragments.HomeTimeLineFragment;
import com.codepath.vijay.twitterraj.fragments.MentionTimeLineFragment;
import com.codepath.vijay.twitterraj.fragments.SearchFragment;
import com.codepath.vijay.twitterraj.fragments.TweetsListFragment;

import java.util.ArrayList;

public class TimeLineActivity extends AppCompatActivity {

    private static final String TAG = TimeLineActivity.class.getName();
    private TweetPagerAdapter tweetPagerAdapter;
    private ArrayList<TweetsListFragment> mFragments;
    PagerSlidingTabStrip tabStrip;
    ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        ActiveAndroid.setLoggingEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.mipmap.twitter_icon);

        mFragments = new ArrayList<TweetsListFragment>();
        mFragments.add(new HomeTimeLineFragment());
        mFragments.add(new MentionTimeLineFragment());

        vpPager = (ViewPager) findViewById(R.id.viewpager);
        tweetPagerAdapter = new TweetPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(tweetPagerAdapter);
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeTimeLineFragment) mFragments.get(0)).showComposeTweet();
            }
        });
    }

    public void onProfileClick(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onSearchClick(MenuItem item) {
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                /*Intent intent = new Intent(TimeLineActivity.this, SearchActivity.class);
                intent.putExtra("search", q);
                startActivity(intent);*/
                Log.d(TAG, "another one");
                if(mFragments.size()==3) {
                    mFragments.set(2, SearchFragment.getNewInstance(q));
                }
                else {
                    mFragments.add(SearchFragment.getNewInstance(q));
                }
                tweetPagerAdapter.notifyDataSetChanged();
                tabStrip.notifyDataSetChanged();
                vpPager.setCurrentItem(2);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public class TweetPagerAdapter extends FragmentStatePagerAdapter {

        private String tabTitles[] = {"Home", "@Mentions"};

        public TweetPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hometimeline, menu);
        return true;
    }

    public abstract class SmartFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
        // Sparse array to keep track of registered fragments in memory
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public SmartFragmentStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Register the fragment when the item is instantiated
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        // Unregister when the item is inactive
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        // Returns the fragment for the position (if instantiated)
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
