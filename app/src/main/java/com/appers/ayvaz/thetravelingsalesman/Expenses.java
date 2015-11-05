package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Expenses extends AppCompatActivity {
Spinner spinnerClient, spinnerAmount, spinnerFrom;
    ListView listViewExpenses;
    String[] client = {"Client 0", "Client 1", "Client 2","Client 3", "Client 4", "Client 5","Client 6", "Client 7", "Client 8","Client 9", "Client 10", "Client 11","Client 12", "Client 13", "Client 14","Client 15", "Client 16", "Client 17"};
    Integer[] amount = {1,5,10,20,50,100,500,1000};
    String[] from= {"Monday", "Tuesday", "Wednesday","Thursday", "Friday", "Saturday","Sunday"};
    String[] expenses= {"Client 1:10", "Client 2: 5", "Client 3: 20","Client 4: 100", "Client 5: 400", "Client 6: 1", "Client 7: 50", "Client 8: 24", "Client 9: 70", "Client 10: 13"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinnerClient = (Spinner) findViewById(R.id.spinnerExpenseClient);
        spinnerAmount = (Spinner) findViewById(R.id.spinnerExpenseAmount);
        spinnerFrom = (Spinner) findViewById(R.id.spinnerExpenseFrom);
        listViewExpenses = (ListView) findViewById(R.id.listViewExpenses);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,client);
        spinnerClient.setAdapter(adapter);
        ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,amount);
        spinnerAmount.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,from);
        spinnerFrom.setAdapter(adapter3);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,expenses);
        listViewExpenses.setAdapter(adapter4);

        listViewExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ExpenseAdd.class);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
