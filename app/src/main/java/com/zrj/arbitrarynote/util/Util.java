package com.zrj.arbitrarynote.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/7/21.
 */

public class Util {

    private static final String KEY_BOARD_HEIGHT ="keyBoardHeight";
    /**
     * 获取图片的真实路径
     * @param data 选择的图片数据
     * @return 图片的真实路径
     */
    public static String getRealImagePath(Context context, Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // 如果是document类型的Uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(
                        uri.getAuthority())) {
                    String id = docId.split(":")[1]; // 解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(
                            context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri
                        .getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(docId));
                    imagePath = getImagePath(context, contentUri, null);
                }
            } else {
                // 如果不是document类型的Uri，则使用普通方式处理
                imagePath = getImagePath(context, uri, null);
            }
        } else {
            imagePath = getImagePath(context, uri, null);
        }
        return imagePath;
    }

    /**
     * 在 ContentProvider 中查找图片路径
     * @param uri       用于查找相应 ContentProvider
     * @param selection 查询的条件,相当于 SQL 的 where
     * @return 查询到的图片真实路径
     */
    private static String getImagePath(Context context, Uri uri, String selection) {
        Cursor cursor = context.getContentResolver().query(
                uri, new String[]{MediaStore.Images.Media.DATA}, selection, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String imagePath = cursor.getString(columnIndex);
        cursor.close();
        return imagePath;
    }

    //获取当前系统时间
    public static String getCurrentTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    /**屏幕分辨率高**/
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }
    /**statusBar高度**/
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }
    /**可见屏幕高度**/
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    //获取软键盘高度
    public static int getKeyboardHeight(Activity activity){
        int height = Util.getScreenHeight(activity) - Util.getStatusBarHeight(activity) -
                Util.getAppHeight(activity);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        if (height==0){
            return sharedPreferences.getInt(KEY_BOARD_HEIGHT,787); //默认高度787
        }else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_BOARD_HEIGHT,height);
            editor.apply();
            return height;
        }
    }

    public static int dp2px(Context context,int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }

    public static int px2dp(Context context,int px){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(px/scale+0.5f);
    }

}
