package com.example.administrator.matchbox.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5.
 */

public class TopicBean extends DataSupport implements Serializable {

    private String name;
    private int topicId;
    private int seeCount;

    @Override
    public String toString() {
        return "TopicBean{" +
                "name='" + name + '\'' +
                ", topicId=" + topicId +
                ", seeCount=" + seeCount +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getSeeCount() {
        return seeCount;
    }

    public void setSeeCount(int seeCount) {
        this.seeCount = seeCount;
    }
}
