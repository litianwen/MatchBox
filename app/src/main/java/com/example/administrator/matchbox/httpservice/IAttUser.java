package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/8.
 */

public interface IAttUser {
    @POST(ServerInterface.USER_ATT_USER)
    @FormUrlEncoded
    Call<String> attUser(@Field("friend.user.id") String userId, @Field("friend.beuser.id") String friendId);
}
