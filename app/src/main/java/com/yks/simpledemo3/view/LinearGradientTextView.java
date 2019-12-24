package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 描述：霓虹灯文字
 * 作者：zzh
 * time:2019/12/24
 * https://blog.csdn.net/baopengjian/article/details/80652149
 */
public class LinearGradientTextView extends TextView {

    private LinearGradient gradient;
    private float mTranslate;
    private float deltax = 20;

    public LinearGradientTextView(Context context) {
        super(context);
    }

    public LinearGradientTextView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到textview的画笔
        TextPaint mPaint = getPaint();
        String text = getText().toString();//获取内容
        float textWidth = mPaint.measureText(text);//获取文字长度
        int gradientSize = (int) (textWidth / text.length() * 3);
        //从左边开始渐变
        gradient = new LinearGradient(-gradientSize,0,0,0,new int[]{0x22ffffff,0xffffffff,0x22ffffff},null, Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mTranslate += deltax;
        float textWidth = getPaint().measureText(getText().toString());
        if (mTranslate > textWidth + 3 || mTranslate < 1){
            deltax = -deltax;
        }

        Matrix matrix = new Matrix();
        matrix.setTranslate(mTranslate,0);
        gradient.setLocalMatrix(matrix);
        postInvalidateDelayed(50);
    }
}
