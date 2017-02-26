package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface IPublishArticle {
    @POST(ServerInterface.PUBLISH_ARTICLE)
    @Multipart
    Call<String> publishArticle(@Part MultipartBody.Part pic, @Part("type") String type, @Part("friendCircle.user.id") String userId, @Part("friendCircle.topic.id") String topicId, @Part("friendCircle.msg") String content);
}
