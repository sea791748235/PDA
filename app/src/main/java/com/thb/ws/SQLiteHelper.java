package com.thb.ws;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sea79 on 2017/12/21.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    //插入报工记录 android studio 3.0 bug sqlite 无法使用占位符 db.execSQL 效果相同
    //db.execSQL("insert into t values (?,?,?,?)",
    //        new String[]{paramsMap.get("ProductionOrder"),paramsMap.get("QuantityToDeliver"), dataStr2,activationMap.get("username")});

                            /*
                            SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                            SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("t_scdd", et_scdd.getText().toString().toUpperCase().trim());
                            values.put("t_sl", et_bgsl.getText().toString().trim());
                            values.put("t_sj", dateStr1);
                            values.put("t_czy", getActivaMap().get("username").toString());

                            db.insert("t", null, values);
                            //时间倒序保留10条记录
                            db.execSQL("delete from t where (select count(t_sj) from t)>10 and t_sj in " +
                                    "(select t_sj from t order by t_sj desc limit(select count(t_sj) from t) offset 10)");
                            //db.execSQL("drop Table t");
                            db.close();
                            */

    public SQLiteHelper(Context context){
        super(context,"pda.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table if not exists t_roc(roc_scdd text,roc_wl text,roc_sl text,roc_sj text,roc_czy text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVer,int newVer){

    }

    public void doExecSQL(String sql){
        getWritableDatabase().execSQL(sql);
    }

    public void doExec(String sql,Object[] bindArgs){
        getWritableDatabase().execSQL(sql,bindArgs);
    }

    //only for reprot
    public void doInsertRE(String roc_scdd,String roc_wl,String roc_sl,String roc_sj,String roc_czy){
        ContentValues values = new ContentValues();
        values.put("roc_scdd", roc_scdd);
        values.put("roc_sl", roc_sl);
        values.put("roc_sj", roc_sj);
        values.put("roc_czy", roc_czy);
        values.put("roc_wl",roc_wl);
        getWritableDatabase().insert("t_roc", null, values);
    }

    public Cursor doQuerySQL(String sql){
        Cursor cursor=getReadableDatabase().rawQuery(sql,null);
        return cursor;
    }

    public void doClose(){
        close();
    }

}
