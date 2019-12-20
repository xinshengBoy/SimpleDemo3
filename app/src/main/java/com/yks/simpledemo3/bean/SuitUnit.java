package com.yks.simpledemo3.bean;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.graphics.PointF;

/**
 * 描述：曲线坐标类
 * 作者：zzh
 * time:2019/12/20
 */
public class SuitUnit implements Comparable<SuitUnit>, Cloneable {

    public static long DURATION = 800;
    private ValueAnimator VALUEANIMATOR = ValueAnimator.ofFloat(0, 1);

    /**
     * 当前点的值
     */
    private float value;
    // 当前点的额外信息（可选，x轴）
    private String extX;
    /**
     * 当前点的坐标信息,都是相对的canvas而不是linesArea
     */
    private PointF xy;

    /**
     * 当前点的动画进度，
     * 默认为1表示无动画
     */
    private float percent = 1f;

    public SuitUnit(float value) {
        this.value = value;
    }

    public SuitUnit(float value, String extX) {
        this.value = value;
        this.extX = extX;
    }


    public float getValue() {
        return value;
    }
    public void setXY(PointF xy) {
        this.xy = xy;
    }
    public PointF getXY() {
        return xy;
    }
    public void setPercent(float percent) {
        this.percent = percent;
    }

    public float getPercent() {
        return percent;
    }

    public void setExtX(String extX) {
        this.extX = extX;
    }

    public String getExtX() {
        return extX;
    }


    public void cancelToEndAnim() {
        if (VALUEANIMATOR.isRunning()) {
            VALUEANIMATOR.cancel();
        }
        percent = 1f;
    }

    public void startAnim(TimeInterpolator value) {
        if (percent > 0 || VALUEANIMATOR.isRunning()) {
            return;
        }
        // 如果value小于一定阈值就不开启动画
        if (Math.abs((int)this.value) < 0.1) {
            percent = 1;
            return;
        }
        VALUEANIMATOR.setFloatValues(0, 1);
        VALUEANIMATOR.setDuration(DURATION);
        VALUEANIMATOR.setInterpolator(value);
        VALUEANIMATOR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
            }
        });
        VALUEANIMATOR.start();
    }



    @Override
    public int compareTo(SuitUnit o) {
        if (value == o.value) {
            return 0;
        } else if (value > o.value) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SuitUnit)) return false;
        SuitUnit unit = (SuitUnit) obj;
        return value == unit.value
                && (extX == unit.extX) || (extX != null && extX.equals(unit.extX));
    }

    @Override
    public String toString() {
        return "Unit{" +
                "xy=" + xy +
                '}';
    }

    @Override
    public SuitUnit clone() {// 转化为深度拷贝，防止在集合中排序时的引用问题
        try {
            return (SuitUnit) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
