package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.appers.ayvaz.thetravelingsalesman.models.Expense;
import com.appers.ayvaz.thetravelingsalesman.models.ExpenseContent;

public class ExpenseAdd extends AppCompatActivity {
Spinner spinner, spinner2;
    String[] clients = {"Client 0", "Client 1", "Client 2","Client 3", "Client 4", "Client 5","Client 6", "Client 7", "Client 8","Client 9", "Client 10", "Client 11","Client 12", "Client 13", "Client 14","Client 15", "Client 16", "Client 17"};
Button button;
    EditText editAmount, editDescription;
    ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageButton = (ImageButton) findViewById(R.id.cameraButton);
        editAmount = (EditText) findViewById(R.id.EditAmount);
        editDescription = (EditText) findViewById(R.id.expenseAddDescription);
        spinner = (Spinner) findViewById(R.id.spinnerTripSelect);
        spinner2 = (Spinner) findViewById(R.id.spinnerExpenseType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,clients);
        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        if(getIntent() != null && getIntent().hasExtra("EXPENSE"))
            loadData(getData(getIntent()));


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Travel.class);
                startActivity(intent);
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
                saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //return super.onOptionsItemSelected(item);
    }


    public void loadData(Expense expense) {
        editAmount.setText(expense.getAmount());
        editDescription.setText(expense.getDescription());
    }


    public Expense getData(Intent i){

        Expense expense = i.getParcelableExtra("EXPENSE");
        return expense;
    }


    public void saveData(){

        Expense mExpense = new Expense();
        mExpense.setAmount(editAmount.getText().toString());
        mExpense.setDescription(editDescription.getText().toString());
      //  mTrip.setBoarding(editTravelBoardingPass.getText().toString());
        //mTrip.setTrip_to(editTravelTo.getText().toString());


        ExpenseContent expenseContent = ExpenseContent.get(getApplicationContext());
        expenseContent.addExpense(mExpense);

        Intent intent = new Intent(getApplicationContext(), TripExpMan.class);
        intent.putExtra("ORIGIN", "EXPENSE");
        startActivity(intent);


    }
}
