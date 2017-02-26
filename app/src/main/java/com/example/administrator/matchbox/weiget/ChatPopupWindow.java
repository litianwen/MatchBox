package com.example.administrator.matchbox.weiget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.example.administrator.matchbox.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8.
 */

public class ChatPopupWindow extends PopupWindow {
    // List<Integer> mList = new ArrayList<>();

    public interface Callback {
        public void callback(int id);
    }


    public ChatPopupWindow(Context context, @NonNull final Callback callback
    ) {
        //[001]
        super(context);
        setFocusable(true);
        setOutsideTouchable(true);
        setWidth(context.getResources().getDisplayMetrics().widthPixels);
        setHeight(250);
        GridView gv = new GridView(context);
        gv.setNumColumns(7);
        SimpleAdapter adapter = new SimpleAdapter(context, getData(), R.layout.item_image, new String[]{"img"}, new int[]{R.id.iv});
        gv.setAdapter(adapter);
        setContentView(gv);
        setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callback.callback(position);
            }
        });
    }

    private List<HashMap<String, Integer>> getData() {
        //显示多少个聊天表情
        //1行7个
        List<HashMap<String, Integer>> list = new ArrayList<>();
        for (int i = 0; i < 107; i++) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            int id = 0;
            DecimalFormat format = new DecimalFormat("000");
            try {
                id = R.mipmap.class.getField("f_static_" + format.format(i)).getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("img", id);
            // mList.add(id);
            list.add(map);
        }
        return list;
    }
}
