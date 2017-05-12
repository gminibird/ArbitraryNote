package com.zrj.arbitrarynote.bmob;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.zrj.arbitrarynote.util.MyLog;

import cn.bmob.v3.exception.BmobException;

/**
 * Created by a on 2017/5/11.
 */

public class BmobUtil {

    public static final String APP_ID = "55d899d33f17878859b67be3d5f02401";

    public static void handleException(Context context, BmobException e, @Nullable String messageDefault) {
        String message = "错误";

        Log.e("Bmob exception code", Integer.toString(e.getErrorCode()));

        switch (e.getErrorCode()) {
            case 9010:
                Toast.makeText(context, "网络超时", Toast.LENGTH_SHORT).show();
                break;
            case 9014:
                Toast.makeText(context, "第三方账号授权失败", Toast.LENGTH_SHORT).show();
                break;
            case 9016:
                Toast.makeText(context, "无网络连接，请检查您的手机网络", Toast.LENGTH_SHORT).show();
                break;
            case 9018:
                Toast.makeText(context,"参数不能为空",Toast.LENGTH_SHORT).show();
                break;
            case 9019:
                Toast.makeText(context, "输入数据格式不正确", Toast.LENGTH_SHORT).show();
                break;
            case 9022:
                Toast.makeText(context, "文件上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                break;

            case 101:
                Toast.makeText(context,"账号或密码错误",Toast.LENGTH_SHORT).show();
                break;
            case 202:
                Toast.makeText(context, "用户已经存在", Toast.LENGTH_SHORT).show();
                break;
            case 203:
                Toast.makeText(context, "邮箱已经存在", Toast.LENGTH_SHORT).show();
                break;
            case 205:
                Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
                break;
            case 207:
                Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                break;
            case 209:
                Toast.makeText(context, "该手机号码已经存在", Toast.LENGTH_SHORT).show();
                break;
            case 210:
                Toast.makeText(context, "旧密码不正确", Toast.LENGTH_SHORT).show();
                break;

            case 10010:
                Toast.makeText(context, "发送信息太过，请稍后再试", Toast.LENGTH_SHORT).show();
                break;

            default:
                if (messageDefault != null && !messageDefault.equals("")) {
                    message = messageDefault;
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
