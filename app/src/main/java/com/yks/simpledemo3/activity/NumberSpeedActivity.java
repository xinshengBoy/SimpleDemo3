package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.DigitSpeedView;
import com.yks.simpledemo3.view.MyActionBar;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * 描述：数字测速仪
 * 作者：zzh
 * time:2020/03/24
 * https://github.com/capur16/DigitSpeedView
 */
public class NumberSpeedActivity extends Activity {

    private Activity mActivity = NumberSpeedActivity.this;
    private final int ADDSPEED = 0;
    private final int REDUCESPEED = 1;

    private DigitSpeedView view_speedview1,view_speedview2;
    private MyHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_speed);

        handler = new MyHandler(mActivity);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "数字测速仪", "", false);

        view_speedview1 = findViewById(R.id.view_speedview1);
        view_speedview2 = findViewById(R.id.view_speedview2);
    }

    public void addSpeed(View view){
        handler.sendEmptyMessage(ADDSPEED);
    }

    public void reduceSpeed(View view){
        handler.sendEmptyMessage(REDUCESPEED);
    }

    private int getSpeed(boolean isAdd,int speed){
        Random random = new Random();
        int speeds;
        if (isAdd){
            speeds = random.nextInt(speed) + speed;
            return speeds <= 1 ? 16 : speeds;
        }else {
            speeds = speed - random.nextInt(speed);
            return speeds <= 1 ? 13 : speeds;
        }
    }

    private class MyHandler extends Handler {

        final WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == ADDSPEED) {//todo 增加速度
                    view_speedview1.updateSpeed(getSpeed(true,view_speedview1.getSpeed()));
                    view_speedview2.updateSpeed(getSpeed(true,view_speedview2.getSpeed()));
                }else if (msg.what == REDUCESPEED){//todo 减少速度
                    view_speedview1.updateSpeed(Math.abs(getSpeed(false,view_speedview1.getSpeed())));
                    view_speedview2.updateSpeed(Math.abs(getSpeed(false,view_speedview2.getSpeed())));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
