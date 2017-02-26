package com.example.administrator.matchbox.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/23.
 */

public abstract class BaseFragment extends Fragment {

    //FragmentManager
    /**
     * attach  --->onCreateView
     * detach  --->onDestroyView
     * 只是移除视图 ，保存实例
     */
    /**
     * hide  隐藏和显示视图
     * show
     */
    /**
     * addd     -->onCreate
     * remove   -->onDestroy
     * 直接移除了实例，每次调用都是新的实例 所有的成员变量，组件全部是新的
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = View.inflate(getContext(), getLayoutRes(), null);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected abstract void initView();

    public abstract int getLayoutRes();

    public void showToast(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }



}
