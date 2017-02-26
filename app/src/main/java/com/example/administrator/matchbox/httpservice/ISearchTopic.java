package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface ISearchTopic {

    @POST(ServerInterface.SEARCH_TOPIC)
    @FormUrlEncoded
    Call<String> searchTopic(@Field("userId") String userId, @Field("user.name") String key);

}
