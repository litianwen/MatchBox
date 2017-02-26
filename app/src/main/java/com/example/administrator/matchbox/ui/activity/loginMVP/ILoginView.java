package com.example.administrator.matchbox.ui.activity.loginMVP;

/**
 * Created by Administrator on 2016/12/1.
 */

public interface ILoginView {


    public void showDialog();

    public void dismissDialog();

    public void showToast(String msg);

    public void success();

    public String getUsername();

    public String getPassword();

}
