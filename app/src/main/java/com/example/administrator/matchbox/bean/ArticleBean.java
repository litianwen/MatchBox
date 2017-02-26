package com.example.administrator.matchbox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ArticleBean implements Serializable {

    private String result;
    private java.util.List<ListBean> List;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "result='" + result + '\'' +
                ", List=" + List +
                '}';
    }

    public static class ListBean implements Serializable {

        private int isAction;
        private int type;
        private String msg;
        private int friendId;
        private int topCount;
        private int shareCount;
        private String imgUrl;
        private int isCollection;
        private int userId;
        private int topicId;
        private String userName;
        private int isTop;
        private String createDate;
        private String topicName;
        private int discussCount;
        private java.util.List<PhotoListBean> photoList;

        public int getIsAction() {
            return isAction;
        }

        public void setIsAction(int isAction) {
            this.isAction = isAction;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getFriendId() {
            return friendId;
        }

        public void setFriendId(int friendId) {
            this.friendId = friendId;
        }

        public int getTopCount() {
            return topCount;
        }

        public void setTopCount(int topCount) {
            this.topCount = topCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public void setShareCount(int shareCount) {
            this.shareCount = shareCount;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(int isCollection) {
            this.isCollection = isCollection;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getTopicId() {
            return topicId;
        }

        public void setTopicId(int topicId) {
            this.topicId = topicId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getIsTop() {
            return isTop;
        }

        public void setIsTop(int isTop) {
            this.isTop = isTop;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "isAction=" + isAction +
                    ", type=" + type +
                    ", msg='" + msg + '\'' +
                    ", friendId=" + friendId +
                    ", topCount=" + topCount +
                    ", shareCount=" + shareCount +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", isCollection=" + isCollection +
                    ", userId=" + userId +
                    ", topicId=" + topicId +
                    ", userName='" + userName + '\'' +
                    ", isTop=" + isTop +
                    ", createDate='" + createDate + '\'' +
                    ", topicName='" + topicName + '\'' +
                    ", discussCount=" + discussCount +
                    ", photoList=" + photoList +
                    '}';
        }

        public int getDiscussCount() {
            return discussCount;
        }

        public void setDiscussCount(int discussCount) {
            this.discussCount = discussCount;
        }

        public List<PhotoListBean> getPhotoList() {
            return photoList;
        }

        public void setPhotoList(List<PhotoListBean> photoList) {
            this.photoList = photoList;
        }

        public static class PhotoListBean implements Serializable {
            @Override
            public String toString() {
                return "PhotoListBean{" +
                        "imgUrl='" + imgUrl + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }

            /**
             * imgUrl : /photo/165eb88c-c824-4e8a-a082-230040a54bd3.png
             * url : /photo/035d1375-e72d-4cd3-a456-bc3a6aafd82b.png
             */


            private String imgUrl;
            private String url;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
