package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：自定义时钟
 * 作者：zzh
 * time:2019/08/05
 * https://github.com/Jmengfei/CustomView/blob/master/app/src/main/res/layout/activity_main.xml
 */
public class CustomClockActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clock);

        initView();
    }

    private void initView(){
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(CustomClockActivity.this, title_layout, "自定义时钟", "", false);
    }
}
