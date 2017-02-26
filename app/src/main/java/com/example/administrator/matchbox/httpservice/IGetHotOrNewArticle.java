package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/12.
 */

public interface IGetHotOrNewArticle {

    @POST(ServerInterface.GET_NEW_OR_HOT_Article)
    @FormUrlEncoded
    Call<ArticleBean> getArticleByType(@Field("userId") String userId, @Field("topicId") String topicId, @Field("type") int type);
}
