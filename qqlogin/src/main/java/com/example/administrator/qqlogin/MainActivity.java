package com.example.administrator.qqlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class MainActivity extends AppCompatActivity {

    TextView tv_info;

    Tencent mTencent;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_info = (TextView) findViewById(R.id.tv_info);
        mTencent = Tencent.createInstance("1105313966", this);
    }

    public void login(View v) {

        mTencent.login(this, "all", listener);

    }

    IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Log.e("TAG", "---------->>" + o.toString());
            Bean json = JSON.parseObject(o.toString(), Bean.class);
            //openid 在qq上是唯一的
            //expires_in 有效期
            //access_token 访问令牌
            //获取用户信息
            mTencent.setOpenId(json.getOpenid());
            mTencent.setAccessToken(json.getAccess_token(), json.getExpires_in() + "");
            userInfo = new UserInfo(getBaseContext(), mTencent.getQQToken());
            userInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object o) {
                    Log.e("TAG", "---" + o.toString());
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }

        /**
         * {"is_yellow_year_vip":"0","ret":0,"figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1105313966\/01940110A516C20A3870D8D10602E7C1\/40","figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1105313966\/01940110A516C20A3870D8D10602E7C1\/100",
         * "nickname":"   ❤  合奏ゞ情绪、","yellow_vip_level":"0","is_lost":0,"msg":"","city":"荆州",
         * "figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105313966\/01940110A516C20A3870D8D10602E7C1\/50","vip":"0",
         * "level":"0","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105313966\/01940110A516C20A3870D8D10602E7C1\/100",
         * "province":"湖北","is_yellow_vip":"0","gender":"男",
         * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1105313966\/01940110A516C20A3870D8D10602E7C1\/30"}
         *
         * @param uiError
         */

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResultData(requestCode, resultCode, data, listener);
    }
}
