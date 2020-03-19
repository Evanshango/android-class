package com.evans.test.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private String postId, postImage, postCaption, likes, comments, date, time, username, userId;

    public Post() {
    }

    public Post(String postId, String postImage, String postCaption, String likes, String comments,
                String date, String time, String username, String userId) {
        this.postId = postId;
        this.postImage = postImage;
        this.postCaption = postCaption;
        this.likes = likes;
        this.comments = comments;
        this.date = date;
        this.time = time;
        this.username = username;
        this.userId = userId;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        postImage = in.readString();
        postCaption = in.readString();
        likes = in.readString();
        comments = in.readString();
        date = in.readString();
        time = in.readString();
        username = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(postImage);
        dest.writeString(postCaption);
        dest.writeString(likes);
        dest.writeString(comments);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(username);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostCaption() {
        return postCaption;
    }

    public void setPostCaption(String postCaption) {
        this.postCaption = postCaption;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
