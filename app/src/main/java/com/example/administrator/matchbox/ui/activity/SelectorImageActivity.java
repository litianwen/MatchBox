package com.example.administrator.matchbox.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.CameraAdapter;
import com.example.administrator.matchbox.base.BaseActivity;
import com.example.administrator.matchbox.config.MyApp;
import com.example.administrator.matchbox.utils.BoxUtils;
import com.example.administrator.matchbox.utils.ContastUtils;
import com.example.administrator.matchbox.weiget.ScaleImageView;
import com.example.administrator.matchbox.weiget.ScrollGridView;
import com.example.administrator.matchbox.weiget.ShowDirPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/28.
 */

public class SelectorImageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final int REQUEST_CAMERA = 151;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.gv_image)
    ScrollGridView gvImage;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.siv_image)
    ScaleImageView siv_image;

    //GridView adapter 的数据源
    List<File> imageList = new ArrayList<>();

    CameraAdapter adapter;

    String path = null;

    String name = "";
    private List<ShowDirPopWindow.Dir> dirList = new ArrayList<>();

    @Override
    protected void initView() {
        //判断本地图片是否缓存成功
        if (MyApp.getInstance().getImgList() == null) {
            showToast("图片正在加载中，请稍后");
            finish();
        }
        //第一次加载所有图片
        imageList.addAll(MyApp.getInstance().getImgList());
        adapter = new CameraAdapter(this, imageList);
        gvImage.setAdapter(adapter);
        gvImage.setOnItemClickListener(this);
        siv_image.setImageBitmap(BitmapFactory.decodeFile(imageList.get(0).getAbsolutePath()));
        initDir();
    }

    private void initDir() {
        //key == dir名称
        //value = dir中有多少图片
        HashMap<String, Integer> sizeMap = new HashMap<>();
        HashMap<String, String> firstMap = new HashMap<>();
        //第一次添加
        dirList.add(new ShowDirPopWindow.Dir(imageList.get(0), "所有图片", imageList.size(), true));
        // C:/aaa/bb.jpg;  C:/aaa/sadf.jpg  firstImage
        for (File file : imageList) {
            //拿 file在HashMap中的数量
            Integer size = sizeMap.get(file.getParentFile().getAbsolutePath());
            //null
            if (size == null) {
                sizeMap.put(file.getParentFile().getAbsolutePath(), 1);
                //图片
                firstMap.put(file.getParentFile().getAbsolutePath(), file.getAbsolutePath());
            } else {
                sizeMap.put(file.getParentFile().getAbsolutePath(), size + 1);
            }
        }
        Set<String> set = sizeMap.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            int size = sizeMap.get(key);
            String firstImage = firstMap.get(key);
            String name = new File(key).getName();
            File firstFile = new File(firstImage);
            ShowDirPopWindow.Dir dir = new ShowDirPopWindow.Dir(firstFile, name, size, false);
            dirList.add(dir);
        }

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        svContent.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_selector_image;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_title, R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_title:
                showPopwindow();
                break;
            case R.id.tv_finish:
                goBack();
                break;
        }
    }

    private void goBack() {
        final Dialog dialog = BoxUtils.showProgressDialog(this);
        dialog.show();
        new Thread() {
            @Override
            public void run() {
                path = ContastUtils.IMAGE_PATH + "/" + System.currentTimeMillis() + ".png";
                siv_image.getImagePath(path);
                dialog.dismiss();
                setResult(RESULT_OK, getIntent().putExtra("path", path));
                finish();
            }
        }.start();
    }

    private void showPopwindow() {
        tvTitle.setSelected(true);
        ShowDirPopWindow popwindow = new ShowDirPopWindow(this, dirList, new ShowDirPopWindow.OnPopwindowItemClick() {
            @Override
            public void onClick(String dirName) {
                //需要开始修改dir
                if (dirName.equals("所有图片")) {
                    for (ShowDirPopWindow.Dir dir : dirList) {
                        dir.setCheck(false);
                    }
                    dirList.get(0).setCheck(true);
                    imageList.clear();
                    imageList.addAll(MyApp.getInstance().getImgList());
                    tvTitle.setText("所有图片");
                } else {
                    //不是第0个所有图片被选中
                    dirList.get(0).setCheck(false);
                    for (int i = 1; i < dirList.size(); i++) {
                        ShowDirPopWindow.Dir dir = dirList.get(i);
                        //遍历判断 是否和用户选中的名称相同
                        if (dir.getFirstImage().getParent().equals(dirName)) {
                            dir.setCheck(true);
                        } else
                            dir.setCheck(false);
                    }
                    //开始遍历本地文件
                    //C:/aaa.jpg c:/aaaa/ddd/bbb.jpg
                    imageList.clear();
                    for (File file : MyApp.getInstance().getImgList()) {
                        if (file.getAbsolutePath().contains(dirName)) {
                            imageList.add(file);
                        }
                    }
                    tvTitle.setText(new File(dirName).getName());
                }
                adapter.notifyDataSetChanged();
            }
        });
        popwindow.showAsDropDown(rlTitle, 0, 0);
        popwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                tvTitle.setSelected(false);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //  adapter.checkImage(position);
        if (position != 0) {
            view.setSelected(true);
            siv_image.setImageBitmap(BitmapFactory.decodeFile(imageList.get(position - 1).getAbsolutePath()));
            svContent.fullScroll(View.FOCUS_UP);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            name = System.currentTimeMillis() + ".jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ContastUtils.IMAGE_PATH, name)));
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            path = ContastUtils.IMAGE_PATH + "/" + name;
            File file = new File(path);
            MyApp.getInstance().getImgList().add(file);
            siv_image.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }
}
