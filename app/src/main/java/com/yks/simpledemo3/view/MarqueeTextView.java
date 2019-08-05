package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 描述：
 * 作者：
 * time:2019/08/05
 */
public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {

    private float mWidth;

    public MarqueeTextView(Context context) {
        this(context,null);
    }

    public MarqueeTextView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MarqueeTextView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setFocusable(true);
        setMarqueeRepeatLimit(-1);
        setFocusableInTouchMode(true);
        setBackgroundColor(Color.BLACK);
        setTextColor(Color.WHITE);
        setTextSize(56);
        setsc
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String input = text.toString();
        Paint paint = getPaint();
        float length = paint.measureText(input);
        while (length < mWidth){
            input += " ";
            length = paint.measureText(input);
        }
        super.setText(input, type);
//        setText(input);
    }

    //重写这个方法，强制返回true
    @Override
    public boolean isFocused() {
        return true;
    }
    /**
     * 用于 EditText 存在时抢占焦点
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused){
            super.onFocusChanged(focused,direction,previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
}
