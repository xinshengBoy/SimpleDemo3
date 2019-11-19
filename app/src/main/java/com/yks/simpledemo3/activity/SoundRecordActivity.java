package com.yks.simpledemo3.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.haozhang.lib.AnimatedRecordingView;
import com.yks.simpledemo3.R;
import com.yks.simpledemo3.bean.ArmBean;
import com.yks.simpledemo3.view.MyActionBar;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 描述：录音及动画
 * 作者：zzh
 * time:2019/11/15
 * https://github.com/HeZaiJin/AnimatedRecordingView/blob/master/android-animated-recording-view/src/main/java/com/haozhang/lib/AnimatedRecordingView.java  动画
 * https://www.jianshu.com/p/de779d509e6c  录音
 */
public class SoundRecordActivity extends Activity {

    private Context mContext = SoundRecordActivity.this;
    private Activity mActivity = SoundRecordActivity.this;
    private final int GETRECORDINGFILE = 0;//扫描文件管理，获取录音文件信息
    private final int GETRECORDINGFAIL = 1;//获取失败
    private final int GETRECORDINGSUCCESS = 2;//获取成功
    private final int GETRECORDVOLUME = 3;//获取音量

    private AnimatedRecordingView view_animate;
    private MyHandler handler;
    private MediaRecorder recorder;
    private List<ArmBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recording);

        handler = new MyHandler(mActivity);
        initView();
        handler.sendEmptyMessage(GETRECORDINGFILE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "录音及动画", "", false);

        RecyclerView cv_recording_file = findViewById(R.id.cv_recording_file);

        view_animate = findViewById(R.id.view_animate);
        view_animate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.view_animate){
                    switch (event.getAction()){
                        case MotionEvent.ACTION_UP://todo 抬起，停止录音
                            view_animate.stop();
                            stopRecord();
                            break;
                        case MotionEvent.ACTION_DOWN://todo 按下，开始录音
                            view_animate.start();
                            Random random = new Random(100);
                            view_animate.setVolume(random.nextInt());
                            view_animate.loading();
                            startRecord();
                            break;
                    }
                }
                return true;
            }
        });
    }

    /**
     * 描述：获取录音文件
     * 作者：zzh
     */
    private void getRecordingFile(){
        PermissionGen.needPermission(mActivity,100,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO});

        if (mList.size() != 0){
            mList.clear();
        }
        String[] projection = new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.PARENT
        };
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " like ?",
                new String[]{"%.arm"},
                null);
        if (cursor == null){
            handler.sendEmptyMessage(GETRECORDINGFAIL);
            return;
        }
        if (cursor.moveToFirst()){
            int ids = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            int names = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
            int sizes = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            int date = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED);
            int parents = cursor.getColumnIndex(MediaStore.Files.FileColumns.PARENT);

            do {
                ArmBean bean = new ArmBean(cursor.getString(ids),cursor.getString(names),cursor.getString(sizes),cursor.getString(date),cursor.getString(parents));
                mList.add(bean);
            }while (cursor.moveToNext());

            if (mList.size() == 0){
                handler.sendEmptyMessage(GETRECORDINGFAIL);
            }else {
                handler.sendEmptyMessage(GETRECORDINGSUCCESS);
            }
        }
    }

    private class MyHandler extends Handler {
        final WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == GETRECORDINGFILE){//扫描文件
                    getRecordingFile();
                }else if (msg.what == GETRECORDVOLUME){//获取音量
                    view_animate.setVolume(recorder.getMaxAmplitude());
                }
            }
        }
    }

    /**
     * 描述：开始录音
     * 作者：zzh
     */
    private void startRecord(){
        if (recorder == null){
            recorder = new MediaRecorder();

            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);//设置麦克风
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置输出文件格式
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);//设置御品文件编码
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String fileName = "record"+format.format(new Date())+ ".amr";
                String saveDir = Environment.getExternalStorageDirectory()+"/yks/";
                File file = new File(saveDir);

                if (!file.exists()){
                    if (!file.mkdirs()){
                        file.mkdirs();
                    }
                }

                String filePath = saveDir + fileName;
                recorder.setOutputFile(filePath);
                recorder.prepare();
                recorder.start();
                handler.sendEmptyMessage(GETRECORDVOLUME);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 描述：停止录音
     * 作者：zzh
     */
    private void stopRecord(){
        try{
            recorder.stop();
            recorder.release();
            recorder = null;
            getRecordingFile();
        }catch (RuntimeException e){
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        if (recorder != null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
