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
import com.example.administrator.matchbox.bean.PushBean;
import com.example.administrator.matchbox.utils.EmUtils;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.example.administrator.matchbox.utils.TimeUtils;
import com.example.administrator.matchbox.weiget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/15.
 */

public class CommentListAdapter extends ListItemAdapter<PushBean> {

    public CommentListAdapter(Context context, List<PushBean> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_list_commant, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        PushBean bean = getItem(position);
        if (!TextUtils.isEmpty(bean.getUrl())) {
            Glide.with(getContext()).load(ServerInterface.getImagePath(bean.getUrl())).into(holder.ivHeadpic);
        } else {
            holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        }
        holder.tvUsername.setText(TextUtils.isEmpty(bean.getUsername()) ? "匿名" : bean.getUsername());
        //没有图片才显示文字
        if (!TextUtils.isEmpty(bean.getIv_content())) {
            //设置图片
            holder.tvContent.setVisibility(View.GONE);
            holder.ivContent.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(ServerInterface.getImagePath(bean.getIv_content())).into(holder.ivContent);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.ivContent.setVisibility(View.GONE);
            holder.tvContent.setText(bean.getTv_content());
        }
        holder.tvTime.setText(TimeUtils.getFrontTime(bean.getCreateDate()));
        holder.tvComment.setText(EmUtils.getEmString(getContext(), bean.getCommentContent()));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_headpic)
        CircleImageView ivHeadpic;
        @BindView(R.id.tv_comment)
        TextView tvComment;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.iv_content)
        ImageView ivContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
