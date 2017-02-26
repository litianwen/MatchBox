package com.example.administrator.matchbox.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.ui.activity.HomeActivity;
import com.example.administrator.matchbox.ui.fragment.thirdMVP.IThirdpartyView;
import com.example.administrator.matchbox.ui.fragment.thirdMVP.ThirdpartyLoginPresenter;
import com.example.administrator.matchbox.utils.BoxUtils;

import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ThirdpartyLoginFragment extends BaseFragment implements IThirdpartyView {

    ThirdpartyLoginPresenter presenter;

    @Override
    protected void initView() {
        presenter = new ThirdpartyLoginPresenter(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_thirdparty_login;
    }

    @OnClick({R.id.iv_login_wechat, R.id.iv_login_weibo, R.id.iv_login_qq})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_wechat:
                break;
            case R.id.iv_login_weibo:
                presenter.sinaLogin();
                break;
            case R.id.iv_login_qq:
                presenter.qqLogin();
                break;
        }
    }


    Dialog dialog;

    @Override
    public void showDialog() {
        dialog = BoxUtils.showProgressDialog(getContext());
        dialog.show();
    }

    @Override
    public void dismissDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    @Override
    public void success() {
        startActivity(new Intent(getContext(), HomeActivity.class));
    }
}
