package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.TimeDownView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：自定义倒计时
 * 作者：zzh
 * time:2019/12/09
 * https://github.com/alidili/Demos/blob/master/CountdownDemo/app/src/main/java/com/yl/countdown/CountdownView.java
 */
public class TimeDownActivity extends Activity {

    private TimeDownView view_timedown;
    private int times = 15;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timedown);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(TimeDownActivity.this, title_layout, "倒计时", "", false);

        view_timedown = findViewById(R.id.view_timedown);
        view_timedown.setTimeDown(times);
        view_timedown.setOnTimeDownListener(new TimeDownView.OnTimeDownListener() {
            @Override
            public void timeDown(int time) {
                times = time;
            }
        });
    }

    public void startTimeDown(View view){
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        times --;
                        view_timedown.setTimeDown(times);
                        if (times == 0){
                            timer.cancel();
                        }
                    }
                });
            }
        },1000,1000);
    }
}
