package com.yks.simpledemo3.bean;

/**
 * 描述：蓝牙已接收文件的bean
 * 作者：zzh
 * time:2019/10/25
 */
public class BluetoothReceivedFileBean {

    private String fileName;//文件名称
    private String fileSize;//文件大小
    private String fileTime;//文件接收时间
    private String fileAddress;//文件路径

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
}
