package com.zrj.arbitrarynote.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.bmob.BmobUtil;
import com.zrj.arbitrarynote.bmob.User;
import com.zrj.arbitrarynote.custom.MyImageSpan;
import com.zrj.arbitrarynote.db.DatabaseHelper;
import com.zrj.arbitrarynote.util.MyLog;
import com.zrj.arbitrarynote.util.Util;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class ArbitraryNoteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CLASS_NAME = "ArbitraryNoteActivity";
    private final int CHOOSE_PHOTO = 0;
    private final int READ_EX_STORAGE = 1;
    private static int measuredWidth = 0;
    private int id = -1;


    private EditText editText;

    private DatabaseHelper dbHelper;
    SQLiteDatabase db;
    BmobUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbitrary_note);
        Bmob.initialize(this, BmobUtil.APP_ID);
        editText = (EditText) findViewById(R.id.edit_arbitrary_note);
        ImageView insertImage = (ImageView) findViewById(R.id.gallery_arbitrary_note);
        user = BmobUser.getCurrentUser(User.class);
        dbHelper = new DatabaseHelper(this, "user" + user.getUsername(), null, 1);
        db = dbHelper.getWritableDatabase();
        setTitle("随心记");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0); //隐藏阴影
        }
        setOnMeasureListener();
        insertImage.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String content = editText.getText().toString();
        //程序退出时自动保存
        if (!content.equals("")) {
            Cursor cursor = db.rawQuery("select * from ArbitraryNote where id = ?",
                    new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                db.execSQL("update ArbitraryNote set content = ? and modify_time =? where id = ?",
                        new String[]{content, String.valueOf(id), Util.getCurrentTime()});
            } else {
                db.execSQL("insert into ArbitraryNote(content, create_time, modify_time) values(?,?,?)",
                        new String[]{content, Util.getCurrentTime(), Util.getCurrentTime()});
            }
            cursor.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery_arbitrary_note:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //运行时权限申请
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EX_STORAGE);
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(intent, CHOOSE_PHOTO);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PHOTO:
                    String imagePath = Util.getRealImagePath(this, data);
                    insertImage(imagePath);
                    break;
            }
        }
    }

    //加载 edittext 内容
    private void loadContent() {
        Cursor cursor = db.rawQuery("select * from ArbitraryNote", null);
        cursor.moveToLast();
        String content = cursor.getString(cursor.getColumnIndex("content"));
        split(content);
    }

    //图片和文本分离并显示
    private void split(String content){
        int pathStart = content.indexOf("[pic]");
        MyLog.e(CLASS_NAME,"索引 pathStart = "+pathStart);
        switch (pathStart){
            case -1:
                editText.append(content);
                break;
            case 0:
                int pathEnd = content.indexOf("[/pic]");
                String imagePath = content.substring(pathStart+5,pathEnd);
                insertImage(imagePath);
                content = content.substring(pathEnd+6);
                split(content);
                break;
            default:
                editText.append(content.substring(0,pathStart));
                content = content.substring(pathStart);
                split(content);
                break;
        }
    }

    //获取 edittext 实际宽度
    private void setOnMeasureListener() {
        ViewTreeObserver vto = editText.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    editText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                measuredWidth = editText.getWidth();
                loadContent(); //获取宽度后再加载内容
                MyLog.e(CLASS_NAME, "measuredWidth = " + measuredWidth);
            }
        });
    }

    /**
     * 插入图片
     * @param imagePath 图片路径
     */
    private void insertImage(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap = configBitmap(bitmap);
        SpannableString spannableString = new SpannableString("[pic]" + imagePath + "[/pic]");
        ImageSpan imageSpan = new ImageSpan(this, bitmap, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan, 0,
                imagePath.length() + "[pic][/pic]".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        Editable edit = editText.getEditableText();
        edit.insert(editText.getSelectionStart(), spannableString);
    }

    /**
     * 按比例配置图片
     *
     * @param bitmap 原图
     * @return 配置好的图片
     **/
    private Bitmap configBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        MyLog.e(getLocalClassName(), "原图 width = " + width);
        int height = bitmap.getHeight();
        MyLog.e(getLocalClassName(), "原图 height = " + height);
        float ratio = 1;
        ratio = (float) (measuredWidth - 10) / (float) width;
        MyLog.e(getLocalClassName(), "比例 ratio = " + ratio);
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }


}












