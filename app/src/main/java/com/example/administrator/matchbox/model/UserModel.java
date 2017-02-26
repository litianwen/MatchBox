package com.example.administrator.matchbox.model;

import com.example.administrator.matchbox.bean.LoginBean;
import com.example.administrator.matchbox.bean.ResigerBean;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.httpservice.IAttUser;
import com.example.administrator.matchbox.httpservice.IAttention;
import com.example.administrator.matchbox.httpservice.IGetUserInfo;
import com.example.administrator.matchbox.httpservice.ILogin;
import com.example.administrator.matchbox.httpservice.IRegister;
import com.example.administrator.matchbox.httpservice.IUpdate;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.IUser;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.utils.PushUtils;
import com.example.administrator.matchbox.utils.SPHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/11/29.
 */

public class UserModel implements IUser {
    Call call;


    /**
     * 环信 注册
     *
     * @param username userId
     * @param password 设置一个统一的密码
     */


    @Override
    public void register(String username, String password, final IBeanCallback<Integer> callback) {

        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        //转换成Call
        Call<ResigerBean> call = retrofit.create(IRegister.class).register(username, password);
        this.call = call;
        call.enqueue(new Callback<ResigerBean>() {
            @Override
            public void onResponse(Call<ResigerBean> call, Response<ResigerBean> response) {
                switch (response.body().getResult()) {
                    case "0":
                        //返回userId
                        callback.Success(response.body().getUserId());
                        //第一次登陆，存储一个键值对
                        SPHelper sp = new SPHelper(MyApp.getInstance(), "user");
                        sp.save(new SPHelper.ContentValue("firstLogin", true), new SPHelper.ContentValue("attention", true));
                        //继续注册环信
                        EaseModel.register(response.body().getUserId() + "");
                        break;
                    case "1":
                        callback.onError("密码不能为空");
                        break;
                    case "2":
                        callback.onError("手机号不能为空");
                        break;
                    case "3":
                        callback.onError("用户已存在，请重新注册");
                        break;

                }
            }

            @Override
            public void onFailure(Call<ResigerBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                callback.onError("网络请求失败");
            }
        });

    }

    @Override
    public void login(String username, String password, final IBeanCallback<Integer> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<LoginBean> call = retrofit.create(ILogin.class).login(username, password);
        call.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                //判断是否登录成功
                if (response.body().getResult().equals("0")) {
                    callback.Success(response.body().getUserId());
                    EaseModel.login(response.body().getUserId() + "");
                } else {
                    //没有这个用户
                    callback.Success(-1);
                }
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });

    }


    @Override
    public void cancel() {
        if (call != null)
            call.cancel();
    }

    @Override
    public void update(String userId, String nickName, String path, String myInfo, String sex, final IBeanCallback callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        //转换文件
        MultipartBody.Part doc = null;
        if (path != null) {
            File file = new File(path);
            //将文件写入body 请求内容体
            RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
            doc = MultipartBody.Part.createFormData("doc", file.getName(), body);
        }
        LogUtils.e("-------" + path);
        //使用内容体去创建一个表单对象

        Call<String> call = retrofit.create(IUpdate.class).userUpdate(doc, userId, nickName, sex, myInfo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LogUtils.e("----------->" + response.body());
                try {
                    JSONObject json = new JSONObject(response.body());
                    if (json.optString("result").equals("0")) {
                        callback.Success(null);
                    } else {
                        callback.onError(json.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });

    }

    @Override
    public void getUserInfo(String userId, final IBeanCallback<UserBean> callback) {
        LogUtils.e2(userId);
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<UserBean> call = retrofit.create(IGetUserInfo.class).getUsrInfo(userId);
        call.enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                UserBean userBean = response.body();
                if (userBean.getResult().equals("0"))
                    callback.Success(userBean);
                else {
                    callback.onError(userBean.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                callback.onError("网络请求失败");
            }
        });
    }

    @Override
    public void attUser(final String userId, final String firendId, final IBeanCallback callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call = retrofit.create(IAttUser.class).attUser(userId, firendId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("数据转换失败");
                    return;
                }
                if (jsonObject.optString("result").equals("0")) {
                    callback.Success("关注成功");
                    PushUtils.sendFans(firendId);
                } else
                    callback.onError(jsonObject.optString("message"));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });
    }

    @Override
    public void getAllAttention(int type, String userId, final IBeanCallback<List<UserBean>> callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call;
        if (type == TYPE_ATT)
            call = retrofit.create(IAttention.class).getAllAttention(userId);
        else
            call = retrofit.create(IAttention.class).getAllFans(userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optString("result").equals("0")) {
                        JSONArray Firends = jsonObject.optJSONArray("Firends");
                        Gson gson = new Gson();
                        List<UserBean> list = gson.fromJson(Firends.toString(), new TypeToken<List<UserBean>>() {
                        }.getType());
                        if (list == null || list.size() == 0)
                            callback.onError("快去添加好友吧！");
                        callback.Success(list);
                    } else {
                        callback.onError("数据解析失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("数据解析失败");
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });
    }


}
