package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.LoginBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface ILogin {

    @POST(ServerInterface.USER_LOGIN)
    @FormUrlEncoded
    Call<LoginBean> login(@Field("name") String username, @Field("password") String password);
}
