package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.CommentBean;
import com.example.administrator.matchbox.utils.EmUtils;
import com.example.administrator.matchbox.utils.ServerInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/11.
 */

public class CommentAdapter extends ListItemAdapter<CommentBean.ListBean> {

    public CommentAdapter(Context context, List<CommentBean.ListBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_commend, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        CommentBean.ListBean bean = getItem(position);
        if (TextUtils.isEmpty(bean.getUserImg())) {
            holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        } else {
            Glide.with(getContext()).load(ServerInterface.getImagePath(bean.getUserImg()))

                    .into(holder.ivHeadpic);
        }
        if (TextUtils.isEmpty(bean.getUserName())) {
            holder.tvUsername.setText("匿名");
        } else {
            holder.tvUsername.setText(bean.getUserName());
        }
        holder.tvTime.setText(bean.getCreateDate());
        //需要修改 表情
        holder.tvContent.setText(EmUtils.getEmString(getContext(), bean.getContent()));
        //所有的适配器都不能返回null
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_headpic)
        ImageView ivHeadpic;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
