package com.example.administrator.matchbox.ui.fragment.me;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.matchbox.adapter.ArticleAdapter;
import com.example.administrator.matchbox.bean.ArticleBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.ArticleModel;
import com.example.administrator.matchbox.utils.ListViewHeightUtils;

/**
 * Created by Administrator on 2016/12/16.
 */

public class MeArticleListView extends ListView implements IBeanCallback<ArticleBean> {

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }


    public MeArticleListView(Context context) {
        super(context);
        doRequest();
    }

    public MeArticleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void doRequest() {
        new ArticleModel().getArticleByID(MyApp.getInstance().getUserBean().getUserId(), MyApp.getInstance().getUserBean().getUserId(), this);
    }


    @Override
    public void Success(ArticleBean articleBean) {
        setAdapter(new ArticleAdapter(getContext(), articleBean.getList(), true));
        ListViewHeightUtils.setListViewHeightBasedOnChildren(this);
    }

    @Override
    public void onError(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
