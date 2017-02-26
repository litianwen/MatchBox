package com.example.administrator.matchbox.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.base.ListItemAdapter;
import com.example.administrator.matchbox.weiget.ShowDirPopWindow;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ImageDirAdapter extends ListItemAdapter<ShowDirPopWindow.Dir> {

    public ImageDirAdapter(Context context, List<ShowDirPopWindow.Dir> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_camera_dir, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        ShowDirPopWindow.Dir dir = getItem(position);
        if (dir.isCheck())
            holder.ivCheck.setVisibility(View.VISIBLE);
        else
            holder.ivCheck.setVisibility(View.INVISIBLE);
        holder.ivFirst.setImageURI(Uri.fromFile(dir.getFirstImage()));
        holder.tvName.setText(dir.getName());
        holder.tvSize.setText(dir.getSize() + "");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_first)
        ImageView ivFirst;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.iv_check)
        ImageView ivCheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
