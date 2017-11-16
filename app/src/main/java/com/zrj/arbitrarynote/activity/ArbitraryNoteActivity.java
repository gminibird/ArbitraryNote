package com.zrj.arbitrarynote.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.adapter.EmojiGridAdapter;
import com.zrj.arbitrarynote.adapter.EmojiPagerAdapter;
import com.zrj.arbitrarynote.bmob.BmobUtil;
import com.zrj.arbitrarynote.bmob.User;
import com.zrj.arbitrarynote.db.DatabaseHelper;
import com.zrj.arbitrarynote.model.Emoji;
import com.zrj.arbitrarynote.model.EmojiManager;
import com.zrj.arbitrarynote.util.MyLog;
import com.zrj.arbitrarynote.util.Util;
import com.zrj.arbitrarynote.view.IndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class ArbitraryNoteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CLASS_NAME = "ArbitraryNoteActivity";
    private final int CHOOSE_PHOTO = 0;
    private final int READ_EX_STORAGE = 1;
    private int mMeasuredWidth = 0;
    private int id = 1;



    private EditText mEditText;

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDB;
    private BmobUser mUser;
    private SharedPreferences mSharedPref;
    private LinearLayout mRootView;
    private ScrollView mScrollView;
    private LinearLayout mEmotionLayout;
    private IndicatorView mIndicatorView;
    private ImageManger mImageManger = new ImageManger();
    private LinearLayout mContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbitrary_note);
        Bmob.initialize(this, BmobUtil.APP_ID);
        mEditText = (EditText) findViewById(R.id.edit_arbitrary_note);
        ImageView insertImage = (ImageView) findViewById(R.id.gallery_arbitrary_note);
        ImageView takePhoto = (ImageView) findViewById(R.id.video_arbitrary_note);
        ImageView moreFun = (ImageView) findViewById(R.id.more_function_arbitrary_note);
        ImageView emoji = (ImageView) findViewById(R.id.emoji_arbitrary_note);
        ImageView soundRecord = (ImageView) findViewById(R.id.sound_record_arbitrary_note);
        mRootView = (LinearLayout) findViewById(R.id.root_view_arbitrary_note);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view_arbitrary_note);
        mContent = (LinearLayout)findViewById(R.id.content_arbitrary_note);
        mUser = BmobUser.getCurrentUser(User.class);
        mDBHelper = new DatabaseHelper(this, "mUser" + mUser.getUsername(), null, 1);
        mDB = mDBHelper.getWritableDatabase();
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        setTitle("随心记");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0); //隐藏阴影
        }
        getMesuredWidth();
        EmotionViewManager emotionManager = new EmotionViewManager();
        emotionManager.init();
        insertImage.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        moreFun.setOnClickListener(this);
        emoji.setOnClickListener(this);
        soundRecord.setOnClickListener(this);
        mEditText.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String content = mEditText.getText().toString();
        //程序退出时自动保存
        if (!content.equals("")) {
            int id = 1;
            Cursor cursor = mDB.rawQuery("select * from ArbitraryNote", null);
            if (cursor.moveToFirst()) {
                MyLog.e(CLASS_NAME, "找到");
                mDB.execSQL("update ArbitraryNote set content = ? and modify_time =? where id = ?",
                        new String[]{content, Util.getCurrentTime(), String.valueOf(id)});
            } else {
                MyLog.e(CLASS_NAME, "么找到");
                mDB.execSQL("insert into ArbitraryNote(content, create_time, modify_time) values(?,?,?)",
                        new String[]{content, Util.getCurrentTime(), Util.getCurrentTime()});
            }
            cursor.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery_arbitrary_note:
                MyLog.e(CLASS_NAME, "点击了相册图标1");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //运行时权限申请
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(intent, CHOOSE_PHOTO);
                    }
                }
                break;
            case R.id.emoji_arbitrary_note:

                if (mEmotionLayout.isShown()) {
                    lockRootViewHeight();
                    toggleViewVisible(mEmotionLayout);
                    toggleSoftInput();
                    unlockRootViewHeightDelayed();
                } else {
                    if (Util.getScreenHeight(this) - Util.getAppHeight(this) - Util.getStatusBarHeight(this) <= 0) {
                        //输入法没显示
                        toggleViewVisible(mEmotionLayout);
                    } else {
                        lockRootViewHeight();
                        toggleSoftInput();
                        toggleViewVisible(mEmotionLayout);
                        unlockRootViewHeightDelayed();
                    }
                }
                break;
            case R.id.edit_arbitrary_note:
                if (mEmotionLayout.isShown()) {
                    lockRootViewHeight();
                    toggleViewVisible(mEmotionLayout);
                    mEditText.requestFocus();
                    unlockRootViewHeightDelayed();
                }
                break;
            case R.id.sound_record_arbitrary_note:
                addRecord();
                break;
        }
    }

    private void addRecord(){
        LinearLayout record = (LinearLayout) getLayoutInflater().inflate(R.layout.sound_record,null);
        Button button = new Button(this);
        mContent.addView(record);
        //mContent.addView(record);
    }

    private void lockRootViewHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mScrollView.getLayoutParams();
        params.weight = 0;
        params.height = mScrollView.getHeight();
    }

    private void unlockRootViewHeightDelayed() {
        mScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mScrollView.getLayoutParams()).weight = 1;
            }
        }, 200);
    }

    //切换输入法键盘可见性
    private void toggleSoftInput() {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //切换视图可见性
    private void toggleViewVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PHOTO:
                    String imagePath = Util.getRealImagePath(this, data);
                    mImageManger.insertImage(imagePath);
                    break;
            }
        }
    }


    //运行时权限申请
    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, new String[]{permission}, READ_EX_STORAGE);
        }
    }

    //加载 edittext 内容
    private void loadContent() {
        Cursor cursor = mDB.rawQuery("select * from ArbitraryNote where id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToLast()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            splitAndDisaplay(content);
        }
        cursor.close();
    }


    //图片和文本分离并显示
    private void splitAndDisaplay(String content) {
        int pathStart = content.indexOf("[pic]");
        MyLog.e(CLASS_NAME, "索引 pathStart = " + pathStart);
        switch (pathStart) {
            case -1:
                mEditText.append(content);
                break;
            case 0:
                int pathEnd = content.indexOf("[/pic]");
                String imagePath = content.substring(pathStart + 5, pathEnd);
                mImageManger.insertImage(imagePath);
                content = content.substring(pathEnd + 6);
                splitAndDisaplay(content);
                break;
            default:
                mEditText.append(content.substring(0, pathStart));
                content = content.substring(pathStart);
                splitAndDisaplay(content);
                break;
        }
    }

    //获取 edittext 实际宽度
    private void getMesuredWidth() {
        mEditText.post(new Runnable() {
            @Override
            public void run() {
                mMeasuredWidth = mEditText.getWidth();
                loadContent(); //获取宽度后再加载内容
            }
        });
    }


    class ImageManger{
        /**
         * 插入图片
         *
         * @param imagePath 图片路径
         */
        private void insertImage(String imagePath) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            bitmap = configBitmap(bitmap);
            SpannableString spannableString = new SpannableString("[pic]" + imagePath + "[/pic]");
            ImageSpan imageSpan = new ImageSpan(ArbitraryNoteActivity.this, bitmap, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(imageSpan, 0,
                    imagePath.length() + "[pic][/pic]".length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            Editable edit = mEditText.getEditableText();
            edit.insert(mEditText.getSelectionStart(), spannableString);
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
            ratio = (float) (mMeasuredWidth - 10) / (float) width;
            MyLog.e(getLocalClassName(), "比例 ratio = " + ratio);
            Matrix matrix = new Matrix();
            matrix.postScale(ratio, ratio);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return bitmap;
        }

    }


    class EmotionViewManager{

        private void init() {
            int keyboardHeight = Util.getKeyboardHeight(ArbitraryNoteActivity.this);
            mEmotionLayout = new LinearLayout(ArbitraryNoteActivity.this);
            mEmotionLayout.setOrientation(LinearLayout.VERTICAL);
            mEmotionLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            mEmotionLayout.setVisibility(View.GONE);
            ViewPager emojiVP = new ViewPager(ArbitraryNoteActivity.this);
            emojiVP.setId("emojiVP".hashCode());
            emojiVP.setAdapter(new EmojiPagerAdapter(initViewList()));
            mEmotionLayout.addView(emojiVP,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    keyboardHeight*4/5));
            LinearLayout.LayoutParams indicatorParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    keyboardHeight/5);
            indicatorParams.gravity=Gravity.CENTER;
            final IndicatorView indicator = new IndicatorView(ArbitraryNoteActivity.this,EmojiManager.PAGES);
            mEmotionLayout.addView(indicator,indicatorParams);
            mRootView.addView(mEmotionLayout,
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,keyboardHeight));
            emojiVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    indicator.setPosition(position,positionOffset);
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        private List<View> initViewList() {
            int numPerPage = EmojiManager.COLUMN_NUM * EmojiManager.ROW_NUM;
            List<View> viewList = new ArrayList<>();
            List<Emoji> allEmojis = EmojiManager.toList();
            for (int i = 0; i <= allEmojis.size() / numPerPage; i++) {
                GridView gridView = new GridView(ArbitraryNoteActivity.this);
                ViewPager.LayoutParams gridParams = new ViewPager.LayoutParams();
                gridView.setLayoutParams(gridParams);
                gridView.setNumColumns(EmojiManager.COLUMN_NUM);
                final List<Emoji> emojiList;
                if (i == allEmojis.size() / numPerPage) {
                    if (allEmojis.size() % numPerPage == 0) {
                        break;
                    } else {
                        emojiList = allEmojis.subList(i * numPerPage, allEmojis.size());
                    }
                } else {
                    emojiList = allEmojis.subList(i * numPerPage, (i + 1) * numPerPage);
                }
                gridView.setAdapter(new EmojiGridAdapter(ArbitraryNoteActivity.this, emojiList));
                viewList.add(gridView);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int imageId = emojiList.get(position).getImageResId();
                        SpannableString spanString = new SpannableString("[Emoji]"+imageId+"[/Emoji]");
                        ImageSpan imageSpan = new ImageSpan(ArbitraryNoteActivity.this,imageId);
                        spanString.setSpan(imageSpan,0,spanString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Editable editable = mEditText.getEditableText();
                        editable.insert(mEditText.getSelectionStart(),spanString);
                    }
                });
            }
            return viewList;
        }

    }
}












