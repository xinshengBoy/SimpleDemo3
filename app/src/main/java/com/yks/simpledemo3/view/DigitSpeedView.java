package com.yks.simpledemo3.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;

/**
 * 描述：数字测速仪
 * Created by Riccardo on 13/01/2017.
 * https://github.com/capur16/DigitSpeedView
 */

public class DigitSpeedView extends RelativeLayout {
    private String unit = "Km/h";
    private float speedTextSize = dpTOpx(40f);
    private float unitTextSize = dpTOpx(10f);
    private int speed = 0;
    private int speedTextColor;
    private int unitTextColor;
    private int backgoundColor = Color.BLACK;
    private TextView mSpeedBgTextView;
    private TextView mSpeedTextView;
    private TextView mSpeedUnitTextView;
    private RelativeLayout mainLayout;
    private boolean showUnit = true;
    private OnSpeedChangeListener onSpeedChangeListener;

    public DigitSpeedView(Context context) {
        super(context);
        init(context);
    }

    public DigitSpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initAttributeSet(context, attrs);
    }

    public DigitSpeedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttributeSet(context, attrs);
    }

    private void init(Context context) {
        speedTextColor = ContextCompat.getColor(context, R.color.green);
        unitTextColor = ContextCompat.getColor(context, R.color.green);
        final View rootView = LayoutInflater.from(context).inflate(R.layout.digit_speed_view, this, true);
        mainLayout = rootView.findViewById(R.id.digit_speed_main);
        mSpeedTextView = rootView.findViewById(R.id.digit_speed);
        mSpeedBgTextView = rootView.findViewById(R.id.digit_speed_bg);
        mSpeedUnitTextView = rootView.findViewById(R.id.digit_speed_unit);
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), "fonts/digital-7_mono.ttf");
        mSpeedTextView.setTypeface(tf);
        mSpeedBgTextView.setTypeface(tf);
        mSpeedUnitTextView.setTypeface(tf);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DigitSpeedView, 0, 0);
        speedTextSize = a.getDimension(R.styleable.DigitSpeedView_speedTextSize, speedTextSize);
        unitTextSize = a.getDimension(R.styleable.DigitSpeedView_unitTextSize, unitTextSize);
        speedTextColor = a.getColor(R.styleable.DigitSpeedView_speedTextColor, speedTextColor);
        unitTextColor = a.getColor(R.styleable.DigitSpeedView_unitTextColor, unitTextColor);
        showUnit = a.getBoolean(R.styleable.DigitSpeedView_showUnit, showUnit);
        if(!showUnit) {
            mSpeedUnitTextView.setVisibility(GONE);
        }
        speed = a.getInt(R.styleable.DigitSpeedView_speed, speed);
        backgoundColor = a.getColor(R.styleable.DigitSpeedView_speedBackgroundColor, backgoundColor);
        String unit = a.getString(R.styleable.DigitSpeedView_speedUnit);
        this.unit =  (unit != null) ? unit : this.unit;
        if(a.getBoolean(R.styleable.DigitSpeedView_disableBackgroundImage, false)) {
            mainLayout.setBackgroundResource(0);
            mainLayout.setBackgroundColor(backgoundColor);
        } else {
            Drawable drawable = a.getDrawable(R.styleable.DigitSpeedView_backgroundDrawable);
            if (drawable != null) {
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    mainLayout.setBackground(drawable);
                } else {
                    mainLayout.setBackgroundDrawable( drawable );
                }

            }
        }
        a.recycle();
        initAttributeValue();
    }

    private void initAttributeValue() {
        mSpeedTextView.setTextColor(speedTextColor);
        mSpeedTextView.setText(""+speed);
        mSpeedTextView.setShadowLayer(20, 0, 0, speedTextColor);
        mSpeedTextView.setTextSize(speedTextSize);
        mSpeedBgTextView.setTextSize(speedTextSize);
        mSpeedUnitTextView.setText(unit);
        mSpeedUnitTextView.setTextColor(unitTextColor);
        mSpeedUnitTextView.setShadowLayer(20, 0, 0, unitTextColor);
        mSpeedUnitTextView.setTextSize(unitTextSize);

    }

    /**
     * update speed
     * @param speed to update
     */
    public void updateSpeed(int speed) {
        boolean isSpeedUp = speed > this.speed;
        this.speed = speed;
        mSpeedTextView.setText(""+speed);
        if (onSpeedChangeListener != null){
            onSpeedChangeListener.onSpeedChange(this, isSpeedUp);
        }
    }

    /**
     * Show unit text
     */
    public void showUnit() {
        mSpeedUnitTextView.setVisibility(VISIBLE);
    }

    /**
     * Hide unit text
     */
    public void hideUnit() {
        mSpeedUnitTextView.setVisibility(GONE);
    }

    /**
     *
     * @return current speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * convert dp to <b>pixel</b>.
     * @param dp to convert.
     * @return Dimension in pixel.
     */
    public float dpTOpx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * convert pixel to <b>dp</b>.
     * @param px to convert.
     * @return Dimension in dp.
     */
    public float pxTOdp(float px) {
        return px / getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * Register a callback to be invoked when speed value changed (in integer).
     * @param onSpeedChangeListener maybe null, The callback that will run.
     */
    public void setOnSpeedChangeListener(OnSpeedChangeListener onSpeedChangeListener) {
        this.onSpeedChangeListener = onSpeedChangeListener;
    }

    private interface OnSpeedChangeListener {
        void onSpeedChange(DigitSpeedView digitSpeedView, boolean isSpeedUp);
    }
}
