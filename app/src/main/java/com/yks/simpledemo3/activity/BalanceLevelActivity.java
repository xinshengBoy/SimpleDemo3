package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.LevelView;
import com.yks.simpledemo3.view.MyActionBar;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * 描述：水平仪
 * 作者：zzh
 * time:2020/03/13
 */
public class BalanceLevelActivity extends Activity implements SensorEventListener {

    private Activity mActivity = BalanceLevelActivity.this;
    private Context mContext = BalanceLevelActivity.this;

    private LevelView view_level;

    protected final Handler mHandler = new Handler();
    private SensorManager mSensorManager;
    private float zAngle,yAngle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_level);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "水平仪", "", false);

        view_level = findViewById(R.id.view_level);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);  //为传感器注册监听器
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        mHandler.postDelayed(runnable,100);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);//取消传感器的监听
        super.onPause();
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);//取消传感器的监听
        super.onStop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        //获取与y轴的夹角
        yAngle = values[1];
        //与z轴的夹角
        zAngle = values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            view_level.updateLevelViewBubble(zAngle,yAngle);
            mHandler.postDelayed(runnable,100);
        }
    };

    //todo 关于给桌面图标增加通知数量的功能
    //https://github.com/leolin310148/ShortcutBadger
    //添加
    public void addMenuIconCount(View view){
        ShortcutBadger.applyCount(mContext,56);
        Info.playRingtone(mContext,true);
    }

    //移除
    public void removeMenuIconCount(View view){
        ShortcutBadger.removeCount(mContext);
        Info.playRingtone(mContext,true);
    }
}
