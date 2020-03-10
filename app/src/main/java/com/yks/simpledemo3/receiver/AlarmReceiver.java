package com.yks.simpledemo3.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yks.simpledemo3.activity.AlarmSettingAgain;

/**
 * 描述：闹钟接收处理器
 * 作者：zzh
 * time:2020/03/05
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, AlarmSettingAgain.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//新创建任务
        context.startActivity(alarmIntent);
    }
}
