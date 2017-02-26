package com.example.administrator.matchbox.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.TopicAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.TopicBean;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/11.
 */

public class TopicListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_topic)
    ListView lvTopic;
    ArrayList<TopicBean> list;

    @Override
    protected void initView() {
        String title = getIntent().getStringExtra("title");
        list = (ArrayList<TopicBean>) getIntent().getSerializableExtra("topic");
        lvTopic.setAdapter(new TopicAdapter(this, list));
        tvTitle.setText(title);
        lvTopic.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_toiclist;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //跳转到详细页
        TopicBean bean = list.get(position);
        startActivity(new Intent(this, TopicDetailActivity.class)
                .putExtra("topic", bean));
    }
}
