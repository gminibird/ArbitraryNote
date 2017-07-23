package com.zrj.arbitrarynote.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2017/7/21.
 */

public class Util {

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

}
