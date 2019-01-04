package com.felix.pickerview.bean;

import java.util.List;

public class CityBean {
    private String cityCode;
    private String cityName;
    private List<AreaBean> area;

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<AreaBean> getArea() {
        return this.area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }
}
