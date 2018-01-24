package com.thb.utils;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.thb.app.R;

/**
 * Created by sea79 on 2017/12/27.
 */

public class Fun {

    /**
     * UI
     */

    /*
    //TextView 走马灯
        <TextView
    android:id="@+id/test_tv"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:textSize="30sp"
    android:singleLine="true"
    android:ellipsize="marquee"
    android:marqueeRepeatLimit="marquee_forever"
    android:focusableInTouchMode="true"
    android:focusable="true"

    android:text="AAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBB"/>

    //TextView 获得焦点全选
    selectAllOnFocus

    */

    /**
     * CODE
     */

    /**
     * 初始化Spinner控件
     */

    /*
    public void initSpinner(){

        //建立数据源
        final String[] spinner_items=getResources().getStringArray(R.array.spinnerItems);
        //建立adapter绑定数据源
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,spinner_items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定控件
        spinner_company.setAdapter(arrayAdapter);
        spinner_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] spinner_items = getResources().getStringArray(R.array.spinnerItems);
                itemNo=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    */

    /**
     * ping服务器测试是否连接
     * @return
     */
    /*
    public boolean isWebConn(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    url=new URL("http://192.168.30.49");
                    conn=(HttpURLConnection)url.openConnection();
                    conn.setConnectTimeout(1000*5);
                    if(conn.getResponseCode()==200){
                        isConn=true;
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        return isConn;
    }
    */

    /*属性动画
    //
        //输入框动画
    public void inputAnimator(final View v,float w,float h){
        AnimatorSet set=new AnimatorSet();
        ValueAnimator animator=ValueAnimator.ofFloat(0,w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value=(Float)valueAnimator.getAnimatedValue();
                ViewGroup.MarginLayoutParams params=(ViewGroup.MarginLayoutParams)
                        v.getLayoutParams();
                params.leftMargin=(int)value;
                params.rightMargin=(int)value;
                v.setLayoutParams(params);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,"scaleX",1f,0.5f);
        set.setDuration(100);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator,animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                //mInputLayout.setVisibility(View.INVISIBLE);
                //login();


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    //进度条动画

    private void progressAnimator(final View v){

        PropertyValuesHolder animator=PropertyValuesHolder.ofFloat("scaleX",0.5f,1f);
        PropertyValuesHolder animator2=PropertyValuesHolder.ofFloat("scaleY",0.5f,1f);
        ObjectAnimator animator3=ObjectAnimator.ofPropertyValuesHolder(v,animator,animator2);
        animator3.setDuration(100);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                progress.setVisibility(View.INVISIBLE);
                mInputLayout.setVisibility(View.VISIBLE);
                mUsername.setVisibility(View.VISIBLE);
                mPassword.setVisibility(View.VISIBLE);
                mCompany.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    //强制UI主线程进行网络通讯
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

      fragment oncreate获取 view
          View view = inflater.inflate(R.layout.fragment_app_tab8, container, false);
          return view;
     //隐藏actionbar
     getSupportActionBar().hide();


    //下拉刷新SwipeRefreshLayout使用方法
    srl.setColorSchemeResources(android.R.color.holo_blue_light,
    android.R.color.holo_red_light,android.R.color.holo_orange_light,
    android.R.color.holo_green_light);

    srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            doShowRecord();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    srl.setRefreshing(false);
                }
            },500);

        }
    });

        //listView加载header,item中有数据时才显示
        View listHeader=LayoutInflater.from(getContext()).inflate(R.layout.tab7_list_header,null,true);
        lv.addHeaderView(listHeader);



        //Handler定时更新UI实现图片切换

            Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==0){
                imgChange.setImageResource(img[imgStart++%4]);
            }
        }
    };

                    new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
            }
        },0,200);

        //edit输入头像
                        SpannableString spanStr=new SpannableString("emoji");
                Drawable drawable=TestActivity.this.getResources().getDrawable(R.mipmap.ic_launcher);
                drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                ImageSpan imageSpan=new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
                spanStr.setSpan(imageSpan,0,4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                int cursor=et1.getSelectionStart();
                et1.getText().insert(cursor,spanStr);



    */
}
