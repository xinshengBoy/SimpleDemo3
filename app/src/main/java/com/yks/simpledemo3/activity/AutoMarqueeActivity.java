package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.view.AutoMarqueeView;

/**
 * 描述：跑马灯2
 * 作者：zzh
 * time:2019/11/05
 */
public class AutoMarqueeActivity extends Activity {

    private AutoMarqueeView view_auto_marquee;
    private LinearLayout bottom_layout;
    private SeekBar sb_textsize;
    private EditText et_auto_marquee;
    private boolean isShow = false;
    private int text_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_marquee);

        initView();
    }

    private void initView() {
        view_auto_marquee = findViewById(R.id.view_auto_marquee);
        bottom_layout = findViewById(R.id.bottom_layout);
        sb_textsize = findViewById(R.id.sb_textsize);
        et_auto_marquee = findViewById(R.id.et_auto_marquee);
        TextView txt_auto_marquee = findViewById(R.id.txt_auto_marquee);

        view_auto_marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                bottom_layout.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
        //获取默认的字体大小
        text_size = sb_textsize.getProgress();
        sb_textsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_size = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        txt_auto_marquee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et_auto_marquee.getText().toString().trim();
                if (!input.equals("")){
                    view_auto_marquee.setText(input);
                }
                view_auto_marquee.setTextSize(text_size);
                bottom_layout.setVisibility(View.GONE);
                isShow = false;
                et_auto_marquee.setText("");
            }
        });
    }
}
