package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 描述：自定义右滑解锁控件
 * 作者：zzh
 * time:2019/08/06
 * https://blog.csdn.net/u012728458/article/details/50747280
 */
public class SlideRightViewDragHelper extends LinearLayout {

    private ViewDragHelper helper;
    private int oldX;
    private Point startPoint = new Point();
    private Point endPosition = new Point();
    private View child;
    private OnReleasedListener onReleasedListener;

    public SlideRightViewDragHelper(Context context, AttributeSet attrs) {
        super(context, attrs);

        helper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                oldX = left;
                return Math.max(0,left);
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (oldX > 200) {
                    helper.settleCapturedViewAt(endPosition.x, endPosition.y);
                    invalidate();//todo 必须强制刷新
                    if (onReleasedListener != null){
                        onReleasedListener.onReleased();
                    }
                }else {
                    helper.settleCapturedViewAt(startPoint.x,endPosition.y);//反弹
                    invalidate();
                }
                super.onViewReleased(releasedChild,xvel,yvel);
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        child = getChildAt(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return helper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        helper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //定位开始坐标
        startPoint.x = child.getLeft();
        startPoint.y = child.getTop();
        //定位滑动成功后的坐标
        endPosition.x = child.getRight();
        endPosition.y = child.getTop();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (helper.continueSettling(true)){
            invalidate();
        }
    }

    public void setOnReleasedListener(OnReleasedListener listener){
        this.onReleasedListener = listener;
    }

    public interface OnReleasedListener {
        void onReleased();
    }
}
