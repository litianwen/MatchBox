package com.example.administrator.matchbox.ui.fragment.contact;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.FansAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.IUser;
import com.example.administrator.matchbox.model.UserModel;
import com.example.administrator.matchbox.ui.activity.ChatActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/2.
 */

public class FansFragment extends BaseFragment {

    @BindView(R.id.lv_content)
    ListView lvContent;

    FansAdapter adapter;
    UserModel model = new UserModel();

    List<UserBean> fans;
    List<UserBean> atts;

    @BindView(R.id.ll_search)
    LinearLayout llSerach;

    boolean isShowSerach = true;

    @Override
    protected void initView() {
        if (!isShowSerach)
            llSerach.setVisibility(View.GONE);
        lvContent.setDivider(null);
        lvContent.setDividerHeight(0);
        model.getAllAttention(IUser.TYPE_ATT, MyApp.getInstance().getUserBean().getUserId() + "", new IBeanCallback<List<UserBean>>() {
            @Override
            public void Success(List<UserBean> userBeen) {
                atts = userBeen;
                setAdapter();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
        model.getAllAttention(IUser.TYPE_FANS, MyApp.getInstance().getUserBean().getUserId() + "", new IBeanCallback<List<UserBean>>() {
            @Override
            public void Success(List<UserBean> userBeen) {
                fans = userBeen;
                setAdapter();
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userBean", fans.get(position)));
            }
        });
    }

    private void setAdapter() {
        if (fans != null && atts != null) {
            adapter = new FansAdapter(getContext(), fans, atts);
            lvContent.setAdapter(adapter);
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_fans;
    }


    @OnClick(R.id.ll_search)
    public void onClick() {
    }

    public void hideSerarch() {
        isShowSerach = false;
    }
}
