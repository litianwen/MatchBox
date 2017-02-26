package com.example.administrator.matchbox.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/7.
 */

public class TimeUtils {

    public static final long getLongTime(String time) {
        return getLongTime(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static final long getLongTime(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //将long转成字符串
    public static final String getStringTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    //将long转成字符串
    public static final String getStringTime(long time, String pattern) {
        return new SimpleDateFormat(pattern).format(new Date(time));
    }

    public static final String getFrontTime(String time) {
        long longTime = getLongTime(time);
        //比对当前时间
        //过去了多久
        return getFrontTime(longTime);
    }

    public static final String getFrontTime(long longTime) {
        long subTime = System.currentTimeMillis() - longTime;
        LogUtils.e(subTime / 1000 + "  " + subTime / 1000 / 60 + "   " + subTime / 1000 / 60 / 60 + "  " + subTime / 1000 / 60 / 60);
        if (subTime / 1000 < 60) {
            //60秒之内
            return subTime / 1000 + "秒之前";
        } else if (subTime / 1000 / 60 < 60) {
            return subTime / 1000 / 60 + "分钟之前";
        } else if (subTime / 1000 / 60 / 60 < 24) {
            return subTime / 1000 / 60 / 60 + "小时之前";
        } else if (subTime / 1000 / 60 / 60 / 24 < 365) {
            return subTime / 1000 / 60 / 60 / 24 + "天之前";
        } else {
            return "已过去太久。。。";
        }
    }
}
