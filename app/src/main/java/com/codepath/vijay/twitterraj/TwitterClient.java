package com.codepath.vijay.twitterraj;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "dLlSqNfBLVAi4i7btuREjREZs";       // Change this
	public static final String REST_CONSUMER_SECRET = "kSWPZls4sWqo7s6VhUmABHyXEsSoYXqfOFBBRMtMA6i2QRR31E"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://simpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeLine(AsyncHttpResponseHandler handler, long max_id) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		if(max_id != 0)
			params.put("max_id",max_id - 1);
		getClient().get(apiUrl, params, handler);
	}

	public void getMentionTimeLine(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		getClient().get(apiUrl, params, handler);
	}

	public void postTweet(AsyncHttpResponseHandler handler, String tweet) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",tweet);
		getClient().post(apiUrl, params, handler);
	}

	public void postLike(AsyncHttpResponseHandler handler, String id) {
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id",id);
		getClient().post(apiUrl, params, handler);
	}

	public void postRetweet(AsyncHttpResponseHandler handler, String id) {
		String apiUrl = getApiUrl("statuses/retweet/"+id+".json");
		getClient().post(apiUrl, handler);
	}

	public void postReply(AsyncHttpResponseHandler handler, String tweet, String id) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status",tweet);
		params.put("in_reply_to_status_id", id);
		getClient().post(apiUrl, params, handler);
	}

	public void getUserInfo(AsyncHttpResponseHandler handler, String screenName) {
		if(screenName == null) {
			String apiUrl = getApiUrl("account/verify_credentials.json");
			getClient().get(apiUrl, handler);
		}
		else {
			String apiUrl = getApiUrl("users/show.json");
			RequestParams params = new RequestParams();
			params.put("screen_name", screenName);
			getClient().get(apiUrl, params, handler);
		}
	}

	public void getUserTimeLine(AsyncHttpResponseHandler handler, String screenName) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count",25);
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void getSearch(AsyncHttpResponseHandler handler, String search) {
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		params.put("q",search);
		params.put("count", 25);
		getClient().get(apiUrl, params, handler);
	}
}