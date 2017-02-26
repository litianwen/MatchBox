package com.example.administrator.matchbox.ui.fragment.topic;

import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.ArticleAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IArticle;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/12.
 */

public class TopicFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, IBeanCallback<ArticleBean> {
    @BindView(R.id.ptrsv)
    PullToRefreshListView ptrsv;

    public static final int TYPE_HOT = 2;
    public static final int TYPE_NEW = 1;

    int type;
    int topicId;

    IArticle iArticle = new ArticleModel();

    public static TopicFragment newInstance(int type, int topicId) {
        TopicFragment fragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("topicId", topicId);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected List<ArticleBean.ListBean> mList = new ArrayList<>();

    ArticleAdapter adapter;

    @Override
    protected void initView() {
        type = getArguments().getInt("type");
        topicId = getArguments().getInt("topicId");
        adapter = new ArticleAdapter(getActivity(), mList, false);
        adapter.setIsShowTopic(false);
        ptrsv.setAdapter(adapter);
        ptrsv.setOnRefreshListener(this);
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        //请求网络
        doQuery();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_topic;
    }

    //子类加载数据

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        doQuery();
    }

    protected void doQuery() {
        iArticle.getArticleByType(MyApp.getInstance().getUserBean().getUserId(), topicId, type, this);
    }


    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void Success(ArticleBean articleBean) {
        ptrsv.onRefreshComplete();
        mList.clear();
        mList.addAll(articleBean.getList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String msg) {
        ptrsv.onRefreshComplete();
        showToast(msg);
    }
}
