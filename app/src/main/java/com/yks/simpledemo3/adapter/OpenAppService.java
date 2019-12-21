package com.yks.simpledemo3.adapter;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 描述：定时自动开启另一个app
 * 作者：zzh
 * time:2019/12/21
 */
public class OpenAppService extends Service {

    private IBinder iBinder = new Binder();
    private Timer mTimer;
    private TimerTask mTask;

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //https://blog.csdn.net/csdn_mm/article/details/78401331
                        PackageManager manager = getPackageManager();
                        Intent intent = manager.getLaunchIntentForPackage("com.yks.newwms");
                        startActivity(intent);
                    }
                }).start();
            }
        };
        mTimer.schedule(mTask,5000,60000);
        mTimer = null;
        mTask = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTask.cancel();
    }
}
