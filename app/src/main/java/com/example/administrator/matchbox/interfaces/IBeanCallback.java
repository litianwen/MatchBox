package com.example.administrator.matchbox.interfaces;

/**
 * Created by Administrator on 2016/11/29.
 */

public interface IBeanCallback<T> {

    void Success(T t);

    void onError(String msg);

}
