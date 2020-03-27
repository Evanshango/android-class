package com.evans.test.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Post implements Parcelable {

    private String postId, postImage, postCaption, date, time, author;
    private List<LikePost> mLikes;
    private List<Comment> mComments;

    public Post() {
    }

    public Post(String postId, String postImage, String postCaption, String date, String time,
                String author, List<LikePost> likes, List<Comment> comments) {
        this.postId = postId;
        this.postImage = postImage;
        this.postCaption = postCaption;
        this.date = date;
        this.time = time;
        this.author = author;
        mLikes = likes;
        mComments = comments;
    }

    protected Post(Parcel in) {
        postId = in.readString();
        postImage = in.readString();
        postCaption = in.readString();
        date = in.readString();
        time = in.readString();
        author = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(postImage);
        dest.writeString(postCaption);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(author);
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
