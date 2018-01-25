package com.zrj.arbitrarynote.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.zrj.arbitrarynote.adapter.RecyclerAdapter;
import com.zrj.arbitrarynote.adapter.ViewHolder;

import java.util.List;

/**
 * Created by a on 2017/12/20.
 */

public class ArbitraryImg implements RecyclerItem {

    private static int TYPE = "img".hashCode();
    private Bitmap mImg;



    public ArbitraryImg(Bitmap img) {
        mImg = img;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public View getView(final Context context, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return imageView;
    }


    @Override
    public void convert(final ViewHolder holder, final List dataList, RecyclerAdapter adapter) {
        final ImageView imageView = (ImageView) holder.getView();
        imageView.setImageBitmap(mImg);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector detector = new GestureDetector(imageView.getContext(),
                    new MyOnGestureListener(holder, dataList));
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
    }

    private Bitmap configImg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int fraction = width;
        return img;
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private ImageView imageView;
        private ViewHolder holder;
        private List dataList;

        public MyOnGestureListener(ViewHolder holder, List dataList){
            imageView = (ImageView) holder.getView();
            this.holder=holder;
            dataList = dataList;
        }


        @Override
        public void onLongPress(MotionEvent e) {
            RecyclerItemTouchHelper.getInstance(dataList).startDrag(holder);
        }

    }
}
