package com.example.administrator.matchbox.ui.activity.selectTopicMVP;

import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.ITopic;
import com.example.administrator.matchbox.model.TopicModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SelectTopicPresenter {
    //xutils 4个模块  afinal
    //glide + retrofit + greenDAO

    ISelectTopicView iSelectTopicView;
    ITopic iTopic;

    public SelectTopicPresenter(ISelectTopicView iSelectTopicView) {
        this.iSelectTopicView = iSelectTopicView;
        iTopic = new TopicModel();
    }

    public void getData() {
        iSelectTopicView.showDialog();
        //获取历史记录
        List<TopicBean> list = DataSupport.findAll(TopicBean.class);
        iSelectTopicView.setHistoryTopic(list);
        //获取网络记录
        iTopic.getHotTopicList(new IBeanCallback<ArrayList<TopicBean>>() {
            @Override
            public void Success(ArrayList<TopicBean> topicBean) {
                iSelectTopicView.dismissDialog();
                iSelectTopicView.setHotTopic(topicBean);
            }

            @Override
            public void onError(String msg) {
                iSelectTopicView.showToast(msg);
            }
        });
    }

}
