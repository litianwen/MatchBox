package com.example.administrator.matchbox.httpservice;

import com.example.administrator.matchbox.utils.ServerInterface;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2016/11/29.
 */

public interface IUpdate {

    @POST(ServerInterface.USER_UPDATE)
    @Multipart
        //文件上传 文件不带Part（）
    Call<String> userUpdate(@Part MultipartBody.Part doc,
                            @Part("user.id") String userId,
                            @Part("user.userName") String nickName,
                            @Part("user.sex") String sex,
                            @Part("user.myInfo") String info
    );


}
