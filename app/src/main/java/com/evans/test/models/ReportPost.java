package com.evans.test.models;

public class ReportPost {

    private String reportId, postId;

    public ReportPost() {
    }

    public ReportPost(String reportId, String postId) {
        this.reportId = reportId;
        this.postId = postId;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
