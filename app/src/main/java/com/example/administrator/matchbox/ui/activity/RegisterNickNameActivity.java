package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.ui.activity.nicknameMVP.INickNameView;
import com.example.administrator.matchbox.ui.activity.nicknameMVP.NickNamePresenter;
import com.example.administrator.matchbox.utils.BoxUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/25.
 */

public class RegisterNickNameActivity extends BaseActivity implements TextWatcher, INickNameView {
    private static final int REQUEST_IMAGE = 100;
    @BindView(R.id.iv_headpic)
    ImageView ivHeadpic;
    @BindView(R.id.et_nickname)
    EditText etNickname;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    NickNamePresenter presenter;

    //注册的账号
    int userId;

    //注册的参数
    //电话号码
    //密码

    // 更新 昵称
    // 头像
    String phoneNumber;
    String password;

    //本地头像地址
    String path;

    @Override
    protected void initView() {
        phoneNumber = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        etNickname.addTextChangedListener(this);
        presenter = new NickNamePresenter(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_editinfo;
    }


    @OnClick({R.id.rl_selectImage, R.id.iv_clear, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_selectImage:
                startActivityForResult(new Intent(this, SelectorImageActivity.class), REQUEST_IMAGE);
                break;
            case R.id.iv_clear:
                etNickname.getEditableText().clear();
                break;
            case R.id.tv_submit:
                presenter.register();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().matches("[\\w\\-\\_]{2,20}")) {
            tvSubmit.setEnabled(true);
        } else {
            tvSubmit.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            path = data.getStringExtra("path");
            Glide.with(this).load(path)
                    .into(ivHeadpic);
        }
    }

    Dialog dialog;

    @Override
    public void showDialog() {
        dialog = BoxUtils.showProgressDialog(this);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                presenter.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public String getNickName() {
        return etNickname.getText().toString();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getImagePath() {
        return path;
    }

    @Override
    public void onSuccess() {
        //跳转到主界面
        startActivity(new Intent(this, HomeActivity.class));
    }


}

