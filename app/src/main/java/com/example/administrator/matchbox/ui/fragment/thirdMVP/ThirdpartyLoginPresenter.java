package com.example.administrator.matchbox.ui.fragment.thirdMVP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.IUser;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.utils.ContastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ThirdpartyLoginPresenter {


    IUser iUser;
    IThirdpartyView iThirdpartyView;

    public ThirdpartyLoginPresenter(IThirdpartyView iThirdpartyView) {
        this.iThirdpartyView = iThirdpartyView;
        iUser = new UserModel();
    }

    public void qqLogin() {
        iThirdpartyView.showDialog();
        Platform platform = ShareSDK.getPlatform(iThirdpartyView.getContext(), QQ.NAME);
        startLogin(platform);
    }


    //登录
    private void startLogin(Platform platform) {
        //为了到主线程
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String userId = msg.getData().getString("userId");
                String username = msg.getData().getString("username");
                String path = msg.getData().getString("path");
                String sex = msg.getData().getString("sex");
                Login(userId, username, path, sex);
            }
        };
        //设置访问 第三方服务器资料的监听
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(final Platform platform, int i, HashMap<String, Object> hashMap) {
                //第三方登录  接口有问题
                //直接去 服务器登录
                //登录成功，获取资料
                //登录失败，注册，上传用户信息，获取资料
                //openID 或者 手机号
                //LogUtils.e(platform.getDb().getUserId() + "  " + platform.getDb().getUserGender() + "  " + platform.getDb().getUserIcon());
                //必须在主线程中使用框架去调用网络

                //将所有需要的数据拿出来 并且 发送到主线程
                String userId = platform.getDb().getUserId();
                String username = platform.getDb().getUserName();
                String path = platform.getDb().getUserIcon();
                String sex = platform.getDb().getUserGender().equals("m") ? "男" : "女";
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);
                bundle.putString("username", username);
                bundle.putString("path", path);
                bundle.putString("sex", sex);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }


            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                iThirdpartyView.dismissDialog();
                iThirdpartyView.showToast("授权失败" + throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                iThirdpartyView.showToast("用户取消");
                iThirdpartyView.dismissDialog();
            }
        });
        platform.showUser(null);
    }

    private void Login(final String userId, final String username, final String path, final String sex) {

        //登录 判断第三方账号是否注册过
        //已注册，。调用获取信息
        //未注册，。开始注册
        iUser.login(userId, ContastUtils.ThreePassword, new IBeanCallback<Integer>() {
            @Override
            public void Success(Integer integer) {
                //成功还是失败
                if (integer == -1) {
                    //失败
                    //注册
                    //第三方账号第一次登陆
                    register(userId, path, username, sex);

                } else {
                    //获取信息
                    getUserInfo(integer);
                }
            }

            @Override
            public void onError(String msg) {
                iThirdpartyView.showToast(msg);
                iThirdpartyView.dismissDialog();
            }
        });
    }

    private void register(String userId, final String path, final String username, final String sex) {
        iUser.register(userId, ContastUtils.ThreePassword, new IBeanCallback<Integer>() {
            @Override
            public void Success(final Integer integer) {
                //修改资料
                //要将网络图片保存在本地。在进行上传
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        update(integer, (File) msg.obj, username, sex);
                    }
                };
                new Thread() {
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL(path);
                            InputStream inputStream = url.openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            File file = new File(ContastUtils.IMAGE_PATH, System.currentTimeMillis() + ".jpg");
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            Message msg = handler.obtainMessage();
                            msg.obj = file;
                            handler.sendMessage(msg);
                            //开始将第三方账户的信息更新到服务器

                        } catch (Exception e) {
                            iThirdpartyView.dismissDialog();
                            e.printStackTrace();
                        }
                    }
                }.start();

            }

            @Override
            public void onError(String msg) {
                iThirdpartyView.dismissDialog();
                iThirdpartyView.showToast(msg);
            }
        });
    }

    private void update(final Integer integer, File file, String username, String sex) {
        iUser.update(integer + "", username, file.getAbsolutePath(), "", sex, new IBeanCallback() {
            @Override
            public void Success(Object o) {
                //修改成功
                //需要继续获取信息
                getUserInfo(integer);
                iThirdpartyView.dismissDialog();
            }

            @Override
            public void onError(String msg) {
                iThirdpartyView.showToast(msg);
                iThirdpartyView.dismissDialog();
            }
        });
    }

    private void getUserInfo(Integer integer) {
        iUser.getUserInfo(integer + "", new IBeanCallback<UserBean>() {
            @Override
            public void Success(UserBean userBean) {
                MyApp.getInstance().setUserBean(userBean);
                iThirdpartyView.success();
                iThirdpartyView.dismissDialog();
            }

            @Override
            public void onError(String msg) {
                iThirdpartyView.showToast(msg);
                iThirdpartyView.dismissDialog();
            }
        });
    }

    public void sinaLogin() {
        iThirdpartyView.showDialog();
        Platform platform = ShareSDK.getPlatform(iThirdpartyView.getContext(), SinaWeibo.NAME);
        startLogin(platform);
    }


}
