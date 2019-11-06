package com.yks.simpledemo3.bean;

/**
 * 描述：省份选择
 * 作者：zzh
 * time:2019/11/05
 */
public class ProvinceBean {
    private int key;
    private String name;
    private String desc;
    private String other;

    public ProvinceBean(int key, String name, String desc, String other) {
        this.key = key;
        this.name = name;
        this.desc = desc;
        this.other = other;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getOther() {
        return other;
    }
}
