package com.zrj.arbitrarynote.model;

/**
 * Created by a on 2017/12/24.
 */

public class MyThread extends Thread {

    public boolean result=false;
    private ThreadListener mListener;

    public MyThread(ThreadListener listener){
        mListener = listener;
    }

    @Override
    public void run() {
        super.run();
        mListener.run();
    }

    public interface ThreadListener{
        int run();
    }
}
