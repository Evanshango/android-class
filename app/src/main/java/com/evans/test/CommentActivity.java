package com.evans.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.evans.test.models.Post;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        mPost = intent.getParcelableExtra("post");
        String postId = mPost != null ? mPost.getPostId() : "";
        Log.d(TAG, "onCreate: postId: " + postId);
    }
}
