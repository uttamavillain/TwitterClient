package com.codepath.vijay.twitterraj.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uttamavillain on 2/23/16.
 */

@Table(name = "Tweets")
public class Tweet extends Model {

    public static final int NO_MEDIA = 0;
    public static final int MEDIA_PHOTO = 1;
    public static final int MEDIA_VIDEO = 2;

    @Column(name = "body")
    private String body;

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "user", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "videoUrl")
    private String videoUrl;

    @Column(name = "mediaType")
    private int mediaType;

    @Column(name = "retweet_count")
    private int retweet_count = 0;

    @Column(name = "likes_count")
    private int likes_count = 0;

    @Column(name = "liked")
    private boolean liked = false;

    @Column(name = "retweeted")
    private boolean retweeted = false;

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Tweet() {
        super();
    }

    public String getBody() {
        return body;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getRetweet_count() {
        return retweet_count;
    }

    public boolean isLiked() {
        return liked;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setRetweet_count(int retweet_count) {
        this.retweet_count = retweet_count;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {

        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");

            tweet.createdAt = jsonObject.getString("created_at");
            tweet.likes_count = jsonObject.getInt("favorite_count");
            tweet.retweet_count = jsonObject.getInt("retweet_count");
            tweet.liked = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");

            long id = jsonObject.getJSONObject("user").getLong("id");
            if(jsonObject.has("extended_entities")) {
                JSONObject media = (JSONObject)jsonObject.getJSONObject("extended_entities").getJSONArray("media").get(0);
                if(media != null) {
                    tweet.imageUrl = media.getString("media_url");
                    tweet.mediaType = MEDIA_PHOTO;
                    if(media.getString("type").equals("video")) {
                        JSONArray varients = media.getJSONObject("video_info").getJSONArray("variants");
                        for(int i=0; i<varients.length(); i++) {
                            Log.d(Tweet.class.getName(), ((JSONObject) varients.get(i)).getString("content_type"));
                            if(((JSONObject)varients.get(i)).getString("content_type").equals("video/mp4")) {
                                tweet.videoUrl = ((JSONObject)varients.get(i)).getString("url");
                                tweet.mediaType = MEDIA_VIDEO;
                                break;
                            }
                        }
                    }
                }
            }
            User existingUser =
                    new Select().from(User.class).where("uid = ?", id).executeSingle();
            if(existingUser == null)
                tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            else
                tweet.user = existingUser;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tweet.save();
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();
        for(int i=0; i<jsonArray.length(); i++) {
            try {
                Tweet tweet = fromJSON(jsonArray.getJSONObject(i));
                if(tweet != null)
                    tweets.add(tweet);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static List<Tweet> getAll() {
        // This is how you execute a query
        return new Select()
                .from(Tweet.class)
                .orderBy("createdAt DESC")
                .execute();
    }

    public static void deleteAll() {
        new Delete().from(Tweet.class).execute();
    }


    @Override
    public String toString() {
        return user+" "+body+" "+createdAt;
    }
}
