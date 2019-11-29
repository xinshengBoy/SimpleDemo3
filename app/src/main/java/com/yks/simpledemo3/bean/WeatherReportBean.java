package com.yks.simpledemo3.bean;

/**
 * 描述：天气bean
 * 作者：zzh
 * time:2019/11/27
 */
public class WeatherReportBean {
    private String date;//日期
    private String week;//周
    private String sunrise;//日出
    private String sunset;//日落
    private String temprate;//温度区间
    private String type;//天气
    private String fengxiang;//风向
    private String fengli;//风力
    private String aqi;//空气质量
    private String notice;//提示信息
    private String address;//地区

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getTemprate() {
        return temprate;
    }

    public void setTemprate(String temprate) {
        this.temprate = temprate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
