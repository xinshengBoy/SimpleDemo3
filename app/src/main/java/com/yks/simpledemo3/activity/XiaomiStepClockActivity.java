package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.XiaomiStepView;

/**
 * 描述：实现小米计时器
 * 作者：zzh
 * time:2019/07/20
 * 参考文章：https://github.com/burro630/MiStopwatch/blob/master/app/src/main/java/com/burro/mistopwatch/StopwatchView.java
 */
public class XiaomiStepClockActivity extends Activity {

    private Activity mActivity = XiaomiStepClockActivity.this;
    private XiaomiStepView view_stepclock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaomi_stepclock);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"小米计时器","",false);

        view_stepclock = findViewById(R.id.view_stepclock);
    }

    public void clockStart(View view){
        view_stepclock.start();
    }

    public void clockPause(View view){
        view_stepclock.pause();
    }

    public void clockReset(View view){
        view_stepclock.reset();
    }

    public void clockRecord(View view){
        Toast.makeText(XiaomiStepClockActivity.this,"毫秒值："+view_stepclock.record(),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view_stepclock.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        view_stepclock.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view_stepclock.reset();
    }
}
