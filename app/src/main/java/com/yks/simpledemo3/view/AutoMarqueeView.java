package com.yks.simpledemo3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 描述：跑马灯
 * 作者：zzh
 * time:2019/11/05
 * https://www.jianshu.com/p/03119bfb0331?tdsourcetag=s_pcqq_aiomsg
 */
public class AutoMarqueeView extends TextView {
    public AutoMarqueeView(Context context) {
        super(context);
    }

    public AutoMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
