package com.example.squadmaps;

/*класс для хранения информации о пользователе.*/
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

	/*конструктор.
	входные значения: айди пользователя, широта, долгота.
  	возвращаемые значения: - .*/
    public UserInfo(int id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
	/*более базовый конструктор.
	входные значения: айди пользователя, имя пользователя.
  	возвращаемые значения: - .*/
	public UserInfo(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/*более расширенный конструктор.
	входные значения: айди пользователя, флаг онлайна статуса, статус, тип точки.
  	возвращаемые значения: - .*/
    public UserInfo(int i, String n, boolean flg, String st, int pt) {
        pointType = pt;
        isOnline = flg;
        status = st;
        name = n;
        id = i;
    }

    public UserInfo() {} // конструктор (да сколько можно)

	/*получение имени пользователя.
	входные значения: - .
  	возвращаемые значения: имя пользователя.*/
    public String getName() {
        return name;
    }

	/*установка имени пользователя.
	входные значения: имя пользователя.
  	возвращаемые значения: - .*/
    public void setName(String name) {
        this.name = name;
    }

	/*получение статуса онлайна пользователя.
	входные значения: - .
  	возвращаемые значения: true если онлайн.*/
	public boolean isOnline() {
		return isOnline;
	}

	/*установка статуса онлайна пользователя.
	входные значения: true если онлайн.
  	возвращаемые значения: - */
	public void setOnline(boolean online) {
		isOnline = online;
	}

	/*получение айди пользователя.
	входные значения: - .
  	возвращаемые значения: айди пользователя.*/
	public int getId() {
		return id;
	}

	/*установка айди пользователя.
	входные значения: айди пользователя.
  	возвращаемые значения: - .*/
	public void setId(int id) {
		this.id = id;
	}
	
	/*получение статуса пользователя.
	входные значения: - .
  	возвращаемые значения: статус пользователя.*/
	public String getStatus() {
		return status;
	}

	/*установка статуса пользователя.
	входные значения: статус пользователя.
  	возвращаемые значения: - .*/
	public void setStatus(String status) {
		this.status = status;
	}
	
	/*получение типа точки.
	входные значения: - .
  	возвращаемые значения: тип точки.*/
	public int getPointType() {
		return pointType;
	}

	/*установка типа точка.
	входные значения: тип точки.
  	возвращаемые значения: - .*/
	public void setPointType(int pointType) {
		this.pointType = pointType;
	}

	/*получение широты.
	входные значения: - .
  	возвращаемые значения: широта.*/
	public float getLatitude() {
		return latitude;
	}

	/*установка широты.
	входные значения: широта.
  	возвращаемые значения: - */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/*получение долготы.
	входные значения: - .
  	возвращаемые значения: долгота.*/
	public float getLongitude() {
		return longitude;
	}

	/*установка долготы
	входные значения: долгота.
  	возвращаемые значения: - .*/
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/*проверка на лидера.
	входные значения: - .
  	возвращаемые значения: true если лидер.*/
	public boolean isLead() {
		return isLead;
	}

	/*установка лидера.
	входные значения: true если лидер.
  	возвращаемые значения: - .*/
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
