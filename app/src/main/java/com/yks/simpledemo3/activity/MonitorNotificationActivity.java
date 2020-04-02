package com.yks.simpledemo3.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yks.simpledemo3.R;
import com.yks.simpledemo3.bean.NotificationBean;
import com.yks.simpledemo3.service.NotificationCollectorService;
import com.yks.simpledemo3.tools.ACache;
import com.yks.simpledemo3.tools.FileUtils;
import com.yks.simpledemo3.tools.Info;
import com.yks.simpledemo3.view.MyActionBar;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * 描述：消息监控
 * 作者：zzh
 * time:2020/03/30
 */
public class MonitorNotificationActivity extends Activity {

    private Activity mActivity = MonitorNotificationActivity.this;
    private Context mContext = MonitorNotificationActivity.this;

    private final int GETNOTIFICATION = 0;//获取数据
    private final int GETNOTIFICATIONFAIL = 1;//获取数据失败
    public static final int GETNOTIFICATIONSUCCESS = 2;//todo 获取数据成功

    private TextView txt_notification_record,txt_input_record;
    private EditText et_monitor;
    public static String fileName = "myNotification.txt";
    private ACache acache;
    public static MyHandler handler;
    public static ArrayList<NotificationBean> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        handler = new MyHandler(mActivity);
        if (!isNotificationListenerEnabled(mContext)){
            openNotificationListenerSettings();
        }
        toggleNotificationListenerService();
        //TODO 缓存通知
        acache = ACache.get(mContext);
        initView();
    }

    private void initView() {
        LinearLayout title_layout = findViewById(R.id.headerLayout);
        MyActionBar.show(mActivity,title_layout,"消息监控","",false);


        txt_notification_record = findViewById(R.id.txt_notification_record);
        txt_input_record = findViewById(R.id.txt_input_record);
        et_monitor = findViewById(R.id.et_monitor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(GETNOTIFICATION);
    }

    public class MyHandler extends Handler {

        final WeakReference<Activity> mWeakReference;

        MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                if (msg.what == GETNOTIFICATION){//todo 自动获取数据
                    initNotificationData();
                    initInputData();
                }else if (msg.what == GETNOTIFICATIONFAIL) {//todo 获取数据失败
                    Info.showToast(mContext,"获取数据失败或数据为空",false);
                    Info.playRingtone(mContext,false);
                }else if (msg.what == GETNOTIFICATIONSUCCESS){//todo 获取通知列表数据成功
                    String tips = "";
                    for (int i=0;i<mList.size();i++){
                        NotificationBean bean = mList.get(i);
                        tips += "标题：" + bean.getTitle() + "\n" +
                                "内容：" + bean.getContent() + "\n" +
                                "日期：" + bean.getShowTime() + "\n" +
                                "应用：" + bean.getPackageName() + "\n";
                    }
                    txt_notification_record.setText(tips);
                    Info.playRingtone(mContext,true);
                }
            }
        }
    }

    /**
     * 描述：初始化通知数据，权限判断，读取sd卡的存储记录
     * 作者：zzh
     */
    private void initNotificationData(){
        PermissionGen.needPermission(mActivity,100,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
        mList = FileUtils.getNotificationList(fileName);
        if (mList.size() !=0){
            handler.sendEmptyMessage(GETNOTIFICATIONSUCCESS);
        }else {
            mList.add(new NotificationBean("活动呀","快活呀，反正有大把时光","2020-03-31 10:22:25","com.tencent"));
            FileUtils.saveStorage2SDCard(mList,fileName);
            handler.sendEmptyMessage(GETNOTIFICATIONFAIL);
        }
    }

    /**
     * 描述：自动获取输入的记录
     */
    private void initInputData(){
        String info = acache.getAsString("monitor");
        if (info == null || info.equals("null")){
            info = "初始化,";
        }
        //拼接
        String txt = "";
        String[] slips = info.split(",");
        for (int i=0;i<slips.length;i++){
            txt += slips[i] + "," + "\n";
        }
        Info.playRingtone(mContext,true);
        txt_input_record.setText(txt);
    }

    /**
     * 描述：判断是否获取通知权限
     * 作者：zzh
     * @param context 上下文
     * @return 是否有权限
     */
    private boolean isNotificationListenerEnabled(Context context){
        Set<String> packageName = NotificationManagerCompat.getEnabledListenerPackages(context);
        return packageName.contains(context.getPackageName());
    }

    /**
     * 描述：打开系统授权页面
     * 作者：zzh
     */
    private void openNotificationListenerSettings(){
        try {
            Intent intent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            }else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //把应用的NotificationListenerService实现类disable再enable，即可触发系统rebind操作
    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, NotificationCollectorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 保存输入
     * 作者：zzh
     * @param view 保存按钮
     */
    public void saveInputToSdcard(View view){
        //先读取
//        acache.remove("monitor");
        String info = acache.getAsString("monitor");
        if (info == null || info.equals("null")){
            info = "初始化,";
        }
        //判断输入
        String input = et_monitor.getText().toString().trim();
        if (input.equals("")){
            Info.showToast(mContext,"请输入...",false);
            Info.playRingtone(mContext,false);
            return;
        }
        //拼接
        String txt = "";
        String[] slips = info.split(",");
        for (int i=0;i<slips.length;i++){
            txt += slips[i] + "," + "\n";
        }
        txt += input + "," + "\n";
        acache.put("monitor",txt);
        Info.playRingtone(mContext,true);
        txt_input_record.setText(txt);
        et_monitor.setText("");
        Info.hideKeyboard(mContext,et_monitor);
    }

    /**
     * 描述：发送一个广播
     * 作者：zzh
     */
    public void sendNotification(View view){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(mContext,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        String id = "my_channel_01";
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel = new NotificationChannel(id,"my_channel",NotificationManager.IMPORTANCE_HIGH);//todo 设置唯一的渠道通知id
            channel.enableLights(true);//开启灯光
            channel.setLightColor(Color.RED);//设置灯光颜色
            channel.enableVibration(true);//开启震动
            channel.setVibrationPattern(new long[]{0,1000,0,1000});
            manager.createNotificationChannel(channel);

            Notification builder = new Notification.Builder(mContext,id)
                    .setContentTitle("卡罗拉新车上市")
                    .setContentText("拉罗拉新车将在本周末上市，值得期待")
                    .setSubText("新车价格在10到15万之间")
                    .setTicker("卡罗拉")
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setVibrate(new long[]{0,300,500,700})
                    .setLights(getResources().getColor(R.color.colorGreen2),2000,1000)
                    .setSmallIcon(R.mipmap.card)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            if (manager != null) {
                manager.notify(123,builder);
            }
        }else {
            Notification notification = new NotificationCompat.Builder(this,"chat")
                    .setContentTitle("收到一条消息")
                    .setContentText("等下去打球吧")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ai_top)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            manager.notify(1,notification);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LemonBubble.forceHide();
        handler.removeCallbacksAndMessages(null);
        if (mList != null && mList.size() != 0){
            mList.clear();
            mList = null;
        }
    }
}
