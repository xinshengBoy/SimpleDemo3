package com.yks.simpledemo3.bean;

import java.io.Serializable;

/**
 * 描述：通知bean
 * 作者：zzh
 * time:2020/03/30
 */
public class NotificationBean implements Serializable {

    private String title;//通知标题
    private String content;//通知内容
    private String showTime;//通知时间
    private String packageName;//应用名称

    public NotificationBean(String title, String content, String showTime, String packageName) {
        this.title = title;
        this.content = content;
        this.showTime = showTime;
        this.packageName = packageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
