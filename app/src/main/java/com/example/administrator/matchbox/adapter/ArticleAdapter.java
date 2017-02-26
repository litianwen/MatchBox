package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.ui.activity.ArticleDetailActivity;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.example.administrator.matchbox.utils.TimeUtils;
import com.example.administrator.matchbox.weiget.ZoomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/7.
 */

public class ArticleAdapter extends ListItemAdapter<ArticleBean.ListBean> {
    private ArticleModel articleModel;
    private UserModel userModel;
    private boolean isShowTopic = true;

    public ArticleAdapter(Context context, List<ArticleBean.ListBean> list, boolean isShowTopic) {
        super(context, list);
        articleModel = new ArticleModel();
        userModel = new UserModel();
        this.isShowTopic = isShowTopic;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_article, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        final ArticleBean.ListBean bean = getItem(position);
        if (isShowTopic) {
            holder.tvTopicName.setText(bean.getTopicName());
        } else {
            holder.tvTopicName.setVisibility(View.GONE);
        }

        //加载头像
        if (TextUtils.isEmpty(bean.getImgUrl()))
            holder.ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        else
            Glide.with(mContext).load(ServerInterface.getImagePath(bean.getImgUrl())).into(holder.ivHeadpic);
        //用户名称
        holder.tvUsername.setText(bean.getUserName());
        if (bean.getPhotoList().size() > 0) {
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            holder.ivContent.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(ServerInterface.getImagePath(bean.getPhotoList().get(0).getUrl()))
                    .override(width, width).into(holder.ivContent);
        } else {
            holder.ivContent.setVisibility(View.GONE);
        }
        //文本
        if (TextUtils.isEmpty(bean.getMsg())) {
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(bean.getMsg());
        }
        //时间
        holder.tvTime.setText(TimeUtils.getFrontTime(bean.getCreateDate()));
        //点赞数量
        int likeCount = bean.getTopCount();
        if (likeCount == 0) {
            holder.cbLike.setText("");
        } else {
            holder.cbLike.setText(likeCount + "");
        }
        //当前是否已经赞过了
        if (bean.getIsTop() == 1) {
            holder.cbLike.setChecked(true);
        } else
            holder.cbLike.setChecked(false);
        //点赞
        holder.cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始状态
                boolean b = bean.getIsTop() == 1 ? true : false;
                //点赞变成赞
                b = !b;
                articleModel.likeAttention(b, bean, null);
                //将数据改变回来 ，为了不请求数据
                bean.setIsTop(b ? 1 : 0);
                bean.setTopCount(b ? bean.getTopCount() + 1 : bean.getTopCount() - 1);
                //将视图修改，防止过多刷新适配器
                holder.cbLike.setChecked(b);
                if (bean.getTopCount() == 0)
                    holder.cbLike.setText("");
                else
                    holder.cbLike.setText(bean.getTopCount() + "");
            }
        });
        //加关注
        //判断是否已经关注
        if (bean.getIsAction() == 1)//已关注
        {
            holder.tvAttention.setText("已关注");
            holder.tvAttention.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.tvAttention.setOnClickListener(null);
        } else {
            holder.tvAttention.setText("");
            holder.tvAttention.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.topic_thread_follow_friend, 0, 0, 0);
            holder.tvAttention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //添加关注
                    userModel.attUser("" + MyApp.getInstance().getUserBean().getUserId(), "" + bean.getUserId(), new IBeanCallback() {
                        @Override
                        public void Success(Object o) {
                            //如果需要整个listView中 所有的关注用户都改变，遍历集合 刷新适配器
                            for (ArticleBean.ListBean listBean : mList) {
                                if (listBean.getUserId() == bean.getUserId()) {
                                    listBean.setIsAction(1);
                                }
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

        //评论
        if (bean.getDiscussCount() != 0) {
            holder.tvComment.setText(bean.getDiscussCount() + "");
        } else {
            holder.tvComment.setText("");
        }
        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要带帖子跳转
                mContext.startActivity(new Intent(mContext, ArticleDetailActivity.class).putExtra("article", bean));
            }
        });

        return convertView;
    }

    public void setIsShowTopic(boolean isShowTopic) {
        this.isShowTopic = isShowTopic;
    }

    static class ViewHolder {
        @BindView(R.id.tv_topic_name)
        TextView tvTopicName;
        @BindView(R.id.tv_attention)
        TextView tvAttention;
        @BindView(R.id.iv_headpic)
        ImageView ivHeadpic;
        @BindView(R.id.tv_username)
        TextView tvUsername;
        @BindView(R.id.iv_share)
        ImageView ivShare;
        @BindView(R.id.iv_content)
        ImageView ivContent;
        @BindView(R.id.tv_content)
        ZoomTextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.cb_like)
        CheckBox cbLike;
        @BindView(R.id.tv_comment)
        TextView tvComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
