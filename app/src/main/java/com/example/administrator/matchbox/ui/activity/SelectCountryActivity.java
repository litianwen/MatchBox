package com.example.administrator.matchbox.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CountryAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.bean.CountriesBean;
import com.example.administrator.matchbox.utils.CountriesUtils;
import com.example.administrator.matchbox.utils.LogUtils;
import com.example.administrator.matchbox.weiget.IndexBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/11/24.
 */

public class SelectCountryActivity extends BaseActivity implements IndexBar.OnIndexClickListener, AbsListView.OnScrollListener, TextWatcher, AdapterView.OnItemClickListener {
    @BindView(R.id.lv_country)
    ListView lvCountry;
    @BindView(R.id.ib_index)
    IndexBar ibIndex;
    CountryAdapter adapter;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    EditText etSerach;
    ImageView ivClear;

    List<CountriesBean> mList;

    @Override
    protected void initView() {
        //拿到所有国家
        mList = CountriesUtils.getAllCountriesList();
        ibIndex.setIndex(mList);
        ibIndex.setOnIndexClickListener(this);
        //ListView设置头必须在适配器之前
        initHead();
        adapter = new CountryAdapter(this, mList);
        lvCountry.setAdapter(adapter);
        lvCountry.setOnScrollListener(this);
        lvCountry.setOnItemClickListener(this);
    }

    private void initHead() {
        View headLayout = View.inflate(this, R.layout.item_country_serach, null);
        etSerach = (EditText) headLayout.findViewById(R.id.et_search);
        etSerach.addTextChangedListener(this);
        ivClear = (ImageView) headLayout.findViewById(R.id.iv_clear);
        lvCountry.addHeaderView(headLayout);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_selectcountry;
    }

    @Override
    public void onIndexBarClick(String letter) {
        LogUtils.e(letter);
        //索引被点击时
        int index = adapter.getFirstLetter(letter);//在集合中的位置，他的最上面还有一个headView
        //滑动到指定条目
        lvCountry.setSelection(index + 1);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(letter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //一般 加载图片 滑动不加载
        if (scrollState != SCROLL_STATE_TOUCH_SCROLL) {
            layoutParams.topMargin = 0;
            tvTitle.setLayoutParams(layoutParams);
        }
    }

    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            adapter = new CountryAdapter(this, mList);
            lvCountry.setAdapter(adapter);
            ibIndex.setIndex(mList);
        } else {
            List<CountriesBean> list = new ArrayList<>();
            for (CountriesBean countriesBean : mList) {
                String key = s.toString();
                if (countriesBean.getArea().contains(key) || countriesBean.getCountry().contains(key) || countriesBean.getString().contains(key)) {
                    list.add(countriesBean);
                }
            }
            LogUtils.e(list.toString());
            adapter = new CountryAdapter(this, list);
            lvCountry.setAdapter(adapter);
            ibIndex.setIndex(list);
        }
        //没有就出现了BUG
        etSerach.requestFocus();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //带结果返回
        CountriesBean bean = adapter.getBean(position - 1);
        Intent intent = getIntent();
        intent.putExtra("bean", bean);
        setResult(RESULT_OK, intent);
        finish();
    }
}
