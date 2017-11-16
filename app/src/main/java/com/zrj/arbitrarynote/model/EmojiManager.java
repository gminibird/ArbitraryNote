package com.zrj.arbitrarynote.model;

import android.util.Log;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.util.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2017/9/20.
 */

public class EmojiManager {

    public static final int ROW_NUM = 4;
    public static final int COLUMN_NUM = 7;
    private static final int NUM_PER_PAGE =ROW_NUM*COLUMN_NUM;

    public static final int IMAGE_ID[] = {
            R.mipmap.emoji_face_1, R.mipmap.emoji_face_2, R.mipmap.emoji_face_3,
            R.mipmap.emoji_face_4, R.mipmap.emoji_face_5, R.mipmap.emoji_face_6,
            R.mipmap.emoji_face_7, R.mipmap.emoji_face_8, R.mipmap.emoji_face_9,
            R.mipmap.emoji_face_10, R.mipmap.emoji_face_11, R.mipmap.emoji_face_12,
            R.mipmap.emoji_face_13, R.mipmap.emoji_face_14, R.mipmap.emoji_face_15,
            R.mipmap.emoji_face_16, R.mipmap.emoji_face_17, R.mipmap.emoji_face_18,
            R.mipmap.emoji_face_19, R.mipmap.emoji_face_20, R.mipmap.emoji_face_21,
            R.mipmap.emoji_face_22, R.mipmap.emoji_face_23, R.mipmap.emoji_face_24,
            R.mipmap.emoji_face_25, R.mipmap.emoji_face_26, R.mipmap.emoji_face_27,
            R.mipmap.emoji_face_28, R.mipmap.emoji_face_29, R.mipmap.emoji_face_30,
            R.mipmap.emoji_face_31, R.mipmap.emoji_face_32, R.mipmap.emoji_face_33,
            R.mipmap.emoji_face_34, R.mipmap.emoji_face_35, R.mipmap.emoji_face_36,
            R.mipmap.emoji_face_37, R.mipmap.emoji_face_38, R.mipmap.emoji_face_39,
            R.mipmap.emoji_face_40, R.mipmap.emoji_face_41, R.mipmap.emoji_food_1,
            R.mipmap.emoji_food_2, R.mipmap.emoji_food_3, R.mipmap.emoji_food_4,
            R.mipmap.emoji_food_5, R.mipmap.emoji_food_6, R.mipmap.emoji_food_7,
            R.mipmap.emoji_food_8, R.mipmap.emoji_food_9, R.mipmap.emoji_food_10,
            R.mipmap.emoji_food_11, R.mipmap.emoji_food_12, R.mipmap.emoji_food_13,
            R.mipmap.emoji_food_14, R.mipmap.emoji_life_1, R.mipmap.emoji_life_2,
            R.mipmap.emoji_life_3, R.mipmap.emoji_life_4, R.mipmap.emoji_life_5,
            R.mipmap.emoji_life_6, R.mipmap.emoji_life_7, R.mipmap.emoji_life_8,
            R.mipmap.emoji_life_9, R.mipmap.emoji_life_10, R.mipmap.emoji_life_11,
            R.mipmap.emoji_other_1, R.mipmap.emoji_other_2, R.mipmap.emoji_other_3,
            R.mipmap.emoji_other_4, R.mipmap.emoji_sport_1, R.mipmap.emoji_sport_2,
            R.mipmap.emoji_sport_3, R.mipmap.emoji_sport_4, R.mipmap.emoji_traffic_1,
            R.mipmap.emoji_traffic_2, R.mipmap.emoji_traffic_3, R.mipmap.emoji_traffic_4,
    };
    private static float PAGES_FLOAT = IMAGE_ID.length/27.0f,PAGES_INT=(int)PAGES_FLOAT;
    public static final int PAGES = PAGES_FLOAT>(int)(PAGES_INT)?(int)PAGES_INT+1:(int)PAGES_INT;

    public static List<Emoji> toList() {
        List<Emoji> emojiList = new ArrayList<>();
        Emoji backspace = new Emoji(R.mipmap.ic_backspace);
        for (int i = 0; i < IMAGE_ID.length; i++) {
            if ((i + 1) % NUM_PER_PAGE == 0 && i != 0) {
                emojiList.add(backspace);
            } else {
                Emoji emoji = new Emoji(IMAGE_ID[i]);
                emojiList.add(emoji);
            }
        }
        if (IMAGE_ID.length % (NUM_PER_PAGE-1) != 0) {  //如果最后一页不满,则用null补足
            int pages = IMAGE_ID.length / (NUM_PER_PAGE-1);
            for (int i = IMAGE_ID.length; i < (pages + 1) * NUM_PER_PAGE - 1; i++) {
                emojiList.add(null);
            }
            emojiList.add(backspace);
        }
        return emojiList;
    }
}
