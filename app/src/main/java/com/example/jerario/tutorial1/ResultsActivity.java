package com.example.jerario.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.managers.ItemManager;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;

import com.example.jerario.tutorial1.adapters.ProductAdapter;
import com.example.jerario.tutorial1.tasks.SearchItemsTask;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.Closure;
import com.example.jerario.tutorial1.utils.EndlessListListener;


public class ResultsActivity extends Activity {

    private String message;
    private LinkedList<Item> productList = new LinkedList<Item>();
    private EndlessListListener listListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Intent intent = getIntent();
        ListView listResults = (ListView) findViewById(R.id.results);
        if (savedInstanceState == null)
            listListener = new EndlessListListener(intent, ResultsActivity.this, listResults, productList);
        else {
            productList = (LinkedList<Item>) savedInstanceState.getSerializable("products");
            listListener = new EndlessListListener(intent, ResultsActivity.this, listResults,productList);
           // listListener.showView(products);
        }
        View inflatedView = getLayoutInflater().inflate(R.layout.product_loading, null);
        listResults.addFooterView(inflatedView);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("products",listListener.getProductList());

    }
}
