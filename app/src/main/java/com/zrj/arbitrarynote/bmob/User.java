package com.zrj.arbitrarynote.bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by a on 2017/5/9.
 */


public class User extends BmobUser{

    private String lastedLogin;

    public void setLastedLogin(String lastedLogin) {
        this.lastedLogin = lastedLogin;
    }
}
