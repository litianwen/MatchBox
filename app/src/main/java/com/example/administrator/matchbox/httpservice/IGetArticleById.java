package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/16.
 */

public interface IGetArticleById {
    @POST(ServerInterface.GET_ARTICLE_BY_ID)
    @FormUrlEncoded
    Call<ArticleBean> getArticleById(@Field("userId") String userId, @Field("beuserId") String friendId);
}
