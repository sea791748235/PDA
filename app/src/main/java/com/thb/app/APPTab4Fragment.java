package com.thb.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link APPTab4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class APPTab4Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_test;

    private String[] titles = {"扫码报工", "历史记录", "设    置", "扫码上架", "整箱转移", "散箱转移", "扫码盘点",
            "销售出库", "包装检验", "整箱转移",
            "散箱转移"};
    private int images[] = {R.mipmap.ic_smbg_black_24dp, R.mipmap.ic_tjxx_black_24dp, R.mipmap.ic_sz_black_24dp, R.mipmap.ic_smsz_black_24dp, R.mipmap.ic_launcher, R.mipmap.ic_sxzy_black_24dp, R.mipmap.ic_smpd_black_24dp,
            R.mipmap.ic_xsck_black_24dp, R.mipmap.ic_bzjy_black_24dp, R.mipmap.ic_zxkwzy_black_24dp,
            R.mipmap.ic_sxkwzy_black_24dp};

    private GridView tab4Gv;
    private List list;
    private SimpleAdapter simpleAdapter;

    public APPTab4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment APPTab4Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static APPTab4Fragment newInstance(String param1, String param2) {
        APPTab4Fragment fragment = new APPTab4Fragment();
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
        View view = inflater.inflate(R.layout.fragment_app_tab4, container, false);
        tab4Gv=view.findViewById(R.id.tab4_gv);
        list=new ArrayList<Map>();
        for(int i=0;i<titles.length;i++){
            Map map=new HashMap();
            map.put("title",titles[i]);
            map.put("image",images[i]);
            list.add(map);
        }
        String [] from={"image","title"};

        int[] to={R.id.grid_iv,R.id.grid_tv};
        simpleAdapter=new SimpleAdapter(getContext(),list,R.layout.grid_item,from,to);
        tab4Gv.setAdapter(simpleAdapter);

        tab4Gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==1){
                    ((APPMainActivity)getActivity()).setMode(1);
                }
            }
        });
        return view;
    }

    private SpannableStringBuilder addClickPart(String str) {
        ImageSpan imgspan = new ImageSpan(getContext(), R.mipmap.ic_launcher);
        SpannableString spanStr = new SpannableString("p.");
        spanStr.setSpan(imgspan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder ssb = new SpannableStringBuilder(spanStr);
        ssb.append(str);
        String[] likeUsers = str.split(",");
        if (likeUsers.length > 0) {
            for (int i = 0; i < likeUsers.length; i++) {
                final String name = likeUsers[i];
                final int start = str.indexOf(name) + spanStr.length();
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLUE);
                        ds.setUnderlineText(false);
                    }
                }, start, start + name.length(), 0);

            }
        }
        return ssb.append("等" + likeUsers.length + "个人觉得很赞");
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
