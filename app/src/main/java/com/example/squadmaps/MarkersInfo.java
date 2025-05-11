package com.example.squadmaps;

import com.yandex.mapkit.geometry.Point;

/*класс с информацией о метках.*/
public class MarkersInfo {
	private int id;
	private int type;
	private Point point = null;

	public MarkersInfo() {} //констурктор.

	/*констурктор с параметрами.
	входные значения: айди, тип метки, координаты метки..
  	возвращаемые значения: - .*/ 
	public MarkersInfo(int id, int type, Point point) {
		this.id = id;
		this.point = point;

		switch (type) {
			case 0:
				this.type = SData.MOVE_MARKER;
				return;
			case 1:
				this.type = SData.ATTENTION_MARKER;
				return;
			case 2:
				this.type = SData.CAR_MARKER;
				return;
			case 3:
				this.type = SData.DANGER_MARKER;
				return;
			case 4:
				this.type = SData.DEATH_MARKER;
				return;
			case 5:
				this.type = SData.FIRE_MARKER;
				return;
			case 6:
				this.type = SData.FISH_MARKER;
				return;
			case 7:
				this.type = SData.FOOD_MARKER;
				return;
			case 8:
				this.type = SData.HAPPY_MARKER;
				return;
			case 9:
				this.type = SData.HOUSE_MARKER;
				return;
			case 10:
				this.type = SData.PILZ_MARKER;
				return;
			case 11:
				this.type = SData.SECRET_MARKER;
				return;
			case 12:
				this.type = SData.WET_MARKER;
				return;
			case 13:
				this.type = SData.WORK_MARKER;
				return;

			default: this.type = type;
		}
	}
	/*получение айди метки.
	входные значения: - .
  	возвращаемые значения: айди метки.*/ 
	public int getId() {
		return id;
	}

	/*установка айди метки.
	входные значения: айди метки .
  	возвращаемые значения: - .*/
	public void setId(int id) {
		this.id = id;
	}

	/*получение типа метки.
	входные значения: - .
  	возвращаемые значения: тип метки.*/
	public int getType() {
		return type;
	}
	
	/*установка типа метки.
	входные значения: тип метки .
  	возвращаемые значения: - .*/
	public void setType(int type) {
		this.type = type;
	}

	/*получение координат метки.
	входные значения: - .
  	возвращаемые значения: координата метки.*/
	public Point getPoint() {
		return point;
	}
	
	/*установка координаты метки.
	входные значения: координата метки .
  	возвращаемые значения: - .*/
	public void setPoint(Point point) {
		this.point = point;
	}
}
