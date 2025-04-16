package com.example.squadmaps;

import com.yandex.mapkit.map.MapObjectCollection;

import java.util.ArrayList;

public class GroupInfo {

    private Integer grId;
    private String name;
    MapObjectCollection col;
    private ArrayList<UserInfo> list = new ArrayList<>();
    private ArrayList<MarkersInfo> markers = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UserInfo> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<UserInfo> productList) {
        this.list = productList;
    }

    public Integer getGrId() {
        return grId;
    }

    public void setGrId(Integer grId) {
        this.grId = grId;
    }
}
