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

/*класс адаптер для группы и их участников.*/
public class GroupListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<GroupInfo> deptList;

    /*констурктор.*/
    public GroupListAdapter(Context context, ArrayList<GroupInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    /*получение обьекта участника группы.
 	входные значения: позиция группы, позииция участника.
  	возвращаемые значения: обьект участника.*/
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<UserInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    /*получение айди участника группы.
 	входные значения: позиция группы, позииция участника.
  	возвращаемые значения: айди участника.*/
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
        if (member.isLead()) img.setImageResource(SData.LEADER_MARKER);
        else img.setImageResource(SData.MEMBER_MARKER);

        return view;
    }

    @Override
    /*получение количества участников в группе.
 	входные значения: позиция группы.
  	возвращаемые значения: количество участников.*/
    public int getChildrenCount(int groupPosition) {
        ArrayList<UserInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.size();
    }

    @Override
    /*получение обьекта группы.
 	входные значения: позиция группы.
  	возвращаемые значения: обьект группы.*/
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    /*получение количества групп.
 	входные значения: - .
  	возвращаемые значения: количество групп.*/
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    /*получение айди группы.
 	входные значения: позиция группы.
  	возвращаемые значения: айди группы.*/
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    /*Путешествуют еврей, индус и чёрный. Близиться к ночи а остаться негде. Видят домик, стучаться, выглядывает хозяин. Просят остаться на ночлег. Владелец говорит есть два места, а третьему придётся спать в амбаре с животными, мол решайте сами кто из вас с ними. Индус говорит:

- Я вырос в бедной индийской деревне и мне ничто не страшно, я буду спать в амбаре.

Индус ушёл. Еврей и чёрный легли спать. Через 15 минут стук в дверь. Открывают а там на пороге индус:

- Ребят там корова а это святое животное я не могу спать с ним под одной крышей.

Еврей говорит:

- Я рос в кибуце, мне никакие животные не страшны. Я пойду спать в амбаре.

Еврей ушёл. Индус и чёрный легли спать. Через 15 минут стук в дверь. Открывают а там на пороге Еврей:

- Ребят там в амбаре свинья, а это не кошерно спать с грязным животным - я так не могу.

Чёрный говорит:

- Ладно, я вырос в гетто Лос Анджелеса и мне ничто не страшно. Я буду спать в амбаре.

Черный уходит а еврей и индус легли спать. Через 15 минут стук в дверь. Открывают а там на пороге свинья и корова.*/
    
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
    /*проверка стабильности айди.
 	входные значения: - .
  	возвращаемые значения: true если стабилен.*/
    public boolean hasStableIds() {
        return true;
    }

    @Override
    /*проверка можно ли выбирать элементы участников.
 	входные значения: позиция группы, позиция участника.
  	возвращаемые значения: true если можно.*/
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /*удаление группы.
 	входные значения: позиция группы.
  	возвращаемые значения: - .*/
    public void removeGr(int grPos) {
        GroupInfo group = (GroupInfo) getGroup(grPos);
        deptList.remove(group);

        notifyDataSetChanged();
    }

    /*удаление участника из группы.
 	входные значения: позиция группы, позиция участника.
  	возвращаемые значения: - .*/
    public void removeChild(int grPos, int childPos) {
        UserInfo user = (UserInfo) getChild(grPos, childPos);
        deptList.get(grPos).getProductList().remove(user);

        notifyDataSetChanged();
    }
}
