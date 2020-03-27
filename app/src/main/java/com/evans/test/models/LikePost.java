package com.evans.test.models;

public class LikePost {

    private String likeId, postId, username, userId;

    public LikePost() {
    }

    public LikePost(String likedId, String postId, String username, String userId) {
        this.likeId = likedId;
        this.postId = postId;
        this.username = username;
        this.userId = userId;
    }

    public String getLikeId() {
        return likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
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
