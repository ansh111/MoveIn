package com.app.movein.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.app.movein.R;

import java.util.ArrayList;

/**
 * Created by MMT5575 on 9/25/2015.
 */
public class FlatSearchActivity extends Activity {
   RecyclerView mRView;
    RecyclerView.LayoutManager mRLayout;
    FlatSearchAdapter mRAdapter;
    private ArrayList<Integer> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchads);

        mRView= (RecyclerView) findViewById(R.id.search_recycler_view);
        mRView.setHasFixedSize(true);
        mRLayout=new LinearLayoutManager(this);
        mRView.setLayoutManager(mRLayout);
        mDataset=new ArrayList<Integer>();
        for(int i=0;i<10;i++) {
            mDataset.add(i, i);
        }
        mRAdapter=new FlatSearchAdapter(mDataset);
        mRView.setAdapter(mRAdapter);
        mRView.animate().translationY(mRView.getTop()).start();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)findViewById(R.id.search_view);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        // Do not iconify the widget; expand it by default

        }


}
