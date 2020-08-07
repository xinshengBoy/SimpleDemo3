package com.yks.simpledemo3.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 描述：DBFLOW数据库的配置
 * 作者：zzh
 * time:2020/08/06
 */
@Database(name = AppDataBase.NAME,version = AppDataBase.VERSION)
public class AppDataBase {

    public static final String NAME = "AppDataBase";
    public static final int VERSION = 3;
}
