package com.thb.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by sea79 on 2017/11/16.
 */

public class TableHorizontalScrollView extends HorizontalScrollView {

    private View mView;

    public TableHorizontalScrollView(Context context){
        super(context);
    }

    public TableHorizontalScrollView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public TableHorizontalScrollView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    protected void onScrollChanged(int l,int t,int oldl,int oldt){
        super.onScrollChanged(l, t, oldl, oldt);
        if(mView!=null){
            mView.scrollTo(l,t);
        }
    }

    public void setScrollView(View view){
        mView=view;
    }


}
