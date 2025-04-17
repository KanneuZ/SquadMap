package com.example.squadmaps;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class SData {
	public static String login = "";
	public static String password = "";
	public static String userName = "";
	public static MutableLiveData<Boolean> isReg = new MutableLiveData<>(false);
	public static MutableLiveData<ArrayList<GroupInfo>> groupArray = new MutableLiveData<>();
	public static MutableLiveData<UserInfo> userPoint = new MutableLiveData<>();
	public static MutableLiveData<ArrayList<MarkersInfo>> marker = new MutableLiveData<>();
}
