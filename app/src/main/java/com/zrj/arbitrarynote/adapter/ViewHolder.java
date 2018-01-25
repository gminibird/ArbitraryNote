package com.zrj.arbitrarynote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.zrj.arbitrarynote.model.RecyclerItem;

/**
 * Created by a on 2017/11/29.
 */

public  class  ViewHolder extends  RecyclerView.ViewHolder{

    private View mView;
    private Context mContext;
    private SparseArray<View> mViewMap = new SparseArray<>();

//    private ViewHolder(View itemView) {
//        super(itemView);
//        mView = itemView;
//    }
    public <T extends RecyclerItem> ViewHolder(Context context, T item, ViewGroup parent){
        super(item.getView(context,parent));
        mView=itemView;
        mContext = context;
    }

    public View getView(){
        return mView;
    }

    public <T extends View> T getView(int ResId){
        View view=mViewMap.get(ResId);
        if (view==null){
            view = mView.findViewById(ResId);
            mViewMap.put(ResId,view);
        }
        return (T) view;
    }


//    public static <T extends RecyclerItem> ViewHolder get(Context context, T t){
//
//        return new ViewHolder(t.getView(context));
//    }

}
