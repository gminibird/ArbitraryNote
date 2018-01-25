package com.zrj.arbitrarynote.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.zrj.arbitrarynote.model.ArbitraryEdit;

/**
 * Created by a on 2017/11/16.
 */

public class DragRecyclerView extends RecyclerView {

    private Context mContext;
    static final int TYPES = ArbitraryEdit.TYPE;
    final int a = 0;


    public DragRecyclerView(Context context) {
        this(context, null, 0);
    }


    public DragRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragRecyclerView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }



}
