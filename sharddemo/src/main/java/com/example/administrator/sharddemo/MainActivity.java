package com.example.administrator.sharddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = new Button(this);
        btn.setOnClickListener(this);
        setContentView(btn);
    }

    @Override
    public void onClick(View v) {
        ShareSDK.initSDK(this);
//        Platform platform = ShareSDK.getPlatform(this, QQ.NAME);
//        platform.setPlatformActionListener(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Log.e("TAG", "-------------------------------->>" + platform.toString());
//                Log.e("TAG", "------>>--->>" + hashMap.toString());
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        });
//        platform.showUser(null);
        OnekeyShare oneKey = new OnekeyShare();
        oneKey.setTitle("测试 标题");
        oneKey.setText("测试内容");
        oneKey.setUrl("http://www.baidu.com");
        oneKey.show(this);
    }
}
