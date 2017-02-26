package com.example.administrator.matchbox.utils;

import android.text.TextUtils;

import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.bean.PushBean;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.crud.DataSupport.where;

/**
 * Created by Administrator on 2016/12/15.
 */

public class PushUtils {
    //发信息
    public static final void sendFans(String userId) {
        PushBean bean = new PushBean();
        UserBean userBean = MyApp.getInstance().getUserBean();
        //设置请求好友的用户
        bean.setUsername(userBean.getUserName());
        bean.setUserId(userBean.getUserId() + "");
        if (!TextUtils.isEmpty(userBean.getUrl()))
            bean.setUrl(userBean.getUrl());
        bean.setCreateDate(System.currentTimeMillis());
        bean.setType(PushBean.TYPE_FANS);
        //转json
        Gson gson = new Gson();
        String json = gson.toJson(bean, PushBean.class);
        LogUtils.e2(json);
        sendMessage(userId, json);
    }

    //点赞 除了参数上的userId是对方的，其他都是自己信息
    public static final void sendTop(String userId, ArticleBean.ListBean articleBean) {
        UserBean userBean = MyApp.getInstance().getUserBean();
        PushBean pushBean = new PushBean();
        pushBean.setType(PushBean.TYPE_TOP);
        if (!TextUtils.isEmpty(userBean.getUrl()))
            pushBean.setUrl(userBean.getUrl());
        pushBean.setCreateDate(System.currentTimeMillis());
        pushBean.setUserId(userBean.getUserId() + "");
        pushBean.setUsername(userBean.getUserName());
        pushBean.setArticle_id(articleBean.getFriendId() + "");
        if (articleBean.getPhotoList().size() > 0)
            pushBean.setIv_content(articleBean.getPhotoList().get(0).getUrl());
        pushBean.setTopic_name(articleBean.getTopicName());
        pushBean.setTv_content(articleBean.getMsg());
        Gson gson = new Gson();
        String json = gson.toJson(pushBean, PushBean.class);
        sendMessage(userId, json);
    }

    //评论，多一个参数 除了参数上的userId是对方的，其他都是自己信息
    public static final void sendComment(String userId, ArticleBean.ListBean articleBean, String comment) {
        UserBean userBean = MyApp.getInstance().getUserBean();
        PushBean pushBean = new PushBean();
        pushBean.setType(PushBean.TYPE_COMMENT);
        if (!TextUtils.isEmpty(userBean.getUrl()))
            pushBean.setUrl(userBean.getUrl());
        pushBean.setCreateDate(System.currentTimeMillis());
        pushBean.setUserId(userBean.getUserId() + "");
        pushBean.setUsername(userBean.getUserName());
        pushBean.setArticle_id(articleBean.getFriendId() + "");
        if (articleBean.getPhotoList().size() > 0)
            pushBean.setIv_content(articleBean.getPhotoList().get(0).getUrl());
        pushBean.setTopic_name(articleBean.getTopicName());
        pushBean.setTv_content(articleBean.getMsg());
        pushBean.setCommentContent(comment);
        Gson gson = new Gson();
        String json = gson.toJson(pushBean, PushBean.class);
        sendMessage(userId, json);
    }


    private static final void sendMessage(String userId, String json) {
        //透传指令
        EMMessage emMessage = EMMessage.createSendMessage(EMMessage.Type.CMD);
        emMessage.setReceipt(userId);
        EMCmdMessageBody body = new EMCmdMessageBody(json);
        emMessage.addBody(body);
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }


    public static final void saveMessage(EMMessage emMessage) {
        if (emMessage.getType() == EMMessage.Type.CMD) {
            String action = ((EMCmdMessageBody) emMessage.getBody()).action();
            //json
            LogUtils.e2("获取到的透传消息指令：" + action);
            Gson gson = new Gson();
            PushBean bean = gson.fromJson(action, PushBean.class);
            //存储
            //如果为点赞，需要去重复
            if (bean.getType() == PushBean.TYPE_TOP) {
                PushBean song = where("userId = ? and article_id = ?", bean.getUserId(), bean.getArticle_id())
                        .findFirst(PushBean.class);
                if (song == null) {
                    //如果不存在记录 存储
                    bean.save();
                    LogUtils.e2("不存在，记录");
                } else {
                    //修改
                    song.setCreateDate(bean.getCreateDate());
                    song.setRead(false);
                    song.save();
                    LogUtils.e2("存在，修改");
                }
            } else {
                bean.save();
            }
        }
    }


    //从数据库中取出符合规范的数据
    public static final int getUnReadCountByType(int type) {
        List<PushBean> list;
        if (type != PushBean.TYPE_ALL) {
            list = DataSupport.where("type = ? and isRead = ?", type + "", "0").find(PushBean.class);
        } else {
            list = DataSupport.where("isRead = ?", "0").find(PushBean.class);
        }
        return list == null ? 0 : list.size();
    }

    public static final List<PushBean> getPushBeanByType(int type) {
        List<PushBean> list = DataSupport.where("type = ?", type + "").find(PushBean.class);
        return list;
    }

    public static final void setReadByType(int type) {
        List<PushBean> list = DataSupport.where("type = ?", type + "").find(PushBean.class);
        for (PushBean pushBean : list) {
            pushBean.setRead(true);
            pushBean.save();
        }
    }

}
