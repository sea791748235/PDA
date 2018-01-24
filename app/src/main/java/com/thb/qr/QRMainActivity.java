package com.thb.qr;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rscja.utility.StringUtility;
import com.thb.app.R;
import com.thb.ws.BOMBean;
import com.thb.ws.BaanJsonOperator;
import com.thb.ws.BaanSoapService;
import com.thb.ws.BeanListAdapter;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class QRMainActivity extends AppCompatActivity {

      /**
       * 声明变量
       */

      /**
       * 扫码变量
       * Barcode2DWithSoft 扫码API
       * Thread 扫码线程
       */
      private BeanListAdapter mAdapter;

      private String method="GetBOM";
      private ArrayList<String> allParamsList=new ArrayList<>();
      private HashMap<String,String> paramsMap=new HashMap<>();
      private HashMap<String,String> activationMap=new HashMap<>();
      private ArrayList<BOMBean> BOMBeanList;
      private ArrayList<BOMBean> allBeanList=new ArrayList<>();

      public Barcode2DWithSoft mReader;
      private Thread thread;
      private String initCode;
      int successCount=0;
      int failCount=0;
      int errorCount=0;
      private boolean isThreadStop=true;
      private boolean isCurrFrag=false;

      /**
       * WebService 变量
       * out_xx 发送数据
       * in_xx 返回数据
       * SoapService
       */

      /**
      * 控件变量
      */
      private ProgressBar qr_progress;
        private Button qr_button_submit;
        private Button qr_button_clear;
        private Button qr_button_start;
        private CheckBox qr_check_compare;
        private CheckBox qr_check_continue;
        private EditText qr_edit_interval;
        private EditText qr_edit_initcode;
        private TextView qr_text_errorcount;
        private TextView qr_text_errorrate;
        private TextView qr_text_failcount;
        private TextView qr_text_failrate;
        private TextView qr_text_successcount;
        private TextView qr_text_successrate;
        private TextView qr_text_scancount;
        private TextView qr_text_result;
        private ScrollView qr_scroll_result;
        private ListView qr_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * UI线程中强制进行网络通讯
         * 缺点：网络线程开销大，影响性能
         */
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        /**
         * UI初始化
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_main);

        /**
         * 绑定控件
         */
        qr_progress=findViewById(R.id.qr_progress);
        qr_button_submit=findViewById(R.id.qr_button_submit);
        qr_button_start=findViewById(R.id.qr_button_start);
        qr_button_clear=findViewById(R.id.qr_button_clear);
        qr_check_continue=findViewById(R.id.qr_check_continue);
        qr_check_compare=findViewById(R.id.qr_check_compare);
        qr_edit_initcode=findViewById(R.id.qr_edit_initcode);
        qr_edit_interval=findViewById(R.id.qr_edit_interval);
        qr_text_successcount=findViewById(R.id.qr_text_successcount);
        qr_text_successrate=findViewById(R.id.qr_text_successrate);
        qr_text_failcount=findViewById(R.id.qr_text_failcount);
        qr_text_failrate=findViewById(R.id.qr_text_failrate);
        qr_text_errorcount=findViewById(R.id.qr_text_errorcount);
        qr_text_errorrate=findViewById(R.id.qr_text_errorrate);
        qr_scroll_result=findViewById(R.id.qr_scroll_result);
        qr_text_result=findViewById(R.id.qr_text_result);
        qr_text_scancount=findViewById(R.id.qr_text_scancount);
        qr_list=findViewById(R.id.qr_list);

        View listHeader= View.inflate(this,R.layout.beanlist_header,null);
        qr_list.addHeaderView(listHeader);
        mAdapter=new BeanListAdapter(QRMainActivity.this,allBeanList);
        qr_list.setAdapter(mAdapter);


        /**
         * 获得扫码实例
         */
        try{
            mReader=Barcode2DWithSoft.getInstance();
            Toast.makeText(QRMainActivity.this,"扫码功能已就绪",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(QRMainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * qr_button
         * doDecode：扫描，解码，回调解码结果；
         * doClear:清除扫码结果
         */
        qr_button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDecode();
            }
        });

        qr_button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doClear();
            }
        });

        qr_button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doCall();
                mAdapter=new BeanListAdapter(QRMainActivity.this,allBeanList);
                qr_list.setAdapter(mAdapter);

            }
        });


    }

    public Barcode2DWithSoft.ScanCallback mScanCallback=new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {

            Log.i("QRMainActivity","onScanComplete() i="+i);
            String strData="";

            if(isCurrFrag){
                return;
            }

            if((successCount+errorCount+failCount)%1000==0){
                qr_text_result.setText("");
            }

            if(length<1){
                failCount+=1;
                strData="扫描失败"+"\n";
                qr_text_result.append(strData);
                return;
            }

            mReader.stopScan();
            String barCode=new String(data);

            allParamsList.add(barCode.trim());

            if(qr_check_compare.isChecked()){
                initCode=qr_edit_initcode.getText().toString().trim();

                if(StringUtility.isEmpty(initCode)){
                    qr_edit_initcode.setText(barCode);
                    initCode=qr_edit_initcode.getText().toString().trim();
                }

                if(initCode.equals(barCode)){
                    successCount+=1;
                }else{
                    errorCount+=1;
                    strData="扫描错误:";
                }
            }else{
                successCount+=1;
                initCode="";
                qr_edit_initcode.setText("");
            }

            strData+=barCode+"\n";

            qr_text_result.append(strData);
            QRMainActivity.this.scrollToBottom(qr_scroll_result,qr_text_result);
            state();
        }
    };

    public void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }

    public void state(){
        int total=successCount+failCount+errorCount;

        if(total>0){
            qr_text_scancount.setText(String.valueOf(total));
            qr_text_successcount.setText(String.valueOf(successCount));
            qr_text_failcount.setText(String.valueOf(failCount));
            qr_text_errorcount.setText(String.valueOf(errorCount));

            qr_text_successrate.setText(String.valueOf(errorCount*1000/total)+"‰");
            qr_text_failrate.setText(String.valueOf(failCount*1000/total)+"‰");
            qr_text_errorrate.setText(String.valueOf(errorCount*1000/total)+"‰");

        }
    }

    private void doDecode(){

        if(mReader != null){
            mReader.setScanCallback(mScanCallback);

        }

        if(isThreadStop){
            boolean isContinue=false;
            int  interval=0;
            isContinue=qr_check_continue.isChecked();

            if(isContinue){
                qr_button_start.setText("停止");
                isThreadStop=false;

                String strInterval=qr_edit_interval.getText().toString().trim();

                if(StringUtility.isEmpty(strInterval)){

                }else{
                    interval=StringUtility.string2Int(strInterval,0);
                }

                qr_button_clear.setEnabled(false);
                qr_check_continue.setEnabled(false);
            }

            initCode=qr_edit_initcode.getText().toString().trim();




            thread=new DecodeThread(isContinue,interval);
            thread.start();

        }else{
            qr_button_start.setText("扫描");
            isThreadStop=true;
            qr_check_continue.setEnabled(true);
            qr_button_clear.setEnabled(true);
        }
    }

    private class DecodeThread extends Thread{
        private boolean isContinue=false;
        private long sleepTime=1000;

        public DecodeThread(boolean isContinue,int sleep){
            this.isContinue=isContinue;
            this.sleepTime=sleep;
        }

        @Override
        public void run(){
            super.run();

            do{
                mReader.scan();


                if(isContinue){
                    try{
                        Thread.sleep(sleepTime);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }while (isContinue&&!isThreadStop);
        }
    }

    private void doClear(){
        qr_text_result.setText("");

        int total=0;
        successCount=0;
        failCount=0;
        errorCount=0;

        qr_edit_initcode.setText("");
        qr_text_scancount.setText(String.valueOf(total));
        qr_text_successcount.setText(String.valueOf(successCount));
        qr_text_failcount.setText(String.valueOf(failCount));
        qr_text_errorcount.setText(String.valueOf(errorCount));
        qr_text_successrate.setText(String.valueOf(0));
        qr_text_errorrate.setText(String.valueOf(0));
        qr_text_failrate.setText(String.valueOf(0));

        qr_button_start.setText("扫描");
        isThreadStop=true;
        allParamsList.clear();
        qr_list.setAdapter(null);
    }
    public class InitTask extends AsyncTask<String,Integer,Boolean>{
        ProgressDialog myDialog;

        @Override
        protected Boolean doInBackground(String...params){
            boolean result=false;

            if(mReader!=null){
                result=mReader.open(QRMainActivity.this);
                if(result){
                    mReader.setParameter(324,1);
                    mReader.setParameter(300,0);
                    mReader.setParameter(361,0);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            super.onPostExecute(result);
            myDialog.cancel();

            if(!result){
                Toast.makeText(QRMainActivity.this,"初始化失败",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            myDialog=new ProgressDialog(QRMainActivity.this);
            myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            myDialog.setMessage("初始化中...");
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.show();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        /*
        isCurrFrag=false;
        isThreadStop=true;
        qr_check_continue.setEnabled(true);
        qr_button_start.setText("扫描");
        qr_button_clear.setEnabled(true);


        if(mReader!=null){
            mReader.stopScan();
        }
        */
        if(mReader!=null){
            mReader.close();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(mReader!=null){
            new InitTask().execute();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void test(){

    }

    public void doCall(){

        activationMap.put("username","zhou");
        activationMap.put("password","123456");
        activationMap.put("company","191");

        Iterator iterator=allParamsList.iterator();

        //out_hashmap.put("byProvinceName","河南");

        //Log.e("******body******",paramsMap.get("mitem"));
        Log.e("******head******",activationMap.get("username"));
        Log.e("******head******",activationMap.get("password"));
        Log.e("******head******",activationMap.get("company"));

        BaanSoapService service=new BaanSoapService();


        while (iterator.hasNext()){
            paramsMap.put("mitem",iterator.next().toString());
            Log.e("!!!!!!!!!!!!!",paramsMap.get("mitem"));

            service.setAll(activationMap,paramsMap,method);
            String resultStr=service.CallBaanWS();

            Log.e("@@@@@@@@@@@result@@@@@",resultStr);
                BOMBeanList=new BaanJsonOperator().HListJson2ListBean(resultStr,BOMBean.class);
                allBeanList.addAll(BOMBeanList);
                Log.e("##############",allBeanList.toString());

        }




    }

}
