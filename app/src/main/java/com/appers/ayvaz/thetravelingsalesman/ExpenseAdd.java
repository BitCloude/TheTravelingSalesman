package com.appers.ayvaz.thetravelingsalesman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.dialog.DatePickerFragment;
import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseContent;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;
import com.appers.ayvaz.thetravelingsalesman.view.PhotoViewFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ExpenseAdd extends AppCompatActivity implements PhotoViewFragment.OnFragmentInteractionListener{
Spinner spinnerType;
    String[] expenseTypesAr = {"Travel Bill", "Hotel Bill", "Restaurant Bill","Gift", "Other"};
    ImageButton buttonDateFrom, buttonDateTo, buttonCamera,buttonAddTravel;
    EditText editAmount, editDescription;
    static TextView textDateFrom, textDateTo;
    String expenseType;
    ClientManager clientManager;
    List<Client> clientList;
    TripContent tripContent;
    List<Trip> tripList;
    static Calendar dateFrom;
    static Calendar dateTo;
    AutoCompleteTextView autoCompleteTextViewClients, autoCompleteTextViewTrips;

    Expense expense_main;
    Client selectedClient;
    Trip selectedTrip;

    byte[] imageFinal = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonCamera = (ImageButton) findViewById(R.id.cameraButton);
        buttonDateFrom = (ImageButton) findViewById(R.id.ButtonCalenderFrom);
        buttonDateTo = (ImageButton) findViewById(R.id.ButtonCalenderTo);
        buttonAddTravel = (ImageButton) findViewById(R.id.buttonAddTravel);
        editAmount = (EditText) findViewById(R.id.EditAmount);
        editDescription = (EditText) findViewById(R.id.expenseAddEditDescription);
        //spinner = (Spinner) findViewById(R.id.spinnerTripSelect);
        spinnerType = (Spinner) findViewById(R.id.spinnerExpenseType);
        textDateFrom = (TextView) findViewById(R.id.EditDateFrom);
        textDateTo = (TextView) findViewById(R.id.EditDateTo);
        autoCompleteTextViewClients = (AutoCompleteTextView) findViewById(R.id.expenseAddClientName);
        autoCompleteTextViewTrips = (AutoCompleteTextView) findViewById(R.id.expenseAddSelectTrip);

        ArrayAdapter<String> adapterExpenseType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,expenseTypesAr);

        spinnerType.setAdapter(adapterExpenseType);

        clientManager= ClientManager.get(getApplicationContext());
        clientList = clientManager.getClients();
        ArrayAdapter<Client> adapterClients = new ArrayAdapter<Client>(this, android.R.layout.simple_dropdown_item_1line, clientList);
        autoCompleteTextViewClients.setAdapter(adapterClients);

        tripContent= TripContent.get(getApplicationContext());

        dateTo = Calendar.getInstance();
        dateFrom = Calendar.getInstance();

        textDateFrom.setText(String.format("%tm/%td/%tY", dateFrom,dateFrom,dateFrom));
        textDateTo.setText(String.format("%tm/%td/%tY", dateTo, dateTo, dateTo));

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MyFragmentExp fragmentFrom = new MyFragmentExp();
        fragmentTransaction.add(fragmentFrom,"HELPER_EXP").commit();


        Intent intentRecieved = getIntent();
        if(intentRecieved != null && intentRecieved.hasExtra("EXPENSE"))
            loadData(getData(intentRecieved));
        else if(intentRecieved !=null && intentRecieved.hasExtra("CLIENT")){
            UUID clientUUID = UUID.fromString(intentRecieved.getStringExtra("CLIENT"));
            selectedClient =clientManager.getClient(clientUUID);
            if(selectedClient!=null){
            clientSelected();
            autoCompleteTextViewClients.setText(selectedClient.toString());
            if(intentRecieved.hasExtra("TRIP_ID"))
            {
                selectedTrip = tripContent.getTrip(intentRecieved.getIntExtra("TRIP_ID",0));
                if(selectedTrip!=null){
                    autoCompleteTextViewTrips.setText(selectedTrip.toString());
                }
            }}
        }
        textDateFrom.setText(String.format("%tm/%td/%tY", dateFrom,dateFrom,dateFrom));
        textDateTo.setText(String.format("%tm/%td/%tY", dateTo, dateTo, dateTo));

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expenseType = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        autoCompleteTextViewClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedClient= (Client)parent.getItemAtPosition(position);
                clientSelected();
                Toast.makeText(getApplicationContext(), selectedClient.getFirstName(),Toast.LENGTH_LONG).show();
            }
        });

        autoCompleteTextViewTrips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTrip= (Trip)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selectedTrip.toString(),Toast.LENGTH_LONG).show();
            }
        });

        buttonDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentExp fragment = (MyFragmentExp) getSupportFragmentManager().findFragmentByTag("HELPER_EXP");
                fragment.showDialog(1);
                // fragmentFrom.showDialog(1);


            }
        });

        buttonDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentExp fragment = (MyFragmentExp) getSupportFragmentManager().findFragmentByTag("HELPER_EXP");
                fragment.showDialog(0);
                // fragmentFrom.showDialog(1);

            }
        });


        buttonAddTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TravelDetail.class);
                if(selectedClient!=null)
                {
                    intent.putExtra("CLIENT", selectedClient.getId().toString());
                }
                startActivity(intent);
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhoto(imageFinal, true);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(1, 11, 100, "OK");
        //menu.findItem(11).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(11).setIcon(android.R.drawable.checkbox_on_background);
        menu.findItem(11).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       // int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            case 11:
                if(saveData()){
                    Intent intent = new Intent(getApplicationContext(), TripExpMan.class);
                    intent.putExtra("ORIGIN", "EXPENSE");
                    intent.putExtra("CLIENT", selectedClient.getId().toString());
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }

    public void spinnerSet(){

        if(expenseType.equals("Travel Bill"))
            spinnerType.setSelection(0);
        else if(expenseType.equals("Hotel Bill"))
            spinnerType.setSelection(1);
        else if(expenseType.equals("Restaurant Bill"))
            spinnerType.setSelection(2);
        else if(expenseType.equals("Gift"))
            spinnerType.setSelection(3);
        else
            spinnerType.setSelection(4);
    }
    public void clientSelected(){
        if(selectedClient!=null){
        tripList = tripContent.getClientTrips(selectedClient.getId());
        ArrayAdapter<Trip> adapterTrip = new ArrayAdapter<Trip>(this, android.R.layout.simple_dropdown_item_1line, tripList);
        autoCompleteTextViewTrips.setAdapter(adapterTrip);}
    }

    public void showPhoto(byte[] imageByte, boolean addImage)
    {
        ExpenseContent expenseContent = ExpenseContent.get(getApplicationContext());
        PhotoViewFragment photoViewFragment = PhotoViewFragment.newInstance(imageByte,addImage);
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.expense_photo_fragment_container, photoViewFragment).commit();
    }

    public void loadData(Expense expense) {
        editAmount.setText(expense.getAmount());
        editDescription.setText(expense.getDescription());
        selectedClient = clientManager.getClient(expense.getClient_id());
        selectedTrip = tripContent.getTrip(expense.getTrip_id());
        if(selectedClient!=null){
            autoCompleteTextViewClients.setText(selectedClient.toString());
            clientSelected();}
        if(selectedTrip!=null)
            autoCompleteTextViewTrips.setText(selectedTrip.toString());

        //textDateFrom.setText(ExpenseContent.CalendarToString(expense.getDate_from()));
        dateFrom=expense.getDate_from();
        //textDateTo.setText(ExpenseContent.CalendarToString(expense.getDate_to()));
        dateTo = expense.getDate_to();
        expenseType = expense.getType();
        spinnerSet();
        imageFinal = expense.getImage();
        expense_main = expense;
        showPhoto(imageFinal,false);
    }


    public Expense getData(Intent i){

        Expense expense = i.getParcelableExtra("EXPENSE");
        return expense;
    }


    public boolean saveData(){

        boolean edit = true;
        if(expense_main == null){
            expense_main= new Expense();
            edit = false;
        }
        if(selectedClient != null && !autoCompleteTextViewClients.getText().toString().equals(""))
            expense_main.setClient_id(selectedClient.getId());
        else {
            autoCompleteTextViewClients.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.hint_text_red));
            return false;
        }


        if(TripContent.compareCalendars(dateFrom,dateTo) != -1) {
            expense_main.setDate_from(dateFrom);
            expense_main.setDate_to(dateTo);
        }
        else{
            Toast.makeText(getApplicationContext(),"Error: Starting date is after ending date", Toast.LENGTH_LONG).show();
            return false;
        }
        expense_main.setAmount(editAmount.getText().toString());
        expense_main.setDescription(editDescription.getText().toString());
        expense_main.setClient_id(selectedClient.getId());
        if(selectedTrip!=null && !autoCompleteTextViewTrips.getText().toString().equals(""))
        expense_main.setTrip_id(selectedTrip.getId());
        else
            expense_main.setTrip_id(0);
        expense_main.setType(expenseType);

      //  mTrip.setBoarding(editTravelBoardingPass.getText().toString());
        //mTrip.setTrip_to(editTravelTo.getText().toString());
        expense_main.setImage(imageFinal);

        ExpenseContent expenseContent = ExpenseContent.get(getApplicationContext());
        if(edit)
            expenseContent.updateExpense(expense_main);
        else
            expenseContent.addExpense(expense_main);


        return  true;

    }

    @Override
    public void onFragmentInteraction(byte[] image) {
        this.imageFinal = image;
    }

    public static class MyFragmentExp extends Fragment {


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
                        Toast.makeText(getActivity(), "Error Date From", Toast.LENGTH_LONG).show();
                    }

                    break;}
                case 0:{
                    if (resultCode == Activity.RESULT_OK) {
                        Bundle bundle = new Bundle();
                        bundle = data.getExtras();
                       dateTo = (Calendar) bundle.get("com.appers.avyaz.thetravelingsalesman.task.date");
                            textDateTo.setText(String.format("%tm/%td/%tY", dateTo, dateTo, dateTo));

                    } else if (resultCode == Activity.RESULT_CANCELED){
                        Toast.makeText(getActivity(),"Error Date To",Toast.LENGTH_LONG).show();
                    }

                    break; }
            }
        }

    }

}
