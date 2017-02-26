package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface IGetTopicList {
    @POST(ServerInterface.TOPIC_HOTLIST)
    @FormUrlEncoded
    Call<String> getHotTopicList(@Field("userId") String userId);


    @POST(ServerInterface.UPDATE_TOPIC)
    @FormUrlEncoded
    Call<String> getUpdateTopicList(@Field("userId") String userId);


    @POST(ServerInterface.NEW_TOPIC)
    @FormUrlEncoded
    Call<String> getNewTopicList(@Field("userId") String userId);


}
