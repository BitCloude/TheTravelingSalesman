package com.simbiosyscorp.thetravelingsalesman.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simbiosyscorp.thetravelingsalesman.R;
import com.simbiosyscorp.thetravelingsalesman.models.Client;
import com.simbiosyscorp.thetravelingsalesman.models.ClientManager;
import com.simbiosyscorp.thetravelingsalesman.models.Trip;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TripsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripsListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "TRIP";
   // private static final String ARG_PARAM2 = "param2";
   TextView date, trip_from_to,description, clientText ;
    ImageView type;

    // TODO: Rename and change types of parameters
    private Trip mTrip;

    private OnFragmentInteractionListener mListener;

    public TripsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trip Parameter 1.
     * @return A new instance of fragment TripsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TripsListFragment newInstance(Trip trip) {
        TripsListFragment fragment = new TripsListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, trip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrip = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_trips_list, container, false);
        date = (TextView) v.findViewById(R.id.fragment_trips_date);
        trip_from_to = (TextView) v.findViewById(R.id.fragment_trips_from);
        description = (TextView) v.findViewById(R.id.fragment_trips_description);
        type = (ImageView) v.findViewById(R.id.fragment_trips_type);
        clientText = (TextView) v.findViewById(R.id.fragment_trips_client);

        Client client = ClientManager.get(getActivity()).getClient(mTrip.getClient_id());

        clientText.setText(client.toString());
        Calendar dateFrom = mTrip.getDate_from();
        date.setText(String.format("%tm/%td/%tY", dateFrom, dateFrom, dateFrom));
        //trip_from_to.setText("From " + mTrip.getTrip_from() + " to " + mTrip.getTrip_to());
        String locFrom = mTrip.getTrip_from();
        String locTo = mTrip.getTrip_to();
        if(locFrom.length() > 13){
            locFrom = locFrom.substring(0,13) +"...";
        }
        if(locTo.length() > 13){
            locTo = locTo.substring(0,13) +"...";
        }
        trip_from_to.setText(locFrom + " to " + locTo);
        description.setText(mTrip.getDescription());
        if (mTrip.getType() != null) {
        switch (mTrip.getType()){
            case "Road":
               // int car = getResources().getIdentifier("drawable/ic_travel_car_white", null, null);
                type.setImageResource(R.drawable.ic_car_white);
                type.setVisibility(View.VISIBLE);
                break;
            case "Rail":
                //int train = getResources().getIdentifier("drawable/ic_travel_train", null, null);
                type.setImageResource(R.drawable.ic_train_white);
                type.setVisibility(View.VISIBLE);
                break;
            case "Air":
                //int plane = getResources().getIdentifier("drawable/ic_travel_plane_light", null, null);
                type.setImageResource(R.drawable.ic_plane_white);
                type.setVisibility(View.VISIBLE);
                break;
        }

        }else{
            type.setVisibility(View.INVISIBLE);
        }
        v.setTag(mTrip);
        if (mListener != null) {
            mListener.onFragmentInteraction(v);
        }


        return v;
    }

   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
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
