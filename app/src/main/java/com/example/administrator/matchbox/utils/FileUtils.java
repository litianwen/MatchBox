package com.example.administrator.matchbox.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */

public class FileUtils {


    public static final List<File> getAllImageFile() {
        List<File> list = new ArrayList<>();
        getDirImage(Environment.getExternalStorageDirectory(), list);
        return list;
    }


    private static void getDirImage(File dir, List<File> list) {
        File[] files = dir.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.isDirectory())
                getDirImage(file, list);
            else {
                if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                    list.add(file);
                }
            }
        }
    }
}
