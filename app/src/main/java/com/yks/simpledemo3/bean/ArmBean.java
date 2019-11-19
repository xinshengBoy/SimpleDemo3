package com.yks.simpledemo3.bean;

/**
 * 描述：录音文件bean
 * 作者：zzh
 * time:2019/11/15
 */
public class ArmBean {
    private String id;
    private String name;
    private String size;
    private String date;
    private String path;

    public ArmBean(String id, String name, String size, String date, String path) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.date = date;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
