package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.CountriesBean;
import com.example.administrator.matchbox.ui.activity.loginMVP.ILoginView;
import com.example.administrator.matchbox.ui.activity.loginMVP.LoginPresenter;
import com.example.administrator.matchbox.utils.BoxUtils;
import com.example.administrator.matchbox.utils.CountriesUtils;
import com.example.administrator.matchbox.utils.NumberTextWatcher;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/1.
 */

public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnFocusChangeListener, ILoginView, NumberTextWatcher.TextChangeListener {
    private static final int REQIEST_COUNTRY = 111;
    @BindView(R.id.tv_countries)
    TextView tvCountries;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_phonenum)
    EditText etPhonenum;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_password_vis)
    CheckBox cbPasswordVis;
    @BindView(R.id.iv_clear2)
    ImageView ivClear2;
    @BindView(R.id.tv_login)
    TextView tv_login;

    List<CountriesBean> countryList;

    LoginPresenter presenter;

    @Override
    protected void initView() {
        presenter = new LoginPresenter(this);
        cbPasswordVis.setOnCheckedChangeListener(this);
        etCode.addTextChangedListener(codeTextChangedListener);
        countryList = CountriesUtils.getAllCountriesList();
        etPassword.setOnFocusChangeListener(this);
        etPhonenum.setOnFocusChangeListener(this);


        NumberTextWatcher textWatcher = new NumberTextWatcher(etPhonenum);
        textWatcher.setTextChangeListener(this);
        etPhonenum.addTextChangedListener(textWatcher);

        etPassword.addTextChangedListener(passwordTextChangedListener);
    }

    TextWatcher passwordTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                ivClear2.setVisibility(View.VISIBLE);
                if (etPhonenum.getText().length() > 0)
                    tv_login.setEnabled(true);
            } else {
                tv_login.setEnabled(false);
                ivClear2.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

//    TextWatcher phonenubTextChangedListener = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.length() > 0) {
//                ivClear.setVisibility(View.VISIBLE);
//                if (etPassword.getText().toString().length() > 0) {
//                    tv_login.setEnabled(true);
//                }
//            } else {
//                tv_login.setEnabled(false);
//                ivClear.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

    private TextWatcher codeTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for (CountriesBean countriesBean : countryList) {
                if (countriesBean.getArea().equals(s.toString())) {
                    tvCountries.setText(countriesBean.getCountry());
                    return;
                }
            }
            tvCountries.setText("国家代码无效");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }


    @OnClick({R.id.tv_countries, R.id.iv_clear, R.id.iv_clear2, R.id.tv_register, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_countries:
                startActivityForResult(new Intent(this, SelectCountryActivity.class), REQIEST_COUNTRY);
                break;
            case R.id.iv_clear:
                etPhonenum.getEditableText().clear();
                break;
            case R.id.iv_clear2:
                etPassword.getEditableText().clear();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterPhoneActivity.class));
                break;
            case R.id.tv_login:
                presenter.login();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQIEST_COUNTRY && resultCode == RESULT_OK) {
            CountriesBean bean = (CountriesBean) data.getSerializableExtra("bean");
            etCode.setText(bean.getArea());
            tvCountries.setText(bean.getCountry());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //密码显示与隐藏
        etPassword.setInputType(isChecked ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == etPhonenum.getId()) {
            if (hasFocus) {
                if (etPhonenum.getText().toString().length() > 0)
                    ivClear.setVisibility(View.VISIBLE);
                else
                    ivClear.setVisibility(View.INVISIBLE);
                ivClear2.setVisibility(View.INVISIBLE);
            }
        } else if (v.getId() == etPassword.getId()) {
            if (hasFocus) {
                if (etPassword.getText().toString().length() > 0)
                    ivClear2.setVisibility(View.VISIBLE);
                else
                    ivClear2.setVisibility(View.INVISIBLE);
                ivClear.setVisibility(View.INVISIBLE);
            }
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
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void success() {
        startActivity(new Intent(this, HomeActivity.class));
    }

    @Override
    public String getUsername() {
        return etPhonenum.getText().toString().replace(" ", "");
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void onTextChanged(String text) {
        if (text.length() > 0) {
            ivClear.setVisibility(View.VISIBLE);
            if (etPassword.getText().toString().length() > 0) {
                tv_login.setEnabled(true);
            }
        } else {
            tv_login.setEnabled(false);
            ivClear.setVisibility(View.INVISIBLE);
        }
    }
}
