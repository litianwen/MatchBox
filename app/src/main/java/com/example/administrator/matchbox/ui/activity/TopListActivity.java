package com.example.administrator.matchbox.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.TopListAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.bean.PushBean;
import com.example.administrator.matchbox.utils.PushUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/15.
 */

public class TopListActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
    @BindView(R.id.lv_content)
    PullToRefreshListView lvContent;
    List<PushBean> mList;

    @Override
    protected void initView() {
        lvContent.setOnRefreshListener(this);
        lvContent.setRefreshing();
        lvContent.smoothScrollTo(-100);
        doRequest();
        lvContent.setOnItemClickListener(this);
    }


    private void doRequest() {
        mList = PushUtils.getPushBeanByType(PushBean.TYPE_TOP);
        Collections.sort(mList, new Comparator<PushBean>() {
            @Override
            public int compare(PushBean o1, PushBean o2) {
                return (int) (o2.getCreateDate() - o1.getCreateDate());
            }
        });
        TopListAdapter adapter = new TopListAdapter(getBaseContext(), mList);
        lvContent.setAdapter(adapter);
        lvContent.onRefreshComplete();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_commentlist;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        doRequest();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        PushBean bean = mList.get(position - 1);
        ArticleBean.ListBean articleBean = new ArticleBean.ListBean();
        articleBean.setUserName(bean.getUsername());
        //articleBean.setIsTop();
        articleBean.setFriendId(Integer.parseInt(bean.getArticle_id()));
        articleBean.setUserId(Integer.parseInt(bean.getUserId()));
        articleBean.setImgUrl(bean.getUrl());
        articleBean.setMsg(bean.getTv_content());
        List<ArticleBean.ListBean.PhotoListBean> list = new ArrayList<>();
        //有图像
        if (!TextUtils.isEmpty(bean.getIv_content())) {
            ArticleBean.ListBean.PhotoListBean photo = new ArticleBean.ListBean.PhotoListBean();
            photo.setImgUrl(bean.getIv_content());
            photo.setUrl(bean.getIv_content());
            list.add(photo);
        }
        articleBean.setPhotoList(list);
        articleBean.setIsTop(1);
        articleBean.setTopicName(bean.getTopic_name());
        intent.putExtra("article", articleBean);
        startActivity(intent);
    }
}
