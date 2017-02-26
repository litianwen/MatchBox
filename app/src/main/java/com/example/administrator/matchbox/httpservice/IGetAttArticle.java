package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/7.
 */

public interface IGetAttArticle {
    @POST(ServerInterface.GETATT_ARTICLE)
    @FormUrlEncoded
    Call<ArticleBean> getAttArticle(@Field("userId") String userId);
}
