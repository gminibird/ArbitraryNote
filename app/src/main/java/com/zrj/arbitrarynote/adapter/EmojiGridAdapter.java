package com.zrj.arbitrarynote.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zrj.arbitrarynote.model.Emoji;
import com.zrj.arbitrarynote.model.EmojiManager;
import com.zrj.arbitrarynote.util.MyLog;
import com.zrj.arbitrarynote.util.Util;

import java.util.List;

/**
 * Created by a on 2017/9/17.
 */

public class EmojiGridAdapter extends BaseAdapter {


    private Activity mActivity;
    private List<Emoji> mEmojiList;
    private int mKeyboardHeight;

    public EmojiGridAdapter(Activity activity, List<Emoji> emojiList) {
        mActivity = activity;
        mEmojiList = emojiList;
        mKeyboardHeight = Util.getKeyboardHeight(mActivity);
    }

    @Override
    public int getCount() {
        return mEmojiList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEmojiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            Emoji emoji = mEmojiList.get(position);
            ImageView imageView = new ImageView(mActivity);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    mKeyboardHeight/ (EmojiManager.ROW_NUM + 1));
            imageView.setLayoutParams(params);
            int paddingHorizontal = ViewGroup.LayoutParams.MATCH_PARENT / EmojiManager.COLUMN_NUM / 5;
            int mPaddingVertical = mKeyboardHeight / EmojiManager.ROW_NUM / 5;
            imageView.setPadding(paddingHorizontal, mPaddingVertical, paddingHorizontal, mPaddingVertical);
            if (emoji != null) {
                imageView.setImageResource(emoji.getImageResId());
            }else {
              imageView.setVisibility(View.INVISIBLE);
            }
            MyLog.e("测试===================","null");
            return imageView;
        }
        MyLog.e("测试===================","非null");
        return convertView;

    }





}
