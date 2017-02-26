package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/2.
 */

public interface ICreateTopic {

    @POST(ServerInterface.TOPIC_CREATE)
    @FormUrlEncoded
    Call<String> createTopic(@Field("name") String topicName, @Field("userId") String userId);
}
