package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.io.IOException;
import java.util.Calendar;

/**
 * 描述：锁屏情况下自动唤醒并闹钟开启
 * 作者：zzh
 * time:2020/03/06
 * https://blog.csdn.net/chmj1208/article/details/51444163
 */
public class AlarmSettingAgain extends Activity {

    private Context mContext = AlarmSettingAgain.this;
    private Activity mActivity = AlarmSettingAgain.this;
    private PowerManager.WakeLock wakeLock;
    private MediaPlayer mp;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mp = new MediaPlayer();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        playAlarmNotice();
        startVibrator();
        showDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        acquireWakeLock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }

    /**
     * 描述：唤醒屏幕
     * 作者：zzh
     */
    private void acquireWakeLock(){
        if (wakeLock == null){
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            assert pm != null;
            wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,mContext.getClass().getCanonicalName());
            wakeLock.acquire(60*1000L /*10 minutes*/);
        }
    }

    /**
     * 描述：释放锁屏
     * 作者：zzh
     */
    private void releaseWakeLock(){
        if (wakeLock != null && wakeLock.isHeld()){
            wakeLock.release();
            wakeLock = null;
        }
    }

    /**
     * 描述：播放闹钟铃声
     * 作者：zzh
     */
    private void playAlarmNotice(){
        try {
            mp.setDataSource(mContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 描述：震动
     * 作者：zzh
     */
    private void startVibrator(){
        long[] pattern = {500,1000,500,1000};
        vibrator.vibrate(pattern,0);//repeat为0时循环
    }

    /**
     * 描述：推迟十分钟提醒
     * 作者：zzh
     */
    private void tenRemind(){
        Calendar calendar_now =Calendar.getInstance();


        calendar_now.setTimeInMillis(System.currentTimeMillis());
        calendar_now.set(Calendar.HOUR_OF_DAY, calendar_now.get(Calendar.HOUR_OF_DAY));
        calendar_now.set(Calendar.MINUTE, calendar_now.get(Calendar.MINUTE)+10);
        calendar_now.set(Calendar.SECOND, 0);
        calendar_now.set(Calendar.MILLISECOND, 0);

        //时间选择好了
        Intent intent = new Intent(mActivity, AlarmClockActivity.class);
        //注册闹钟广播
        PendingIntent sender = PendingIntent.getBroadcast(
                mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am;
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert am != null;
        am.set(AlarmManager.RTC_WAKEUP, calendar_now.getTimeInMillis(), sender);
    }
    /**
     * 描述：弹出对话框
     * 作者：zzh
     */
    private void showDialog(){
        LemonHello.getInformationHello("闹钟","起床时间到了").addAction(new LemonHelloAction("推迟10分钟", new LemonHelloActionDelegate() {
            @Override
            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                tenRemind();
                mp.stop();
                vibrator.cancel();
                lemonHelloView.hide();
                finish();
            }
        })).addAction(new LemonHelloAction("关闭", new LemonHelloActionDelegate() {
            @Override
            public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                mp.stop();
                vibrator.cancel();
                lemonHelloView.hide();
                finish();
            }
        })).show(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.hide();
        releaseWakeLock();
        if (mp != null){
            mp.release();
            mp = null;
        }
        if (vibrator != null){
            vibrator.cancel();
            vibrator = null;
        }
    }
}
