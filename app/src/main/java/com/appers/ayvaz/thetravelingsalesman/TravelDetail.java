package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appers.ayvaz.thetravelingsalesman.models.Client;
import com.appers.ayvaz.thetravelingsalesman.models.ClientManager;
import com.appers.ayvaz.thetravelingsalesman.models.Trip;
import com.appers.ayvaz.thetravelingsalesman.models.TripContent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TravelDetail extends AppCompatActivity {
//Spinner spinnerTravelClient;
    String[] clients = {"Client 0", "Client 1", "Client 2","Client 3", "Client 4", "Client 5","Client 6", "Client 7", "Client 8","Client 9", "Client 10", "Client 11","Client 12", "Client 13", "Client 14","Client 15", "Client 16", "Client 17"};
    ImageButton button;
    Trip trip_main = null;
    Client selection=null;
    EditText editTravelFrom, editTravelTo, editTravelBoardingPass, editTravelDescription;
    AutoCompleteTextView autoCompleteTextView;

    ClientManager clientManager;
    List<Client> clientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = (ImageButton) findViewById(R.id.cameraTravelButton);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.travelDetailAutoCompleteTextView);
        editTravelDescription = (EditText) findViewById(R.id.travelDetailEditDescription);
        editTravelBoardingPass = (EditText) findViewById(R.id.travelDetailBoardingEdit);
        editTravelFrom = (EditText) findViewById(R.id.travelDetailOriginEdit);
        editTravelTo = (EditText) findViewById(R.id.travelDetailDestinationEdit);

        clientManager= ClientManager.get(getApplicationContext());
        clientList = clientManager.getClients();

        ArrayAdapter<Client> adapter = new ArrayAdapter<Client>(this, android.R.layout.simple_dropdown_item_1line, clientList);
        autoCompleteTextView.setAdapter(adapter);

        Intent intentRecieved = getIntent();

        if(intentRecieved != null && intentRecieved.hasExtra("TRIP")){
             loadData(getData(intentRecieved));}
        else if(intentRecieved !=null && intentRecieved.hasExtra("CLIENT")){
            UUID clientUUID = UUID.fromString(intentRecieved.getStringExtra("CLIENT"));
            selection =clientManager.getClient(clientUUID);
            autoCompleteTextView.setText(selection.toString());
        }


        //String[] clientNames = loadClientList(clientList);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, clientNames);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selection= (Client)parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selection.getFirstName(),Toast.LENGTH_LONG).show();
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TripExpMan.class);
                startActivity(intent);
            }
        });
        // spinnerTravelClient = (Spinner) findViewById(R.id.spinnerTravelAddClient);
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,clients);
        //spinnerTravelClient.setAdapter(adapter);

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

    public void loadData(Trip trip){
        editTravelTo.setText(trip.getTrip_to());
        selection = clientManager.getClient(trip.getClient_id());
        autoCompleteTextView.setText(selection.toString());
        editTravelFrom.setText(trip.getTrip_from());
        editTravelBoardingPass.setText(trip.getBoarding());
        editTravelDescription.setText(trip.getDescription());
        trip_main = trip;
    }

    public Trip getData(Intent i){

        Trip trip = (Trip) i.getParcelableExtra("TRIP");
        return trip;
    }

        //last method to be run, if a Trip does not exist add. otherwise update
    public void saveData(){

        boolean edit = true;
        if(trip_main == null){
            trip_main= new Trip();
            edit = false;
        }
        //if(selection != null)
        trip_main.setClient_id(selection.getId());
        trip_main.setTrip_from(editTravelFrom.getText().toString());
        trip_main.setTrip_to(editTravelTo.getText().toString());
        trip_main.setDescription(editTravelDescription.getText().toString());
        trip_main.setBoarding(editTravelBoardingPass.getText().toString());

        TripContent tripContent = TripContent.get(getApplicationContext());
        if(edit)
            tripContent.updateTrip(trip_main);
        else
         tripContent.addTrip(trip_main);

        Intent intent = new Intent(getApplicationContext(), TripExpMan.class);
        intent.putExtra("ORIGIN", "TRIP");
        intent.putExtra("CLIENT", selection.getId().toString());
        startActivity(intent);


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
                saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


        //return super.onOptionsItemSelected(item);
    }

}
