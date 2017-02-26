package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface IAddTopic {

    @POST(ServerInterface.ADD_TOPIC)
    @FormUrlEncoded
    Call<String> addTopic(@Field("userTopic.user.id") String userId, @Field("userTopic.topic.id") String topicId);


    @POST(ServerInterface.DE_ADD_TOPIC)
    @FormUrlEncoded
    Call<String> deAddTopic(@Field("userTopic.user.id") String userId, @Field("userTopic.topic.id") String topicId);
}
