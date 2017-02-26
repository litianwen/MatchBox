package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.bean.CommentBean;
import com.example.administrator.matchbox.utils.ServerInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/12/11.
 */

public interface IGetCommentList {
    @POST(ServerInterface.GET_COMMENT_LIST)
    @FormUrlEncoded
    Call<CommentBean> getCommentList(@Field("friendCircle.id") String articleId);
}
