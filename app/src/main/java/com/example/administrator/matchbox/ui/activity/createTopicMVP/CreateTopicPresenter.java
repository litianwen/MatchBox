package com.example.administrator.matchbox.ui.activity.createTopicMVP;

import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.ITopic;
import com.example.administrator.matchbox.model.TopicModel;

/**
 * Created by Administrator on 2016/12/2.
 */

public class CreateTopicPresenter {

    ICreateTopicView iCreateTopicView;
    ITopic iTopic;

    public CreateTopicPresenter(ICreateTopicView iCreateTopicView) {
        this.iCreateTopicView = iCreateTopicView;
        iTopic = new TopicModel();
    }

    public void createTopic() {
        iCreateTopicView.showDialog();
        iTopic.createTopic(iCreateTopicView.getTopicName(), new IBeanCallback() {
            @Override
            public void Success(Object o) {
                iCreateTopicView.dismissDialog();
                iCreateTopicView.success();
            }

            @Override
            public void onError(String msg) {
                iCreateTopicView.dismissDialog();
                iCreateTopicView.showToast(msg);
            }
        });

    }

}
