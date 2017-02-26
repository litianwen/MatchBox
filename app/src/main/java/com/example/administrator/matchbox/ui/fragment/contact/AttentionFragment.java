package com.example.administrator.matchbox.ui.fragment.contact;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.AttentionAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.IUser;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.ui.activity.ChatActivity;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.weiget.IndexBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/2.
 */

public class AttentionFragment extends BaseFragment implements View.OnClickListener, IBeanCallback<List<UserBean>>, IndexBar.OnIndexClickListener, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_country)
    ListView lvAttention;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_index)
    IndexBar ibIndex;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    LinearLayout llSearch;

    UserModel model = new UserModel();

    AttentionAdapter adapter;
    private List<UserBean> userBean;


    @Override
    protected void initView() {
        //把布局中不同的先去掉
        initHead();
        doRequest();
    }

    private void doRequest() {
        model.getAllAttention(IUser.TYPE_ATT, MyApp.getInstance().getUserBean().getUserId() + "", this);
    }

    private void initHead() {
        rlTitle.setVisibility(View.GONE);
        View headView = View.inflate(getContext(), R.layout.item_contact_search, null);
        llSearch = (LinearLayout) headView.findViewById(R.id.ll_search);
        llSearch.setOnClickListener(this);
        lvAttention.addHeaderView(headView);
        //设置监听
        lvAttention.setOnScrollListener(this);
        lvAttention.setOnItemClickListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_selectcountry;
    }

    @Override
    public void onClick(View v) {
        //跳转到搜索界面
    }

    @Override
    public void Success(List<UserBean> userBeen) {
        //显示界面
        this.userBean = userBeen;
        adapter = new AttentionAdapter(getContext(), userBeen);
        lvAttention.setAdapter(adapter);
        ibIndex.setIndex(userBeen);
        ibIndex.setOnIndexClickListener(this);
    }

    @Override
    public void onError(String msg) {
        showToast(msg);
    }

    @Override
    public void onIndexBarClick(String letter) {
        lvAttention.setSelection(adapter.getFirstLetter(letter) + 1);
    }


    //将title顶上去

    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //一般 加载图片 滑动不加载
        if (scrollState != SCROLL_STATE_TOUCH_SCROLL) {
            layoutParams.topMargin = 0;
            tvTitle.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        LogUtils.e(firstVisibleItem);
        if (firstVisibleItem == 0) {
            tvTitle.setVisibility(View.GONE);
            return;
        }
        if (tvTitle.getVisibility() == View.VISIBLE) {
            String lastName = adapter.getPositionLetter(firstVisibleItem);
            if (!tvTitle.getText().toString().equals(lastName)) {
                //如果tvTitle在不在第一行
                //挤压
                View childView = view.getChildAt(0);
                float bottom = childView.getBottom();
                float height = tvTitle.getHeight();
                Log.e("TAG", "-----------" + bottom + ":    " + height);
                if (bottom < height) {
                    layoutParams.topMargin = (int) (bottom - height);
                    tvTitle.setLayoutParams(layoutParams);
                } else {
                    layoutParams.topMargin = 0;
                    tvTitle.setLayoutParams(layoutParams);
                    tvTitle.setText(lastName);
                }
            }
        } else {
            tvTitle.setVisibility(View.VISIBLE);
        }
        //最终显示
        String str = adapter.getPositionLetter(firstVisibleItem - 1);
        tvTitle.setText(str);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //开始聊天
        if (position == 0)
            return;
        startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userBean", userBean.get(position - 1)));
    }
}
