package com.appers.ayvaz.thetravelingsalesman;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
FloatingActionButton fabAddClient;
    ImageButton button;
    ListView listViewClient;
    String[] clients = {"Client 0", "Client 1", "Client 2","Client 3", "Client 4", "Client 5","Client 6", "Client 7", "Client 8","Client 9", "Client 10", "Client 11","Client 12", "Client 13", "Client 14","Client 15", "Client 16", "Client 17"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewClient = (ListView) findViewById(R.id.listClients);
        button = (ImageButton) findViewById(R.id.landingScreenAddClientButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Expenses.class);
                startActivity(intent);
            }
        });
       /* fabAddClient = (FloatingActionButton) findViewById(R.id.fabAdd);

        fabAddClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"ADD",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Expenses.class);
                startActivity(intent);
            }
        });

*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,clients);
        listViewClient.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, 11, 100, "Client Tasks");
        menu.findItem(11).setIcon(android.R.drawable.ic_menu_agenda);
        menu.findItem(11).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 22, 200, "Client Groups");
        menu.findItem(22).setIcon(android.R.drawable.ic_menu_share);
        menu.findItem(22).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 33, 300, "Favorite Clients");
        menu.findItem(33).setIcon(android.R.drawable.star_on);
        menu.findItem(33).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(1, 44, 400, "Revently Contacted Clients");
        menu.findItem(44).setIcon(android.R.drawable.ic_menu_recent_history);
        menu.findItem(44).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
