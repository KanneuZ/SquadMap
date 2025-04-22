package com.example.squadmaps;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class GroupListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<GroupInfo> deptList;

    public GroupListAdapter(Context context, ArrayList<GroupInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<UserInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        UserInfo member = (UserInfo) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_items, null);
        }

        TextView childItem = view.findViewById(R.id.childItem);
        childItem.setText(member.getName().trim());


        ImageView img = view.findViewById(R.id.mark_icon);
        if (member.isLead()) img.setImageResource(SData.USER_MARKER);
        else img.setImageResource(SData.MEMBER_MARKER);

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<UserInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
        GroupInfo headerInfo = (GroupInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_items, null);
        }

        TextView heading = view.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());

        ImageView img = view.findViewById(R.id.imgBtnHide);
        if (Objects.equals(SData.primGrId, headerInfo.getId())) img.setVisibility(View.VISIBLE);
        else img.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void removeGr(int grPos) {
        GroupInfo group = (GroupInfo) getGroup(grPos);
        deptList.remove(group);

        notifyDataSetChanged();
    }

    public void removeChild(int grPos, int childPos) {
        UserInfo user = (UserInfo) getChild(grPos, childPos);
        deptList.get(grPos).getProductList().remove(user);

        notifyDataSetChanged();
    }
}
