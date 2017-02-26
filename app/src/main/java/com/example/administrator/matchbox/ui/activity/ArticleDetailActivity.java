package com.example.administrator.matchbox.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CommentAdapter;
import com.example.administrator.matchbox.adapter.LikeAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.bean.CommentBean;
import com.example.administrator.matchbox.bean.LikeBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.utils.SPHelper;
import com.example.administrator.matchbox.utils.ServerInterface;
import com.example.administrator.matchbox.weiget.ChatPopupWindow;
import com.example.administrator.matchbox.weiget.LikeListView;
import com.example.administrator.matchbox.weiget.ScrollListView;
import com.example.administrator.matchbox.weiget.ZoomTextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.text.DecimalFormat;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/8.
 */

public class ArticleDetailActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView>, CompoundButton.OnCheckedChangeListener {

    ArticleBean.ListBean bean;

    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.cb_like)
    CheckBox cbLike;
    @BindView(R.id.tv_topic_name)
    TextView tvTopicName;
    @BindView(R.id.iv_headpic)
    ImageView ivHeadpic;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.tv_content)
    ZoomTextView tvContent;
    @BindView(R.id.llv_like)
    LikeListView llvLike;
    @BindView(R.id.slv_command)
    ScrollListView slvCommand;
    @BindView(R.id.ptrsv)
    PullToRefreshScrollView ptrsv;


    ArticleModel articleModel = new ArticleModel();

    @Override
    protected void initView() {
        bean = (ArticleBean.ListBean) getIntent().getSerializableExtra("article");
        ptrsv.setOnRefreshListener(this);
        //请求点赞列表
        //请求评论列表
        initArticle();
        //初始化点赞列表
        initLikeList();
        //初始化评论列表
        initComment();
        initLike();

    }

    private void initLike() {
        //判断点赞
        if (bean.getIsTop() == 1) {
            cbLike.setChecked(true);
        }
        cbLike.setOnCheckedChangeListener(this);
    }

    private void initComment() {
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        articleModel.getCommentList(bean.getFriendId(), new IBeanCallback<CommentBean>() {
            @Override
            public void Success(CommentBean commentBean) {
                slvCommand.setAdapter(new CommentAdapter(getBaseContext(), commentBean.getList()));
                ptrsv.onRefreshComplete();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                ptrsv.onRefreshComplete();
            }
        });
    }

    LikeBean likeBean;

    private void initLikeList() {
        articleModel.getLikeList(bean.getFriendId(), new IBeanCallback<LikeBean>() {
            @Override
            public void Success(LikeBean likeBean) {
                ArticleDetailActivity.this.likeBean = likeBean;
                llvLike.setAdapter(new LikeAdapter(getBaseContext(), likeBean.getList()));
            }

            @Override
            public void onError(String msg) {
                showToast("点赞列表请求失败");
            }
        });
    }

    private void initArticle() {
        tvTopicName.setText(bean.getTopicName());
        tvUsername.setText(bean.getUserName());
        if (TextUtils.isEmpty(bean.getImgUrl())) {
            ivHeadpic.setImageResource(R.mipmap.icon_register_avatar_default);
        } else {
            Glide.with(this).load(ServerInterface.getImagePath(bean.getImgUrl()))
                    .into(ivHeadpic);
        }
        if (bean.getPhotoList().size() > 0) {
            Glide.with(this).load(ServerInterface.getImagePath(bean.getPhotoList().get(0).getUrl())).into(ivContent);
        } else {
            ivContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(bean.getMsg())) {
            tvContent.setText(bean.getMsg());
        } else {
            tvContent.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_article_detail;
    }


    @OnClick({R.id.iv_more, R.id.iv_emoticons, R.id.tv_send_comment, R.id.llv_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                break;
            case R.id.iv_emoticons:
                showChatPopupwindow();
                break;
            case R.id.tv_send_comment:
                sendComment();
                break;
            case R.id.llv_like:
                //跳转到点赞详细页
                startActivity(new Intent(this, LikeActivity.class).putExtra("id", bean.getFriendId()));
                break;
        }
    }

    private void sendComment() {
        if (etComment.getText().toString().equals("")) {
            showToast("内容不能为空");
        } else {
            String content = etComment.getText().toString();
            articleModel.publishComment(MyApp.getInstance().getUserBean().getUserId(), bean, content, new IBeanCallback() {
                @Override
                public void Success(Object o) {
                    showToast("发布成功");
                    etComment.getEditableText().clear();
                    //刷新适配器
                    initComment();
                    //将发送的数量保存
                    SPHelper spHelper = new SPHelper(getBaseContext(), "article");
                    int size = spHelper.getInt("command", 0) + 1;
                    spHelper.save(new SPHelper.ContentValue("command", size), new SPHelper.ContentValue("id", bean.getFriendId()));
                }

                @Override
                public void onError(String msg) {
                    showToast(msg);
                }
            });
        }
    }

    //【001】
    private void showChatPopupwindow() {
        etComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(etComment.getSelectionStart());
            }
        });
        ChatPopupWindow window = new ChatPopupWindow(this, new ChatPopupWindow.Callback() {
            @Override
            public void callback(int id) {
                //替换当前光标所在的地方
                Editable editable = etComment.getEditableText();
                int start = etComment.getSelectionStart();
                String sid = new DecimalFormat("000").format(id);
                //插入一个表情
                try {
                    id = R.mipmap.class.getField("f_static_" + sid).getInt(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                ImageSpan span = new ImageSpan(getBaseContext(), id);
                String format = "【" + sid + "】"; //[001]
                editable.insert(start, format);
                etComment.getEditableText().setSpan(span, start, start + format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                LogUtils.e(start);
                start += format.length();
                etComment.setSelection(start);
            }
        });
        window.showAsDropDown(etComment);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        initComment();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        articleModel.likeAttention(isChecked, bean, new IBeanCallback() {
            @Override
            public void Success(Object o) {
                //后面需要做操作 推送消息
                SPHelper spHelper = new SPHelper(getBaseContext(), "article");
                //-1代表没有
                spHelper.save(new SPHelper.ContentValue("like", isChecked ? 1 : 0), new SPHelper.ContentValue("id", bean.getFriendId()));
                //点赞成功
                //根据当前的isChecked是true还是false来判断是移除还是添加
                if (likeBean == null)
                    return;
                if (isChecked) {
                    //添加
                    LikeBean.ListBean like = new LikeBean.ListBean();
                    like.setUserId(MyApp.getInstance().getUserBean().getUserId());
                    like.setUserImg(MyApp.getInstance().getUserBean().getUrl());
                    like.setUserName(MyApp.getInstance().getUserBean().getUserName());
                    likeBean.getList().add(0, like);
                } else {
                    //移除
                    Iterator<LikeBean.ListBean> it = likeBean.getList().iterator();
                    while (it.hasNext()) {
                        LikeBean.ListBean b = it.next();
                        if (b.getUserId() == MyApp.getInstance().getUserBean().getUserId()) {
                            it.remove();
                        }
                    }
                }
                //刷新
                llvLike.setAdapter(new LikeAdapter(getBaseContext(), likeBean.getList()));
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
