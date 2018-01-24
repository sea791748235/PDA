package com.thb.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by sea79 on 2017/12/28.
 */

public abstract class ComListViewAdapter<T> extends BaseAdapter {

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected List<T> mData;
    protected int mItemLayoutId;

    public ComListViewAdapter(Context mContext,List<T> mData,int mItemLayoutId){
        mLayoutInflater=LayoutInflater.from(mContext);
        this.mContext=mContext;
        this.mData=mData;
        this.mItemLayoutId=mItemLayoutId;
    }

    @Override
    public int getCount(){
        return mData==null?0:mData.size();
    }

    @Override
    public T getItem(int position){
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ComViewHolder holder=getViewHolder(position,convertView,parent);
        convert(holder,getItem(position));
        return holder.getmConvertView();
    }

    private ComViewHolder getViewHolder(int position, View convertView, ViewGroup parent){
        return ComViewHolder.getHolder(mContext,mItemLayoutId,convertView,parent);
    }

    public abstract void convert(ComViewHolder holder,T item);
}
