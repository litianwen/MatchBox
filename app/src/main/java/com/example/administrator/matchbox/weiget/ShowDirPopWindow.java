package com.example.administrator.matchbox.weiget;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.administrator.matchbox.adapter.ImageDirAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ShowDirPopWindow extends PopupWindow {
    ListView lv;
    OnPopwindowItemClick onPopwindowItemClick;

    public ShowDirPopWindow(Activity activity, final List<Dir> list, final OnPopwindowItemClick onPopwindowItemClick) {
        super(activity);
        //数据
        this.setOutsideTouchable(true);
        this.setWidth(activity.getResources().getDisplayMetrics().widthPixels);
        this.setHeight(activity.getResources().getDisplayMetrics().widthPixels);
        lv = new ListView(activity);
        lv.setDivider(null);
        lv.setDividerHeight(0);
        this.setContentView(lv);
        lv.setAdapter(new ImageDirAdapter(activity, list));
        lv.setBackgroundColor(Color.TRANSPARENT);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    onPopwindowItemClick.onClick("所有图片");
                } else if (onPopwindowItemClick != null)
                    onPopwindowItemClick.onClick(list.get(position).firstImage.getParent());
                dismiss();
            }
        });
        this.setFocusable(true);
        this.onPopwindowItemClick = onPopwindowItemClick;
    }


    public interface OnPopwindowItemClick {
        public void onClick(String dirName);
    }


    public static class Dir {
        File firstImage;
        String name;
        int size;
        boolean isCheck;

        public File getFirstImage() {
            return firstImage;
        }

        public void setFirstImage(File firstImage) {
            this.firstImage = firstImage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public Dir(File firstImage, String name, int size, boolean isCheck) {
            this.firstImage = firstImage;
            this.name = name;
            this.size = size;
            this.isCheck = isCheck;
        }
    }

}
