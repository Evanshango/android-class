package com.evans.test.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Post implements Parcelable {

    private String postId, postImage, postCaption, date, time;
    private List<LikePost> mLikes;
    private List<Comment> mComments;
    private User mUser;

    public Post() {
    }

    public Post(String postId, String postImage, String postCaption, String date, String time,
                List<LikePost> likes, List<Comment> comments, User user) {
        this.postId = postId;
        this.postImage = postImage;
        this.postCaption = postCaption;
        this.date = date;
        this.time = time;
        mLikes = likes;
        mComments = comments;
        mUser = user;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        postImage = in.readString();
        postCaption = in.readString();
        date = in.readString();
        time = in.readString();
        mUser = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(postImage);
        dest.writeString(postCaption);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeParcelable(mUser, flags);
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

    public List<LikePost> getLikes() {
        return mLikes;
    }

    public void setLikes(List<LikePost> likes) {
        mLikes = likes;
    }

    public List<Comment> getComments() {
        return mComments;
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
