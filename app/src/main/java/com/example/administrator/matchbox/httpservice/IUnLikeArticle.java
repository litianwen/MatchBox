package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface IUnLikeArticle {

    @POST(ServerInterface.UN_LIKE_ARTICLE)
    @FormUrlEncoded
    Call<String> unlikeArticle(@Field("userId") String userId, @Field("friendId") String articleId);
}
