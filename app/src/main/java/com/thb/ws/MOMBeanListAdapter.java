package com.thb.ws;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.thb.app.R;

import java.util.ArrayList;

/**
 * Created by sea79 on 2017/12/19.
 */

public class MOMBeanListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<MOMBean> MOMBeanList;
    private Context context;


    public MOMBeanListAdapter(Context context, ArrayList<MOMBean> MOMBeanList) {
        this.MOMBeanList = MOMBeanList;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return MOMBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return MOMBeanList.get(position);
    }

    @Override
    public long getItemId(int positon) {
        return positon;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup parent) {


        MOMBeanListAdapter.ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tab7_list_item, parent, false);
            holder=new MOMBeanListAdapter.ViewHolder();
            holder.item1=convertView.findViewById(R.id.tab7_item_scdd);
            //holder.item2=convertView.findViewById(R.id.tab7_item_wl);
            holder.item3=convertView.findViewById(R.id.tab7_item_sl);
            holder.item4=convertView.findViewById(R.id.tab7_item_sj);
            //holder.item5=convertView.findViewById(R.id.tab7_item_czy);
            convertView.setTag(holder);
        }else {
            holder=(MOMBeanListAdapter.ViewHolder)convertView.getTag();
        }

        MOMBean bean=MOMBeanList.get(positon);
        holder.item1.setText(bean.getT$PDNO().trim());
       // holder.item2.setText(bean.getT$MITM().trim());
        holder.item3.setText(bean.getT$PONO().trim());
        holder.item4.setText(bean.getT$SITM().trim());
        holder.item5.setText(bean.getT$QUES().trim());

        return convertView;
    }

    private class ViewHolder {
        TextView item1;
        //TextView item2;
        TextView item3;
        TextView item4;
        TextView item5;
    }
}
