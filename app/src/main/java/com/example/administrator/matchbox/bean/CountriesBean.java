package com.example.administrator.matchbox.bean;

import com.example.administrator.matchbox.interfaces.IGetString;
import com.example.administrator.matchbox.utils.CharacterParser;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CountriesBean implements IGetString ,Serializable{

    @Override
    public String toString() {
        return "CountriesBean{" +
                "country='" + country + '\'' +
                ", area='" + area + '\'' +
                '}';
    }

    private String country;
    private String area;

    public CountriesBean(String country, String area) {
        this.country = country;
        this.area = area;
    }

    public CountriesBean() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String getString() {
        //返回拼音
        return CharacterParser.getInstance().getSelling(country);
    }
}
