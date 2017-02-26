package com.example.administrator.matchbox.ui.fragment.thirdMVP;

import android.content.Context;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface IThirdpartyView {


    public void showDialog();

    public void dismissDialog();

    public Context getContext();

    public void showToast(String msg);

    public void success();

}
