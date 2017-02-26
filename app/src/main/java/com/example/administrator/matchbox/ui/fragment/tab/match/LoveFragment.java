package com.example.administrator.matchbox.ui.fragment.tab.match;

import android.widget.ListView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.ArticleAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.example.administrator.matchbox.utils.SPHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/7.
 */

public class LoveFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, IBeanCallback<ArticleBean> {

    @BindView(R.id.ptrsv)
    PullToRefreshListView ptrsv;
    ArticleModel model = new ArticleModel();
    ArticleAdapter adapter;
    List<ArticleBean.ListBean> mList = new ArrayList<>();

    @Override
    protected void initView() {
        ptrsv.setOnRefreshListener(this);
        adapter = new ArticleAdapter(getContext(), mList, true);
        ptrsv.setAdapter(adapter);
        requset();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_love;
    }


    public void requset() {
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        model.getAttentionArticle(LoveFragment.this);
    }

    public void firstRequest() {
        model.getAttentionArticle(LoveFragment.this);
    }

    @Override
    public void Success(final ArticleBean articleBean) {
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

    @Override
    public void onResume() {
        super.onResume();
        resumeState();
    }

    private void resumeState() {
        if (mList.size() != 0) {
            //修改状态
            SPHelper sp = new SPHelper(getActivity(), "article");
            int id = sp.getInt("id");
            if (id == -1)
                return;
            else {
                for (ArticleBean.ListBean listBean : mList) {
                    if (listBean.getFriendId() == id) {
                        //当前帖子
                        int size = sp.getInt("command", 0);
                        int like = sp.getInt("like");
                        listBean.setDiscussCount(listBean.getDiscussCount() + size);
                        if (like != -1) {
                            if (listBean.getIsTop() != like) {
                                listBean.setIsTop(like);
                                if (like == 1)
                                    listBean.setTopCount(listBean.getTopCount() + 1);
                                else
                                    listBean.setTopCount(listBean.getTopCount() - 1);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        sp.clear();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        //下拉刷新
        requset();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }
}
