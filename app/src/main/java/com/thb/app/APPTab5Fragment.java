package com.thb.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.thb.ui.ComCapTransMethod;
import com.thb.ui.ComEditText;
import com.thb.ui.UIUtil;
import com.thb.ws.SQLiteHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class APPTab5Fragment extends APPComFragment {

    private static final String DEBUG_TAG = "tab5*********";
    private static final String METHOD_RE = "ReportOrdersCompleted";
    private static final String METHOD_SPO="StdProductionOrders";

    private APPMainActivity mContext;
    private UIUtil uiUtil = new UIUtil();

    private ComEditText et_scdd;
    private ComEditText et_bgsl;

    private TextView tv_pc;
    private TextView tv_ck;
    private TextView tv_wl;
    private TextView tv_sm;
    private TextView tv_ddsl;
    private TextView tv_wjsl;
    private TextView tv_dw;

    private Button b_sm;
    private Button b_tj;
    private Button b_qc;

    private LinearLayout ll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_tab5, container, false);
        //init
        initView(view);

        b_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), CaptureActivity.class),1);
            }
        });

        b_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_scdd.hasFocus()){
                    doGetOrder();
                }else if(et_bgsl.hasFocus()){
                    doReport();
                }
            }
        });

        b_qc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClear();
            }
        });

        et_scdd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(uiUtil.getSystemModel().contains("C4050")){
                    doGetOrder();

                }
                return false;
            }
        });

        et_bgsl.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if(uiUtil.getSystemModel().contains("C4050")){
                    doReport();

                }
                return false;
            }
        });
        return view;
    }

    public void initView(View view) {

        et_scdd =  view.findViewById(R.id.tab5_et_scdd);
        tv_pc = view.findViewById(R.id.tab5_tv_pc);
        et_bgsl = view.findViewById(R.id.tab5_et_bgsl);
        tv_ck = view.findViewById(R.id.tab5_tv_ck);
        tv_wl = view.findViewById(R.id.tab5_tv_wl);
        tv_sm = view.findViewById(R.id.tab5_tv_sm);
        tv_ddsl = view.findViewById(R.id.tab5_tv_ddsl);
        tv_wjsl = view.findViewById(R.id.tab5_tv_wjsl);
        tv_dw=view.findViewById(R.id.tab5_tv_dw);

        b_sm=view.findViewById(R.id.tab5_btn_sm);
        b_tj=view.findViewById(R.id.tab5_btn_tj);
        b_qc=view.findViewById(R.id.tab5_btn_qc);

        ll=view.findViewById(R.id.tab5_ll);
        //获取AppMainActivity对象 to scan
        if(uiUtil.getSystemModel().contains("C4050")){
            mContext = (APPMainActivity) getActivity();
            et_scdd.setMainContext(mContext);
            et_bgsl.setMainContext(mContext);
        }else {
            //zxing init
            ZXingLibrary.initDisplayOpinion(getContext());
            ll.setVisibility(View.VISIBLE);
        }

        tv_sm.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_scdd.requestFocus();
    }

    public void doReport(){

        //回调返回参数：操作信息（成功1：*****，失败0：******）info，数据jsonobject
        final Map paramsMap = new HashMap();
        paramsMap.put("Unique", uiUtil.getSysTime());
        paramsMap.put("ProductionOrder", et_scdd.getText().toString().toUpperCase().trim());

        paramsMap.put("QuantityToDeliver", et_bgsl.getText().toString().trim());
        paramsMap.put("LotCode", "");
        et_bgsl.setIn(METHOD_RE,getActivaMap(),paramsMap);

        if(!uiUtil.getSystemModel().contains("C4050")) {
            et_bgsl.doWS();
        }

        et_bgsl.setCetwsCallBack(new ComEditText.CETWSCallBack() {
            @Override
            public void wscallBack(String info, JSONObject jsonObject) {
                if(info.substring(0,1).equals("1")){

                    uiUtil.successSound(getContext());
                    //报工成功插入数据库
                    SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
                    sqLiteHelper.doInsertRE(et_scdd.getText().toString().toUpperCase().trim(),tv_wl.getText().toString().trim(),
                            et_bgsl.getText().toString().trim(),uiUtil.getSysTime(),getActivaMap().get("username").toString());
                    //保留最新10条记录
                    sqLiteHelper.doExecSQL("delete from roc where (select count(roc_sj) from roc)>10 and roc_sj in " +
                            "(select roc_sj from roc order by roc_sj desc limit(select count(roc_sj) from roc) offset 10)");
                    sqLiteHelper.doClose();

                    doClear();
                }else {
                    et_scdd.requestFocus();
                    et_bgsl.requestFocus();
                }
            }
        });
    }

    public void doGetOrder(){
        //setin获取参数并调用WS

        Map paramsMap = new HashMap();
        paramsMap.put("pdno", et_scdd.getText().toString().toUpperCase().trim());
        et_scdd.setIn(METHOD_SPO,getActivaMap(),paramsMap);

        if(!uiUtil.getSystemModel().contains("C4050")) {
            et_scdd.doWS();
        }

        //回调返回参数：操作信息（成功1：*****，失败0：******）info，数据jsonobject
        et_scdd.setCetwsCallBack(new ComEditText.CETWSCallBack() {
            @Override
            public void wscallBack(String info, JSONObject jsonObject) {
                if(info==null){
                    tv_ck.setText(jsonObject.get("T$CWAR").toString().trim());
                    tv_wl.setText(jsonObject.get("T$MITM").toString().trim());
                    tv_sm.setText(jsonObject.get("T$DSCA").toString().trim());
                    tv_ddsl.setText(jsonObject.get("T$QRDR").toString().trim()+jsonObject.get("T$CUNI").toString().trim());
                    BigDecimal ddsl=new BigDecimal(jsonObject.get("T$QRDR").toString().trim());
                    BigDecimal yjsl=new BigDecimal(jsonObject.get("T$QDLV").toString().trim());
                    tv_wjsl.setText((ddsl.subtract(yjsl)).doubleValue()+jsonObject.get("T$CUNI").toString().trim());
                    tv_dw.setText(jsonObject.get("T$CUNI").toString().trim());
                    tv_pc.setText(jsonObject.get("T$CLOT").toString().trim());

                    if(tv_dw.getText().toString().trim().equals("EA")) {
                        et_bgsl.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }

                    et_bgsl.requestFocus();
                }else {
                    et_scdd.requestFocus();
                }
            }
        });
    }

    //手机摄像头
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            if(data!=null){
                Bundle bundle=data.getExtras();
                if(bundle==null){
                    return;
                }
                if(bundle.getInt(CodeUtils.RESULT_TYPE)==CodeUtils.RESULT_SUCCESS){
                    String result=bundle.getString(CodeUtils.RESULT_STRING);
                    if(result.length()>0){

                        if(et_scdd.hasFocus()){
                            et_scdd.setText(result);
                        }
                        if(et_bgsl.hasFocus()){
                            et_bgsl.setText(result);
                        }

                    }else{
                        Toast.makeText(getContext(),"扫描失败",Toast.LENGTH_SHORT).show();
                        uiUtil.errorSound(getContext());
                    }
                }
            }
        }
    }

    public void doClear() {
        et_bgsl.setText("");
        tv_pc.setText("");
        et_scdd.setText("");
        tv_ck.setText("");
        tv_wl.setText("");
        tv_sm.setText("");
        tv_ddsl.setText("");
        tv_wjsl.setText("");
        tv_dw.setText("");
        et_scdd.requestFocus();
    }

    @Override
    public void clearOnKeyDown() {
        doClear();
    }
}

