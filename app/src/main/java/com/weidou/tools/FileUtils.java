package com.weidou.tools;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 文件操作工具
 *
 * @author chen.lin
 */
public class FileUtils {

    private static final String TAG = "FileUtil";

    /**
     * 从sd卡取文件
     *
     * @param filename
     * @return
     */
    public static String getFileFromSdcard(String filename) {
        ByteArrayOutputStream outputStream = null;
        FileInputStream fis = null;
        try {
            outputStream = new ByteArrayOutputStream();
            File file = new File(Environment.getExternalStorageDirectory(), filename);
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                fis = new FileInputStream(file);
                int len = 0;
                byte[] data = new byte[1024];
                while ((len = fis.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                outputStream.close();
                fis.close();
            } catch (Exception e) {
            }
        }
        return new String(outputStream.toByteArray());
    }

    /**
     * 保存文件到sd
     *
     * @param filename
     * @param content
     * @return
     */
    public static boolean saveContentToSdcard(String filename, String content) {
        boolean flag = false;
        FileOutputStream fos = null;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            if (file.exists()){
                file.createNewFile();
            }
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                flag = true;
//            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}