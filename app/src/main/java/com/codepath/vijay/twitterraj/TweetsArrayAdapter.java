package com.codepath.vijay.twitterraj;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.vijay.twitterraj.models.Tweet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
        final Tweet tweet = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvGivenName = (TextView) convertView.findViewById(R.id.tvGivenName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        final TextView tvLike = (TextView) convertView.findViewById(R.id.tvLikes);
        final TextView tvRetweet = (TextView) convertView.findViewById(R.id.tvRetweet);
        final ImageView ivMediaView = (ImageView) convertView.findViewById(R.id.ivMediaImage);
        final ImageView ivLikes = (ImageView) convertView.findViewById(R.id.ivLikes);
        final ImageView ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweet);
        ImageView ivReplyTweet = (ImageView) convertView.findViewById(R.id.ivReplyTweet);

        ivReplyTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((TimeLineActivity) getContext()).showReplyTweet(tweet.getUser().getScreenName(), tweet.getUid()+"");
            }
        });

        tvLike.setText(Integer.toString(tweet.getLikes_count()));
        tvRetweet.setText(Integer.toString(tweet.getRetweet_count()));
        ivLikes.setImageResource(R.drawable.like_default);
        ivRetweet.setImageResource(R.drawable.retweet_default);

        if(tweet.isLiked()) {
            ivLikes.setImageResource(R.drawable.like_liked);
        }

        if(tweet.isRetweeted()) {
            ivRetweet.setImageResource(R.drawable.retweet_retweeted);
        }

        ivLikes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!tweet.isLiked()) {
                    tvLike.setText(Integer.toString(tweet.getLikes_count() + 1));
                    ivLikes.setImageResource(R.drawable.like_liked);
                    ((TimeLineActivity) getContext()).postLike(tweet.getUid() + "");
                    tweet.setLiked(true);
                    tweet.setLikes_count(tweet.getLikes_count() + 1);
                    tweet.save();
                }
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!tweet.isRetweeted()) {
                    tvRetweet.setText(Integer.toString(tweet.getRetweet_count() + 1));
                    ivRetweet.setImageResource(R.drawable.retweet_retweeted);
                    ((TimeLineActivity) getContext()).postRetweet(tweet.getUid() + "");
                    tweet.setRetweeted(true);
                    tweet.setRetweet_count(tweet.getRetweet_count() + 1);
                    tweet.save();
                }
            }
        });

        tvGivenName.setText(tweet.getUser().getName());
        tvUserName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent); //clear out the image for recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).placeholder(R.drawable.blurred_placeholder).into(ivProfileImage);
        SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                                                        //Wed Feb 24 09:15:27 +0000 2016
        try {
            long time = formatter.parse(tweet.getCreatedAt()).getTime();
            String timeSince = (String) DateUtils.getRelativeTimeSpanString(time);
            tvRelativeTime.setText(timeSince);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(tweet.getMediaType() == Tweet.MEDIA_PHOTO) {
            ivMediaView.setVisibility(View.VISIBLE);
            Log.d(TweetsArrayAdapter.class.getName(), "Photo Media "+tweet.getBody());
            Picasso.with(getContext()).load(tweet.getImageUrl()).into(ivMediaView);
        } else if(tweet.getMediaType() == Tweet.MEDIA_VIDEO) {
            ivMediaView.setVisibility(View.VISIBLE);
            Log.d(TweetsArrayAdapter.class.getName(), "vidoe Media "+tweet.getBody());
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Bitmap newImag = overlay(bitmap, BitmapFactory.decodeResource(getContext().getResources(), R.drawable.play));
                    ivMediaView.setImageBitmap(newImag);
                    ivMediaView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playVideo(tweet.getVideoUrl());
                        }
                    });

                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(getContext()).load(tweet.getImageUrl()).placeholder(R.drawable.blurred_placeholder).into(target);
        } else {
            Log.d(TweetsArrayAdapter.class.getName(), "No Media");
            ivMediaView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void playVideo(String url) {
        Intent i = new Intent(getContext(), VideoPlayerActivity.class);
        i.putExtra("url", url);
        getContext().startActivity(i);
    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, new Matrix(), null);
        return bmOverlay;
    }
}
