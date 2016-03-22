package com.simbiosyscorp.thetravelingsalesman.ui;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.adapter.MessageAdapterSlow;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.MessageBox;
import com.simbiosyscorp.thetravelingsalesman.models.MyMessage;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientMessageFragment extends Fragment  implements ClientActivity.ClientChanged {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CLIENT_ID = "clientId";
    private static final String ARG_NUMBER2 = "number1";
    private static final String ARG_NUMBER1 = "number2";
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.emptyView) View mEmptyView;
    @Bind(R.id.emptyTextView)
    TextView mEmptyTextView;
    @Bind(R.id.addNew)
    Button mAddNewButton;

    //    int[] toViews = {0, R.id.sms_contact, R.id.sms_body};
    int[] toViews = {0, android.R.id.text1, android.R.id.text2};
    private String mNumber1 = "10086";
    private String mNumber2 = "7535";

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
//            mClient = ClientManager.get(getContext()).getClient(mClientId);
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
        ButterKnife.bind(this, view);

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
        new UpdateMessageTask().execute();
    }

    public void updateUI(Client client) {
        mNumber1 = client.getFirstPhone();
        mNumber2 = client.getSecondPhone();
        updateUI();
    }

    private void displayEmptyView(boolean isEmpty) {
        if (isEmpty) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyTextView.setText(getActivity().getString(R.string.emptyList, "message"));
            mAddNewButton.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private class UpdateMessageTask extends AsyncTask<Void, Void, List<MyMessage>> {

        @Override
        protected List<MyMessage> doInBackground(Void... params) {
            List<MyMessage> messages= MessageBox.get(getContext()).getMessages(mNumber1, mNumber2);
            return messages;
        }

        @Override
        protected void onPostExecute(List<MyMessage> messages) {
            displayEmptyView(messages.isEmpty());

            if (mAdapter == null) {
                mAdapter = new MessageAdapterSlow(getContext(), messages);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setMessages(messages);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


}



