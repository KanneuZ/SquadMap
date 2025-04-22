package com.example.squadmaps;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    private LinkedHashMap<Integer, GroupInfo> subjects = new LinkedHashMap<>();
    private ArrayList<GroupInfo> deptList = new ArrayList<>();
    private GroupListAdapter listAdapter;

    private ExpandableListView simpleExpandableListView;
    private Button btnCreateGroup;
    private Button btnJoinGroup;
    private EditText edtGrName;
    private EditText edtGrPassword;
    private PopupWindow popup;

    private MyApplication myApplication;

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

    Observer<ArrayList<GroupInfo>> obs = new Observer<>() {
		@Override
		public void onChanged(ArrayList<GroupInfo> groupInfos) {
			ArrayList<UserInfo> childs;
            int i, j;

			for (i = 0; i < groupInfos.size(); i++) {
                GroupInfo groupInfo = groupInfos.get(i);
				childs = groupInfo.getProductList();

				for (j = 0; j < childs.size(); j++) createGr(groupInfo, childs.get(j));

                listAdapter.notifyDataSetChanged();
            }
        }
	};

    public GroupFragment() {}

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
        registerForContextMenu(simpleExpandableListView);

        SData.groupArray.observe(getViewLifecycleOwner(), obs);

        btnJoinGroup = view.findViewById(R.id.grJoin);
        btnJoinGroup.setOnClickListener(v -> {
            popup = createWindowPopup("Join group", btnJoinGroupListener, view.getLayoutParams().width, view.getHeight());
            view.post(() -> popup.showAtLocation(view, Gravity.CENTER, 0, 20));
        });

        btnCreateGroup = view.findViewById(R.id.grCreate);
        btnCreateGroup.setOnClickListener(v -> {
            popup = createWindowPopup("Add new group", btnCreateGroupListener, view.getLayoutParams().width, view.getHeight());
            view.post(() -> {
                    popup.showAtLocation(view, Gravity.CENTER, 0, 20);
            });
        });

        return view;
    }

    private PopupWindow createWindowPopup(String title, View.OnClickListener btnAcceptListener, int width, int height) {
        LayoutInflater inflater = getLayoutInflater();
        View popupView = inflater.inflate(R.layout.grouppopup, null);

        PopupWindow popup = new PopupWindow(popupView, width, height, true);

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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            getActivity().getMenuInflater().inflate(R.menu.group_member_menu, menu);
        } else {
            getActivity().getMenuInflater().inflate(R.menu.group_menu, menu);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public boolean onContextItemSelected(MenuItem item) {
        UserInfo member;
        GroupInfo group;
        int type, groupPos, childPos, itmId, id;

        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
		if (info == null) return false;

		type = ExpandableListView.getPackedPositionType(info.packedPosition);
        groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            group = (GroupInfo) listAdapter.getGroup(groupPos);
            member = (UserInfo) listAdapter.getChild(groupPos, childPos);

            itmId = item.getItemId();
            if (itmId == R.id.memberMakeSL) {
                if (!Objects.equals(SData.id, group.getLeadId())) {
                    Toast.makeText(myApplication, "Недостаточно прав!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                myApplication.getCli().PTKChangeLead(group.getId(), member.getId());
            } else if (itmId == R.id.memberKick) {
                if (!Objects.equals(SData.id, group.getLeadId())) {
                    Toast.makeText(myApplication, "Недостаточно прав!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                myApplication.getCli().PKTGroupKick(group.getId(), member.getId());
                listAdapter.removeChild(groupPos, childPos);
            }

            return true;
        }

        itmId = item.getItemId();
        if (itmId == R.id.groupExt) {
            id = ((GroupInfo) listAdapter.getGroup(groupPos)).getId();
            myApplication.getCli().PKTExitGroup(id);
            listAdapter.removeGr(groupPos);
            subjects.remove(id);
        } else if (itmId == R.id.groupMakePrim) {
            myApplication.getCli().PKTChangePrimGroup(((GroupInfo) listAdapter.getGroup(groupPos)).getId());
        }

        return true;
    }

    private void createGr(GroupInfo group, UserInfo user) {
        int i, groupPos;

        GroupInfo headerInfo = subjects.get(group.getId());

        if (headerInfo == null) {
            headerInfo = new GroupInfo(group.getId(), group.getName());
            headerInfo.setPrim(group.getPrim());
            subjects.put(group.getId(), headerInfo);
            deptList.add(headerInfo);
        }

        if (headerInfo.getPrim() != group.getPrim()) {
            Log.d("TAG", "createGr: ");

            subjects.remove(group.getId());
            listAdapter.removeGr(deptList.indexOf(headerInfo));

            headerInfo = new GroupInfo(group.getId(), group.getName());
            headerInfo.setPrim(group.getPrim());
            subjects.put(group.getId(), headerInfo);
            deptList.add(headerInfo);
        }

        ArrayList<UserInfo> productList = headerInfo.getProductList();
        if (user.isToDell()) {
            groupPos = deptList.indexOf(headerInfo);

            if (user.getId() == SData.id) {
                listAdapter.removeGr(groupPos);
                subjects.remove(group.getId());
                return;
            }

            for (i=0; i < productList.size(); i++) if (user.getId() == productList.get(i).getId()) break;
            listAdapter.removeChild(groupPos, i);
            productList.remove(user);
        } else {
            for (i = 0; i < productList.size(); i++) {
                if (productList.get(i).getId() == user.getId()) {
                    if (productList.get(i).isLead() != user.isLead()) {
                        productList.remove(productList.get(i));
                        break;
                    }

                    return;
                }
            }
            productList.add(user);
        }

        if (user.isLead()) headerInfo.setLeadId(user.getId());
        headerInfo.setProductList(productList);
    }

    private boolean isValid(String name, String password) {
        if (name.isEmpty()) return false;

        /* TODO сделать проверку на корректность имени группы */

        return true;
    }

}