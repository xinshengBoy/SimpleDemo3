package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;
import com.yks.simpledemo3.view.SlideButton;
import com.yks.simpledemo3.view.SlideRightViewDragHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    public void zipFile(View view){
        long startTime = System.currentTimeMillis();
        String file_name = "screen1.png";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/yujisofile/"+file_name;
        File zipFile = new File(path);
        int SUFFIX_FILE = 8888;
        long FILE_SIZE = zipFile.length();
        try {
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(zipFile));
            WritableByteChannel channel = Channels.newChannel(zipout);
            for (int i=0;i<10;i++){
                FileChannel fileChannel = new FileInputStream(path).getChannel();
                zipout.putNextEntry(new ZipEntry(String.valueOf(i+SUFFIX_FILE)));
                fileChannel.transferTo(0,FILE_SIZE,channel);
            }
            long endTime = System.currentTimeMillis();
            Log.d("zipTime","用时："+(endTime-startTime));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
