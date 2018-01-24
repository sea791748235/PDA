package com.thb.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.thb.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    private static final String METHOD = "GetMOMaterialsLine";
    private static final String METHOD_RE = "ReportOrdersCompleted";
    @BindView(R.id.custom_tb)
    Toolbar customTb;
    @BindView(R.id.dl_iv1)
    ImageView dlIv1;
    @BindView(R.id.dl_lv1)
    ListView dlLv1;
    @BindView(R.id.custom_dl1)
    DrawerLayout customDl1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_test, null);
        setContentView(view);
        ButterKnife.bind(this);

        customTb.setTitle("AAAAAAAAAA");
        customTb.setTitleTextColor(Color.GREEN);
        setSupportActionBar(customTb);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle1 = new ActionBarDrawerToggle(this, customDl1, customTb, 0, 0) {
            @Override
            public void onDrawerOpened(View dView) {
                super.onDrawerOpened(dView);
            }

            @Override
            public void onDrawerClosed(View dView) {
                super.onDrawerClosed(dView);
            }
        };

        toggle1.syncState();

        customDl1.addDrawerListener(toggle1);

        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add("item" + i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        dlLv1.setAdapter(adapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void redirectTo() {

    }

}
