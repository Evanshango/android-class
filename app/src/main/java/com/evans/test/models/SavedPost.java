package com.evans.test.models;

public class SavedPost {

    private String savedPostId;
    private Post mPost;

    public SavedPost() {
    }

    public SavedPost(String savedPostId, Post post) {
        this.savedPostId = savedPostId;
        mPost = post;
    }

    public String getSavedPostId() {
        return savedPostId;
    }

    public void setSavedPostId(String savedPostId) {
        this.savedPostId = savedPostId;
    }

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        mPost = post;
    }
}
