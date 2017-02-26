package com.example.administrator.matchbox.ui.fragment.tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.RecommendTopicAdapter;
import com.example.administrator.matchbox.adapter.SloganAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.RecommendTopicBean;
import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.model.TopicModel;
import com.example.administrator.matchbox.ui.activity.CreateNewTopicActivity;
import com.example.administrator.matchbox.ui.activity.SearchTopicActivity;
import com.example.administrator.matchbox.ui.activity.TopicDetailActivity;
import com.example.administrator.matchbox.ui.activity.TopicListActivity;
import com.example.administrator.matchbox.utils.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */

public class TabTopicFragment extends BaseFragment implements ViewPager.OnPageChangeListener, PullToRefreshBase.OnRefreshListener2<ScrollView>, ExpandableListView.OnChildClickListener {
    private static final int REQUEST_TOPIC = 111;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.vp_slogan)
    ViewPager vpSlogan;
    @BindView(R.id.ll_points)
    LinearLayout llPoints;
    @BindView(R.id.selv_topicList)
    ExpandableListView selvTopicList;
    @BindView(R.id.ptrsv)
    PullToRefreshScrollView ptrsv;

    //expandalbe的数据
    ArrayList<RecommendTopicBean> list;

    int currIndex = 0;

    TopicModel model = new TopicModel();

    @Override
    protected void initView() {
        initViewPager();
    }

    private void initViewPager() {
        vpSlogan.setAdapter(new SloganAdapter(getContext()));
        vpSlogan.addOnPageChangeListener(this);
        llPoints.getChildAt(0).setSelected(true);
        ptrsv.setOnRefreshListener(this);
        //0点
        int id = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % 5;
        vpSlogan.setCurrentItem(id);
        start();
        initTopicList();
    }

    ArrayList<TopicBean> newList, updateList, hotList;

    int index;

    private void initTopicList() {
        index = 0;
        ptrsv.setRefreshing();
        ptrsv.smoothScrollTo(-100);
        model.getHotTopicList(new IBeanCallback<ArrayList<TopicBean>>() {
            @Override
            public void Success(ArrayList<TopicBean> topicBeen) {
                hotList = topicBeen;
                index++;
                setAdapter();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                index++;
                setAdapter();
            }
        });
        model.getNewToicList(new IBeanCallback<ArrayList<TopicBean>>() {
            @Override
            public void Success(ArrayList<TopicBean> topicBeen) {
                newList = topicBeen;
                index++;
                setAdapter();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                index++;
                setAdapter();
            }
        });
        model.getUpdateToicList(new IBeanCallback<ArrayList<TopicBean>>() {
            @Override
            public void Success(ArrayList<TopicBean> topicBeen) {
                updateList = topicBeen;
                index++;
                setAdapter();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
                index++;
                setAdapter();
            }
        });
    }

    //设置expandable的适配器
    public void setAdapter() {
        if (index != 3) {
            return;
        }
        ptrsv.onRefreshComplete();
        //child == TopicBean
        //长度
//        List<List<TopicBean>> list; //putString topicList
//        List<String> title;
        list = new ArrayList<>();
        ArrayList<TopicBean> bean1 = new ArrayList<>();
        if (hotList != null) {
            for (int i = 0; i < 5; i++) {
                if (i < hotList.size())
                    bean1.add(hotList.get(i));
            }
        }
        list.add(new RecommendTopicBean("热门话题", bean1));

        ArrayList<TopicBean> bena2 = new ArrayList<>();
        if (newList != null) {
            for (int i = 0; i < 5; i++) {
                if (i < newList.size())
                    bena2.add(newList.get(i));
            }
        }
        list.add(new RecommendTopicBean("新话题", bena2));

        ArrayList<TopicBean> bena3 = new ArrayList<>();
        if (updateList != null) {
            for (int i = 0; i < 5; i++) {
                if (i < updateList.size())
                    bena3.add(updateList.get(i));
            }
        }
        list.add(new RecommendTopicBean("有更新的话题", bena3));
        RecommendTopicAdapter adpter = new RecommendTopicAdapter(getContext(), list);
        selvTopicList.setAdapter(adpter);
        for (int i = 0; i < 3; i++) {
            selvTopicList.expandGroup(i);
        }
        //禁止收起来
        selvTopicList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        selvTopicList.setOnChildClickListener(this);
        adpter.setOnGroupMoreClickListener(new RecommendTopicAdapter.OnGroupMoreClickListener() {
            @Override
            public void onGroupClick(String title) {
                // mContext.startActivity(new Intent(mContext, TopicListActivity.class)
//                        .putExtra("title", mList.get(groupPosition).getTitle())
//                        .putExtra("topic", mList.get(groupPosition).getList()));
                Intent intent = new Intent(getContext(), TopicListActivity.class);
                intent.putExtra("title", title);
                switch (title) {
                    case "热门话题":
                        intent.putExtra("topic", hotList);
                        break;
                    case "新话题":
                        intent.putExtra("topic", newList);
                        break;
                    default:
                        intent.putExtra("topic", updateList);
                        break;
                }
                startActivity(intent);
            }
        });
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_tab_topic;
    }

    @OnClick({R.id.tv_title, R.id.tv_type, R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title: //创建话题
                startActivity(new Intent(getContext(), CreateNewTopicActivity.class));
                break;
            case R.id.tv_type:
                break;
            case R.id.ll_search:
                startActivityForResult(new Intent(getContext(), SearchTopicActivity.class), REQUEST_TOPIC);
                break;
        }
    }

    public void start() {
        if (!handler.hasMessages(1))
            handler.sendEmptyMessageDelayed(1, 2000);
    }

    public void stop() {
        if (handler.hasMessages(1))
            handler.removeMessages(1);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(1, 2000);
            vpSlogan.setCurrentItem(vpSlogan.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        llPoints.getChildAt(currIndex % 5).setSelected(false);
        llPoints.getChildAt(position % 5).setSelected(true);
        currIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            //闲置
            start();
        } else
            stop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TOPIC && resultCode == Activity.RESULT_OK) {
            LogUtils.e(data.getSerializableExtra("topic").toString());
            TopicBean bean = (TopicBean) data.getSerializableExtra("topic");
            startActivity(new Intent(getContext(), TopicDetailActivity.class)
                    .putExtra("topic", bean));
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        initTopicList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        TopicBean bean = list.get(groupPosition).getList().get(childPosition);
        //跳转到 话题详细页
        startActivity(new Intent(getContext(), TopicDetailActivity.class)
                .putExtra("topic", bean));
        return false;
    }
}
