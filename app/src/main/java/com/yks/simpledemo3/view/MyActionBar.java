package com.yks.simpledemo3.view;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;

/**
 * 描述：通用标题头，返回---标题---是否拍照
 * 作者：zzh
 * time:2018/11/23
 */

public class MyActionBar {

    @SuppressLint("StaticFieldLeak")
    private static TextView txt_actionbar_right;
    @SuppressLint("StaticFieldLeak")
    private static ImageView iv_actionbar_back;
    @SuppressLint("StaticFieldLeak")
    protected static View view;
    /**
     * 描述：统一标题头
     * @param activity 所在的类
     * @param layout 要显示的layout
     * @param title 标题名称
     * @return 返回当前视图
     */
    public static void show(final Activity activity, LinearLayout layout, String title, String right, boolean isShowRight){
        view = LayoutInflater.from(activity).inflate(R.layout.actionbar, null);
        iv_actionbar_back = view.findViewById(R.id.iv_actionbar_back);
        TextView txt_actionbar_title = view.findViewById(R.id.txt_actionbar_title);
        txt_actionbar_right = view.findViewById(R.id.txt_actionbar_right);

        //返回操作
        iv_actionbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        //设置标题
        txt_actionbar_title.setText(title);
        if (isShowRight) {
            txt_actionbar_right.setText(right);
            txt_actionbar_right.setVisibility(View.VISIBLE);
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        layout.addView(view,params);
    }

    public static void setOnLeftClicklistener(View.OnClickListener onClicklistener){
        iv_actionbar_back.setOnClickListener(onClicklistener);
    }

    public static void setOnRightClicklistener(View.OnClickListener onClicklistener){
        txt_actionbar_right.setOnClickListener(onClicklistener);
    }

    /**
     * 描述：设置背景颜色
     * 作者：zzh
     * @param color 背景颜色
     */
    public static void setViewBackgroundColor(int color){
        view.setBackgroundColor(color);
    }
}
