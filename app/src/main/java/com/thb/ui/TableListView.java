package com.thb.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by sea79 on 2017/11/16.
 */

public class TableListView extends ListView{

    public TableListView(Context context){
        super(context);
    }

    public TableListView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public TableListView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);

    }
}
