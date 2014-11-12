package com.example.jerario.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.managers.ItemManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;

import com.example.jerario.tutorial1.adapters.ProductAdapter;
import com.example.jerario.tutorial1.tasks.SearchItemsTask;
import com.example.jerario.tutorial1.utils.Closure;


public class ResultsActivity extends Activity {

    private String message;
    private LinkedList<Item> productList;
    private ListView listResults;
    private ProductAdapter adapter;

    //Constants
    public static final String QUERY = "queryString";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        productList = new LinkedList<Item>();
        listResults = (ListView) findViewById(R.id.results);
        //Intent
        Intent intent = getIntent();
        message = intent.getStringExtra(QUERY);
        SearchItemsTask.searchItems(message,0,15,new Closure<LinkedList<Item>>() {
            @Override
            public void executeOnSuccess(LinkedList<Item> result) {
                refreshView(result);
            }
        });
        adapter = new ProductAdapter(ResultsActivity.this,productList);
        listResults.setAdapter(adapter);
    }

    private void refreshView(LinkedList<Item> list){
        Log.d("RESULTS ACTIVITY", "In refresh view");
        Log.d("FIRST ITEM",list.getFirst().getTitle());
        productList.addAll(list);
        adapter.notifyDataSetChanged();
        Log.d("RESULTS ACTIVITY", "Finishing refresh view");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
