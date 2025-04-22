package com.example.squadmaps;

import androidx.lifecycle.MutableLiveData;

import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PlacemarkMapObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SData {
	public static Integer id = 0;
	public static Integer primGrId = 0;

	public static String login = "";
	public static String password = "";
	public static String userName = "";

	public static MutableLiveData<Boolean> isReg = new MutableLiveData<>(false);
	public static MutableLiveData<ArrayList<GroupInfo>> groupArray = new MutableLiveData<>();
	public static MutableLiveData<UserInfo> userPoint = new MutableLiveData<>();
	public static MutableLiveData<ArrayList<MarkersInfo>> marker = new MutableLiveData<>();

	public static ArrayList<MarkersInfo> marks = new ArrayList<>();

	public static final int USER_MARKER = android.R.drawable.btn_star_big_on;
	public static final int MEMBER_MARKER = android.R.drawable.btn_star_big_off;

	public static final int MOVE_MARKER = android.R.drawable.ic_delete;
//	public static final int HOUSE_MARKER = android.R.drawable.ic_dialog_alert
	public static final int HOUSE_MARKER = R.drawable.marker_house;

	public static final int MARKER = R.drawable.marker_pilz;
}
