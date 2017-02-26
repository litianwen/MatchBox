package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.TopicBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/5.
 */

public class TopicAdapter extends ListItemAdapter<TopicBean> {

    public TopicAdapter(Context context, List<TopicBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_topic, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        TopicBean bean = getItem(position);
        holder.tvTopicName.setText(bean.getName());
        holder.tvTopicSeeCount.setText(bean.getSeeCount() + " 次浏览");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_topic_name)
        TextView tvTopicName;
        @BindView(R.id.tv_topic_seeCount)
        TextView tvTopicSeeCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
