package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/28.
 */

public class CameraAdapter extends ListItemAdapter<File> {

    public CameraAdapter(Context context, List<File> list) {
        super(context, list);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1;
    }


    private int selectImage = -1;

    public void checkImage(int position) {
        this.selectImage = position;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_camera, null);
            holder = new ViewHolder(convertView);
            int width = getContext().getResources().getDisplayMetrics().widthPixels / 3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            holder.ivImage.setLayoutParams(params);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (position == 0) {
            holder.ivImage.setImageResource(R.mipmap.icon_camera_pick_ng);
        } else {
            File file = getItem(position - 1);
            // holder.ivImage.setImageURI(Uri.fromFile(file));
            Glide.with(mContext).load(file).into(holder.ivImage);
            if (position == selectImage) {
                holder.llImage.setSelected(true);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.ll_image)
        LinearLayout llImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
