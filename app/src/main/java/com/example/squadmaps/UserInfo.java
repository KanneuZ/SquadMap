package com.example.squadmaps;

public class UserInfo {

    private String name = "";
    private String status = "";
    private boolean isOnline = false;
	private boolean isLead = false;
	private boolean isToDell = false;
	private float longitude;
	private float latitude;
	private int pointType;
	private int id;

    public UserInfo(int id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

	public UserInfo(int id, String name) {
		this.id = id;
		this.name = name;
	}

    public UserInfo(int i, String n, boolean flg, String st, int pt) {
        pointType = pt;
        isOnline = flg;
        status = st;
        name = n;
        id = i;
    }

    public UserInfo() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean online) {
		isOnline = online;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPointType() {
		return pointType;
	}

	public void setPointType(int pointType) {
		this.pointType = pointType;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public boolean isLead() {
		return isLead;
	}

	public void setLead(boolean lead) {
		isLead = lead;
	}

	public boolean isToDell() {
		return isToDell;
	}

	public void setToDell(boolean toDell) {
		isToDell = toDell;
	}
}
