package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.ui.activity.createTopicMVP.CreateTopicPresenter;
import com.example.administrator.matchbox.ui.activity.createTopicMVP.ICreateTopicView;
import com.example.administrator.matchbox.utils.BoxUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */

public class CreateNewTopicActivity extends BaseActivity implements TextWatcher, ICreateTopicView {

    @BindView(R.id.et_topic)
    EditText etTopic;
    @BindView(R.id.tv_finish)
    TextView tvFinish;

    CreateTopicPresenter presenter;

    @Override
    protected void initView() {
        etTopic.addTextChangedListener(this);
        presenter = new CreateTopicPresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_newtopic;
    }


    @OnClick(R.id.tv_finish)
    public void onClick() {
        presenter.createTopic();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0)
            tvFinish.setEnabled(true);
        else
            tvFinish.setEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

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
    public String getTopicName() {
        return etTopic.getText().toString();
    }

    @Override
    public void success() {
        showToast("创建成功");
        finish();
    }

}
