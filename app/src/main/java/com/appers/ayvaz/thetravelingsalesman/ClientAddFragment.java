package com.appers.ayvaz.thetravelingsalesman;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appers.ayvaz.thetravelingsalesman.Model.Client;
import com.appers.ayvaz.thetravelingsalesman.Model.DummyContent;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientAddFragment extends Fragment {
    // parameter arguments, choose names that match
    private static final String ARG_CLIENT_ID = "client_id";

    private int mClientId;
    private Client mClient;



    public ClientAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ClientAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientAddFragment newInstance(int clientId) {
        ClientAddFragment fragment = new ClientAddFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLIENT_ID, clientId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mClientId = getArguments().getInt(ARG_CLIENT_ID);
            mClient = DummyContent.getItem(mClientId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_add, container, false);
        mClient.setText(view);


        return view;
    }

}
