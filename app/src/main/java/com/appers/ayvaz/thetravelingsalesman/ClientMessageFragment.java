package com.appers.ayvaz.thetravelingsalesman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.adapter.MessageAdapterSlow;
import com.appers.ayvaz.thetravelingsalesman.modell.Client;
import com.appers.ayvaz.thetravelingsalesman.modell.MessageBox;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientMessageFragment extends Fragment  implements ClientActivity.ClientChanged {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLIENT_ID = "clientId";
    private static final String ARG_NUMBER2 = "param2";
    private static final String ARG_NUMBER1 = "param1";
    //    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    RecyclerView mRecyclerView;
    //    int[] toViews = {0, R.id.sms_contact, R.id.sms_body};
    int[] toViews = {0, android.R.id.text1, android.R.id.text2};
    private String mNumber1 = "10086";
    private String mNumber2 = "7535";
    private UUID mClientId;
    private Client mClient;
    private MessageAdapterSlow mAdapter;


    public ClientMessageFragment() {
        // Required empty public constructor
    }

    public static ClientMessageFragment newInstance() {
        return new ClientMessageFragment();
    }

    public static ClientMessageFragment newInstance(UUID clientId) {
        ClientMessageFragment fragment = new ClientMessageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT_ID, clientId);

        fragment.setArguments(args);
        return fragment;
    }

    public static ClientMessageFragment newInstance(String num1, String num2) {
        ClientMessageFragment fragment = new ClientMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER1, num1);
        args.putString(ARG_NUMBER2, num2);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mClientId = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
//            mClient = ClientContent.get(getContext()).getClient(mClientId);
//            mNumber1 = mClient.getFirstPhone();
//            mNumber2 = mClient.getSecondPhone();

            mNumber1 = getArguments().getString(ARG_NUMBER1);
            mNumber2 = getArguments().getString(ARG_NUMBER2);
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
//        updateUI();
    }

    private void updateUI() {
//        if (mAdapter == null) {
        mAdapter = MessageBox.get(getContext()).query(mNumber1, mNumber2);
//        } else {
//            mAdapter.notifyDataSetChanged();
//        }

        mRecyclerView.setAdapter(mAdapter);
    }

    public void updateUI(Client client) {
        mNumber1 = client.getFirstPhone();
        mNumber2 = client.getSecondPhone();
        updateUI();
    }


}



