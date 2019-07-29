package com.yks.simpledemo3.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;


/**
 * 描述：首页功能按钮的自定义（上面图片，下面文字）
 * 作者：zzh
 * time:2019/07/19
 * 参考文章:https://www.jb51.net/article/121873.htm
 */
public class MenuItemView extends LinearLayout {

    private ImageView ivMenu = null;
    private TextView txtMenu = null;
    private int text;
    private int imageId;
    private int textColor;

    public MenuItemView(Context context) {
        this(context,null);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MenuItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER);

        if (ivMenu == null){
            ivMenu = new ImageView(context);
        }

        if (txtMenu == null){
            txtMenu = new TextView(context);
        }

        if (attrs == null){
            return;
        }

        int count = attrs.getAttributeCount();
        for (int i=0;i<count;i++){
            String attrName = attrs.getAttributeName(i);
            switch (attrName){
                case "myImageResource":
                    imageId = attrs.getAttributeResourceValue(i,0);
                    break;
                case "myTextContent":
                    text = attrs.getAttributeResourceValue(i,0);
                    break;
                case "myTextColor":
                    textColor = attrs.getAttributeResourceValue(i,0);
                    break;
            }
        }

        inits();
    }

    private void inits(){
        this.setText(text);
        txtMenu.setGravity(Gravity.CENTER);
        this.setImageResource(imageId);
        this.setTextColor(textColor);
        addView(ivMenu);
        addView(txtMenu);
    }

    /**
     * 描述：设置图标
     * 作者：zzh
     */
    private void setImageResource(int resourceId){
        if (resourceId == 0){
            this.ivMenu.setImageResource(0);
        }else {
            this.ivMenu.setImageResource(resourceId);
        }
        //设置权重
        LinearLayout.LayoutParams ivParams = new LayoutParams(LayoutParams.WRAP_CONTENT,0,5.0f);
        this.ivMenu.setLayoutParams(ivParams);
    }


    public void setText(int text){
        this.txtMenu.setText(text);
        LinearLayout.LayoutParams ivParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,3.0f);
        this.txtMenu.setLayoutParams(ivParams);
    }

    private void setTextColor(int color){
        if (color == 0){
            this.txtMenu.setTextColor(getResources().getColor(R.color.colorBlack2));
        }else {
            this.txtMenu.setTextColor(getResources().getColor(color));
        }
    }

    public String getText(){
        return txtMenu.getText().toString().trim();
    }
}
