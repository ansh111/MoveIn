package com.app.movein.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.movein.R;

import java.util.ArrayList;

/**
 * Created by MMT5575 on 9/25/2015.
 */
public class FlatSearchAdapter extends RecyclerView.Adapter<FlatSearchAdapter.ViewHolder> {
    ArrayList<Integer> mDataSet = null;

    public FlatSearchAdapter(ArrayList<Integer> mDataSet) {
        super();
        this.mDataSet = mDataSet;

    }

    @Override
    public FlatSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_ads_adapter, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tv.setText(mDataSet.get(i) + "");
        viewHolder.tv1.setText("Hello");

    }

    @Override
    public int getItemCount() {

        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv, tv1;
        private ImageView imageview;

        public ViewHolder(View v) {
            super(v);

            tv = (TextView) v.findViewById(R.id.search_tv);
            tv1 = (TextView) v.findViewById(R.id.secondLine);
            imageview = (ImageView) v.findViewById(R.id.icon);

        }
    }

}
