package com.example.administrator.matchbox.ui.activity.nicknameMVP;

import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.IUser;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.utils.LogUtils;

/**
 * Created by Administrator on 2016/11/29.
 */
public class NickNamePresenter {

    IUser iUser;
    INickNameView iNickNameView;

    public NickNamePresenter(INickNameView iNickNameView) {
        this.iNickNameView = iNickNameView;
        iUser = new UserModel();
    }

    //两个公开方法
    //注册
    //取消
    public void register() {
        iNickNameView.showDialog();
        iUser.register(iNickNameView.getUsername(), iNickNameView.getPassword(), new IBeanCallback<Integer>() {
            @Override
            public void Success(Integer integer) {
                //iNickNameView.dismissDialog();
                update(integer);
            }

            @Override
            public void onError(String msg) {
                iNickNameView.showToast(msg);
                iNickNameView.dismissDialog();
            }
        });
    }

    public void cancel() {
        iUser.cancel();
    }


    //修改资料
    public void update(final int id) {
        iUser.update(id + "", iNickNameView.getNickName(), iNickNameView.getImagePath(), "", "", new IBeanCallback() {
            @Override
            public void Success(Object o) {
                //获取用户资料
                iUser.getUserInfo(id + "", new IBeanCallback<UserBean>() {
                    @Override
                    public void Success(UserBean userBean) {
                        //数据已经拿到
                        //登录成功
                        MyApp.getInstance().setUserBean(userBean);
                        LogUtils.e(userBean.toString());
                        iNickNameView.dismissDialog();
                        iNickNameView.onSuccess();
                    }

                    @Override
                    public void onError(String msg) {
                        iNickNameView.showToast(msg);
                        iNickNameView.dismissDialog();
                    }
                });
            }

            @Override
            public void onError(String msg) {
                iNickNameView.showToast(msg);
                iNickNameView.dismissDialog();
            }
        });
    }
}


