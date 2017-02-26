package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.bean.RecommendTopicBean;
import com.example.administrator.matchbox.bean.TopicBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/11.
 */

public class RecommendTopicAdapter extends BaseExpandableListAdapter {

    Context mContext;
    List<RecommendTopicBean> mList;

    public RecommendTopicAdapter(Context mContext, List<RecommendTopicBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getList().size();
    }

    @Override
    public RecommendTopicBean getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public TopicBean getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_recommend, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (GroupHolder) convertView.getTag();
        holder.tvTitle.setText(getGroup(groupPosition).getTitle());
        holder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGroupMoreClickListener != null) {
                    onGroupMoreClickListener.onGroupClick(mList.get(groupPosition).getTitle());
                }
            }
        });
        return convertView;
    }


    public void setOnGroupMoreClickListener(OnGroupMoreClickListener onGroupMoreClickListener) {
        this.onGroupMoreClickListener = onGroupMoreClickListener;
    }

    OnGroupMoreClickListener onGroupMoreClickListener;

    public interface OnGroupMoreClickListener {
        public void onGroupClick(String title);
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_topic, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ChildHolder) convertView.getTag();
        TopicBean bean = getChild(groupPosition, childPosition);
        holder.tvTopicName.setText(bean.getName());
        holder.tvTopicSeeCount.setText(bean.getSeeCount() + " 次浏览");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_more)
        TextView tvMore;

        GroupHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildHolder {
        @BindView(R.id.tv_topic_name)
        TextView tvTopicName;
        @BindView(R.id.tv_topic_seeCount)
        TextView tvTopicSeeCount;

        ChildHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
