package com.zrj.arbitrarynote.model;

/**
 * Created by a on 2017/9/12.
 */

public class Emoji {

    private int imageResId;

    public Emoji(int imageId){
        this.imageResId = imageId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
