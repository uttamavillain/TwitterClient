package com.codepath.vijay.twitterraj;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.codepath.vijay.twitterraj.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by uttamavillain on 2/10/16.
 */
public class ComposeTweet extends DialogFragment {

    private Button btTweet;
    private ImageButton ibCancel;
    private EditText tvTweet;
    private ImageView ivProfileImage;
    private User curUser;
    private TweetSenderListener mCallBack;

    public ComposeTweet() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ComposeTweet newInstance(User user) {
        ComposeTweet frag = new ComposeTweet();
        Bundle bundle = new Bundle();
        bundle.putParcelable("curUser",user);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        curUser = getArguments().getParcelable("curUser");
        return inflater.inflate(R.layout.fragment_new_tweet, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallBack = (TweetSenderListener)getTargetFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVeiws(view);
        getDialog().setTitle("Compose Tweet");
        initListeners(view);
    }

    private void initListeners(View view) {
        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.postTweet(tvTweet.getText().toString());
                dismiss();
            }
        });

        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initVeiws(View view) {
        // Get field from view
        btTweet = (Button) view.findViewById(R.id.btTweet);
        ibCancel = (ImageButton) view.findViewById(R.id.ibCancel);
        tvTweet = (EditText) view.findViewById(R.id.tvTweet);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        Picasso.with(getContext()).load(curUser.getProfileImageUrl()).into(ivProfileImage);
    }

    public interface TweetSenderListener {
        public void postTweet(String tweet);
    }
}
