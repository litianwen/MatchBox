package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.HistoryAdapter;
import com.example.administrator.matchbox.adapter.TopicAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.ui.activity.selectTopicMVP.ISelectTopicView;
import com.example.administrator.matchbox.ui.activity.selectTopicMVP.SelectTopicPresenter;
import com.example.administrator.matchbox.utils.BoxUtils;
import com.example.administrator.matchbox.weiget.ScrollGridView;
import com.example.administrator.matchbox.weiget.ScrollListView;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SelectTopicActivity extends BaseActivity implements ISelectTopicView, AdapterView.OnItemClickListener {
    private static final int REQUEST_SERACH = 11;
    @BindView(R.id.gv_topic)
    ScrollGridView gvTopic;
    @BindView(R.id.lv_topic)
    ScrollListView lvTopic;

    List<TopicBean> hotList;
    List<TopicBean> historyList;

    @BindView(R.id.tv_history)
    TextView tv_history;

    SelectTopicPresenter presenter;

    @Override
    protected void initView() {
        //获取数据
        presenter = new SelectTopicPresenter(this);
        presenter.getData();
        lvTopic.setOnItemClickListener(this);
        gvTopic.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_select_topic;
    }


    @OnClick({R.id.tv_back, R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.ll_search:
                startActivityForResult(new Intent(this, SearchTopicActivity.class), REQUEST_SERACH);
                break;
        }
    }

    Dialog dialog;

    @Override
    public void showDialog() {
        dialog = BoxUtils.showProgressDialog(this);
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void setHistoryTopic(List<TopicBean> list) {
        if (list != null && list.size() > 0) {
            if (list.size() % 2 == 1)
                list.remove(list.size() - 1);
            historyList = list;
            tv_history.setVisibility(View.GONE);
            gvTopic.setAdapter(new HistoryAdapter(this, list));
        }
    }

    @Override
    public void setHotTopic(List<TopicBean> list) {
        hotList = list;
        //需要显示数据
        TopicAdapter adapter = new TopicAdapter(this, list);
        lvTopic.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //带参数返回
        Intent intent = getIntent();
        TopicBean topic;
        if (parent.getId() == R.id.lv_topic)
            topic = hotList.get(position);
        else
            topic = historyList.get(position);
        intent.putExtra("topic", topic);
        // 存储数据
        TopicBean bean = DataSupport.where("topicid = ?", topic.getTopicId() + "").findFirst(TopicBean.class);
        if (bean == null) {
            //找到了，；历史记录中已经有了该对象。
            //没有这个数据，存储
            topic.save();
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SERACH && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
