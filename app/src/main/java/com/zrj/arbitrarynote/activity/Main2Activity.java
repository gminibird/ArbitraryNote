package com.zrj.arbitrarynote.activity;

import android.animation.ObjectAnimator;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.adapter.RecyclerAdapter;
import com.zrj.arbitrarynote.model.ArbitraryEdit;
import com.zrj.arbitrarynote.model.ArbitraryImg;
import com.zrj.arbitrarynote.model.ItemTouchCallback;
import com.zrj.arbitrarynote.model.MyThread;
import com.zrj.arbitrarynote.model.RecyclerItem;
import com.zrj.arbitrarynote.model.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class Main2Activity extends AppCompatActivity {

    private ItemTouchHelper mTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbitrary_main);
        init();
    }

    private void init(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        List<RecyclerItem> dataList = initData();
        RecyclerAdapter adapter = new RecyclerAdapter(this,dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        mTouchHelper = RecyclerItemTouchHelper.getInstance(dataList);
        mTouchHelper.attachToRecyclerView(recyclerView);

    }

    private List<RecyclerItem> initData(){
        List<RecyclerItem> dataList = new ArrayList<>();
        for (int i=0;i<20;i++){
            if (i%2==0){
                ArbitraryEdit edit = new ArbitraryEdit("hahah"+i);
                dataList.add(edit);
            }else {
                ArbitraryImg img = new ArbitraryImg(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                dataList.add(img);
            }

        }
        return dataList;
    }

//    class MyOnItemCallbackListener implements ItemTouchCallback.OnItemTouchCallbackListener{
//        ObjectAnimator mAnimator;
//
//        private List mDataList;
//        private boolean swipeEnabled = false;
//        private boolean longPressDragEnabled = false;
//
//        public MyOnItemCallbackListener(List dataList){
//            mDataList=dataList;
//        }
//
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            int srcPosition = viewHolder.getAdapterPosition();
//            int targetPosition = target.getAdapterPosition();
//            if (srcPosition < targetPosition) {
//                for (int i = srcPosition; i < targetPosition; i++) {
//                    Collections.swap(mDataList, i, i + 1);
//                }
//            } else {
//                for (int i = srcPosition; i > targetPosition; i--) {
//                    Collections.swap(mDataList, i, i - 1);
//                }
//            }
//            recyclerView.getAdapter().notifyItemMoved(srcPosition, targetPosition);
//
//            return true;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//
//        }
//
//        @Override
//        public boolean isItemViewSwipeEnabled() {
//            return swipeEnabled;
//        }
//
//        @Override
//        public boolean isLongPressDragEnabled() {
//            return longPressDragEnabled;
//        }
//
//        @Override
//        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
//            if (viewHolder!=null){
//                mAnimator = ObjectAnimator.ofFloat(viewHolder.itemView,"scaleY",1f,0.5f);
//                mAnimator.setDuration(200);
//                mAnimator.start();
//            }
//
//        }
//
//        @Override
//        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//            if (mAnimator!=null){
//                mAnimator.setFloatValues(0.5f,1f);
//                mAnimator.start();
//            }
//        }
//
//        public void setSwipeEnable(boolean isEnabled){
//            swipeEnabled = isEnabled;
//        }
//
//        public void setLongPressDragEnabled(boolean isEnabled){
//            longPressDragEnabled=isEnabled;
//        }
//    }
}
