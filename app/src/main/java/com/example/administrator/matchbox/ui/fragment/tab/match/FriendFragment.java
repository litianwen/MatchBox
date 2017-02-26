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

//1.修改接口 泛型为 ListView
//2. 修改 布局中 去掉ListView 将下拉ScrollView改成ListView
public class FriendFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, IBeanCallback<ArticleBean> {

    //3 去掉listView ，将下拉ScrollList改成下拉ListView
    //4 将监听的泛型改为ListView
    @BindView(R.id.ptrsv)
    PullToRefreshListView ptrsv;

    ArticleAdapter adapter;

    List<ArticleBean.ListBean> mList = new ArrayList<>();

    ArticleModel model = new ArticleModel();


    @Override
    protected void initView() {
        ptrsv.setOnRefreshListener(this);
        adapter = new ArticleAdapter(getActivity(), mList, true);
        //5. 设置适配器 使用下拉刷新ListView
        ptrsv.setAdapter(adapter);
        doRequest();
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

    public void doRequest() {
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        model.getAttentionUserArticle(this);
    }

    public void fristRequest() {
        model.getAttentionUserArticle(this);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_friend;
    }

//    @Override
//    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//
//    }
//
//    @Override
//    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//
//    }

    @Override
    public void Success(ArticleBean articleBean) {
        mList.clear();
        mList.addAll(articleBean.getList());
        adapter.notifyDataSetChanged();
        if (ptrsv != null) {
            ptrsv.onRefreshComplete();
        }
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
        if (ptrsv != null) {
            ptrsv.onRefreshComplete();
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        doRequest();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }
}
