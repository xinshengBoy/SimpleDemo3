package com.yks.simpledemo3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;

/**
 * 描述：
 * 作者：
 * time:2019/08/01
 */
public class TextSeekBarView extends LinearLayout {

    private TextView txt_seekbar;
    private int mProgress = 0;

    public TextSeekBarView(Context context) {
        this(context,null);
    }

    public TextSeekBarView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextSeekBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_text_seekbar,this);
        txt_seekbar = view.findViewById(R.id.txt_seekbar);
        SeekBar sb_textseekbar = view.findViewById(R.id.sb_textseekbar);

        txt_seekbar.setText(String.valueOf(sb_textseekbar.getProgress()));
        sb_textseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_seekbar.setText(String.valueOf(progress));
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                txt_seekbar.setVisibility(VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                txt_seekbar.setVisibility(GONE);
            }
        });
    }

    public int getProgress(){
        return mProgress;
    }
}
