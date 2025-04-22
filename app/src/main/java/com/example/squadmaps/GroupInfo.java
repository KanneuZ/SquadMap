package com.example.squadmaps;

import com.yandex.mapkit.map.MapObjectCollection;

import java.util.ArrayList;

public class GroupInfo {
    private Integer id;
    private Integer leadId;
    private String name;
    private Boolean isPrim = false;

    private ArrayList<UserInfo> list = new ArrayList<>();
    private ArrayList<MarkersInfo> markers = new ArrayList<>();

    public GroupInfo() {}
    public GroupInfo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Integer getLeadId() {
		return leadId;
	}

	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}

	public Boolean getPrim() {
		return isPrim;
	}

	public void setPrim(Boolean prim) {
		isPrim = prim;
	}
}
