package com.example.administrator.matchbox.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ContastUtils {

    public static final String IMAGE_PATH = Environment.getExternalStorageDirectory() + "/MatchBoxImage";


    public static final String ThreePassword = "12345678";

    static {
        new File(IMAGE_PATH).mkdirs();
    }
}
