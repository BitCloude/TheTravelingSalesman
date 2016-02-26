package com.appers.ayvaz.thetravelingsalesman.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appers.ayvaz.thetravelingsalesman.R;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpenseListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpenseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "EXPENSE";
    TextView date, expense_trip,amount,type,description,clientText ;

    // TODO: Rename and change types of parameters
    private Expense mExpense;

    private OnFragmentInteractionListener mListener;

    public ExpenseListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param expense Parameter 1.
     * @return A new instance of fragment ExpenseListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseListFragment newInstance(Expense expense) {
        ExpenseListFragment fragment = new ExpenseListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mExpense = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_expense_list, container, false);
        date = (TextView) v.findViewById(R.id.fragment_expense_date);
        expense_trip = (TextView) v.findViewById(R.id.fragment_expense_trip);
        description = (TextView) v.findViewById(R.id.fragment_expense_description);
        amount = (TextView) v.findViewById(R.id.fragment_expense_amount);
        type = (TextView) v.findViewById(R.id.fragment_expense_type);
        clientText = (TextView) v.findViewById(R.id.fragment_expense_client);

        Client client = ClientManager.get(getActivity()).getClient(mExpense.getClient_id());

        clientText.setText(client.toString());

        Calendar dateFrom = mExpense.getDate_from();
        if(dateFrom != null)
        date.setText(String.format("%tm/%td/%tY", dateFrom, dateFrom, dateFrom));
        else
        date.setText(null);
        Trip trip = TripContent.get(getActivity()).getTrip(mExpense.getTrip_id());
        if(trip!= null){
        expense_trip.setText(trip.toString());}
        else{
        expense_trip.setText(null);}
        amount.setText("$" + mExpense.getAmount());
        description.setText(mExpense.getDescription());
        type.setText(mExpense.getType());

        v.setTag(mExpense);
        if (mListener != null) {
            mListener.onFragmentInteraction(v);
        }

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(View view);
    }
}
