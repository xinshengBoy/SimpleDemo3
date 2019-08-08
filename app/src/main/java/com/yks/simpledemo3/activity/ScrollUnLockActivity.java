package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.SlideButton;
import com.yks.simpledemo3.view.SlideRightViewDragHelper;

/**
 * 描述：滑动解锁
 * 作者：zzh
 * time:2019/08/06
 */
public class ScrollUnLockActivity extends Activity {

    private Context mContext = ScrollUnLockActivity.this;
    private Activity mActivity = ScrollUnLockActivity.this;
    private SlideRightViewDragHelper get_order_drag_helper;
    private SeekBar sb_unlock;
    private TextView txt_seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_unlock);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "滑动解锁", "", false);

        get_order_drag_helper = findViewById(R.id.get_order_drag_helper);
        get_order_drag_helper.setOnReleasedListener(new SlideRightViewDragHelper.OnReleasedListener() {
            @Override
            public void onReleased() {
                Info.showToast(mContext,"解锁成功",true);
                Info.playRingtone(mContext,true);

                get_order_drag_helper.computeScroll();
            }
        });

        SlideButton view_slidebutton = findViewById(R.id.view_slidebutton);
        view_slidebutton.setOnScrollListener(new SlideButton.SlideEndListener() {
            @Override
            public void onScrollFinish() {
                Info.showToast(mContext,"解锁成功",true);
                Info.playRingtone(mContext,true);
            }
        });

        txt_seekbar = findViewById(R.id.txt_seekbar);
        sb_unlock = findViewById(R.id.sb_unlock);
        sb_unlock.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int alpha = 255 - 255 / seekBar.getMax() * progress;
                txt_seekbar.setTextColor(Color.argb(alpha,0,255,0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() != seekBar.getMax()){
                    sb_unlock.setProgress(0);
                }else {
                    Info.showToast(mContext,"解锁成功",true);
                    Info.playRingtone(mContext,true);
                    sb_unlock.setProgress(0);
                    txt_seekbar.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }
        });
    }
}
