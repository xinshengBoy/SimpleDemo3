package com.yks.simpledemo3.bean;

/**
 * 描述：聊天bean
 * 作者：zzh
 * time:2019/11/20
 */
public class ChatBean {

    private String sendName;//发送人姓名
    private String chatContent;//聊天内容
    private String sendTime;//发送时间
    private boolean isMySend;//是否为自己发送的

    public ChatBean(String sendName, String chatContent, String sendTime, boolean isMySend) {
        this.sendName = sendName;
        this.chatContent = chatContent;
        this.sendTime = sendTime;
        this.isMySend = isMySend;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public boolean isMySend() {
        return isMySend;
    }

    public void setMySend(boolean mySend) {
        isMySend = mySend;
    }
}
