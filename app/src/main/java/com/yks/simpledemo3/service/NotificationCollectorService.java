package com.yks.simpledemo3.service;

import android.app.Notification;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.yks.simpledemo3.activity.MonitorNotificationActivity;
import com.yks.simpledemo3.bean.NotificationBean;
import com.yks.simpledemo3.tools.ACache;
import com.yks.simpledemo3.tools.FileUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述：读取手机通知服务
 * 作者：zzh
 * time:2020/03/30
 */
public class NotificationCollectorService extends NotificationListenerService {

    private ACache acache;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        //TODO 缓存用户名
        acache = ACache.get(this);
        Notification notification = sbn.getNotification();
        if (notification == null){
            return;
        }
        Bundle extras = notification.extras;
        if (extras != null){
            //todo 如果列表数据超过10条，则先清理掉
            if (MonitorNotificationActivity.mList.size() >= 10){
                MonitorNotificationActivity.mList.clear();
                FileUtils.deleteFile(Environment.getExternalStorageDirectory()+"/yks/"+MonitorNotificationActivity.fileName);
            }
            String title = extras.getString(Notification.EXTRA_TITLE,"");//todo 标题
            String content = extras.getString(Notification.EXTRA_TEXT,"");//todo 内容
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String showTime = format.format(new Date(sbn.getPostTime()));//todo 时间
            String packageName = sbn.getPackageName();//todo 应用名称

            NotificationBean bean = new NotificationBean(title,content,showTime,packageName);
//            acache.put("myNotification",bean);
            MonitorNotificationActivity.mList.add(bean);
            //todo 先移除
            FileUtils.deleteFile(Environment.getExternalStorageDirectory()+"/yks/"+MonitorNotificationActivity.fileName);
            //todo 再写入
            FileUtils.saveStorage2SDCard(MonitorNotificationActivity.mList,MonitorNotificationActivity.fileName);
            //todo 发送handler通过activity更新
            MonitorNotificationActivity.handler.sendEmptyMessageDelayed(MonitorNotificationActivity.GETNOTIFICATIONSUCCESS,300);
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (acache != null){
            acache = null;
        }
    }
}
