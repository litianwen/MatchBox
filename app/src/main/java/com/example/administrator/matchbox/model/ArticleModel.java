package com.example.administrator.matchbox.model;

import android.support.annotation.Nullable;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.bean.CommentBean;
import com.example.administrator.matchbox.bean.LikeBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.httpservice.IGetArticleById;
import com.example.administrator.matchbox.httpservice.IGetAttArticle;
import com.example.administrator.matchbox.httpservice.IGetAttUserArticle;
import com.example.administrator.matchbox.httpservice.IGetCommentList;
import com.example.administrator.matchbox.httpservice.IGetHotOrNewArticle;
import com.example.administrator.matchbox.httpservice.IGetLikeList;
import com.example.administrator.matchbox.httpservice.ILikeArticle;
import com.example.administrator.matchbox.httpservice.IPublishArticle;
import com.example.administrator.matchbox.httpservice.IPublishComment;
import com.example.administrator.matchbox.httpservice.IUnLikeArticle;
import com.example.administrator.matchbox.interfaces.IArticle;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.utils.PushUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/12/6.
 */

public class ArticleModel implements IArticle {

    @Override
    public void publishArticle(@Nullable File file, int topicId, String msg, final IBeanCallback callback) {
        LogUtils.e(topicId);
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        int type = 1;
        MultipartBody.Part pic = null;
        if (file != null && file.exists()) {
            type = 2;
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            pic = MultipartBody.Part.createFormData("pic", file.getName(), body);
        }
        Call<String> call = retrofit.create(IPublishArticle.class).publishArticle(pic, type + "", MyApp.getInstance().getUserBean().getUserId() + "", topicId + "", msg);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject json = null;
                try {
                    json = new JSONObject(response.body());
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("数据解析异常");
                    return;
                }
                if (json.optString("result").equals("0")) {
                    callback.Success(null);
                } else {
                    callback.onError(json.optString("message"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //还没有开始请求就出错了
                callback.onError("网络请求失败");
            }
        });

    }

    @Override
    public void getAttentionArticle(final IBeanCallback<ArticleBean> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<ArticleBean> call = retrofit.create(IGetAttArticle.class).getAttArticle(MyApp.getInstance().getUserBean().getUserId() + "");
        call.enqueue(new Callback<ArticleBean>() {
            @Override
            public void onResponse(Call<ArticleBean> call, Response<ArticleBean> response) {
                if (response.body().getResult().equals("0")) {
                    if (response.body().getList() != null && response.body().getList().size() > 0)
                        callback.Success(response.body());
                    else
                        callback.onError("没有数据");
                } else {
                    callback.onError("请求失败");
                }
            }

            @Override
            public void onFailure(Call<ArticleBean> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });

    }

    @Override
    public void getAttentionUserArticle(final IBeanCallback<ArticleBean> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<ArticleBean> call = retrofit.create(IGetAttUserArticle.class).getAttUserArticle("" + MyApp.getInstance().getUserBean().getUserId());
        call.enqueue(new Callback<ArticleBean>() {
            @Override
            public void onResponse(Call<ArticleBean> call, Response<ArticleBean> response) {
                if (response.body().getResult().equals("0")) {
                    //但是没有数据
                    if (response.body().getList().size() == 0)
                        callback.onError("没有数据");
                    else
                        callback.Success(response.body());
                } else {
                    callback.onError("数据请求失败");
                }
            }

            @Override
            public void onFailure(Call<ArticleBean> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });

    }

    @Override
    public void likeAttention(final boolean b, final ArticleBean.ListBean bean, final IBeanCallback callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call;
        if (b) {
            call = retrofit.create(ILikeArticle.class).likeArticle(MyApp.getInstance().getUserBean().getUserId() + "", bean.getFriendId() + "");
        } else
            call = retrofit.create(IUnLikeArticle.class).unlikeArticle(MyApp.getInstance().getUserBean().getUserId() + "", bean.getFriendId() + "");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body());
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (callback != null)
                        callback.onError("数据解析失败");
                    return;
                }
                if (jsonObject.optString("result").equals("0")) {
                    if (callback != null)
                        callback.Success(null);
                    //点赞成功，取消点赞成功
                    if (b) {
                        PushUtils.sendTop(bean.getUserId() + "", bean);
                    }
                } else {
                    if (callback != null)
                        callback.onError(jsonObject.optString("message"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (callback != null)
                    callback.onError("网络请求异常");
            }
        });


    }

    @Override
    public void getLikeList(int articleId, final IBeanCallback<LikeBean> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<LikeBean> call = retrofit.create(IGetLikeList.class).getLikeList(articleId + "");
        call.enqueue(new Callback<LikeBean>() {
            @Override
            public void onResponse(Call<LikeBean> call, Response<LikeBean> response) {
                LogUtils.e(response.body());
                if (response.body().getResult().equals("0")) {
                    callback.Success(response.body());
                } else {
                    callback.onError("数据请求失败");
                }
            }

            @Override
            public void onFailure(Call<LikeBean> call, Throwable t) {
                LogUtils.e(t.getMessage());
                callback.onError("网络请求异常");
            }
        });


    }

    @Override
    public void publishComment(final int userId, final ArticleBean.ListBean bean, final String content, final IBeanCallback callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call = retrofit.create(IPublishComment.class).publishComment(userId + "", bean.getFriendId() + "", content);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LogUtils.e(response.body().toString());
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optString("result").equals("0")) {
                        callback.Success(null);
                        PushUtils.sendComment(bean.getUserId() + "", bean, content);
                    } else {
                        callback.onError(jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("数据解析异常");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });

    }

    @Override
    public void getCommentList(int articleId, final IBeanCallback<CommentBean> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<CommentBean> call = retrofit.create(IGetCommentList.class).getCommentList(articleId + "");
        call.enqueue(new Callback<CommentBean>() {
            @Override
            public void onResponse(Call<CommentBean> call, Response<CommentBean> response) {
                if (response.body().getResult().equals("0")) {
                    callback.Success(response.body());
                } else {
                    callback.onError("数据获取失败");
                }
            }

            @Override
            public void onFailure(Call<CommentBean> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });
    }

    @Override
    public void getArticleByType(int userId, int topicId, int type, final IBeanCallback<ArticleBean> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<ArticleBean> call = retrofit.create(IGetHotOrNewArticle.class).getArticleByType(userId + "", topicId + "", type);
        call.enqueue(new Callback<ArticleBean>() {
            @Override
            public void onResponse(Call<ArticleBean> call, Response<ArticleBean> response) {
                if (response.body().getResult().equals("0")) {
                    callback.Success(response.body());
                } else {
                    LogUtils.e(response.body().toString());
                    callback.onError("数据解析失败");
                }
            }

            @Override
            public void onFailure(Call<ArticleBean> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });
    }

    @Override
    public void getArticleByID(int userId, int friendId, final IBeanCallback<ArticleBean> callback) {
        Retrofit retrofit = MyApp.getInstance().getGsonRetrofit();
        Call<ArticleBean> call = retrofit.create(IGetArticleById.class).getArticleById(userId + "",
                friendId + "");
        call.enqueue(new Callback<ArticleBean>() {
            @Override
            public void onResponse(Call<ArticleBean> call, Response<ArticleBean> response) {
                if (response.body().getResult().equals("0")) {
                    callback.Success(response.body());
                } else
                    callback.onError("数据解析失败");
            }

            @Override
            public void onFailure(Call<ArticleBean> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });
    }

}
