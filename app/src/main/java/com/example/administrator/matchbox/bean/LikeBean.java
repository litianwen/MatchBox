package com.example.administrator.matchbox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public class LikeBean implements Serializable {

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

    public static class ListBean implements Serializable {
        /**
         * userId : 256
         * userName : 摄影君
         * userImg : /head/7101b068-9216-492c-89f3-d5d1034c250d.png
         */

        private int userId;
        private String userName;
        private String userImg;

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

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }
    }
}
