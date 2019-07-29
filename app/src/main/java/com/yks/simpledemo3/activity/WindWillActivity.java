package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.WindPath;

/**
 * 描述：自定义风车
 * 作者：zzh
 * time:2019/07/25
 * 参考文章：https://github.com/yinyiliang/Windmill/blob/master/app/src/main/res/layout/activity_main.xml
 */
public class WindWillActivity extends Activity {

    private Activity mActivity = WindWillActivity.this;
    private WindPath view_wind1,view_wind2;
    private TextView txt_windwill;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windwill);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"自定义风车","",false);

        view_wind1 = findViewById(R.id.view_wind1);
        view_wind2 = findViewById(R.id.view_wind2);
        txt_windwill = findViewById(R.id.txt_windwill);
        SeekBar sb_windwill = findViewById(R.id.sb_windwill);
        sb_windwill.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txt_windwill.setText("风速：" + seekBar.getProgress() + "/" + seekBar.getMax());

                view_wind1.setWindVelocity(seekBar.getProgress());
                view_wind2.setWindVelocity(seekBar.getProgress());

                view_wind1.startAnim();
                view_wind2.startAnim();
            }
        });

        //设置默认
        view_wind1.setWindVelocity(2);
        view_wind2.setWindVelocity(2);

        view_wind1.startAnim();
        view_wind2.startAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view_wind1.stopAnim();
        view_wind2.stopAnim();
    }
}
