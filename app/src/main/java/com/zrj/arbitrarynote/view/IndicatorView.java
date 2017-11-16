package com.zrj.arbitrarynote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.util.Util;

/**
 * Created by a on 2017/11/2.
 */

public class IndicatorView extends View {

    private int num;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSmallRadius;
    private int mBigRadius;
    private int mColorSmall;
    private int mColorBig;
    private float mOffset;
    private int mPosition;
    private int mDistance;

    public IndicatorView(Context context,int num) {
        this(context,null,num);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs,int num){
        this(context,attrs,0,num);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,int num){
        super(context,attrs,defStyleAttr);
        this.num=num;
        mPaint.setStyle(Paint.Style.FILL);
        mColorSmall = context.getResources().getColor(R.color.colorEditHint);
        mColorBig= context.getResources().getColor(R.color.colorTextGrey);
        mSmallRadius = Util.dp2px(context,3);
        mBigRadius = Util.dp2px(context,4);
        mDistance = mBigRadius*4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColorSmall);
        int x = mBigRadius;
        int y = getMeasuredHeight()/2;
        for (int i=0;i<num;i++){
            canvas.drawCircle(mDistance*i+x,y, mSmallRadius,mPaint);
        }
        mPaint.setColor(mColorBig);
        canvas.drawCircle((mPosition+mOffset)*mDistance+x,y,mBigRadius,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec){
        int result =0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode==MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = mBigRadius*2+(num-1)*mDistance;
            if (specMode==MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec){
        int result =0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode==MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = mBigRadius*2;
            if (specMode==MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    public void setPosition(int position, float offset){
        mPosition=position;
        mOffset=offset;
        invalidate();
    }
}
