package com.thb.qr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.thb.app.R;

import java.util.LinkedList;

/**
 * Created by sea79 on 2017/11/30.
 */

public class QRReusltListAdapter extends BaseAdapter {

    private LinkedList<QRResultBean> mData;
    private Context mContext;

    public QRReusltListAdapter(LinkedList<QRResultBean> mData,Context mContext){
        this.mContext=mContext;
        this.mData=mData;
    }

    @Override
    public int getCount(){
        return mData.size();
    }

    @Override
    public Object getItem(int position){
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.result_list_item,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.number=convertView.findViewById(R.id.result_number);
            viewHolder.item1=convertView.findViewById(R.id.result_item1);
            viewHolder.item2=convertView.findViewById(R.id.result_item2);
            viewHolder.item3=convertView.findViewById(R.id.result_item3);
            viewHolder.item4=convertView.findViewById(R.id.result_item4);
            viewHolder.item5=convertView.findViewById(R.id.result_item5);

            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.number.setText(mData.get(position).getNumber());
        viewHolder.item1.setText(mData.get(position).getItem1());
        viewHolder.item2.setText(mData.get(position).getItem2());
        viewHolder.item3.setText(mData.get(position).getItem3());
        viewHolder.item4.setText(mData.get(position).getItem4());
        viewHolder.item5.setText(mData.get(position).getItem5());

        return convertView;
    }

    private class ViewHolder{
        EditText number;
        EditText item1;
        EditText item2;
        EditText item3;
        EditText item4;
        EditText item5;
    }

    public void add(QRResultBean data){
        if(mData==null){
            mData=new LinkedList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }
}
