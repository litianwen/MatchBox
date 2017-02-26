package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.bean.TopicBean;
import com.moxun.tagcloudlib.view.TagsAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */

public class CursorTagsAdapter extends TagsAdapter {


    private List<TopicBean> mList;

    public CursorTagsAdapter(@NonNull List<TopicBean> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        TextView tv = (TextView) View.inflate(context, R.layout.item_tag, null);
//        CheckBox checkBox = new CheckBox(context);
//        checkBox.setText(getItem(position).getName());
//        checkBox.setButtonDrawable(null);
//        checkBox.setTextSize(16);
//        checkBox.setTextColor(context.getResources().getColor(R.color.textcolor_tags));
        tv.setText(getItem(position).getName());
        return tv;
    }

    @Override
    public TopicBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 1;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
