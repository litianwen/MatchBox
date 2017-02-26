package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/13.
 */

public interface IAttention {

    //获取我关注的
    @POST(ServerInterface.USER_GET_ATTENTION)
    @FormUrlEncoded
    Call<String> getAllAttention(@Field("userId") String userId);

    //获取我关注的
    @POST(ServerInterface.USER_GET_FANS)
    @FormUrlEncoded
    Call<String> getAllFans(@Field("userId") String userId);


}
