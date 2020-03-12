package com.yks.simpledemo3.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.receiver.AlarmReceiver;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.BadgeButton;
import com.yks.simpledemo3.view.MyActionBar;

import java.util.Calendar;

/**
 * 描述：闹钟
 * 作者：zzh
 * time:2020/03/05
 * https://blog.csdn.net/chmj1208/article/details/51444163
 * https://www.jb51.net/article/163128.htm
 */
public class AlarmClockActivity extends Activity implements View.OnClickListener {

    private Activity mActivity = AlarmClockActivity.this;
    private Context mContext = AlarmClockActivity.this;
    private TextView txt_alarmclock_time;
    private Button btn_set_alarmclock,btn_cancel_alarmclock;
    private BadgeButton view_badgeButton3;
    private Calendar calendar;
    private boolean isVisible = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        calendar = Calendar.getInstance();
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"闹钟","",false);

        txt_alarmclock_time = findViewById(R.id.txt_alarmclock_time);
        btn_set_alarmclock = findViewById(R.id.btn_set_alarmclock);
        btn_cancel_alarmclock = findViewById(R.id.btn_cancel_alarmclock);
        view_badgeButton3 = findViewById(R.id.view_badgeButton3);

        btn_set_alarmclock.setOnClickListener(this);
        btn_cancel_alarmclock.setOnClickListener(this);
        view_badgeButton3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_set_alarmclock){
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                    calendar.set(Calendar.MINUTE,minute);
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                    //简历intent和pendingintent来调用目标组件
                    Intent intent = new Intent(mActivity,AlarmReceiver.class);
                    PendingIntent pi = PendingIntent.getBroadcast(mContext,0,intent,0);
                    //设置闹钟
                    AlarmManager am = (AlarmManager) mContext.getSystemService(ALARM_SERVICE);
                    assert am != null;
                    am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
                    Info.showToast(mContext,"设置成功",true);
                    String times = format(hourOfDay)+":"+format(minute);
                    txt_alarmclock_time.setText("闹钟时间："+times);

                    SharedPreferences share = getPreferences(0);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("TIME1",times);
                    editor.apply();
                }
            },hour,minute,true).show();
        }else if (v == btn_cancel_alarmclock){
            //停止闹钟
            Intent intent = new Intent(mActivity, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(mContext,0,intent,0);
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            assert am != null;
            am.cancel(pi);
            Info.showToast(mContext,"取消成功",true);
        }else if (v == view_badgeButton3){
            if (isVisible){
                view_badgeButton3.setBadgeVisible(false);
                isVisible = false;
            }else {
                view_badgeButton3.setBadgeVisible(true);
                isVisible = true;
            }
        }
    }

    // 格式化字符串7:3-->07:03
    private String format(int x) {
        String s = "" + x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
