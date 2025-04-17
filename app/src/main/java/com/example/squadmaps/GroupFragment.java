package com.example.squadmaps;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class GroupFragment extends Fragment {

    private LinkedHashMap<Integer, GroupInfo> subjects = new LinkedHashMap<Integer, GroupInfo>();
    private ArrayList<GroupInfo> deptList = new ArrayList<GroupInfo>();
    private GroupListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;
    private Button btnCreateGroup;
    private Button btnJoinGroup;
    private EditText edtGrName;
    private EditText edtGrPassword;
    private MyApplication myApplication;
    private PopupWindow popup;

    private final View.OnClickListener btnCreateGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name, password;
            name = edtGrName.getText().toString().trim();
            password = edtGrPassword.getText().toString().trim();

            if (isValid(name, password)) {
                myApplication.getCli().PKTGroupCreate(name, password);
                popup.dismiss();
                return;
            }

            Toast.makeText(myApplication, "Некорректное название группы", Toast.LENGTH_SHORT).show();
        }
    };

    private final View.OnClickListener btnJoinGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name, password;
            name = edtGrName.getText().toString().trim();
            password = edtGrPassword.getText().toString().trim();

            if (isValid(name, password)) {
                myApplication.getCli().PKTGroupJoin(name, password);
                popup.dismiss();
                return;
            }

            Toast.makeText(myApplication, "Неправильное название группы или пароль", Toast.LENGTH_SHORT).show();
        }
    };

    Observer<ArrayList<GroupInfo>> obs = new Observer<ArrayList<GroupInfo>>() {
        @Override
        public void onChanged(ArrayList<GroupInfo> groupInfos) {
            ArrayList<UserInfo> childs;
            String name, userName;
            Integer id;
            for (int i=0; i < groupInfos.size(); i++) {
                id = groupInfos.get(i).getGrId();
                name = groupInfos.get(i).getName();
                childs = groupInfos.get(i).getProductList();
                for (int j=0; j < childs.size(); j++) {
                    userName = childs.get(j).getName();
                    createGr(id, name, userName);
                }

                expandAll();
                collapseAll();
            }
        }
    };

    public GroupFragment() {}
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        simpleExpandableListView = view.findViewById(R.id.simpleExpandableListView);
        listAdapter = new GroupListAdapter(view.getContext(), deptList);
        simpleExpandableListView.setAdapter(listAdapter);

        SData.groupArray.observe(getViewLifecycleOwner(), obs);

        btnJoinGroup = view.findViewById(R.id.grJoin);
        btnJoinGroup.setOnClickListener(v -> {
            PopupWindow popup = createWindowPopup("Join group", btnJoinGroupListener, view.getLayoutParams().width, view.getHeight());
            view.post(() -> popup.showAtLocation(view, Gravity.CENTER, 0, 20));
        });

        btnCreateGroup = view.findViewById(R.id.grCreate);
        btnCreateGroup.setOnClickListener(v -> {
            PopupWindow popup = createWindowPopup("Add new group", btnCreateGroupListener, view.getLayoutParams().width, view.getHeight());
            view.post(() -> {
                    popup.showAtLocation(view, Gravity.CENTER, 0, 20);
            });
        });

        return view;
    }

    private PopupWindow createWindowPopup(String title, View.OnClickListener btnAcceptListener, int width, int height) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.grouppopup, null);

        popup = new PopupWindow(popupView, width, height, true);

        TextView tw = popupView.findViewById(R.id.popupTitle);
        tw.setText(title);

        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edtGrName = popupView.findViewById(R.id.edtGrName);
        edtGrPassword = popupView.findViewById(R.id.edtGrPassword);

        Button btnCansel = popupView.findViewById(R.id.btnCansel);
        btnCansel.setOnClickListener(v -> popup.dismiss());

        Button btnAccept = popupView.findViewById(R.id.btnAccept);
        btnAccept.setText(title);
        btnAccept.setOnClickListener(btnAcceptListener);

        return popup;
    }

    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    private int createGr(Integer groupId, String GroupName, String UserName) {
        int groupPosition = 0;

        GroupInfo headerInfo = subjects.get(groupId);
        if (headerInfo == null) {
            headerInfo = new GroupInfo();
            headerInfo.setName(GroupName);
            subjects.put(groupId, headerInfo);
            deptList.add(headerInfo);
        }

        ArrayList<UserInfo> productList = headerInfo.getProductList();

        for (int i=0; i < productList.size(); i++) if (Objects.equals(productList.get(i).getName(), UserName)) return 0;

        UserInfo detailInfo = new UserInfo();
        detailInfo.setName(UserName);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private boolean isValid(String name, String password) {
        if (name.isEmpty()) return false;

        return true;
    }

}