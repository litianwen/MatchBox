package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/8.
 */

public interface IGetAttUserArticle {
    @POST(ServerInterface.GETATT_USER_ARTICLE)
    @FormUrlEncoded
    Call<ArticleBean> getAttUserArticle(@Field("userId") String userId);
}
