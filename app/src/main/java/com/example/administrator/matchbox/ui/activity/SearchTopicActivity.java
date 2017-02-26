package com.example.administrator.matchbox.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.TopicAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.ITopic;
import com.example.administrator.matchbox.model.TopicModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 */

public class SearchTopicActivity extends BaseActivity implements TextWatcher, AdapterView.OnItemClickListener {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.lv_topic)
    ListView lvTopic;

    List<TopicBean> mList = new ArrayList<>();

    ITopic iTopic = new TopicModel();

    TopicAdapter adapter;

    @Override
    protected void initView() {
        etContent.addTextChangedListener(this);
        adapter = new TopicAdapter(this, mList);
        lvTopic.setAdapter(adapter);
        lvTopic.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_searchtopic;
    }


    @OnClick({R.id.iv_clear, R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etContent.getEditableText().clear();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            search();
        }
    };

    private void search() {
        iTopic.searchTopicList(etContent.getText().toString(), new IBeanCallback<List<TopicBean>>() {
            @Override
            public void Success(List<TopicBean> topicBeen) {
                mList.clear();
                mList.addAll(topicBeen);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (handler.hasMessages(1)) {
            handler.removeMessages(1);
        }
        if (s.length() > 0)
            handler.sendEmptyMessageDelayed(1, 500);
        else {
            mList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = getIntent();
        intent.putExtra("topic", mList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
