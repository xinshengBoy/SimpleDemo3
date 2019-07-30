package com.yks.simpledemo3.bean;

/**
 * 描述：csv导出bean
 * 作者：zzh
 * time:2019/07/30
 */
public class CsvBean {

    private String name;
    private String age;

    public CsvBean(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
}
