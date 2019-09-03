package com.felix.pickerview.bean;

import java.util.List;

public class CityBean {
    private String code;
    private String name;
    private List<AreaBean> area;

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

    public List<AreaBean> getArea() {
        return this.area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }
}
