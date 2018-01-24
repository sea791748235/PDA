package com.thb.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.thb.qr.QRResultBean;
import com.thb.qr.QRReusltListAdapter;
import com.thb.qr.QRUtil;
import com.thb.ws.WSUtil;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * create an instance of this fragment.
 */
public class APPTab2Fragment extends Fragment {

    private static final String URL="http://192.168.9.91/BaanWebService/BaanWebService.asmx?wsdl";
    private static final String NAMESPACE="http://tempuri.org/";
    private static final String METHOD="ReportOrdersCompleted";

    private QRUtil qrUtil=new QRUtil();

    private Barcode2DWithSoft mReader;

    private ArrayList<String> paramsList=new ArrayList<>();

    private ArrayList<HashMap<String,String>> allList=new ArrayList<>();
    private HashMap<String,String> paramsMap=new HashMap<>();
    private HashMap<String,String> activationMap=new HashMap<>();

    private String qrResult;
    private String wsResult;
    private String buttonStr;

    private int interval;

    private boolean isThreadStop=true;
    private boolean isContinue=true;
    private boolean isCurrFrag=true;

    private EditText et_dd;
    private EditText et_ck;
    private EditText et_wl;
    private EditText et_sm;
    private EditText et_wjsl;
    private EditText et_sl;

    private Button b_sm;
    private Button b_tj;
    private Button b_qk;
    private Button b_act4;


    private ScrollView scroll;
    private TextView text;

    private ListView listView;
    private LinkedList<QRResultBean> mData=null;
    private QRReusltListAdapter mAdapter=null;
    private Context mContext=null;
    private int i=1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public APPTab2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment APPTab2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static APPTab2Fragment newInstance(String param1, String param2) {
        APPTab2Fragment fragment = new APPTab2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_app_tab2,container,false);

        et_dd=view.findViewById(R.id.tab2_et_dd);
        et_ck=view.findViewById(R.id.tab2_et_ck);
        et_wl=view.findViewById(R.id.tab2_et_wl);
        et_sm=view.findViewById(R.id.tab2_et_sm);
        et_wjsl=view.findViewById(R.id.tab2_et_wjsl);
        et_sl=view.findViewById(R.id.tab2_et_sl);

        et_wjsl.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        b_sm=view.findViewById(R.id.tab2_b_sm);
        b_tj=view.findViewById(R.id.tab2_b_tj);
        b_qk=view.findViewById(R.id.tab2_b_qk);
        b_act4=view.findViewById(R.id.tab2_b_act4);

        //scroll=view.findViewById(R.id.tab2_scroll_result);
        //text=view.findViewById(R.id.tab2_text_result);


        listView=(ListView)view.findViewById(R.id.tab2_elv);
        mData=new LinkedList<QRResultBean>();

        mAdapter=new QRReusltListAdapter((LinkedList<QRResultBean>) mData,getContext());
        listView.setAdapter(mAdapter);


        //qrUtil.setQR(getContext(),mReader,true,1000);

        b_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //paramsList.addAll(qrUtil.getQRList());
                //b_sm.setText(qrUtil.doDecode());
                Log.e("++++++++++++++",paramsList.toString());

                Iterator iterator=paramsList.iterator();
                while (iterator.hasNext()){
                    doBaan(iterator.next().toString().trim());
                }
            }
        });

        b_tj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //doBaan();

            }
        });

        b_qk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                doClear();
            }
        });

        b_act4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void doBaan(String str){

        String method="ReportOrdersCompleted";
        HashMap<String,String> activationMap=new HashMap<String,String>();
        final HashMap<String,String> paramsMap=new HashMap<>();

        activationMap.put("username",((APPMainActivity)getActivity()).getUsername());
        activationMap.put("password",(((APPMainActivity) getActivity()).getPassword()));
        activationMap.put("company",((APPMainActivity)getActivity()).getCompany());

        Log.e("doBaan+++++++++++++++",paramsList.toString());

        SimpleDateFormat dateFormat1=new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:SS");
        SimpleDateFormat dateFormat2=new SimpleDateFormat("mm:ss:SS");
        Date date=new Date(System.currentTimeMillis());
        String dateStr1=dateFormat1.format(date);
        String dataStr2=dateFormat2.format(date);

        paramsMap.put("Unique",dataStr2);
        paramsMap.put("ProductionOrder",str);
        paramsMap.put("QuantityToDeliver","1");
        paramsMap.put("LotCode","");

        WSUtil.CallWS(null,null,METHOD,activationMap, paramsMap, new WSUtil.WSCallBack() {
            @Override
            public void callBack(String result) {

                wsResult=result;
                Log.e("###################",wsResult);
                Log.e("编号",i+"");
                mAdapter.add(new QRResultBean(i+"",paramsMap.get("Unique"),paramsMap.get("ProductionOrder"),
                        paramsMap.get("QuantityToDeliver"),paramsMap.get("LotCode"),wsResult));
                listView.setAdapter(mAdapter);
                i++;
            }
        });
    }

    public void doClear(){
        et_dd.setText("");
        et_ck.setText("");
        et_wl.setText("");
        et_sm.setText("");
        et_wjsl.setText("");
        et_sl.setText("");

        //qrUtil.clearQR();
        paramsList.clear();
        paramsMap.clear();
        wsResult="";

        mData=new LinkedList<QRResultBean>();
        mAdapter=new QRReusltListAdapter((LinkedList<QRResultBean>) mData,getContext());
        listView.setAdapter(mAdapter);
    }
}
