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
import android.widget.TextView;

import com.codepath.vijay.twitterraj.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by uttamavillain on 2/10/16.
 */
public class ReplyTweet extends DialogFragment {

    private Button btTweet;
    private ImageButton ibCancel;
    private EditText etTweet;
    private ImageView ivProfileImage;
    private User curUser;
    private TextView tvReplyTweet;
    private String replyTo;
    private String id;
    private ReplySenderListener mCallBack;

    public ReplyTweet() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReplyTweet newInstance(User user, String replyTo, String id) {
        ReplyTweet frag = new ReplyTweet();
        Bundle bundle = new Bundle();
        bundle.putParcelable("curUser",user);
        bundle.putString("replyTo", replyTo);
        bundle.putString("id",id);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        curUser = getArguments().getParcelable("curUser");
        replyTo = getArguments().getString("replyTo");
        id = getArguments().getString("id");
        return inflater.inflate(R.layout.fragment_reply_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVeiws(view);
        getDialog().setTitle("Compose Tweet");
        initListeners(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallBack = (ReplySenderListener)getTargetFragment();
    }

    private void initListeners(View view) {
        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.postReply(etTweet.getText().toString(), id);
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
        etTweet = (EditText) view.findViewById(R.id.etTweet);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        tvReplyTweet = (TextView) view.findViewById(R.id.tvReplyTweet);
        tvReplyTweet.setText("In reply to "+replyTo);
        etTweet.setText("@"+replyTo);
        Picasso.with(getContext()).load(curUser.getProfileImageUrl()).into(ivProfileImage);
    }

    public interface ReplySenderListener {
        public void postReply(String tweet, String id);
    }
}
