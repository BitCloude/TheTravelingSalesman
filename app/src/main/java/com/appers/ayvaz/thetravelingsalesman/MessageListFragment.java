package com.appers.ayvaz.thetravelingsalesman;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import com.appers.ayvaz.thetravelingsalesman.adapter.MessageAdapterSlow;
import com.appers.ayvaz.thetravelingsalesman.Model.MessageBox;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1 = "10086";
    private String mParam2 = "95533";

//    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    RecyclerView mRecyclerView;

    //    int[] toViews = {0, R.id.sms_contact, R.id.sms_body};
    int[] toViews = {0, android.R.id.text1, android.R.id.text2 };
    ContentResolver cr;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    public MessageListFragment() {
        // Required empty public constructor
    }

    public static MessageListFragment newInstance() {
        return new MessageListFragment();
    }

    // TODO: Rename and change types and number of parameters
    public static MessageListFragment newInstance(String param1, String param2) {
        MessageListFragment fragment = new MessageListFragment();
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
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
//        ButterKnife.bind(this, view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        MessageAdapterSlow messageAdapterSlow = MessageBox.get(getContext()).query(mParam1, mParam2);
        mRecyclerView.setAdapter(messageAdapterSlow);
    }



}



