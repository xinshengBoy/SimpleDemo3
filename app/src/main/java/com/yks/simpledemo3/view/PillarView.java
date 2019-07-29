package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 描述：风车支柱的自定义
 * 作者：zzh
 * time:2019/07/25
 */
public class PillarView extends View {

    private Paint paint;
    private Path path;
    private int width,height;

    public PillarView(Context context) {
        this(context,null);
    }

    public PillarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PillarView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint();
        path = new Path();

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新计算宽高
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        path.moveTo(width / 2 + getFitSize(10),width / 2 + getFitSize(40));
        path.lineTo(width / 2 - getFitSize(10),width / 2 + getFitSize(40));
        path.lineTo(width / 2 - getFitSize(20),height - getFitSize(20));
        path.quadTo(width / 2,height,width / 2 + getFitSize(40),height - getFitSize(20));
        path.close();
        canvas.drawPath(path,paint);
        canvas.restore();
    }

    private float getFitSize(float size){
        return size * width / 496;
    }
}
