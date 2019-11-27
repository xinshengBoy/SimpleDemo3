package com.yks.simpledemo3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 描述：解决gridview与scrollview的滚动冲突
 * 作者：zzh
 * time:2019/11/27
 * https://blog.csdn.net/u013227064/article/details/51113662
 */
public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
