package com.thb.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thb.app.APPMainActivity;
import com.thb.app.R;
import com.thb.ui.UIUtil;
import com.thb.ws.WSUtil;

import java.util.HashMap;

public class LOGINTestActivity extends AppCompatActivity {

    private static final String DEBUG_TAG="LOGINTestActivity******";

    private String usernameCheck;
    private String passwordCheck;
    private String companyCheck;

    private UIUtil uiUtil=new UIUtil();

    private EditText et_company;
    private EditText et_username;
    private EditText et_password;
    private CheckBox check_memory;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static final String METHOD1="PDALoginCheck";
    private static final String METHOD2="PDALoginMode";

    private TextView mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //隐藏actionBar
        //getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        final View view=View.inflate(this,R.layout.activity_login_test,null);
        setContentView(view);

        initView();
        getSp();
    }

    private void initView(){

        mBtn=findViewById(R.id.test_text);

        et_company=findViewById(R.id.input_company);
        et_username=findViewById(R.id.input_username);
        et_password=findViewById(R.id.input_password);

        check_memory=findViewById(R.id.test_checkbox);

        et_password.requestFocus();

        if(uiUtil.getSystemModel().contains("C4050")){
            uiUtil.hideKeyboard(et_company);
            uiUtil.hideKeyboard(et_username);
            uiUtil.hideKeyboard(et_password);
        }


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doWSLoginCheck(view);
            }
        });
    }

    //获取sp保存的信息
    public void getSp(){

        sp=getSharedPreferences("info",MODE_PRIVATE);
        editor=sp.edit();

        String username=sp.getString("info_username","");
        //String password=sp.getString("info_password","");
        String company=sp.getString("info_company","");

        if(username.equals("") ||company.equals("")){
            check_memory.setChecked(false);
        }else{
            et_username.setText(username);
            //et_password.setText(password);
            et_company.setText(company);
            check_memory.setChecked(true);
        }
    }

    //WebService 登陆验证
    public void doWSLoginCheck(final View view) {
        //mBtn.setEnabled(false);
        //mBtn.setVisibility(View.INVISIBLE);
        //check_memory.setVisibility(View.INVISIBLE);

        final ProgressDialog pd = new ProgressDialog(LOGINTestActivity.this);
        pd.show();

        final HashMap<String, String> activationMap = new HashMap<String, String>();
        final HashMap<String, String> paramsMap = new HashMap<>();

        usernameCheck = et_username.getText().toString().trim();
        passwordCheck = et_password.getText().toString().trim();
        companyCheck = et_company.getText().toString().trim();

        activationMap.put("username", usernameCheck);
        activationMap.put("password", passwordCheck);
        activationMap.put("company", companyCheck);

        paramsMap.put("loginuser", usernameCheck);
        paramsMap.put("loginpwd", passwordCheck);

        WSUtil.CallWS(null, null, METHOD1, activationMap, paramsMap, new WSUtil.WSCallBack() {
            @Override
            public void callBack(String result) {

                if (result.substring(0, 1).equals("0")) {

                    editor.remove("info_username");
                    //editor.remove("info_password");
                    editor.remove("info_company");
                    editor.commit();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    et_password.requestFocus();
                } else {

                    if (check_memory.isChecked()) {
                        editor.putString("info_username", usernameCheck);
                        //editor.putString("info_password",passwordCheck);
                        editor.putString("info_company", companyCheck);
                        editor.commit();
                    } else {
                        editor.remove("info_username");
                        //editor.remove("info_password");
                        editor.remove("info_company");
                        editor.commit();
                    }

                    Toast.makeText(getApplicationContext(), "1:登陆成功", Toast.LENGTH_SHORT).show();

                    //Bundle activity之间传递数据
                    Bundle bundle = new Bundle();
                    bundle.putString("usernameCheck", usernameCheck);
                    bundle.putString("passwordCheck", passwordCheck);
                    bundle.putString("companyCheck", companyCheck);
                    final Intent intent = new Intent(LOGINTestActivity.this, APPMainActivity.class);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    //结束登陆activity
                    LOGINTestActivity.this.finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }

                Handler mHandler=new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                },100);

            }
        });
    }
}
