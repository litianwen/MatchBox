package com.example.administrator.matchbox.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/11.
 */

public class RecommendTopicBean {

    public RecommendTopicBean(String title, ArrayList<TopicBean> list) {
        this.title = title;
        this.list = list;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<TopicBean> getList() {
        return list;
    }

    public void setList(ArrayList<TopicBean> list) {
        this.list = list;
    }

    String title; //父窗体的名称

    ArrayList<TopicBean> list;

}
