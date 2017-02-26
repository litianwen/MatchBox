package com.example.administrator.matchbox.utils;

/**
 * Created by Administrator on 2016/11/29.
 */

public class ServerInterface {
    //基地址后面带/
    //路径不要带
    public static final String BASE_URL = "http://118.192.147.148/Matchbox/";

    //获取图片地址
    public static final String getImagePath(String path) {
        return BASE_URL + path;
    }

    //获取错误图片地址
    public static final String getErrorImagePath(String path) {
        LogUtils.e(path.replace("http://123.184.33.74:8080", BASE_URL));
        return path.replace("http://123.184.33.74:8080/Matchbox/", BASE_URL);
    }

    /**
     * 用户操作
     **/

    public static final String USER_REGISTER = "userregist";

    public static final String USER_UPDATE = "userupdateUserInfo";

    public static final String USER_GETUSERINFO = "usergetUserInfoById";

    public static final String USER_LOGIN = "useruserlogin";

    public static final String USER_ATT_USER = "useraddFocus";

    //获取我关注的
    public static final String USER_GET_ATTENTION = "usergetMyAction";

    //获取关注我的
    public static final String USER_GET_FANS = "usergetMyFans";

    /**
     * 话题操作
     */
    public static final String TOPIC_CREATE = "usersaveTopic";

    //热门话题
    public static final String TOPIC_HOTLIST = "usergetTopicListHot";

    //新话题
    public static final String NEW_TOPIC = "usergetTopicListNew";

    //有更新的话题
    public static final String UPDATE_TOPIC = "usergetTopicListHasNew";


    //搜索
    public static final String SEARCH_TOPIC = "usergetTopicList";


    //关注话题
    public static final String ADD_TOPIC = "useraddFocusTopic";
    //取消关注
    public static final String DE_ADD_TOPIC = "usercancleFocusTopic";

    /**
     * 帖子操作
     */
    public static final String PUBLISH_ARTICLE = "userpostFriendCircle";
    //获取关注话题的帖子
    public static final String GETATT_ARTICLE = "usergetMyActionTopIcByUserId";
    //获取关注好友的帖子
    public static final String GETATT_USER_ARTICLE = "usergetMyFriendPost";
    //点赞和取消赞
    public static final String LIKE_ARTICLE = "useraddTopForFriend";
    public static final String UN_LIKE_ARTICLE = "usercancleTop";

    //点赞列表
    public static final String GET_LIKELIST = "usergetTopUrlByFriendId";

    //发布评论
    public static final String PUBLISH_COMMENT = "useraddDiscuss";

    //获取评论列表
    public static final String GET_COMMENT_LIST = "usergetDiscussByFriendId";

    //通过id和话题获取最新最热
    public static final String GET_NEW_OR_HOT_Article = "usergetHotOrNewFriendCircles";

    //获取我的帖子或者好友的帖子
    public static final String GET_ARTICLE_BY_ID = "usergetMyFriendCircles";
}
