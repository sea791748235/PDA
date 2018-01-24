package com.thb.ws;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thb.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sea79 on 2017/11/3.
 */

public class BeanListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<BOMBean> BOMBeanList;
    private Context context;


    public BeanListAdapter(Context context, ArrayList<BOMBean> BOMBeanList) {
        this.BOMBeanList = BOMBeanList;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return BOMBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return BOMBeanList.get(position);
    }

    @Override
    public long getItemId(int positon) {
        return positon;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup parent) {


        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.beanlist_item, parent, false);
            holder=new ViewHolder();
            holder.item1=convertView.findViewById(R.id.item1);
            holder.item2=convertView.findViewById(R.id.item2);
            holder.item3=convertView.findViewById(R.id.item3);
            holder.item4=convertView.findViewById(R.id.item4);
            holder.item5=convertView.findViewById(R.id.item5);
            holder.item6=convertView.findViewById(R.id.item6);
            holder.item7=convertView.findViewById(R.id.item7);
            holder.item8=convertView.findViewById(R.id.item8);
            holder.item9=convertView.findViewById(R.id.item9);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }

        BOMBean bean=BOMBeanList.get(positon);

        holder.item1.setText(bean.getT$SITM().trim());
        holder.item2.setText(bean.getT$SCPF().trim());
        holder.item3.setText(bean.getT$UNOM().trim());
        holder.item4.setText(bean.getT$SCPQ().trim());
        holder.item5.setText(bean.getT$PONO().trim());

        return convertView;
    }

    private class ViewHolder {
        TextView item1;
        TextView item2;
        TextView item3;
        TextView item4;
        TextView item5;
        TextView item6;
        TextView item7;
        TextView item8;
        TextView item9;
    }
}