package com.example.jerario.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.*;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends Activity {
    private EditText toSearch;
    private Button send;
    private int offset;

    public static final String MYPREFERENCES = "myPrefs";
    public static final String LASTQUERY = "lastQuery";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
    }

    private void initComponents(){
        setContentView(R.layout.activity_main);
        toSearch = (EditText)findViewById(R.id.toSearch);
        send = (Button)findViewById(R.id.buttonSearch);

        //Shared Preferences
        sharedPreferences = getSharedPreferences(MYPREFERENCES,MODE_PRIVATE);
        toSearch.setText(sharedPreferences.getString(LASTQUERY, ""));


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSearch();
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

    public void savingLastQuery(){
        Editor editor = sharedPreferences.edit();
        editor.putString(LASTQUERY, toSearch.getText().toString());
        editor.commit();
    }

    public boolean lastQueryChanged(){
        String savedQuery = sharedPreferences.getString(LASTQUERY," ");
        String queryString = toSearch.getText().toString();
        if(savedQuery.equalsIgnoreCase(queryString))
            return false;
        return true;
    }

    public void sendSearch(){
        savingLastQuery();
        String queryString = toSearch.getText().toString().trim();
        Intent queryIntent = new Intent(this,ResultsActivity.class);
        queryIntent.putExtra("queryString",queryString);
        startActivity(queryIntent);
    }
}
