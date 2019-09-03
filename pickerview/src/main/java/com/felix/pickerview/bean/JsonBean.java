package com.felix.pickerview.bean;

import java.util.List;

public class JsonBean {
    private String code;
    private String name;
    private List<CityBean> city;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return this.city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

}
