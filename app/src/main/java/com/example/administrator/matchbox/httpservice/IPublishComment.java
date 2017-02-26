package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/11.
 */

public interface IPublishComment {
    @POST(ServerInterface.PUBLISH_COMMENT)
    @FormUrlEncoded
    Call<String> publishComment(@Field("discuss.user.id") String userId, @Field("discuss.friendCircle.id") String articleId, @Field("discuss.content") String Content);
}
