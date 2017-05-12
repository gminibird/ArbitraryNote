package com.zrj.arbitrarynote.util;

import android.util.Log;

/**
 * Created by a on 2017/5/11.
 */

public class MyLog {

    public static final int VERBOSE = 0;

    public static final int DEBUG =1;

    public static final int INFO =2;

    public static final int WARN = 3;

    public static final int ERROR = 4;

    public static final int NOTHING = 5;

    public static final int CURRENT_LEVEL = VERBOSE;

    public static void v(String tag,String msg){
        if (CURRENT_LEVEL<=VERBOSE)
        Log.v(tag, msg);
    }

    public static void d(String tag,String msg){
        if (CURRENT_LEVEL<=DEBUG)
        Log.d(tag, msg);
    }

    public static void i(String tag,String msg){
        if (CURRENT_LEVEL<=INFO)
        Log.i(tag, msg);
    }

    public static void w(String tag,String msg){
        if (CURRENT_LEVEL<=WARN)
        Log.w(tag, msg);
    }

    public static void e(String tag,String msg){
        if (CURRENT_LEVEL<=ERROR)
        Log.e(tag, msg);
    }
}
