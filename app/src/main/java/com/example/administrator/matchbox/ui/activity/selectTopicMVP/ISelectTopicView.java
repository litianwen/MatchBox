package com.example.administrator.matchbox.ui.activity.selectTopicMVP;

import com.example.administrator.matchbox.bean.TopicBean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ISelectTopicView {

    public void showDialog();

    public void dismissDialog();

    //设置历史记录
    public void setHistoryTopic(List<TopicBean> list);

    //设置网络数据
    public void setHotTopic(List<TopicBean> list);

    public void showToast(String msg);


}
