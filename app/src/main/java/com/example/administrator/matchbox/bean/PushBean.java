package com.example.administrator.matchbox.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/12/15.
 */

public class PushBean extends DataSupport {
    public static final int TYPE_COMMENT = 1;
    public static final int TYPE_TOP = 2;
    public static final int TYPE_FANS = 3;

    public static final int TYPE_ALL = 4;


    @Override
    public String toString() {
        return "PushBean{" +
                "username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", url='" + url + '\'' +
                ", tv_content='" + tv_content + '\'' +
                ", iv_content='" + iv_content + '\'' +
                ", topic_name='" + topic_name + '\'' +
                ", article_id='" + article_id + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", createDate=" + createDate +
                ", type=" + type +
                ", isRead=" + isRead +
                '}';
    }

    String username;
    String userId;
    String url;


    String tv_content;
    String iv_content;
    String topic_name;
    String article_id;


    String commentContent; //Em表情

    long createDate;

    int type;
    boolean isRead;


    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTv_content() {
        return tv_content;
    }

    public void setTv_content(String tv_content) {
        this.tv_content = tv_content;
    }

    public String getIv_content() {
        return iv_content;
    }

    public void setIv_content(String iv_content) {
        this.iv_content = iv_content;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
