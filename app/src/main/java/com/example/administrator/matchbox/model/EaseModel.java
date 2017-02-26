package com.example.administrator.matchbox.model;

import android.os.Handler;
import android.os.Message;

import com.example.administrator.matchbox.utils.ContastUtils;
import com.example.administrator.matchbox.utils.LogUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by Administrator on 2016/12/13.
 */

public class EaseModel {
    //所有的环信操作

    //注册
    public static void register(final String userId) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    EMClient.getInstance().createAccount(userId, ContastUtils.ThreePassword);
                    LogUtils.e("环信注册成功");
                    Message msg = handler.obtainMessage();
                    msg.obj = userId;
                    handler.sendMessage(msg);
                } catch (HyphenateException e) {
                    LogUtils.e("环信注册失败");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            login(msg.obj.toString());
        }
    };

    public static void login(final String userId) {
        EMClient.getInstance().login(userId, ContastUtils.ThreePassword, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.e("环信登录成功");
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.e("环信登录失败" + s.toString());
                //注册以及登录
                register(userId);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
