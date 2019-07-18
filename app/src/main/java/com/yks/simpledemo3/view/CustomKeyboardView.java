package com.yks.simpledemo3.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.yks.simpledemo3.R;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 描述：自定义智能安全键盘
 * 作者：zzh
 * time:2019/07/15
 * 参考：https://github.com/peiniwan/SafeKeyBoard/blob/master/keyboard/src/main/java/com/kh/keyboard/CustomKeyboardView.java
 */
public class CustomKeyboardView extends KeyboardView {

    private Context mContext;
    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            List<Keyboard.Key> keys = getKeyboard().getKeys();
            for (Keyboard.Key key : keys){
                if (key.codes[0] == -5){//删除
                    Drawable dr = (Drawable) mContext.getResources().getDrawable(R.drawable.keyboard_word_del_layerlist);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);
                }else if (key.codes[0] == -35){//删除
                    Drawable dr = (Drawable) mContext.getResources().getDrawable(R.drawable.keyboard_word_del_layerlist2);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr.draw(canvas);
                }else if (key.codes[0] == -1){//大小写切换
                    Drawable dr = (Drawable) mContext.getResources().getDrawable(R.drawable.keyboard_word_shift_layerlist);
                    Drawable dr_da = (Drawable) mContext.getResources().getDrawable(R.drawable.keyboard_word_shift_layerlist_da);
                    dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                    dr_da.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);

                    if (KhKeyboardView.isUpper) {
                        dr_da.draw(canvas);
                    } else {
                        dr.draw(canvas);
                    }
                }else if (key.codes[0] == -2 || key.codes[0] == 90001){//数字键盘  特殊字符键盘
                    Drawable dr = mContext.getResources().getDrawable(R.drawable.keyboard_selector_blue_bg);
                    dr.setBounds(key.x,key.y,key.x+key.width,key.y+key.height);
                    dr.draw(canvas);
                    drawText(canvas,key);
                }else {

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void drawText(Canvas canvas, Keyboard.Key key) {
        try {
            Rect rect = new Rect();
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            if (key.label != null){
                String lable = key.label.toString();
                Field field;
                if (lable.length() > 1 && key.codes.length < 2){
                    int lableTextSize = 0;
                    try {
                        field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                        field.setAccessible(true);
                        lableTextSize = (int) field.get(this);
                    }catch (NoSuchFieldException e){
                        e.printStackTrace();
                    }catch (IllegalAccessException e){
                        e.printStackTrace();
                    }
                    paint.setTextSize(lableTextSize);
                    paint.setTypeface(Typeface.DEFAULT_BOLD);
                }else {
                    int keyTextSize = 0;
                    try {
                        field = KeyboardView.class.getDeclaredField("mLabelTextSize");
                        field.setAccessible(true);
                        keyTextSize = (int) field.get(this);
                    }catch (NoSuchFieldException e){
                        e.printStackTrace();
                    }catch (IllegalAccessException e){
                        e.printStackTrace();
                    }
                    paint.setTextSize(keyTextSize);
                    paint.setTypeface(Typeface.DEFAULT);
                }

                paint.getTextBounds(key.label.toString(),0,key.label.toString().length(),rect);
                canvas.drawText(key.label.toString(), key.x + (key.width / 2),
                        (key.y + key.height / 2) + rect.height() / 2, paint);
            }else if (key.icon != null) {
                key.icon.setBounds(key.x + (key.width - key.icon.getIntrinsicWidth()) / 2, key.y + (key.height - key.icon.getIntrinsicHeight()) / 2,
                        key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth(), key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight());
                key.icon.draw(canvas);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
