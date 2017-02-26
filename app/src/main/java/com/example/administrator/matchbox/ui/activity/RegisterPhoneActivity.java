package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.CountriesBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.utils.CountriesUtils;
import com.example.administrator.matchbox.utils.NumberTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/11/23.
 */

public class RegisterPhoneActivity extends BaseActivity implements TextView.OnEditorActionListener, NumberTextWatcher.TextChangeListener, TextWatcher {
    @BindView(R.id.tv_server)
    TextView tvServer;
    @BindView(R.id.tv_countries)
    TextView tvCountries;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_send)
    TextView tv_send;

    String str = "《服务条款》";

    public static final int COUNTRY_CODE = 1;


    @Override
    protected void initView() {
        //设置服务
        initServerClause();

        NumberTextWatcher textWatcher = new NumberTextWatcher(etPhonenum);
        etPhonenum.addTextChangedListener(textWatcher);
        textWatcher.setTextChangeListener(this);
        etPhonenum.setOnEditorActionListener(this);
        etCode.addTextChangedListener(this);
    }

    private void initServerClause() {
        //Android 做文字处理   图文混排   改变颜色， 加斜体 粗体，下划线， 删除线 ，点击事件
        String str = tvServer.getText().toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        //ClickableSpan
        //ImageSpan
        //ForegroundColorSpan用颜色来标记
//        ImageSpan span = new ImageSpan(this, R.mipmap.ic_launcher);
//        ssb.setSpan(span, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(0x33, 0x66, 0x99));
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showToast("点击了服务条款");
            }
        };
        ssb.setSpan(span, str.indexOf("《"), str.indexOf("》") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ssb.setSpan(colorSpan, str.indexOf("《"), str.indexOf("》") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvServer.setText(ssb);
        tvServer.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register_phone;
    }


    @OnClick({R.id.tv_login, R.id.tv_countries, R.id.iv_clear, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.tv_countries:
                //获取国家列表，需要返回
                startActivityForResult(new Intent(getActivity(), SelectCountryActivity.class), COUNTRY_CODE);
//                CountriesUtils.getAllCountriesList();
                break;
            case R.id.iv_clear:
                etPhonenum.getEditableText().clear();
                break;
            case R.id.tv_send:
                checkSend();
                break;
        }
    }

    private void checkSend() {
        if (!tvCountries.getText().equals("国家代码无效")) {
            if (MyApp.getInstance().isPhoneVerify(etPhonenum.getText().toString().replace(" ", "")))
                send();
            else
                showToast("30秒内不能重复发送");
        } else
            showToast("国家代码无效");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COUNTRY_CODE && resultCode == RESULT_OK) {
            CountriesBean bean = (CountriesBean) data.getSerializableExtra("bean");
            tvCountries.setText(bean.getCountry());
            etCode.setText(bean.getArea());
        }
    }

    Dialog dialog;

    public void send() {
//        dialog = BoxUtils.showProgressDialog(this);
//        dialog.show();
//        //发送短信
//        SMSSDK.getVerificationCode(etCode.getText().toString(), etPhonenum.getText().toString().replace(" ", ""));

        jumpActivity();
    }


    @Override
    protected void onStart() {
        super.onStart();
        SMSSDK.registerEventHandler(eventHandler);
    }

    private EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            //需要判断result是否是 成功
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (result == SMSSDK.RESULT_COMPLETE) {
                switch (event) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        //获取验证码成功
                        //跳转界面
                        //showToast("获取验证码成功");
                        jumpActivity();
                        break;
                }
            } else {
                showToast("短信发送失败!");
            }
        }
    };

    private void jumpActivity() {
        Intent intent = new Intent(getActivity(), RegisterVerifyActivity.class);
        intent.putExtra("phone", etPhonenum.getText().toString().replace(" ", ""));
        intent.putExtra("country", etCode.getText().toString());
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (etPhonenum.getText().toString().replace(" ", "").matches("[1][34578][\\d]{9}"))
            checkSend();
        else
            showToast("手机号码不正确");
        return true;
    }

    @Override
    public void onTextChanged(String text) {
        if (text.length() > 0) {
            ivClear.setVisibility(View.VISIBLE);
            if (text.toString().matches("[1][34578][\\d]{9}")) {
                tv_send.setEnabled(true);
            } else
                tv_send.setEnabled(false);
        } else {
            ivClear.setVisibility(View.INVISIBLE);
            tv_send.setEnabled(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        for (CountriesBean countriesBean : CountriesUtils.getAllCountriesList()) {
            if (s.toString().equals(countriesBean.getArea())) {
                tvCountries.setText(countriesBean.getCountry());
                return;
            }
        }
        tvCountries.setText("国家代码无效");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
