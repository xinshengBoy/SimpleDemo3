package com.yks.simpledemo3.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * 描述：考勤打卡记录的字段
 * 作者：zzh
 * time:2020/08/06
 */
@Table(database = AppDataBase.class)
public class ClockOutModel extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String clockTime;

    @Column
    private String clockRemark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }

    public String getClockRemark() {
        return clockRemark;
    }

    public void setClockRemark(String clockRemark) {
        this.clockRemark = clockRemark;
    }
}
