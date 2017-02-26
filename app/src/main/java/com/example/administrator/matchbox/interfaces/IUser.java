package com.example.administrator.matchbox.interfaces;

import com.example.administrator.matchbox.bean.UserBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */

public interface IUser {
    public static final int TYPE_ATT = 1;
    public static final int TYPE_FANS = 2;


    //所有的用户操作都在这里
    //注册
    //登录
    //获取用户资料
    //修改资料
    //修改密码
    public void register(String username, String password, IBeanCallback<Integer> callback);

    public void login(String username, String password, IBeanCallback<Integer> callback);

    public void cancel();

    public void update(String userId, String nickName, String path, String myInfo, String sex, IBeanCallback callback);

    public void getUserInfo(String userId, IBeanCallback<UserBean> callback);

    //关注好友
    public void attUser(String userId, String firendId, IBeanCallback callback);

    //获取我关注的 或者 关注我的
    public void getAllAttention(int type, String userId, IBeanCallback<List<UserBean>> callback);


}
