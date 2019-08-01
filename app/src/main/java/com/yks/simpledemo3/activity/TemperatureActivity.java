package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.SimpleTemperatureView;
import com.yks.simpledemo3.view.TemperatureView;
import com.yks.simpledemo3.view.TextSeekBarView;

/**
 * 描述：自定义温度计
 * 作者：zzh
 * time:2019/07/31
 * https://blog.csdn.net/mxw3755/article/details/48658425
 */
public class TemperatureActivity extends Activity {

    private TemperatureView view_temperature;
    private SimpleTemperatureView view_temperature2;
    private SensorManager manager;
    private TextView txt_temperature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        initView();
    }

    private void initListener(){
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        manager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_GAME);
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(TemperatureActivity.this, title_layout, "自定义温度计", "", false);

        view_temperature = findViewById(R.id.view_temperature);
        view_temperature2 = findViewById(R.id.view_temperature2);
        view_temperature.setTemperature(41.2f);

        txt_temperature = findViewById(R.id.txt_temperature);
        TextSeekBarView view_textseekbar = findViewById(R.id.view_textseekbar);
        int progress = view_textseekbar.getProgress();
        SeekBar sb_temperature = findViewById(R.id.sb_temperature);
        view_temperature2.start(80);
        sb_temperature.setProgress(80+40);
        txt_temperature.setText("温度："+80);
        sb_temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                view_temperature2.start(progress-40);
                txt_temperature.setText("温度："+(progress-40));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (manager != null){
            manager.unregisterListener(listener);
        }
    }

    private final SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH){
                if (event.sensor.getStringType().toUpperCase().indexOf("TEMP") > 0){
                    float temprature = event.values[0];
                    Log.d("mytemperature","当前温度："+temprature);
                    view_temperature.setTemperature(temprature);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null) {
            manager.unregisterListener(listener);
        }
    }
}
