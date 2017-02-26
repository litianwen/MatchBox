package com.example.administrator.matchbox.ui.fragment.tab;

import android.graphics.Color;
import android.widget.TextView;

import com.example.administrator.matchbox.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/2.
 */

public class TabBoxFragment extends BaseFragment {
    @BindView(android.R.id.text1)
    TextView text1;

    @Override
    protected void initView() {
        text1.setBackgroundColor(Color.RED);
    }

    @Override
    public int getLayoutRes() {
        return android.R.layout.simple_list_item_1;
    }
}
