package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.RadarImageView;
import com.yks.simpledemo3.view.RadarView;

/**
 * 描述：雷达扫描
 * 作者：zzh
 * time:2019/08/02
 * https://github.com/donkingliang/RadarView/blob/master/app/src/main/java/com/donkingliang/radar/RadarView.java  第一种：自定义
 * https://blog.csdn.net/qq_35114086/article/details/70053116 第二种：利用图片匀速旋转
 */
public class RadarViewActivity extends Activity {

    private Activity mActivity = RadarViewActivity.this;
    private RadarView view_radar;
    private RadarImageView view_radar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"雷达扫描","",false);

        view_radar = findViewById(R.id.view_radar);
        view_radar2 = findViewById(R.id.view_radar2);
    }

    public void startScanRadar(View view){
        view_radar.start();
        view_radar2.start();
    }

    public void stopScanRadar(View view){
        view_radar.stop();
        view_radar2.stop();
    }
}
