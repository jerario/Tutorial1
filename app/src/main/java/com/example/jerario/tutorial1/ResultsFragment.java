package com.example.jerario.tutorial1;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jerario.tutorial1.adapters.ProductAdapter;
import com.example.jerario.tutorial1.entities.Item;
import com.example.jerario.tutorial1.utils.CONST;
import com.example.jerario.tutorial1.utils.EndlessListListener;

import java.util.LinkedList;


public class ResultsFragment extends ListFragment implements AdapterView.OnItemClickListener{
    public static final String TAG = ResultsFragment.class.getName();
    private String message;
    private LinkedList<Item> productList = new LinkedList<Item>();
    private EndlessListListener listListener;
    private View rootView;
    private int lPosition;
    private IOnClick oClick;
    private ProductAdapter pAdapter;
    ListView listResults;
    private Item item;


    public static final ResultsFragment newInstance(Bundle bundle, String query){
        ResultsFragment fragment = new ResultsFragment();
        if (bundle == null)
            bundle = new Bundle();
        bundle.putString(CONST.QUERYSTRING,query);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        initComponents();
        Bundle bundle = getArguments();
        String msg = getArguments().getString(CONST.QUERYSTRING);
        //item = (Item) getArguments().get(CONST.ITEM);
        item = (Item) bundle.getSerializable(CONST.ITEM);
        ListView listResults = (ListView) rootView.findViewById(android.R.id.list);
     //   if (bundle.getSerializable(CONST.PRODUCTLIST) == null)
        if(savedInstanceState==null)
            listListener = new EndlessListListener(msg, getActivity(), listResults, productList);
        else {
            Log.d(TAG,"IS not null");
            //productList = (LinkedList<Item>) bundle.getSerializable(CONST.PRODUCTLIST);
            productList = (LinkedList<Item>) savedInstanceState.getSerializable(CONST.PRODUCTLIST);

            listListener = new EndlessListListener(msg, getActivity(), listResults,productList);
        }
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            oClick = (IOnClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement IOnItemClick");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_results, container, false);
        return rootView;
    }

    public LinkedList<Item> getResultList(){
        if (listListener != null)
            return listListener.getProductList();
        return productList;
    }

    private void initComponents(){

        listResults = getListView();
        View inflatedView = getActivity().getLayoutInflater().inflate(R.layout.product_loading, null);
        listResults.addFooterView(inflatedView);
        listResults.setOnItemClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_results, menu);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CONST.PRODUCTLIST, listListener.productList);
        outState.putSerializable(CONST.ITEM, item);
        outState.putSerializable(CONST.CALLER,CONST.LIST);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lPosition = position;
        Item selected = productList.get(position);
        listResults.setItemChecked(position, true);
        oClick.onItemClick(selected, productList);
    }

    public interface IOnClick{
        public void onItemClick(Item item, LinkedList<Item> productList);

    }
}
