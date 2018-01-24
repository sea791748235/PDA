package com.thb.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.thb.app.R;

import java.util.List;

/**
 * Created by sea79 on 2017/12/6.
 */

public class ElistViewAdapter implements ExpandableListAdapter {

    private Context context;
    private List<ElistGroupBean> groupList;
    private List<List<ElistChildBean>> childLList;

    public ElistViewAdapter(Context context,List<ElistGroupBean> groupList,List<List<ElistChildBean>> childLList){
        this.context=context;
        this.childLList=childLList;
        this.groupList=groupList;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer){

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer){

    }

    @Override
    public int getGroupCount(){
        int ret=0;
        if(groupList!=null){
            ret=groupList.size();
        }
        return ret;
    }

    @Override
    public int getChildrenCount(int groupPosition){
        int ret=0;
        if(childLList!=null){
            ret=childLList.get(groupPosition).size();
        }
        return ret;
    }

    @Override
    public Object getGroup(int groupPosition){
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition,int childPosition){
        return childLList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition){
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition,int childPosition){
        return childPosition;
    }

    @Override
    public boolean hasStableIds(){
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent){
        GroupViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.elv_item_group,null);
            holder=new GroupViewHolder();
            holder.g1=convertView.findViewById(R.id.elv_tv_g1);
            holder.g2=convertView.findViewById(R.id.elv_tv_g2);

            convertView.setTag(holder);
        }else {
            holder=(GroupViewHolder)convertView.getTag();
        }
        ElistGroupBean groupBean=this.groupList.get(groupPosition);
        if(isExpanded){
            holder.g1.setText("g1展开");
        }else {
            holder.g1.setText("g1");
        }
        holder.g1.setText(groupBean.getG1());
        holder.g2.setText(groupBean.getG2());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,int childPosition,boolean isLastChild,View convertView,ViewGroup parent){
        ChildViewHolder holder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.elv_item_child,null);
            holder=new ChildViewHolder();
            holder.c1=convertView.findViewById(R.id.elv_tv_c1);
            holder.c2=convertView.findViewById(R.id.elv_tv_c2);
            holder.c3=convertView.findViewById(R.id.elv_tv_c3);
            convertView.setTag(holder);
        }else{
            holder=(ChildViewHolder)convertView.getTag();
        }
        ElistChildBean childBean=this.childLList.get(groupPosition).get(childPosition);
        holder.c1.setText("c1");
        holder.c2.setText("c2");
        holder.c3.setText("c3");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPostion,int childPosition){
        return true;
    }

    @Override
    public boolean areAllItemsEnabled(){
        return false;
    }

    @Override
    public boolean isEmpty(){
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition){

    }

    @Override
    public void onGroupCollapsed(int groupPosition){

    }

    @Override
    public long getCombinedChildId(long groupId,long childId){
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId){
        return 0;
    }

    class GroupViewHolder{
        TextView g1;
        TextView g2;
    }

    class ChildViewHolder{
        TextView c1;
        TextView c2;
        TextView c3;
    }
}
