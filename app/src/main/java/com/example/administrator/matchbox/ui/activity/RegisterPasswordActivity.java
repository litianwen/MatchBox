package com.example.administrator.matchbox.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/25.
 */

public class RegisterPasswordActivity extends BaseActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_password_vis)
    CheckBox cbPasswordVis;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    private String phoneNumber;

    @Override
    protected void initView() {
        phoneNumber = getIntent().getStringExtra("phone");
        etPassword.addTextChangedListener(this);
        cbPasswordVis.setOnCheckedChangeListener(this);
        etPassword.setOnEditorActionListener(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register_setpassword;
    }


    @OnClick({R.id.iv_clear, R.id.tv_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etPassword.getEditableText().clear();
                break;
            case R.id.tv_submit:
                next();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            ivClear.setVisibility(View.VISIBLE);
            if (s.length() >= 6) {
                tvSubmit.setEnabled(true);
            }
        } else {
            tvSubmit.setEnabled(false);
            ivClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        } else
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (etPassword.getText().length() >= 6)
            next();
        return true;
    }

    public void next() {
        Intent intent = new Intent(this, RegisterNickNameActivity.class);
        intent.putExtra("phone", phoneNumber);
        intent.putExtra("password", etPassword.getText().toString());
        startActivity(intent);
    }
}
