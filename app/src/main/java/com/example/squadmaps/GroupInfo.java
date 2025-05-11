package com.example.squadmaps;

import com.yandex.mapkit.map.MapObjectCollection;

import java.util.ArrayList;

/*класс для хранения информации о группе.*/
public class GroupInfo {
    private Integer id; //ID группы
    private Integer leadId; //ID лидера группы
    private String name; //название группы
    private Boolean isPrim = false; //является ли группа праймом

    private ArrayList<UserInfo> list = new ArrayList<>(); //список участников
    private ArrayList<MarkersInfo> markers = new ArrayList<>(); //список меток

	/*конструктор.*/
    public GroupInfo() {}
	
	/*конструктор.*/
    public GroupInfo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

	/*получение названия группы.
 	входные значения: - .
  	возвращаемые значения: название группы.*/
    public String getName() {
        return name;
    }

	/*установка названия группы.
 	входные значения: название группы.
  	возвращаемые значения: - .*/
    public void setName(String name) {
        this.name = name;
    }

	/*получение списка участников.
 	входные значения: - .
  	возвращаемые значения: список участников.*/
    public ArrayList<UserInfo> getProductList() {
        return list;
    }

	/*уставнока списка участников.
 	входные значения: список участников.
  	возвращаемые значения: - .*/
    public void setProductList(ArrayList<UserInfo> productList) {
        this.list = productList;
    }

	/*получение айди группы.
 	входные значения: - .
  	возвращаемые значения: айди группы.*/
    public Integer getId() {
        return id;
    }

	/*установка айди группы.
 	входные значения: айди группы .
  	возвращаемые значения: - .*/
    public void setId(Integer id) {
        this.id = id;
    }

	/*получение айди лидера группы.
 	входные значения: - .
  	возвращаемые значения: айди лидера группы.*/
	public Integer getLeadId() {
		return leadId;
	}

	/*установка айди лидера.
 	входные значения: айди лидера группы.
  	возвращаемые значения: - .*/
	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}

	/*проверка является ли группа основной.
 	входные значения: - .
  	возвращаемые значения: true/false.*/
	public Boolean getPrim() {
		return isPrim;
	}

	/*установка прайм группы.
 	входные значения: true чтобы сделать группу основной.
  	возвращаемые значения: - .
	public void setPrim(Boolean prim) {
		isPrim = prim;
	}
}
