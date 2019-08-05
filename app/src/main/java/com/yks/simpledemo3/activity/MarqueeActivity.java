package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.MarqueeTextView;
import com.yks.simpledemo3.view.MarqueeTextView2;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：跑马灯
 * 作者：zzh
 * time:2019/08/05
 */
public class MarqueeActivity extends Activity {

    private MarqueeTextView txt_view_marquees;
    private MarqueeTextView2 txt_view_marquees2;
    private EditText et_marquee;
    private int mProgress = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(MarqueeActivity.this, title_layout, "跑马灯", "", false);

        txt_view_marquees = findViewById(R.id.txt_view_marquees);
        txt_view_marquees2 = findViewById(R.id.txt_view_marquees2);
        txt_view_marquees2.init(getWindowManager(),txt_view_marquees2.getText().toString(), Color.WHITE,mProgress);
        txt_view_marquees2.startScroll();
        et_marquee = findViewById(R.id.et_marquee);
        TextView txt_sure_marquee = findViewById(R.id.txt_sure_marquee);
        txt_sure_marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et_marquee.getText().toString().trim();
                if (!input.equals("")){
                    txt_view_marquees.setText(input);
                    txt_view_marquees2.init(getWindowManager(),input, Color.WHITE,mProgress);
                }
            }
        });

        SeekBar sb_speed = findViewById(R.id.sb_speed);
        sb_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String input = et_marquee.getText().toString().trim();
                mProgress = progress;
                txt_view_marquees2.init(getWindowManager(), input.equals("") ? "我好帅呀" : input,Color.WHITE,mProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
