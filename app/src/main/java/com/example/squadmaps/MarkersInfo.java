package com.example.squadmaps;

import com.yandex.mapkit.geometry.Point;

public class MarkersInfo {
	private int id;
	private int type;
	private Point point = null;

	public MarkersInfo() {}

	public MarkersInfo(int id, int type, Point point) {
		this.id = id;
		this.type = type;
		this.point = point;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
