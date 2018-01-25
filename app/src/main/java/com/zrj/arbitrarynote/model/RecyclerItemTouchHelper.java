package com.zrj.arbitrarynote.model;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Layout;
import android.widget.EditText;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.adapter.RecyclerAdapter;
import com.zrj.arbitrarynote.adapter.ViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by a on 2017/12/24.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper {

    private static RecyclerItemTouchHelper mInstance;

    private RecyclerItemTouchHelper(Callback callback) {
        super(callback);
    }

    public static RecyclerItemTouchHelper getInstance(List dataList){
        if (mInstance==null){
            mInstance = new RecyclerItemTouchHelper(new ItemTouchCallback(new MyOnItemCallbackListener(dataList)));
        }
        return mInstance;
    }

    private static class  MyOnItemCallbackListener implements ItemTouchCallback.OnItemTouchCallbackListener {
        ObjectAnimator mAnimator;

        private List<RecyclerItem> mDataList;
        private boolean swipeEnabled = false;
        private boolean longPressDragEnabled = false;

        public  MyOnItemCallbackListener(List dataList) {
            mDataList = dataList;
        }


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int srcPosition = viewHolder.getAdapterPosition();
            int targetPosition = target.getAdapterPosition();
            if (srcPosition < targetPosition) {
                for (int i = srcPosition; i < targetPosition; i++) {
                    Collections.swap(mDataList, i, i + 1);
                }
            } else {
                for (int i = srcPosition; i > targetPosition; i--) {
                    Collections.swap(mDataList, i, i - 1);
                }
            }
            recyclerView.getAdapter().notifyItemMoved(srcPosition, targetPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return swipeEnabled;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (viewHolder != null) {
                mAnimator = ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1f, 0.5f);
                mAnimator.setDuration(200);
                mAnimator.start();
                for (int i=0;i<mDataList.size();i++){
                    Object item =  mDataList.get(i);
                    if (item instanceof ArbitraryEdit){
                        ((ArbitraryEdit) item).setOnConvertListener(new ArbitraryEdit.OnConvertListener() {
                            @Override
                            public void convert(ViewHolder holder, final RecyclerAdapter adapter) {
                                holder.setIsRecyclable(false);
                                int position = holder.getAdapterPosition();
                                mDataList.remove(position);
                                EditText editText = holder.getView(R.id.edit_arbitrary_note);
                                Layout layout = editText.getLayout();
                                if (layout!=null){
                                    int lineCount = layout.getLineCount();
                                    String content =editText.getText().toString();
                                    for (int i=0;i<lineCount;i++){
                                        int start = layout.getLineStart(i);
                                        int end = layout.getLineEnd(i);
                                        String lineContent = content.substring(start,end);
                                        ArbitraryEdit edit = new ArbitraryEdit(lineContent);
                                        edit.setMinLines(1);
                                        mDataList.add(position+i,edit);
                                    }
                                    ((Activity)editText.getContext()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }


                            }
                        });
                    }
                }
            }


        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (mAnimator != null) {
                mAnimator.setFloatValues(0.5f, 1f);
                mAnimator.start();
            }
        }

        public void setSwipeEnable(boolean isEnabled) {
            swipeEnabled = isEnabled;
        }

        public void setLongPressDragEnabled(boolean isEnabled) {
            longPressDragEnabled = isEnabled;
        }
    }

}
