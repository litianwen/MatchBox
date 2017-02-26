package com.example.administrator.matchbox.interfaces;

import com.example.administrator.matchbox.bean.TopicBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ITopic {

    //所有的接口都是 对于 话题的操作
    public void createTopic(String topicName, IBeanCallback callback);

    //获取热门话题
    public void getHotTopicList(IBeanCallback<ArrayList<TopicBean>> callback);

    //获取新话题
    public void getNewToicList(IBeanCallback<ArrayList<TopicBean>> callback);

    //获取有更新话题
    public void getUpdateToicList(IBeanCallback<ArrayList<TopicBean>> callback);

    //搜索话题
    public void searchTopicList(String key, IBeanCallback<List<TopicBean>> callback);

    public void addTopicList(List<TopicBean> list, IBeanCallback<Integer> callback);

    //关注话题
    public void attTopic(int userId, int topicId, boolean isAtt,IBeanCallback callback);


}
