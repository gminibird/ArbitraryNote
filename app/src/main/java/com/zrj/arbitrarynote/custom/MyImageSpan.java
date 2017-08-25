package com.zrj.arbitrarynote.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by Administrator on 2017/8/5.
 */

public class MyImageSpan extends ImageSpan {

    public MyImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent +y+fm.ascent) / 2
                - drawable.getBounds().bottom+12;
        canvas.save();
        canvas.translate(x+5, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
