package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;

import java.text.DecimalFormat;

/**
 * 描述：自定义数字雨
 * 作者：zzh
 * https://github.com/liujianpc/NumberFlowView
 * time:2019/08/26
 */
public class NumberRainActivity extends Activity {

    private long rxtxTotal = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_rain);

        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(NumberRainActivity.this, title_layout, "数字雨", "", false);
        handler.postDelayed(runnable,5000);
    }

    private String initView(){
        long tempSum = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
        long rxtxLast = tempSum - rxtxTotal;
        double totalSpeed = rxtxLast*1000/2000d;
        rxtxTotal = tempSum;
        DecimalFormat format =new DecimalFormat("0.00");
        String result;
        if (totalSpeed >= 1048576d){
            result = format.format(totalSpeed/1048576d)+"MB/s";
        }else {
            result = format.format(totalSpeed/1024d) + "KB/s";
        }
        return result;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this,5000);
            Toast.makeText(NumberRainActivity.this,initView(),Toast.LENGTH_SHORT).show();
        }
    };
}
