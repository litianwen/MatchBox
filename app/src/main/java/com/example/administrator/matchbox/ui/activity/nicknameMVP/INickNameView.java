package com.example.administrator.matchbox.ui.activity.nicknameMVP;

/**
 * Created by Administrator on 2016/11/29.
 */

public interface INickNameView {
    //Activity操作
    public void showDialog();

    public void dismissDialog();

    public String getUsername();

    public String getNickName();

    public String getPassword();

    public String getImagePath();

    public void showToast(String msg);

//    public void setUserId(int id);
//
//    public int getUserId();

    public void onSuccess();
}
