package com.felix.pickerview.bean;

import java.util.List;

public class JsonBean {
    private String provinceCode;
    private String provinceName;
    private List<CityBean> city;

    public String getProvinceCode() {
        return this.provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public List<CityBean> getCity() {
        return this.city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

}
