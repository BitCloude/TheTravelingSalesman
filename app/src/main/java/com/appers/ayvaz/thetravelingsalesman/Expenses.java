package com.appers.ayvaz.thetravelingsalesman;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Expenses extends AppCompatActivity {
Spinner spinnerClient, spinnerAmount, spinnerFrom;
    ListView listViewExpenses;
    String[] client = {"Client 0", "Client 1", "Client 2","Client 3", "Client 4", "Client 5","Client 6", "Client 7", "Client 8","Client 9", "Client 10", "Client 11","Client 12", "Client 13", "Client 14","Client 15", "Client 16", "Client 17"};
    Integer[] amount = {1,5,10,20,50,100,500,1000};
    String[] from= {"October 2015", "November 2015", "December 2015","January 2016", "February 2016", "March 2016","April 2016", "May 2016", "June 2016", "July 2016", "August 2016", "September 2016"};
    String[] expenses= {"Client 1:10", "Client 2: 5", "Client 3: 20","Client 4: 100", "Client 5: 400", "Client 6: 1", "Client 7: 50", "Client 8: 24", "Client 9: 70", "Client 10: 13"};
    LinearLayout linearLayout;
    RelativeLayout childLayout;
    String data = "Hotel Bill From Delhi to Hyderhabad";
    String cost = "$25,000";
    //Trip From Delhi to Hyderhabad
    ImageButton expensesBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listViewExpenses = new ListView(this);
        expensesBut = (ImageButton) findViewById(R.id.expensesCalenderButton);
        linearLayout = (LinearLayout) findViewById(R.id.expensesLinearLayout);

       for(int i = 0; i< 12; i++) {
           childLayout = new RelativeLayout(this);
           RelativeLayout.LayoutParams layoutParamsCost = new RelativeLayout.LayoutParams(
                   RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
           layoutParamsCost.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

           RelativeLayout.LayoutParams layoutParamsData = new RelativeLayout.LayoutParams(
                   RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
           layoutParamsData.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
           //childLayout.setOrientation(LinearLayout.HORIZONTAL);
           TextView textdata = new TextView(this);
           textdata.setLines(2);

           textdata.setText(data);
           TextView textcost = new TextView(this);
           textcost.setLines(1);
           textcost.setText(cost);

           childLayout.setBackgroundColor(Color.LTGRAY);
           childLayout.addView(textdata, layoutParamsData);
           childLayout.addView(textcost, layoutParamsCost);


           TextView textView = new TextView(this);
           textView.setText(from[i]);
           //TextView textView2 = new TextView(this);
           //textView2.setText("November 2015");
           //   ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,expenses);
           // listViewExpenses.setAdapter(adapter4);

           //childlayout.addView(textView);
           linearLayout.addView(textView);
           linearLayout.addView(childLayout);
           //linearLayout.addView(textView2);

           registerForContextMenu(childLayout);
       }

       // linearLayout.addView(listViewExpenses);
       /* listViewExpenses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ExpenseAdd.class);
                startActivity(intent);
            }
        });
*/
        expensesBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ExpenseAdd.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1, 11, 100, "ADD");
        //menu.findItem(11).setIcon(android.R.drawable.ic_menu_save);
        menu.findItem(11).setIcon(android.R.drawable.ic_menu_add);
        menu.findItem(11).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        int groupId = 1;
        menu.add(groupId, 11, 100, "Edit");
        menu.add(groupId, 22, 200, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == 11) {
            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == 22) {
            Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onContextItemSelected(item);
    }

}
