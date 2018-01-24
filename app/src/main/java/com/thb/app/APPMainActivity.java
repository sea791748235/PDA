package com.thb.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thb.qr.QRUtil;
import com.thb.ui.UIUtil;
import com.thb.ws.SQLiteHelper;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APPMainActivity extends APPComActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String username;
    private String password;
    private String company;

    private APPMainActivity mContext;
    private UIUtil uiUtil;

    private String qrResult="qrResult";

    private int mode=1;

    private SQLiteDatabase db;

    private long lastTime=0;

    public Barcode2DWithSoft mReader;

    private List<HashMap<String,String>> allList;

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getCompany(){
        return company;
    }
    public void setCompany(String company){
        this.company=company;
    }
    public String getQrResult(){
        return qrResult;
    }
    public void setQrResult(String QrResult){
        this.qrResult=qrResult;
    }
    public int getMode(){
        return mode;
    }

    public void setMode(int mode){
        this.mode=mode;
    }

    public List<HashMap<String,String>> getAllList(){
        return allList;
    }

    public void setAllList(List<HashMap<String,String>> allList){
        this.allList=allList;
    }

    private TabLayout tabTop;
    private ViewPager viewPager;

    private List<Fragment> list;
    private MyAdapter adapter;
    private String[] titles = {"扫码报工","历史记录","设    置","扫码上架", "整箱转移", "散箱转移", "扫码盘点",
            "销售出库", "包装检验","整箱库位转移",
            "散箱库位转移"};
    private int images[] = {R.mipmap.ic_smbg_white_24dp,R.mipmap.ic_tjxx_white_24dp,R.mipmap.ic_sz_white_24dp,R.mipmap.ic_smsz_white_24dp,R.mipmap.ic_zxzy_white_24dp,R.mipmap.ic_sxzy_white_24dp,R.mipmap.ic_smpd_white_24dp,
            R.mipmap.ic_xsck_white_24dp, R.mipmap.ic_bzjy_white_24dp,R.mipmap.ic_zxkwzy_white_24dp,
    R.mipmap.ic_sxkwzy_white_24dp};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);
        uiUtil=new UIUtil();
        //实例化
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewpager缓存fragment数量=0
        //viewPager.setOffscreenPageLimit(0);
        tabTop = (TabLayout) findViewById(R.id.tabtop);

        Log.e("system%%%%%%%%%%%%",uiUtil.getSystemModel());
        //getTabBottomView();
        //页面，数据源



        Bundle bundle=this.getIntent().getExtras();
        setUsername(bundle.getString("usernameCheck"));
        setPassword(bundle.getString("passwordCheck"));
        setCompany(bundle.getString("companyCheck"));

        /*
        try{
            mReader=Barcode2DWithSoft.getInstance();
            Toast.makeText(APPMainActivity.this,"扫码功能已就绪",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(APPMainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
        */

        mContext=APPMainActivity.this;
        mReader=mContext.mReader;

        SQLiteHelper sqLiteHelper=new SQLiteHelper(APPMainActivity.this);
        sqLiteHelper.doExecSQL("create table if not exists t_roc(roc_scdd text,roc_wl text,roc_sl text,roc_sj text,roc_czy text)");
        sqLiteHelper.doClose();
        //qrUtil.setQR(APPMainActivity.this,mReader,true,1000);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,0,0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nv);
        navigationView.setNavigationItemSelectedListener(this);
        doChooseMode(mode);
    }

    public void doChooseMode(int mode){

        list = new ArrayList<>();

        if(mode==11){
            //list.clear();
            //list.add(new APPTab3Fragment());
            //list.add(new APPTab5Fragment());
            //list.add(new APPTab8Fragment());
            //list.add(new APPTab6Fragment());
            //list.add(new APPTab7Fragment());
            //list.add(new APPTab8Fragment());
            // list.add(new APPTab1Fragment());
            //list.add(new APPTab2Fragment());

        }else if(mode==1){
            //list.clear();
            //list.add(new APPTab3Fragment());
            //list.add(new APPTab4Fragment());
            list.add(new APPTab5Fragment());
            //list.add(new APPTab6Fragment());
            list.add(new APPTab7Fragment());
            //list.add(new APPTab8Fragment());
            // list.add(new APPTab1Fragment());
            //list.add(new APPTab2Fragment());



        }else if(mode==2){
            //list.clear();
            //list.add(new APPTab3Fragment());
            //list.add(new APPTab4Fragment());
            //list.add(new APPTab5Fragment());
            //list.add(new APPTab6Fragment());
            //list.add(new APPTab7Fragment());
            //list.add(new APPTab8Fragment());
            // list.add(new APPTab1Fragment());
            //list.add(new APPTab2Fragment());
        }else if(mode==3){
            //list.clear();
            //list.add(new APPTab3Fragment());
            //list.add(new APPTab4Fragment());
            //list.add(new APPTab5Fragment());
            //list.add(new APPTab6Fragment());
            //list.add(new APPTab7Fragment());
            //list.add(new APPTab8Fragment());
            // list.add(new APPTab1Fragment());
            //list.add(new APPTab2Fragment());
        }

        //ViewPager的适配器

        adapter = new MyAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);


        //绑定
        tabTop.setupWithViewPager(viewPager);

        tabTop.getTabAt(0).setCustomView(adapter.getTabView(0));

        tabTop.getTabAt(1).setCustomView(adapter.getTabView(1));
            /*
            //设置自定义视图
            for (int i = 0; i < tabTop.getTabCount(); i++) {
                TabLayout.Tab tab = tabTop.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }
            */
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.m01) {
            // Handle the camera action
            doChooseMode(1);
        } else if (id == R.id.m02) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
            //doChooseMode(2);
        } else if (id == R.id.m03) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
            //doChooseMode(3);
        } else if (id == R.id.m04) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.m05) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.m06) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        }else if (id==R.id.m07){
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.m08) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.m09) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.m10) {
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.m11) {
            //doChooseMode(11);
            Toast.makeText(APPMainActivity.this,"此功能尚不可用",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_dl);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    private void getTabBottomView() {
        tabBottom = (HorizontalScrollView) findViewById(R.id.tabbottom_scroll);
        Button button1 = (Button) findViewById(R.id.tabbottom_button1);
        Button button2 = (Button) findViewById(R.id.tabbottom_button2);
        Button button3 = (Button) findViewById(R.id.tabbottom_button3);
        Button button4 = (Button) findViewById(R.id.tabbottom_button4);
        Button button5 = (Button) findViewById(R.id.tabbottom_button5);
        Button button6 = (Button) findViewById(R.id.tabbottom_button6);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(APPMainActivity.this, LOGINMainActivity.class));
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), com.thb.qr.QRMainActivity.class));
                Toast.makeText(APPMainActivity.this, "click button1", Toast.LENGTH_SHORT).show();
            }
        });
    }
*/


    class MyAdapter extends FragmentPagerAdapter {

        private Context context;

        public MyAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * 自定义方法，提供自定义Tab
         *
         * @param position 位置
         * @return 返回Tab的View
         */
        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.tabtop_custom, null);
            TextView textView = (TextView) v.findViewById(R.id.tv_title);
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_icon);
            textView.setText(titles[position]);
            imageView.setImageResource(images[position]);
            //添加一行，设置颜色
            textView.setTextColor(tabTop.getTabTextColors());//
            return v;
        }
    }

    //设置返回键两次退出
    @Override
    public void onBackPressed(){


        long currentTime=System.currentTimeMillis();
        if(currentTime-lastTime<2000){
            super.onBackPressed();
        }else{
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            lastTime=System.currentTimeMillis();
        }
    }

    //设置物理键盘
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if(keyCode==139){
            if(event.getRepeatCount()==0){
                if(viewPager!=null){
                    APPComFragment af=(APPComFragment)adapter.getItem(viewPager.getCurrentItem());
                    af.scanStartOnKeyDown();
                    //doQR();
                }
            }
            return true;
        }

        if(keyCode==136){
            if (event.getRepeatCount()==0){
                if(viewPager!=null){
                    APPComFragment af=(APPComFragment)adapter.getItem(viewPager.getCurrentItem());
                    af.scanStopOnKeyDown();
                }
            }
            return true;
        }

        if(keyCode==137){
            if (event.getRepeatCount()==0){
                if(viewPager!=null){
                    APPComFragment af=(APPComFragment)adapter.getItem(viewPager.getCurrentItem());
                    af.clearOnKeyDown();
                }
            }
            return true;
        }

        if(keyCode==66){
            if(event.getRepeatCount()==0){
                if(viewPager!=null){
                    APPComFragment af=(APPComFragment)adapter.getItem(viewPager.getCurrentItem());
                    af.submitOnKeyDown();
                }
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void QRinit(){
        try{
            mReader=Barcode2DWithSoft.getInstance();
            Toast.makeText(mContext,"扫描功能已就绪",Toast.LENGTH_SHORT).show();
            new InitTask().execute();

        }catch (Exception e){
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void doScan(Barcode2DWithSoft.ScanCallback mScanCallback){
        mReader.scan();
        mReader.setScanCallback(mScanCallback);
        setQrResult(qrResult);
        Log.e("appmain******",qrResult);
    }

    public void stopScan(){
        if(mReader!=null){
            mReader.close();
        }
    }

    /*
    public Barcode2DWithSoft.ScanCallback mScanCallback=new Barcode2DWithSoft.ScanCallback() {
        @Override
        public void onScanComplete(int i, int length, byte[] data) {

            if (length < 1) {
                failCount+=1;
                qrResult="0:扫描失败";
                return;
            }

            mReader.stopScan();
            qrResult=new String(data).trim();
            Log.e("QRUtil**********",qrResult);


        }
    };
    */

    public class InitTask extends AsyncTask<String,Integer,Boolean> {
        ProgressDialog myDialog;

        @Override
        protected Boolean doInBackground(String...params){
            boolean result=false;

            if(mReader!=null){
                result=mReader.open(mContext);
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
                Toast.makeText(mContext,"初始化失败",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute(){

            super.onPreExecute();
            myDialog=new ProgressDialog(mContext);
            myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            myDialog.setMessage("初始化中...");
            myDialog.setCanceledOnTouchOutside(false);
            //myDialog.show();
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        //qrUtil.stopScan();
        //mReader.stopScan();
        stopScan();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(uiUtil.getSystemModel().contains("C4050")){
            QRinit();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}