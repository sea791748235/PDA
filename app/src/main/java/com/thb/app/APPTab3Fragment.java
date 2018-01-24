package com.thb.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.thb.ui.ElistChildBean;
import com.thb.ui.ElistGroupBean;
import com.thb.ui.ElistViewAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link APPTab3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class APPTab3Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ExpandableListView eListView;
    private ElistViewAdapter elistViewAdapter;
    private List<ElistGroupBean> groupList;
    private List<List<ElistChildBean>> childLList;

    public APPTab3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment APPTab3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static APPTab3Fragment newInstance(String param1, String param2) {
        APPTab3Fragment fragment = new APPTab3Fragment();
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
        View view=inflater.inflate(R.layout.fragment_app_tab3, container, false);
        init(view);
        loadData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onDetach() {
        super.onDetach();

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

    private void init(View view){
        eListView=view.findViewById(R.id.tab3_elv);
        groupList=new ArrayList<>();
        childLList=new ArrayList<>();
        elistViewAdapter =new ElistViewAdapter(getContext(),groupList,childLList);
        eListView.setAdapter(elistViewAdapter);

        eListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                if(expandableListView.isGroupExpanded(i)){
                    expandableListView.collapseGroup(i);
                }else {
                    expandableListView.expandGroup(i,true);
                }
                return true;
            }
        });
    }

    private void loadData(){
        List<String> group=new ArrayList<>();
        group.add("group1");
        group.add("group2");
        group.add("group3");

        for(int i=0;i<group.size();i++){
            ElistGroupBean gb=new ElistGroupBean(group.get(i),(i+2)+"/"+(2*i+2));
            groupList.add(gb);
        }

        for(int i=0;i<group.size();i++){
            List<ElistChildBean> list=new ArrayList<>();
            for(int j=0;j<2*i+2;j++){
                ElistChildBean cb=null;
                if(i==0){
                    cb=new ElistChildBean("child1-1","child1-2","child1-3");
                    list.add(cb);
                    cb=new ElistChildBean("child2-1","child2-2","child2-3");
                    list.add(cb);
                    break;
                }else {
                    cb=new ElistChildBean("child3-1","child3-2","child3-3");
                    list.add(cb);
                }
            }
            childLList.add(list);
        }
    }
}
