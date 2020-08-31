package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.monitor.CameraManager;
import com.yks.simpledemo3.monitor.CameraPreview;
import com.yks.simpledemo3.monitor.SocketClient;
import com.yks.simpledemo3.view.MyActionBar;

/**
 * 描述：手机监控
 * 作者：zzh
 * time:2020/08/10
 * https://github.com/yushulx/Android-IP-Camera
 */
public class PhoneMonitorActivity extends Activity {

    private Context mContext = PhoneMonitorActivity.this;
    private Activity mActivity = PhoneMonitorActivity.this;

    private CameraPreview mPreview;
    private CameraManager manager;
    private SocketClient mThread;

    private Button btn_start_camera;
    private boolean mIsOn = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_monitor);

        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity, title_layout, "手机监控", "", false);

        manager = new CameraManager(mContext);
        mPreview = new CameraPreview(mContext,manager.getCamera());

        FrameLayout camera_preview = findViewById(R.id.camera_preview);
        camera_preview.addView(mPreview);

        btn_start_camera = findViewById(R.id.btn_start_camera);
        btn_start_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOn){
                    mThread = new SocketClient(mPreview);
                    mIsOn = false;
                    btn_start_camera.setText("停止");
                }else {
                    closeSocketClient();
                    reset();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager.onResume();
        mPreview.setCamera(manager.getCamera());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
        manager.onPause();
        reset();
        closeSocketClient();
    }

    private void reset(){
        btn_start_camera.setText("开始");
        mIsOn = true;
    }

    private void closeSocketClient(){
        if (mThread == null){
            return;
        }
        mThread.interrupt();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mThread = null;
    }
}
