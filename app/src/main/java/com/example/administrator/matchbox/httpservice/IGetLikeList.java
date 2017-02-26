package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.LikeBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/8.
 */

public interface IGetLikeList {
    @POST(ServerInterface.GET_LIKELIST)
    @FormUrlEncoded
    Call<LikeBean> getLikeList(@Field("friendCircle.id") String articleId);
}
