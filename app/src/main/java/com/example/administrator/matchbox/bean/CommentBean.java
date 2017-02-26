package com.example.administrator.matchbox.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class CommentBean {


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

    public static class ListBean {

        private String content;
        private int dicussId;
        private int userId;
        private String userName;
        private String createDate;
        private String userImg;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDicussId() {
            return dicussId;
        }

        public void setDicussId(int dicussId) {
            this.dicussId = dicussId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }
    }
}
