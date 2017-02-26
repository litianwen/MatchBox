package com.example.administrator.matchbox.ui.activity.createTopicMVP;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ICreateTopicView {


    public void showDialog();

    public void dismissDialog();

    public void showToast(String msg);

    public String getTopicName();

    public void success();

}
