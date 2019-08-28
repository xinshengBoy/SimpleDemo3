package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;

/**
 * 描述：数字雨
 * 作者：zzh
 * time:2019/08/27
 */
public class NumberRainView extends LinearLayout {

    private int mNormalColor = Color.GREEN;
    private int mHightLightColor = Color.YELLOW;
    private float mTextSize = 30 * getResources().getDisplayMetrics().density;
    private  LinearLayout.LayoutParams mLayoutParams;

    private final int mCount = (int) (getResources().getDisplayMetrics().widthPixels / mTextSize);

    public NumberRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberRainView);
            mHightLightColor = array.getColor(R.styleable.NumberRainView_rainHighLightColor,Color.GREEN);
            mNormalColor = array.getColor(R.styleable.NumberRainView_rainNormalColor,Color.YELLOW);
            mTextSize = array.getDimension(R.styleable.NumberRainView_rainTextSize,mTextSize);
            array.recycle();
        }
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.BLACK);
        mLayoutParams = new LinearLayout.LayoutParams((int) mTextSize + 10, getResources().getDisplayMetrics().heightPixels);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        addRainItems();
    }

    private void addRainItems(){
        for (int i=0;i<mCount;i++){
            NumberRainItem item = new NumberRainItem(getContext(),null);
            item.setHighLightColor(mHightLightColor);
            item.setNormalColor(mNormalColor);
            item.setTextSize(mTextSize);
            item.setStartOffset((int)(Math.random()*10000));
            addView(item,mLayoutParams);
        }
    }
}
