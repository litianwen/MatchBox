package com.example.administrator.matchbox.bean;

/**
 * Created by Administrator on 2016/11/30.
 */

public class LoginBean {

    private String message;
    private String result;
    private String username;
    private String sex;
    private int userId;
    private String url;
    private String myInfo;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(String myInfo) {
        this.myInfo = myInfo;
    }
}
