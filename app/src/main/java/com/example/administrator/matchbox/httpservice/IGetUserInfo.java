package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/11/29.
 */

public interface IGetUserInfo {

    @POST(ServerInterface.USER_GETUSERINFO)
    @FormUrlEncoded
    Call<UserBean> getUsrInfo(@Field("userId") String userId);


}
