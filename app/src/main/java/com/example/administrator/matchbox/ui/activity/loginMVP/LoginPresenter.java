package com.example.administrator.matchbox.ui.activity.loginMVP;

import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.IUser;
import com.example.administrator.matchbox.model.UserModel;

/**
 * Created by Administrator on 2016/12/1.
 */

public class LoginPresenter {

    IUser iUser;
    ILoginView iLoginView;

    public LoginPresenter(ILoginView iLoginView) {
        iUser = new UserModel();
        this.iLoginView = iLoginView;
    }


    public void login() {
        iLoginView.showDialog();
        iUser.login(iLoginView.getUsername(), iLoginView.getPassword(), new IBeanCallback<Integer>() {
            @Override
            public void Success(Integer integer) {
                if (integer == -1) {
                    iLoginView.showToast("没有这个用户");
                    iLoginView.dismissDialog();
                } else
                    //登录成功后，获取用户资料
                    getUserInfo(integer);
            }

            @Override
            public void onError(String msg) {
                iLoginView.dismissDialog();
                iLoginView.showToast(msg);
            }
        });
    }

    private void getUserInfo(Integer integer) {
        iUser.getUserInfo(integer + "", new IBeanCallback<UserBean>() {
            @Override
            public void Success(UserBean userBean) {
                MyApp.getInstance().setUserBean(userBean);
                iLoginView.dismissDialog();
                iLoginView.success();
            }

            @Override
            public void onError(String msg) {
                iLoginView.showToast(msg);
                iLoginView.dismissDialog();
            }
        });


    }


}
