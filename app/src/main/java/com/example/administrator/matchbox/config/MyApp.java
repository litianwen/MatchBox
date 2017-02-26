package com.example.administrator.matchbox.config;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.ui.activity.MainActivity;
import com.example.administrator.matchbox.utils.EmUserUtils;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.utils.PushUtils;
import com.example.administrator.matchbox.utils.SPHelper;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import org.litepal.LitePal;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/11/23.
 */

public class MyApp extends Application implements EMConnectionListener, EMMessageListener {
    /**
     * 共享数据
     */

    /**
     * 初始化第三方sdk
     * retrofit
     * glide
     */
    public static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化短信验证
        SMSSDK.initSDK(this, "194c5152fd6ae", "39c5da494c32ae21da2a3bf516f8e42a");
        ShareSDK.initSDK(this);
        LitePal.initialize(this);
        EMOptions options = new EMOptions();
        EaseUI.getInstance().init(this, options);
        EMClient.getInstance().addConnectionListener(this);
        EMClient.getInstance().chatManager().addMessageListener(this);
    }


    //循环遍历退出
    private Set<Activity> activityList = new HashSet<>();


    //添加activity
    public void addActivity(Activity a) {
        activityList.add(a);
    }

    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
    }

    //防止电话号码重复发送
    private HashMap<String, Long> verifyMap = new HashMap<String, Long>();

    public void addVerifyPhone(String phoneNum, int time) {
        //毫秒
        //系统当前时间+ 还剩余的时间= 什么时间能在发送
        verifyMap.put(phoneNum, System.currentTimeMillis() + time * 1000);
    }

    public boolean isPhoneVerify(String phonenumber) {
        Long time = verifyMap.get(phonenumber);
        if (time == null)
            return true;
        if (System.currentTimeMillis() > time) {
            verifyMap.remove(phonenumber);
            return true;
        } else
            return false;
    }

    /**
     * 所有本地图片
     **/
    private List<File> imgList;

    public List<File> getImgList() {
        return imgList;
    }

    public void setImgList(List<File> imgList) {
        this.imgList = imgList;
    }


    /**
     * 打开网络请求
     */
    public Retrofit getGsonRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public Retrofit getStringRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerInterface.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit;
    }


    //在这里确定是否登录
    private UserBean userBean;

    public boolean isLogin() {

        LogUtils.e(userBean == null);
        LogUtils.e(userBean.getUserId());
        return userBean != null && userBean.getUserId() > 0;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
        //数据存储
        SPHelper sp = new SPHelper(this, "user");
        sp.save(new SPHelper.ContentValue("userId", userBean.getUserId()));
        EaseUI.getInstance().setUserProfileProvider(EmUserUtils.getInstance());
        EmUserUtils.getInstance().addMe(userBean);
    }

    public void clearAutoLogin() {
        SPHelper sp = new SPHelper(this, "user");
        sp.clear();
        userBean = null;
    }

    @Override
    public void onConnected() {
        //环信登录成功
        LogUtils.e("---环信自动登录");
    }

    @Override
    public void onDisconnected(int i) {
        if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
            Toast.makeText(instance, "账号在其他设备上登录", Toast.LENGTH_SHORT).show();
            Logout();
        }
    }

    private void Logout() {
        //  EMClient.getInstance().logout(true);
        clearAutoLogin();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        //透传指令到达
        for (EMMessage emMessage : list) {
            //很多消息
            PushUtils.saveMessage(emMessage);
        }
        //发广播
        sendBroadcast(new Intent("newMessage"));
    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}

