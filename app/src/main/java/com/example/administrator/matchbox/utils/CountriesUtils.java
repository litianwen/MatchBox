package com.example.administrator.matchbox.utils;

import com.example.administrator.matchbox.bean.CountriesBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CountriesUtils {

    private static List<CountriesBean> list;

    public static List<CountriesBean> getAllCountriesList() {
        if (list == null) {
            list = getCountriesList();
        }
        return list;
    }


    private static List<CountriesBean> getCountriesList() {
        List<CountriesBean> list = new ArrayList<>();
        for (Map.Entry<Character, ArrayList<String[]>> ent : SMSSDK
                .getGroupedCountryList().entrySet()) {
            ArrayList<String[]> cl = ent.getValue();
            for (String[] paire : cl) {
                CountriesBean bean = new CountriesBean(paire[0], paire[1]);
                list.add(bean);
            }
        }
        LogUtils.e(list.toString());
        return list;
    }

}


