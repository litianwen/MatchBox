package com.example.administrator.matchbox.model;

import com.example.administrator.matchbox.bean.TopicBean;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.httpservice.IAddTopic;
import com.example.administrator.matchbox.httpservice.ICreateTopic;
import com.example.administrator.matchbox.httpservice.IGetTopicList;
import com.example.administrator.matchbox.httpservice.ISearchTopic;
import com.example.administrator.matchbox.interfaces.IBeanCallback;
import com.example.administrator.matchbox.interfaces.ITopic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/12/2.
 */

public class TopicModel implements ITopic {


    @Override
    public void createTopic(String topicName, final IBeanCallback callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        if (!MyApp.getInstance().isLogin()) {
            callback.onError("没有登录成功");
            return;
        }

        String id = MyApp.getInstance().getUserBean().getUserId() + "";
        Call<String> call = retrofit.create(ICreateTopic.class).createTopic(topicName, id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optString("result").equals("0"))
                        callback.Success(null);
                    else {
                        callback.onError("话题名已存在，请修改");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });

    }

    private void getTopicList(final IBeanCallback<ArrayList<TopicBean>> callback, Call<String> call) {
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject json = new JSONObject(response.body());
                    String result = json.optString("result");
                    if (result.equals("0")) {
                        //解析数据
                        Gson gson = new Gson();
                        JSONArray array = json.optJSONArray("list");
                        //手动转换集合
                        ArrayList<TopicBean> list = gson.fromJson(array.toString(), new TypeToken<List<TopicBean>>() {
                        }.getType());
                        callback.Success(list);
                    } else {
                        callback.onError(json.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("数据解析失败");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });
    }

    @Override
    public void getHotTopicList(final IBeanCallback<ArrayList<TopicBean>> callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call = retrofit.create(IGetTopicList.class).getHotTopicList(MyApp.getInstance().getUserBean().getUserId() + "");
        getTopicList(callback, call);
    }

    @Override
    public void getNewToicList(IBeanCallback<ArrayList<TopicBean>> callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call = retrofit.create(IGetTopicList.class).getNewTopicList(MyApp.getInstance().getUserBean().getUserId() + "");
        getTopicList(callback, call);
    }

    @Override
    public void getUpdateToicList(IBeanCallback<ArrayList<TopicBean>> callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call = retrofit.create(IGetTopicList.class).getUpdateTopicList(MyApp.getInstance().getUserBean().getUserId() + "");
        getTopicList(callback, call);
    }

    @Override
    public void searchTopicList(String key, final IBeanCallback<List<TopicBean>> callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call = retrofit.create(ISearchTopic.class).searchTopic(MyApp.getInstance().getUserBean().getUserId() + "", key);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optString("result").equals("0")) {
                        //解析
                        Gson gson = new Gson();
                        List<TopicBean> list = gson.fromJson(jsonObject.optJSONArray("list").toString(), new TypeToken<List<TopicBean>>() {
                        }.getType());
                        callback.Success(list);
                    } else {
                        callback.onError(jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("解析错误");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求失败");
            }
        });

    }

    int success = 0;
    int failure = 0;
    //3个接口


    @Override
    public void addTopicList(final List<TopicBean> list, final IBeanCallback<Integer> callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        for (TopicBean topicBean : list) {
            Call<String> call = retrofit.create(IAddTopic.class).addTopic(MyApp.getInstance().getUserBean().getUserId() + "", topicBean.getTopicId() + "");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("result").equals("0")) {
                            success++;
                        } else {
                            failure++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        failure++;
                    }
                    if (list.size() == (success + failure)) {
                        //成功+失败 == 总调用接口
                        callback.Success(failure);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    failure++;
                    if (list.size() == (success + failure)) {
                        //成功+失败 == 总调用接口
                        callback.Success(failure);
                    }
                }
            });
        }
    }

    @Override
    public void attTopic(int userId, int topicId, boolean isAtt,final IBeanCallback callback) {
        Retrofit retrofit = MyApp.getInstance().getStringRetrofit();
        Call<String> call;
        if (isAtt)
            call = retrofit.create(IAddTopic.class).addTopic(userId + "", topicId + "");
        else
            call = retrofit.create(IAddTopic.class).deAddTopic(userId + "", topicId + "");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    if (jsonObject.optString("result").equals("0"))
                        callback.Success(null);
                    else
                        callback.onError(jsonObject.optString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.onError("数据解析失败");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError("网络请求异常");
            }
        });

    }
}
