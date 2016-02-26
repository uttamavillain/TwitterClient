package com.codepath.apps.twitterraj;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterraj.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by uttamavillain on 2/23/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvGivenName = (TextView) convertView.findViewById(R.id.tvGivenName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);

        tvGivenName.setText(tweet.getUser().getName());
        tvUserName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent); //clear out the image for recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                                                        //Wed Feb 24 09:15:27 +0000 2016
        try {
            long time = formatter.parse(tweet.getCreatedAt()).getTime();
            String timeSince = (String) DateUtils.getRelativeTimeSpanString(time);
            tvRelativeTime.setText(timeSince);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
