package com.simbiosyscorp.thetravelingsalesman.ui;


import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.CallEntry;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.MessageBox;
import com.simbiosyscorp.thetravelingsalesman.utils.DateTimeHelper;
import com.simbiosyscorp.thetravelingsalesman.view.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientCallLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientCallLogFragment extends Fragment implements ClientActivity.ClientChanged{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NUMBER1 = "param1";
    private static final String ARG_NUMBER2 = "param2";


    private String mNumber1 = "10086";
    private String mNumber2 = "95533";

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
//    RecyclerView mRecyclerView;

    //    int[] toViews = {0, R.id.sms_contact, R.id.sms_body};

    CallLogAdapter mAdapter;


    public ClientCallLogFragment() {
        // Required empty public constructor
    }

    public static ClientCallLogFragment newInstance() {
        return new ClientCallLogFragment();
    }

    public static ClientCallLogFragment newInstance(String number1, String number2) {
        ClientCallLogFragment fragment = new ClientCallLogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NUMBER1, number1);
        args.putString(ARG_NUMBER2, number2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(true);
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        List<CallEntry> callLog = MessageBox.get(getContext()).getCallLog(mNumber1, mNumber2);
        if (mAdapter == null) {
            mAdapter = new CallLogAdapter(callLog);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(callLog);
            mAdapter.notifyDataSetChanged();
        }

    }


    @Override
    public void updateUI(Client client) {
        mNumber1 = client.getFirstPhone();
        mNumber2 = client.getSecondPhone();
        updateUI();
    }


    /**
     * Adapter and View Holder
     *
     * */


    class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.ViewHolder> {

        private List<CallEntry> mCallLog;

        public CallLogAdapter(List<CallEntry> list) {
            mCallLog = list;
        }


        public void setData(List<CallEntry> log) {
            mCallLog = log;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_call_log_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            CallEntry entry = mCallLog.get(position);
            holder.callNumber.setText(entry.getNumber());
            holder.callTime.setText(DateTimeHelper.formatTime(entry.getTime()));
            int resId;
            switch (entry.getType()) {

                case CallLog.Calls.INCOMING_TYPE:
                    resId = R.drawable.ic_call_received_blue_700_24dp;
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    resId = R.drawable.ic_call_missed_red_800_24dp;
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    resId = R.drawable.ic_call_made_green_700_24dp;
                    break;
                default:
                    return;
            }

            holder.callType.setImageResource(resId);
        }

        @Override
        public int getItemCount() {
            return mCallLog.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.callTime)    TextView callTime;
            @Bind(R.id.callType)    ImageView callType;
            @Bind(R.id.callNumber) TextView callNumber;
            //// TODO: 008 01/08 number type(eg. home, work)
            @Bind(R.id.numberType) TextView numberType;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}






