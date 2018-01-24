package com.thb.app;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thb.utils.UpdateUtil;
import com.thb.ws.SQLiteHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class APPTab8Fragment extends APPComFragment {
    // 更新版本要用到的一些信息
    private final static String DEBUG_TAG="tab8*******";
    private Button button_update;
    private Button btn_drop;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_tab8, container, false);
        button_update=view.findViewById(R.id.tab8_button_update);
        btn_drop=view.findViewById(R.id.tab8_button_conn);
        button_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateUtil updateUtil=new UpdateUtil(getContext(),"http://192.168.30.62:10086/APK");
                updateUtil.update();
            }

        });
        btn_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteHelper sqLiteHelper=new SQLiteHelper(getContext());
                sqLiteHelper.doExecSQL("delete from roc");
                sqLiteHelper.doClose();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
