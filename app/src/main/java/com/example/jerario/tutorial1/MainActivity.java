package com.example.jerario.tutorial1;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jerario.tutorial1.tasks.TrackerService;
import com.example.jerario.tutorial1.utils.CONST;


public class MainActivity extends Activity {
    public static final String TAG = MainActivity.class.getName();
    private EditText toSearch;
    private Button send;
    private int offset;
    PendingIntent pendingIntent;
    AlarmManager alarm;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        initService();
    }

    private void initComponents(){
        setContentView(R.layout.activity_main);
        toSearch = (EditText)findViewById(R.id.toSearch);
        send = (Button)findViewById(R.id.buttonSearch);

        //Shared Preferences
        sharedPreferences = getSharedPreferences(CONST.MYPREFERENCES,MODE_PRIVATE);
        toSearch.setText(sharedPreferences.getString(CONST.LASTQUERY, ""));


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSearch();
            }
        });

    }

    private void initService(){
        Intent tracker = new Intent(this, TrackerService.class);
        pendingIntent = PendingIntent.getService(this, 0, tracker, 0);
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
                60 * 1000, pendingIntent);
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
        editor.putString(CONST.LASTQUERY, toSearch.getText().toString());
        editor.commit();
    }

    public boolean lastQueryChanged(){
        String savedQuery = sharedPreferences.getString(CONST.LASTQUERY," ");
        String queryString = toSearch.getText().toString();
        if(savedQuery.equalsIgnoreCase(queryString))
            return false;
        return true;
    }

    public void sendSearch(){
        savingLastQuery();
        String queryString = toSearch.getText().toString().trim();
        Intent queryIntent = new Intent(this,ResultsActivity.class);
       // Intent queryIntent = new Intent(this,ResultsFragment.class);
        queryIntent.putExtra(CONST.QUERYSTRING,queryString);
        startActivity(queryIntent);
    }
}
