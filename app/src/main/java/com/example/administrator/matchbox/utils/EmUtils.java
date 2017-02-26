package com.example.administrator.matchbox.utils;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.example.administrator.matchbox.R;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/12/11.
 */

public class EmUtils {

    public static final SpannableStringBuilder getEmString(Context context, String string) {
        //将 普通的字符串 哈哈哈哈[id号]
        //转换成 对应的表情字符串
        //【java从入门到放弃】
        //哈哈哈哈[id号]djk[id号]asdf[][][]
        int index = 0;//索引号
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        while (string.length() > index && string.indexOf("【", index) != -1 && string.indexOf("】", index) != -1) {
            int start = string.indexOf("【", index);
            int end = string.indexOf("】", index);
            int id = 0;
            try {
                id = Integer.parseInt(string.substring(start + 1, end));
            } catch (Exception e) {
                index = end + 1;
                continue;
            }
            ImageSpan span;
            if (id > 106) { //
                span = new ImageSpan(context, id);
            } else {
                String sid = new DecimalFormat("000").format(id);
                try {
                    id = R.mipmap.class.getField("f_static_" + sid).getInt(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                span = new ImageSpan(context, id);
            }
            spannableStringBuilder.setSpan(span, start, end + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = end + 1;
        }
        return spannableStringBuilder;
    }
}
