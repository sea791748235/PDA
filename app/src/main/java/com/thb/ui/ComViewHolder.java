package com.thb.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sea79 on 2017/12/28.
 */

public class ComViewHolder {

    //类似map,效率高，键值只能Integer
    private SparseArray<View> mViews;
    private View mConvertView;

    private ComViewHolder(Context context, int resLayoutId, ViewGroup parent){
        this.mViews=new SparseArray<View>();
        this.mConvertView= LayoutInflater.from(context).inflate(resLayoutId,parent,false);
        this.mConvertView.setTag(this);
    }

    public static ComViewHolder getHolder(Context context, int resLayoutId, View convertView, ViewGroup parent){
        if(convertView==null){
            return new ComViewHolder(context,resLayoutId,parent);
        }
        return (ComViewHolder) convertView.getTag();
    }

    public <T extends View> T getItemView(int viewId){
        View view=mViews.get(viewId);
        if(view==null){
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public View getmConvertView(){
        return mConvertView;
    }

    public ComViewHolder setTextView(int viewId, String text){
        TextView view=getItemView(viewId);
        view.setText(text);
        return this;
    }

    public ComViewHolder setImageResource(int viewId, int drawabledId){
        ImageView view =getItemView(viewId);
        view.setImageResource(drawabledId);
        return this;
    }

    public ComViewHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView view=getItemView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }
}
