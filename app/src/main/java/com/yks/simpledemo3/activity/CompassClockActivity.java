package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：罗盘时钟
 * 作者：zzh
 * time:2019/10/10
 * 参考：https://github.com/2487686673/ClockCompass
 */
public class CompassClockActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass_clock);

        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(CompassClockActivity.this, title_layout, "罗盘时钟", "", false);
    }
}
