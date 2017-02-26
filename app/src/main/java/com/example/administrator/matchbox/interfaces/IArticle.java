package com.example.administrator.matchbox.interfaces;

import android.support.annotation.Nullable;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.bean.CommentBean;
import com.example.administrator.matchbox.bean.LikeBean;

import java.io.File;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface IArticle {

    //发布文章
    public void publishArticle(@Nullable File file, int topicId, String msg, IBeanCallback callback);

    //获取我关注的所有文章
    public void getAttentionArticle(IBeanCallback<ArticleBean> callback);

    //获取我关注的好友的文章
    public void getAttentionUserArticle(IBeanCallback<ArticleBean> callback);

    //点赞和取消赞
    public void likeAttention(boolean b, ArticleBean.ListBean bean, IBeanCallback callback);

    //获取点赞用户列表
    public void getLikeList(int articleId, IBeanCallback<LikeBean> callback);

    //发布评论
    public void publishComment(int userId, ArticleBean.ListBean bean, String content, IBeanCallback callback);

    //获取评论列表
    public void getCommentList(int articleId, IBeanCallback<CommentBean> callback);

    //获取最新最热
    public void getArticleByType(int userId, int topicId, int type, IBeanCallback<ArticleBean> callback);

    //根据id获取发布的帖子
    public void getArticleByID(int userId, int friendId, IBeanCallback<ArticleBean> callback);
}
