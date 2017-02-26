package com.example.administrator.matchbox.ui.activity;

import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.LikeListAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.LikeBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.example.administrator.matchbox.weiget.ScrollListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/11.
 */

public class LikeActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv_like)
    ScrollListView lvLike;
    @BindView(R.id.ptrsv)
    PullToRefreshScrollView ptrsv;

    LikeBean likeBean;

    int id;

    int pager = 1;

    ArticleModel model = new ArticleModel();

    LikeListAdapter adapter;

    List<LikeBean.ListBean> mList = new ArrayList<>();

    @Override
    protected void initView() {
        //获取数据
        id = getIntent().getIntExtra("id", -1);
        if (id == -1)
            finish();
        //设置模式 上下以及下拉
        ptrsv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrsv.setOnRefreshListener(this);
        adapter = new LikeListAdapter(getBaseContext(), mList);
        lvLike.setAdapter(adapter);
        doQuery();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_like;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        //刷新视图
        doQuery();
        pager = 1;
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (likeBean.getList().size() < pager * 10) { //9
            showToast("没有更多了");
        }
        pager++; //  19 29
        if (likeBean.getList().size() > 10 * pager) {
            for (int i = (pager - 1) * 10; i < pager * 10; i++) {
                mList.add(likeBean.getList().get(i));
            }
        } else {
            //全部加载
            for (int i = (pager - 1) * 10; i < likeBean.getList().size(); i++) {
                mList.add(likeBean.getList().get(i));
            }
        }
        adapter.notifyDataSetChanged();
        ptrsv.onRefreshComplete();
    }


    //请求点赞列表
    //第一次刷新的时候
    public void doQuery() {
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        model.getLikeList(id, new IBeanCallback<LikeBean>() {
            @Override
            public void Success(LikeBean likeBean) {
                tvTitle.setText("赞(" + likeBean.getList().size() + ")");
                mList.clear();
                ptrsv.onRefreshComplete();
                LikeActivity.this.likeBean = likeBean;
                if (likeBean.getList().size() > 10) {
                    for (int i = 0; i < 10; i++) {
                        mList.add(likeBean.getList().get(i));
                    }
                } else
                    mList.addAll(likeBean.getList());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                ptrsv.onRefreshComplete();
            }
        });
    }
}
