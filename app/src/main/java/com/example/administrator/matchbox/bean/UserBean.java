package com.example.administrator.matchbox.bean;

import android.text.TextUtils;

import com.example.administrator.matchbox.interfaces.IGetString;
import com.example.administrator.matchbox.utils.CharacterParser;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/29.
 */

public class UserBean extends DataSupport implements IGetString, Serializable {

    @Override
    public String toString() {
        return "UserBean{" +
                "result='" + result + '\'' +
                ", sex='" + sex + '\'' +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", userName='" + userName + '\'' +
                ", myActionCount='" + myActionCount + '\'' +
                ", fansCount='" + fansCount + '\'' +
                ", url='" + url + '\'' +
                ", myInfo='" + myInfo + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    private String result;
    private String sex;
    private int userId;
    private String name;
    private String userName;
    private String myActionCount;
    private String fansCount;
    private String url;
    private String myInfo;
    private String message;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        if (TextUtils.isEmpty(userName))
            setUserName("匿名");
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMyActionCount() {
        return myActionCount;
    }

    public void setMyActionCount(String myActionCount) {
        this.myActionCount = myActionCount;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
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

    @Override
    public String getString() {
        return CharacterParser.getInstance().getSelling(getUserName());
    }
}
