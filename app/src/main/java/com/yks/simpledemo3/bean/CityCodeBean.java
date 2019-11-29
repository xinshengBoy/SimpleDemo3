package com.yks.simpledemo3.bean;

/**
 * 描述：天气查询时的接口城市对应的城市代码的bean
 * 作者：zzh
 * time:2019/11/28
 */
public class CityCodeBean {
    private String cityCode;//城市代号
    private String cityName;//城市名称

    public CityCodeBean(String cityCode, String cityName) {
        this.cityCode = cityCode;
        this.cityName = cityName;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
