package com.thb.app;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.thb.qr.QRUtil;
import com.thb.ui.ComEditText;
import com.thb.ui.MyEditText;
import com.thb.ui.UIUtil;
import com.thb.ws.BaanJsonOperator;
import com.thb.ws.ComWSUtil;
import com.thb.ws.MOMBean;
import com.thb.ws.SQLiteHelper;
import com.thb.ws.WSUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class APPTab6Fragment extends APPComFragment {

    private static final String DE_BUG_TAG = "tab6*********";
    private int flag = 0;

    private static final String METHOD = "GetMOMaterialsLine";
    private static final String METHOD_RE = "ReportOrdersCompleted";

    private APPMainActivity mContext;
    private Barcode2DWithSoft mReader;
    private UIUtil uiUtil = new UIUtil();
    private QRUtil qrUtil;

    private EditText et_scdd;
    private EditText et_pc;
    private EditText et_bgsl;

    private TextView tv_ck;
    private TextView tv_wl;
    private TextView tv_sm;
    private TextView tv_ddsl;
    private TextView tv_wjsl;

    private ProgressBar pb;

    Map paramsMap2 = new HashMap();

    private ArrayList<MOMBean> MOMBeanList;
    private BaanJsonOperator operator = new BaanJsonOperator();
    private MOMBean bean;

    public APPTab6Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_app_tab6, container, false);

        initView(view);



        /*

        */

        et_scdd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //final ProgressDialog pd = new ProgressDialog(getContext());
                //pd.show();
                doWSGetOrder();
                //pd.dismiss();
                return false;
            }
        });

        et_bgsl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //final ProgressDialog pd = new ProgressDialog(getContext());
                //pd.show();
                doWSReport();
                //pd.dismiss();
                return false;
            }
        });

        /*

        et_scdd.setOnMyEditorActionListener(new MyEditText.OnMyEditorActionListener() {
            @Override
            public void OnMyEditorDoneListener(JSONObject jsonObject, String info) {
                Map paramsMap=new HashMap();
                Log.e("tab6**********","EEEEEEEEEEEEEEEEEEE");
                paramsMap.put("pdno",et_scdd.getText().toString().toUpperCase().trim());
                et_scdd.setInParams(METHOD,getActivaMap(),paramsMap);

                tv_ck.setText(jsonObject.get("T$PDNO").toString().trim());
                tv_wl.setText(jsonObject.get("T$MITM").toString().trim());
                tv_sm.setText(jsonObject.get("T$PONO").toString().trim());
                tv_ddsl.setText(jsonObject.get("T$SITM").toString().trim());
                tv_wjsl.setText(jsonObject.get("T$QUES").toString().trim());
            }
        });

        et_scdd.setOnMyEditTextListener(new MyEditText.OnMyEditTextListener() {
            @Override
            public void OnMyEditTextGetFocusListener() {

            }

            @Override
            public void OnMyEditTextLostFocusListener(JSONObject jsonObject,String info) {

            }


        });
        */

        /*

        et_bgsl.setOnMyEditTextListener(new MyEditText.OnMyEditTextListener() {
            @Override
            public void OnMyEditTextGetFocusListener() {


                SimpleDateFormat dateFormat1=new SimpleDateFormat("yyMMdd HH:mm:ss");
                SimpleDateFormat dateFormat2=new SimpleDateFormat("MMdd HH:mm:ss");
                Date date=new Date(System.currentTimeMillis());
                final String dateStr1=dateFormat1.format(date);
                final String dateStr2=dateFormat2.format(date);

                paramsMap2.put("Unique",dateStr1);
                paramsMap2.put("ProductionOrder",et_scdd.getText().toString().toUpperCase().trim());
                paramsMap2.put("QuantityToDeliver",et_bgsl.getText().toString().trim());
                paramsMap2.put("LotCode","");
                et_bgsl.setInParams(METHOD_RE,getActivaMap(),paramsMap2);
            }

            @Override
            public void OnMyEditTextLostFocusListener(JSONObject jsonObject,String info) {

                if(info.substring(0,1).equals("1")){
                    SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
                    SQLiteDatabase db=sqLiteHelper.getWritableDatabase();

                    //插入报工记录 android studio 3.0 bug sqlite 无法使用占位符 db.execSQL 效果相同
                    //db.execSQL("insert into t values (?,?,?,?)",
                    //        new String[]{paramsMap.get("ProductionOrder"),paramsMap.get("QuantityToDeliver"), dataStr2,activationMap.get("username")});
                    ContentValues values=new ContentValues();
                    values.put("t_scdd",et_scdd.getText().toString().trim());
                    values.put("t_sl",et_bgsl.getText().toString().trim());
                    values.put("t_sj",paramsMap2.get("Unique").toString());
                    values.put("t_czy",getActivaMap().get("username").toString());

                    db.insert("t",null,values);
                    //时间倒序保留10条记录
                    db.execSQL("delete from t where (select count(t_sj) from t)>10 and t_sj in " +
                            "(select t_sj from t order by t_sj desc limit(select count(t_sj) from t) offset 10)");
                    //db.execSQL("drop Table t");
                    db.close();
                    doClear();
                }

            }
        });

        */

        /*
        et_scdd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                doWSGetOrder();
                return false;
            }
        });

        et_bgsl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                doWSReport();
                return false;
            }
        });
        */

        return view;
    }

    public void initView(View view) {

        //获取AppMainActivity的扫描实例
        mContext = (APPMainActivity) getActivity();
        mReader = mContext.mReader;

        //qrUtil=new QRUtil(mContext);
        pb=view.findViewById(R.id.tab6_pb);

        et_scdd = view.findViewById(R.id.tab6_et_scdd);
        et_pc = view.findViewById(R.id.tab6_et_pc);
        et_bgsl = view.findViewById(R.id.tab6_et_bgsl);
        tv_ck = view.findViewById(R.id.tab6_tv_ck);
        tv_wl = view.findViewById(R.id.tab6_tv_wl);
        tv_sm = view.findViewById(R.id.tab6_tv_sm);
        tv_ddsl = view.findViewById(R.id.tab6_tv_ddsl);
        tv_wjsl = view.findViewById(R.id.tab6_tv_wjsl);

        et_pc.setVisibility(View.INVISIBLE);
        et_scdd.requestFocus();

        uiUtil.hideKeyboard(et_scdd);
        uiUtil.hideKeyboard(et_bgsl);
    }

    public void doClear() {
        et_bgsl.setText("");
        et_pc.setText("");
        et_scdd.setText("");
        tv_ck.setText("");
        tv_wl.setText("");
        tv_sm.setText("");
        tv_ddsl.setText("");
        tv_wjsl.setText("");
        et_scdd.requestFocus();
    }

    @Override
    public void scanStartOnKeyDown() {
        ((APPMainActivity) getActivity()).doScan(new Barcode2DWithSoft.ScanCallback() {
            @Override
            public void onScanComplete(int i, int i1, byte[] bytes) {
                mReader.stopScan();
                if (i1 > 0) {
                    String qrResult = new String(bytes).trim();

                    if (qrResult.length() == 9 && Character.isLetter(qrResult.charAt(0)) && Character.isLetter(qrResult.charAt(1))) {
                        et_scdd.setText(qrResult);
                        //doWSGetOrder();
                    }
                    if (Character.isDigit(qrResult.charAt(0))) {
                        et_bgsl.setText(qrResult);
                        //doReport();
                    }

                } else {
                    Toast.makeText(getContext(), "扫描失败", Toast.LENGTH_SHORT).show();
                    uiUtil.errorSound(getContext());
                }
            }

        });
    }


    @Override
    public void scanStopOnKeyDown() {
        mReader.stopScan();
    }

    @Override
    public void clearOnKeyDown() {
        doClear();
    }


    public void doWSGetOrder() {

        //pb.bringToFront();
        pb.setVisibility(View.VISIBLE);

        //final ProgressDialog pd=new ProgressDialog(getContext());
        //pd.show();
        Map paramsMap = new HashMap();
        paramsMap.put("pdno", et_scdd.getText().toString().toUpperCase().trim());

        ComWSUtil.callComWS(null, null, METHOD, getContext(), getActivaMap(), paramsMap, new ComWSUtil.ComWSCallBack() {
            @Override
            public void callBack(String info, JSONObject jsonObject) {
                if (info != null) {
                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                    uiUtil.errorSound(getContext());
                    et_scdd.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_scdd.requestFocus();
                        }
                    },300);

                    pb.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                        }
                    },300);

                } else {
                    tv_ck.setText(jsonObject.get("T$PDNO").toString().trim());
                    tv_wl.setText(jsonObject.get("T$MITM").toString().trim());
                    tv_sm.setText(jsonObject.get("T$PONO").toString().trim());
                    tv_ddsl.setText(jsonObject.get("T$SITM").toString().trim());
                    tv_wjsl.setText(jsonObject.get("T$QUES").toString().trim());

                    pb.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                        }
                    },300);

                    et_bgsl.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_bgsl.requestFocus();
                            //pd.dismiss();



                        }
                    },300);

                }
            }
        });

    }

    public void doWSReport() {

        pb.setVisibility(View.VISIBLE);
        //final ProgressDialog pd=new ProgressDialog(getContext());
        //pd.show();
        final Map paramsMap = new HashMap();

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyMMdd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMdd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        final String dateStr1 = dateFormat1.format(date);
        final String dateStr2 = dateFormat2.format(date);

        paramsMap.put("Unique", dateStr1);
        paramsMap.put("ProductionOrder", et_scdd.getText().toString().toUpperCase().trim());
        paramsMap.put("QuantityToDeliver", et_bgsl.getText().toString().trim());
        paramsMap.put("LotCode", "");

        ComWSUtil.callComWS(null, null, METHOD_RE, getContext(), getActivaMap(), paramsMap, new ComWSUtil.ComWSCallBack() {
            @Override
            public void callBack(String info, JSONObject jsonObject) {
                if (info.substring(0, 1).equals("1")) {
                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();

                    SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
                    SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

                    //插入报工记录 android studio 3.0 bug sqlite 无法使用占位符 db.execSQL 效果相同
                    //db.execSQL("insert into t values (?,?,?,?)",
                    //        new String[]{paramsMap.get("ProductionOrder"),paramsMap.get("QuantityToDeliver"), dataStr2,activationMap.get("username")});
                    ContentValues values = new ContentValues();
                    values.put("t_scdd", paramsMap.get("ProductionOrder").toString());
                    values.put("t_sl", paramsMap.get("QuantityToDeliver").toString());
                    values.put("t_sj", dateStr1);
                    values.put("t_czy", getActivaMap().get("username").toString());

                    db.insert("t", null, values);
                    //时间倒序保留10条记录
                    db.execSQL("delete from t where (select count(t_sj) from t)>10 and t_sj in " +
                            "(select t_sj from t order by t_sj desc limit(select count(t_sj) from t) offset 10)");
                    //db.execSQL("drop Table t");
                    db.close();
                    doClear();
                    //pd.dismiss();
                    pb.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                        }
                    },300);

                } else {
                    Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
                    uiUtil.errorSound(getContext());

                    et_bgsl.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_bgsl.requestFocus();
                            //pd.dismiss();
                        }
                    },300);

                    pb.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                        }
                    },300);
                }
            }
        });

    }
}

