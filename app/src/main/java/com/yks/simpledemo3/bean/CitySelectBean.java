package com.yks.simpledemo3.bean;

import java.util.List;

/**
 * 描述：城市选择
 * 作者：zzh
 * time:2020/03/20
 */
public class CitySelectBean {

    private boolean isSelect;
    private String stateName;
    private List<Country> country;

    public static class Country{
        private String countryName;
        private String cityName;

        public Country(String countryName, String cityName) {
            this.countryName = countryName;
            this.cityName = cityName;
        }

        public String getCountryName() {
            return countryName;
        }


        public String getCityName() {
            return cityName;
        }

    }

    public CitySelectBean(boolean isSelect, String stateName, List<Country> country) {
        this.isSelect = isSelect;
        this.stateName = stateName;
        this.country = country;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getStateName() {
        return stateName;
    }

    public List<Country> getCountry() {
        return country;
    }
}
