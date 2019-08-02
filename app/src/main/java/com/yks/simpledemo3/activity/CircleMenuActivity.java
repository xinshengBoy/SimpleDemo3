package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.CircleMenuView;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：转盘菜单
 * 作者：zzh
 * time:2019/08/01
 * https://github.com/hongyangAndroid/Android-CircleMenu/blob/master/sample_zhy_CircleMenu/src/com/zhy/sample_circlemenu/CCBActivity.java
 */
public class CircleMenuActivity extends Activity {

    private Activity mActivity = CircleMenuActivity.this;
    private Context mContext = CircleMenuActivity.this;
    private int [] icons = {R.drawable.ic_circle_menu,R.drawable.ic_csv_table,R.drawable.ic_timing_tasks,R.drawable.ic_temperature,
                            R.drawable.ic_water_picture,R.drawable.ic_windwill,R.drawable.ic_super_button};
    private String [] texts = {"旋转菜单","表格导出","定时请求","温度计","水印图片","大风车","超级按钮"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_menu_center);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "转盘菜单", "", false);

        CircleMenuView id_menulayout = findViewById(R.id.id_menulayout);
        id_menulayout.setMenuItemIconAndText(icons,texts);
        id_menulayout.setOnMenuItemClickListener(new CircleMenuView.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                Info.showToast(mContext,texts[pos],true);
                Info.playRingtone(mContext,true);
            }

            @Override
            public void itemCenterClick(View view) {
                Info.showToast(mContext,"欢迎光临",true);
            }
        });
    }
}
