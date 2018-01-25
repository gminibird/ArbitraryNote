package com.zrj.arbitrarynote.model;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.adapter.RecyclerAdapter;
import com.zrj.arbitrarynote.adapter.ViewHolder;

import java.util.List;

/**
 * Created by a on 2017/11/16.
 */

public class ArbitraryEdit implements RecyclerItem {

    public static final int TYPE = "Edit".hashCode();
    private String mContent;
    private int mMinLines = 1;
    private OnConvertListener mConvertListener;

    public ArbitraryEdit() {
    }

    public ArbitraryEdit(String content) {
        mContent = content;
    }


    public int getMinLines() {
        return mMinLines;
    }

    public void setMinLines(int minLines) {
        this.mMinLines = minLines;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }


    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public View getView(Context context, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        return inflater.inflate(R.layout.arbitrary_edit, parent, false);
    }

    @Override
    public void convert(ViewHolder holder, List dataList, RecyclerAdapter adapter) {
        final EditText editText = holder.getView(R.id.edit_arbitrary_note);
//        MyLog.e("=====================", editText.toString());

        TextWatcher watcher = (TextWatcher) editText.getTag();
        editText.removeTextChangedListener(watcher);
        editText.setText(getContent());
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                MyLog.e("============================", "afterTextChanged() 执行====" + s.toString());
                setContent(s.toString());
            }
        };
        editText.setTag(watcher);
        editText.addTextChangedListener(watcher);
        if (mConvertListener!=null){
            mConvertListener.convert(holder, adapter);
        }
    }

    public void setOnConvertListener(OnConvertListener listener){
        mConvertListener = listener;
    }

    public interface OnConvertListener {
        void convert(ViewHolder holder, RecyclerAdapter adapter);
    }

}







