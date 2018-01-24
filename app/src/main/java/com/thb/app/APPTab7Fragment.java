package com.thb.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.thb.ui.ComListViewAdapter;
import com.thb.ui.ComViewHolder;
import com.thb.ws.MOMBean;
import com.thb.ws.ROCBean;
import com.thb.ws.SQLiteHelper;

import java.util.ArrayList;

public class APPTab7Fragment extends APPComFragment {

    private static final String DEBUG_TAB7="tab7***********";
    private ListView lv;
    private SwipeRefreshLayout srl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_app_tab7,container,false);

        srl=view.findViewById(R.id.tab7_srl);
        lv=view.findViewById(R.id.tab7_lv);

        //listView加载header,item中有数据时才显示
        View listHeader=LayoutInflater.from(getContext()).inflate(R.layout.tab7_list_header,null,true);
        lv.addHeaderView(listHeader);

        //SwipeRefreshLayout上拉刷新
        srl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                doShowRecord();
                //延时5刷新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        srl.setRefreshing(false);
                    }
                },100);

            }
        });
        doShowRecord();
        return view;
    }

    public void doShowRecord(){

        ArrayList<ROCBean> beanList=new ArrayList<>();
        //sqlite查询操作：
        //1 获取sqlite实例打开数据库，继承SqliteOpenHelper Override oncreate
        //SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
        //2 获取sqlitedatabase实例，getReadableDatabase
        //SQLiteDatabase db=sqLiteHelper.getReadableDatabase();
        //3 rawquery获取每行查询结果，遍历游标获取每列信息cursor.getString(cursor.getColumnIndex("t_scdd"))
        //Cursor cursor=db.rawQuery("select * from t order by t_sj desc",null);
        SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
        Cursor cursor=sqLiteHelper.doQuerySQL("select * from t_roc order by roc_sj desc");
        while (cursor.moveToNext()){

            ROCBean bean=new ROCBean();
            String str=cursor.getString(cursor.getColumnIndex("roc_sj"));
            bean.setRoc_scdd(cursor.getString(cursor.getColumnIndex("roc_scdd")));
            //bean.setT$MITM(cursor.getString(cursor.getColumnIndex("t_sl")));
            bean.setRoc_wl(cursor.getString(cursor.getColumnIndex("roc_wl")));
            bean.setRoc_sl(cursor.getString(cursor.getColumnIndex("roc_sl")));
            bean.setRoc_sj( str.substring(2,str.length()));
            beanList.add(bean);
            Log.e(DEBUG_TAB7,cursor.getString(cursor.getColumnIndex("roc_sj")));
        }
        cursor.close();
        sqLiteHelper.doClose();
        //4 关闭 游标
        //cursor.close();
        //5 关闭sqlite
        //db.close();

        //MOMBeanListAdapter mAdapter=new MOMBeanListAdapter(getContext(),beanList);
        //lv.setAdapter(mAdapter);

        ComListViewAdapter comListViewAdapter=new ComListViewAdapter<ROCBean>(getContext(),beanList,R.layout.tab7_list_item) {
            @Override
            public void convert(ComViewHolder holder, ROCBean bean) {
                holder.setTextView(R.id.tab7_item_scdd,bean.getRoc_scdd());
                holder.setTextView(R.id.tab7_item_wl,bean.getRoc_wl());
                holder.setTextView(R.id.tab7_item_sl,bean.getRoc_sl());
                holder.setTextView(R.id.tab7_item_sj,bean.getRoc_sj());
            }
        };
        lv.setAdapter(comListViewAdapter);
    }

}
