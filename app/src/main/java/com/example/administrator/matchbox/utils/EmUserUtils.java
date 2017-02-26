package com.example.administrator.matchbox.utils;

import android.text.TextUtils;

import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.UserModel;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2016/12/14.
 */

public class EmUserUtils implements EaseUI.EaseUserProfileProvider {

    private static EmUserUtils instance = new EmUserUtils();

    public static EmUserUtils getInstance() {
        return instance;
    }

    UserModel userModel = new UserModel();

    private EmUserUtils() {
    }

    //存储到数据库
    //从数据库取出来

    //username --->  EaseUser
    //HashMap<String, EaseUser> map = new HashMap<>();
    //添加用户

    public void addUser(UserBean userBean) {
//        EaseUser user = new EaseUser(userBean.getUserId() + "");
//        user.setNickname(userBean.getUserName());
//        if (!TextUtils.isEmpty(userBean.getUrl()))
//            user.setAvatar(ServerInterface.getErrorImagePath(userBean.getUrl()));
//        map.put(userBean.getUserId() + "", user);
        //查找数据库中的这个人有没记录
        UserBean song = DataSupport.where("userId = ?",
                userBean.getUserId() + "").findFirst(UserBean.class);
        //如果有，更新
        if (song != null) {
            //有这个值
            song.setUserName(userBean.getUserName());
            if (!TextUtils.isEmpty(userBean.getUrl()))
                song.setUrl(ServerInterface.getErrorImagePath(userBean.getUrl()));
            song.save();
        } else {
            //存储
            if (!TextUtils.isEmpty(userBean.getUrl()))
                userBean.setUrl(ServerInterface.getErrorImagePath(userBean.getUrl()));
            userBean.save();
        }
    }

    public void addMe(UserBean userBean) {
        UserBean song = DataSupport.where("userId = ?",
                userBean.getUserId() + "").findFirst(UserBean.class);
        //如果有，更新
        if (song != null) {
            //有这个值
            song.setUserName(userBean.getUserName());
            if (!TextUtils.isEmpty(userBean.getUrl()))
                song.setUrl(ServerInterface.getImagePath(userBean.getUrl()));
            song.save();
        } else {
            if (!TextUtils.isEmpty(userBean.getUrl()))
                userBean.setUrl(ServerInterface.getImagePath(userBean.getUrl()));
            userBean.save();
        }
    }

    @Override
    public EaseUser getUser(String username) {
        UserBean song = DataSupport.where("userId = ?",
                username).findFirst(UserBean.class);
        EaseUser easeUser = new EaseUser(username);
        if (song != null) {
            if (!TextUtils.isEmpty(song.getUrl()))
                easeUser.setAvatar(song.getUrl());
            easeUser.setNickname(song.getUserName());
            LogUtils.e2(song.getUrl() != null ? song.getUrl() : "空");
        }
        return easeUser;
    }

    /**
     * 二级缓存
     * 磁盘
     * 网络
     *
     * @param callback
     * @param userid
     */
    public void getUser(final UserCallback callback, String userid) {
        UserBean song = DataSupport.where("userId = ?",
                userid).findFirst(UserBean.class);
        if (song != null) {
            EaseUser easeUser = new EaseUser(userid);
            if (!TextUtils.isEmpty(song.getUrl()))
                easeUser.setAvatar(song.getUrl());
            easeUser.setNickname(song.getUserName());
            callback.getUser(easeUser);
        } else {
            //请求网络
            LogUtils.e2("网络请求");
            userModel.getUserInfo(userid, new IBeanCallback<UserBean>() {
                @Override
                public void Success(UserBean userBean) {
                    LogUtils.e2(userBean);
                    EaseUser easeUser = new EaseUser(userBean.getUserId() + "");
                    if (!TextUtils.isEmpty(userBean.getUrl())) {
                        easeUser.setAvatar(ServerInterface.getImagePath(userBean.getUrl()));
                    }
                    easeUser.setNickname(userBean.getUserName());
                    callback.getUser(easeUser);
                    addUser(userBean);
                }

                @Override
                public void onError(String msg) {
                    callback.getUser(null);
                    LogUtils.e2(msg);
                }
            });

        }
    }

    public interface UserCallback {
        public void getUser(EaseUser easeUser);
    }




}
