package com.appers.ayvaz.thetravelingsalesman;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.database.TripCursorWrapper;
import com.appers.ayvaz.thetravelingsalesman.dialog.DatePickerFragment;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;
import com.appers.ayvaz.thetravelingsalesman.utils.DbBitmapUtility;
import com.appers.ayvaz.thetravelingsalesman.view.PhotoViewFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class TravelDetail extends AppCompatActivity implements PhotoViewFragment.OnFragmentInteractionListener {
//Spinner spinnerTravelClient;
    String[] clients = {"Client 0", "Client 1", "Client 2","Client 3", "Client 4", "Client 5","Client 6", "Client 7", "Client 8","Client 9", "Client 10", "Client 11","Client 12", "Client 13", "Client 14","Client 15", "Client 16", "Client 17"};
    ImageButton buttonDateFrom, buttonDateTo, cameraButton;
    Trip trip_main = null;
    Client selection=null;
    EditText editTravelFrom, editTravelTo, editTravelBoardingPass, editTravelDescription;
    static TextView textDateFrom, textDateTo;
    AutoCompleteTextView autoCompleteTextView;
    Button addExpense;

    int id_trip_main = -1;
    File photoFile, tempFile;

    RadioGroup radioGroup;
    RadioButton radPlane, radTrain, radCar;
    boolean radPlaneWasChecked, radTrainWasChecked, radCarWasChecked;

    String tripType;
    ClientManager clientManager;
    List<Client> clientList;
    static Calendar dateFrom;
    static Calendar dateTo;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Trip Add/Edit");
        addExpense = (Button) findViewById(R.id.travelDetailAddExpense);
        radPlane = (RadioButton) findViewById(R.id.buttonTravelPlane);
        radTrain = (RadioButton) findViewById(R.id.buttonTravelTrain);
        radCar = (RadioButton) findViewById(R.id.buttonTravelCar);
        radioGroup = (RadioGroup) findViewById(R.id.travelDetailRadioGroup);
        textDateFrom = (TextView) findViewById(R.id.travelDetailEditDateFrom);
        textDateTo = (TextView) findViewById(R.id.travelDetailEditDateTo);
        buttonDateFrom = (ImageButton) findViewById(R.id.travelDetailButtonCalenderFrom);
        buttonDateTo = (ImageButton) findViewById(R.id.travelDetailButtonCalenderTo);
        cameraButton = (ImageButton) findViewById(R.id.cameraTravelButton);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.travelDetailAutoCompleteTextView);
        editTravelDescription = (EditText) findViewById(R.id.travelDetailEditDescription);
        editTravelBoardingPass = (EditText) findViewById(R.id.travelDetailBoardingEdit);
        editTravelFrom = (EditText) findViewById(R.id.travelDetailOriginEdit);
        editTravelTo = (EditText) findViewById(R.id.travelDetailDestinationEdit);

       radPlane.setButtonDrawable(R.drawable.ic_travel_detail_plane_dark);
        radTrain.setButtonDrawable(R.drawable.ic_travel_detail_train_dark);
        radCar.setButtonDrawable(R.drawable.ic_travel_detail_car_dark);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radCar.getId()) {
                        tripType = "Road";

                        radCar.setButtonDrawable(R.drawable.ic_travel_detail_car);
                    radPlane.setButtonDrawable(R.drawable.ic_travel_detail_plane_dark);
                    radTrain.setButtonDrawable(R.drawable.ic_travel_detail_train_dark);

                } else if (checkedId == radPlane.getId()) {
                        tripType = "Air";
                         radPlane.setButtonDrawable(R.drawable.ic_travel_detail_plane);
                    radCar.setButtonDrawable(R.drawable.ic_travel_detail_car_dark);
                    radTrain.setButtonDrawable(R.drawable.ic_travel_detail_train_dark);

                } else if (checkedId == radTrain.getId()) {

                        tripType = "Rail";
                        radTrain.setButtonDrawable(R.drawable.ic_travel_detail_train);
                    radCar.setButtonDrawable(R.drawable.ic_travel_detail_car_dark);
                    radPlane.setButtonDrawable(R.drawable.ic_travel_detail_plane_dark);

                }

            }
        });


        dateFrom= Calendar.getInstance();
        dateTo = Calendar.getInstance();

        clientManager= ClientManager.get(getApplicationContext());
        clientList = clientManager.getClients();

        textDateFrom.setText(String.format("%tm/%td/%tY", dateFrom,dateFrom,dateFrom));
        textDateTo.setText(String.format("%tm/%td/%tY", dateTo, dateTo, dateTo));

        FragmentManager fm = getSupportFragmentManager();
        final android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MyFragment fragmentFrom = new MyFragment();
        fragmentTransaction.add(fragmentFrom,"HELPER").commit();


        ArrayAdapter<Client> adapter = new ArrayAdapter<Client>(this, android.R.layout.simple_dropdown_item_1line, clientList);
        autoCompleteTextView.setAdapter(adapter);

        Intent intentRecieved = getIntent();

        if(intentRecieved != null && intentRecieved.hasExtra("TRIP")){
            boolean original = false;
            if(savedInstanceState == null)
                original = true;

             loadData(getData(intentRecieved), (savedInstanceState == null));}
        else if(intentRecieved !=null && intentRecieved.hasExtra("CLIENT")){
            UUID clientUUID = UUID.fromString(intentRecieved.getStringExtra("CLIENT"));
            selection =clientManager.getClient(clientUUID);
            autoCompleteTextView.setText(selection.toString());
        }
        textDateFrom.setText(String.format("%tm/%td/%tY", dateFrom,dateFrom,dateFrom));
        textDateTo.setText(String.format("%tm/%td/%tY", dateTo, dateTo, dateTo));


        //String[] clientNames = loadClientList(clientList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, clientNames);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selection= (Client)parent.getItemAtPosition(position);
              //  Toast.makeText(getApplicationContext(), selection.getFirstName(),Toast.LENGTH_LONG).show();
            }
        });




        buttonDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragment fragment = (MyFragment) getSupportFragmentManager().findFragmentByTag("HELPER");
                fragment.showDialog(1);
                // fragmentFrom.showDialog(1);


            }
        });

        buttonDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragment fragment = (MyFragment) getSupportFragmentManager().findFragmentByTag("HELPER");
                fragment.showDialog(0);
                // fragmentFrom.showDialog(1);

            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveData(false)){
                TripContent tripContent = TripContent.get(getApplicationContext());
                showPhoto(tripContent.getPhotoFile(trip_main, false),tripContent.getPhotoFile(trip_main, true), false);}
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(saveData(savePhoto())) {
                    Intent intent = new Intent(getApplicationContext(), ExpenseAdd.class);
                    intent.putExtra("CLIENT", selection.getId().toString());
                    intent.putExtra("TRIP_ID", trip_main.getId());
                    startActivity(intent);
                }
            }
        });
        // spinnerTravelClient = (Spinner) findViewById(R.id.spinnerTravelAddClient);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,clients);
        //spinnerTravelClient.setAdapter(adapter);

    }
    public void RadioCheck(String tripType){
        switch (tripType){
            case "Road":
                radioGroup.check(radCar.getId());
                break;
            case "Rail":
                radioGroup.check(radTrain.getId());
                break;
            case "Air":
                radioGroup.check(radPlane.getId());
                break;

        }
    }
    public void showPhoto(File photoFile, File tempFile,boolean load)
    {
        //TripContent tripContent = TripContent.get(getApplicationContext());
        PhotoViewFragment photoViewFragment = PhotoViewFragment.newInstance(photoFile,tempFile,load);
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.trip_photo_fragment_container, photoViewFragment).commit();
    }


    private boolean savePhoto() {
        if (tempFile != null && tempFile.exists()) {
            if ((!photoFile.exists() || photoFile.delete()) && tempFile.renameTo(photoFile)) {
                Log.i("......", "saving photo" + tempFile.getAbsolutePath());
                return true;
            }
            Toast.makeText(getApplicationContext(), "Photo not saved", Toast.LENGTH_LONG).show();

        }
        return false;
    }

    public static void dateRecieved(Calendar date, int dateSel){
        if(dateSel == 1){
            dateFrom = date;
            textDateFrom.setText(String.format("%tm/%td/%tY", date, date, date));
        }
        else{
            dateTo = date;
            textDateTo.setText(String.format("%tm/%td/%tY", date,date,date));
        }
    }

    public String[] loadClientList(List<Client> clientList){

        int client_num= clientList.size();
        String[] client_Names= new String[client_num];

        int i = 0;
        for(Client client: clientList){

            client_Names[i] = client.getFirstName() + client.getLastName();

            i++;
        }
        return  client_Names;
    }

    public void loadData(Trip trip, boolean savedInstance){
        editTravelTo.setText(trip.getTrip_to());
        selection = clientManager.getClient(trip.getClient_id());
        if(selection!=null)
        autoCompleteTextView.setText(selection.toString());

        editTravelFrom.setText(trip.getTrip_from());
        editTravelBoardingPass.setText(trip.getBoarding());
        editTravelDescription.setText(trip.getDescription());
        //textDateFrom.setText(TripContent.CalendarToString(trip.getDate_from()));
        dateFrom=trip.getDate_from();
        //textDateTo.setText(TripContent.CalendarToString(trip.getDate_to()));
        dateTo = trip.getDate_to();
        tripType = trip.getType();
        if(tripType!=null)
        RadioCheck(tripType);

        trip_main = trip;
        id_trip_main = trip_main.getId();


        if(trip.getImageFile() != null) {
            photoFile = new File(trip.getImageFile());
        }
        if(savedInstance)
        showPhoto(TripContent.get(getApplicationContext()).getPhotoFile(trip_main, false),TripContent.get(getApplicationContext()).getPhotoFile(trip_main, true), true);

    }

    public Trip getData(Intent i){

        Trip trip = (Trip) i.getParcelableExtra("TRIP");
        return trip;
    }

        //last method to be run, if a Trip does not exist add. otherwise update
    public boolean saveData(boolean savePhoto){

        boolean edit = true;
        if(trip_main == null){
            trip_main= new Trip();
            edit = false;
        }
        if(selection != null && !autoCompleteTextView.getText().toString().equals(""))
        trip_main.setClient_id(selection.getId());
        else {
            autoCompleteTextView.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_text_red));
            trip_main = null;
            return false;
        }
        if(TripContent.compareCalendars(dateFrom,dateTo) != -1) {
            trip_main.setDate_from(dateFrom);
            trip_main.setDate_to(dateTo);
        }
        else{
            Toast.makeText(getApplicationContext(),"Error: Starting date is after ending date", Toast.LENGTH_LONG).show();
            trip_main = null;
            return false;
        }
        trip_main.setTrip_from(editTravelFrom.getText().toString());
        trip_main.setTrip_to(editTravelTo.getText().toString());
        trip_main.setDescription(editTravelDescription.getText().toString());
        trip_main.setBoarding(editTravelBoardingPass.getText().toString());
        trip_main.setType(tripType);

        if(savePhoto) {
            Log.i("......", "photo Final Save" + tempFile.getAbsolutePath());
            trip_main.setImageFile(photoFile.getAbsolutePath());
        }
        else if(photoFile == null || !photoFile.exists())
            trip_main.setImageFile(null);

        TripContent tripContent = TripContent.get(getApplicationContext());
        if(edit || trip_main.getId() != -1)
            tripContent.updateTrip(trip_main);
        else if(id_trip_main == -1){
             id_trip_main = tripContent.addTrip(trip_main);
            Log.i("......", "Added Row id: " + id_trip_main);
            trip_main.setId(id_trip_main);}
        else {
            Toast.makeText(getApplicationContext(), "Error: Save Error", Toast.LENGTH_LONG).show();
            trip_main = null;
            return false;
        }

        return true;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(2, 22, 100, "OK");
        //menu.findItem(11).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(22).setIcon(android.R.drawable.checkbox_on_background);
        menu.findItem(22).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            case 22:
                if(saveData(savePhoto())){
                Intent intent = new Intent(getApplicationContext(), TripExpMan.class);
                intent.putExtra("ORIGIN", "TRIP");
                intent.putExtra("CLIENT", selection.getId().toString());
                startActivity(intent);}

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(File photoFile, File tempFile) {
       this.photoFile = photoFile;
        this.tempFile = tempFile;

    }


    public static class MyFragment extends Fragment {


        int mStackLevel = 0;
        public static final int DIALOG_FRAGMENT = 1;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (savedInstanceState != null) {
                mStackLevel = savedInstanceState.getInt("level");
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt("level", mStackLevel);
        }

        void showDialog(int type) {

            mStackLevel++;

            android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            switch (type) {

                case DIALOG_FRAGMENT:{

                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(dateFrom);
                    datePickerFragment.setTargetFragment(this, DIALOG_FRAGMENT);
                    datePickerFragment.show(getFragmentManager().beginTransaction(), "dialog");

                    break;}
                case 0:{

                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(dateTo);
                    datePickerFragment.setTargetFragment(this, 0);
                    datePickerFragment.show(getFragmentManager().beginTransaction(), "dialog");
                    break;}
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch(requestCode) {
                case DIALOG_FRAGMENT:{

                    if (resultCode == Activity.RESULT_OK) {
                        Bundle bundle = new Bundle();
                        bundle = data.getExtras();
                        dateFrom = (Calendar) bundle.get("com.appers.avyaz.thetravelingsalesman.task.date");
                        textDateFrom.setText(String.format("%tm/%td/%tY", dateFrom, dateFrom, dateFrom));
                    } else if (resultCode == Activity.RESULT_CANCELED){
                        Toast.makeText(getActivity(),"Error Date From",Toast.LENGTH_LONG).show();
                    }

                    break;}
                case 0:{
                    if (resultCode == Activity.RESULT_OK) {
                        Bundle bundle = new Bundle();
                        bundle = data.getExtras();
                        Calendar tempDate = (Calendar) bundle.get("com.appers.avyaz.thetravelingsalesman.task.date");
                            dateTo = tempDate;
                            textDateTo.setText(String.format("%tm/%td/%tY", dateTo, dateTo, dateTo));
                    } else if (resultCode == Activity.RESULT_CANCELED){
                        Toast.makeText(getActivity(),"Error Date To",Toast.LENGTH_LONG).show();
                    }

                    break; }
            }
        }

    }


}




