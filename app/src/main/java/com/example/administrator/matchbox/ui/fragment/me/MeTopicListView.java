package com.example.administrator.matchbox.ui.fragment.me;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.matchbox.adapter.TopicAdapter;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.TopicModel;
import com.example.administrator.matchbox.utils.ListViewHeightUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/16.
 */

public class MeTopicListView extends ListView implements IBeanCallback<ArrayList<TopicBean>> {
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }


    public MeTopicListView(Context context) {
        super(context);
        doRequest();
    }

    public MeTopicListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void doRequest() {
        new TopicModel().getHotTopicList(this);
    }


    @Override
    public void Success(ArrayList<TopicBean> topicBeen) {
        setAdapter(new TopicAdapter(getContext(), topicBeen));
        ListViewHeightUtils.setListViewHeightBasedOnChildren(this);
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
