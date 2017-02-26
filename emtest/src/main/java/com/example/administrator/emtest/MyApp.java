package com.example.administrator.emtest;

import android.app.Application;

import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by Administrator on 2016/12/13.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //UI
        EMOptions options = new EMOptions();
        EaseUI.getInstance().init(this, options);
    }
}
