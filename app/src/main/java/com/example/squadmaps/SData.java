package com.example.squadmaps;

import androidx.lifecycle.MutableLiveData;

import com.yandex.mapkit.geometry.Point;

import java.util.ArrayList;

public class SData {
	public static Integer id = 0;
	public static Integer primGrId = 0;
	public static Integer primGrLeadId = 0;

	public static String login = "";
	public static String password = "";
	public static String userName = "";

	public static MutableLiveData<Boolean> isReg = new MutableLiveData<>(false);
	public static MutableLiveData<ArrayList<GroupInfo>> groupArray = new MutableLiveData<>();
	public static MutableLiveData<UserInfo> userPoint = new MutableLiveData<>();
	public static MutableLiveData<ArrayList<MarkersInfo>> marker = new MutableLiveData<>();
	public static MutableLiveData<Integer> markerRem = new MutableLiveData<>();

	public static MutableLiveData<String> primGrName = new MutableLiveData<>();
	public static MutableLiveData<Point> userLocation = new MutableLiveData<>();

	public static ArrayList<MarkersInfo> marks = new ArrayList<>();

	public static final int USER_MARKER = R.drawable.marker_group_user;
	public static final int MEMBER_MARKER = R.drawable.marker_group_member;
	public static final int LEADER_MARKER = R.drawable.marker_group_leader;

	public static final int MOVE_MARKER = R.drawable.marker_move;

	public static final int ATTENTION_MARKER = R.drawable.marker_attention;
	public static final int CAR_MARKER = R.drawable.marker_car;
	public static final int DANGER_MARKER = R.drawable.marker_danger;
	public static final int DEATH_MARKER = R.drawable.marker_death;
	public static final int FIRE_MARKER = R.drawable.marker_fire;
	public static final int FISH_MARKER = R.drawable.marker_fish;
	public static final int FOOD_MARKER = R.drawable.marker_food;
	public static final int HAPPY_MARKER = R.drawable.marker_happy;
	public static final int HOUSE_MARKER = R.drawable.marker_house;
	public static final int PILZ_MARKER = R.drawable.marker_pilz;
	public static final int SECRET_MARKER = R.drawable.marker_secret;
	public static final int WET_MARKER = R.drawable.marker_wet;
	public static final int WORK_MARKER = R.drawable.marker_work;
}
