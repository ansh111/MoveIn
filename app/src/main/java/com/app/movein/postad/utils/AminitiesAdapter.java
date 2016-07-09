package com.app.movein.postad.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.app.movein.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AminitiesAdapter extends BaseAdapter {

    Context mContext;
    boolean selected = true;
    boolean isBoolAminities = false;
    private String[] mAminities;
    private IAminitiesAdapter mAminitiesAdapter;
    private String[] mAminitiesValue = {"", "", "", "", "", "", "", "", "", ""};
    private boolean isFlatAminities = false;

    public AminitiesAdapter(Context c, String[] mAminities, IAminitiesAdapter mAminitiesAdapter, boolean isFlatAminities) {
        mContext = c;
        this.mAminities = mAminities;
        this.mAminitiesAdapter = mAminitiesAdapter;
        this.isFlatAminities = isFlatAminities;
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mAminities.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mAminities[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressWarnings("null")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.aminitieslayout, parent, false);
            holder.mCheckBox = (CheckBox) convertView.findViewById(R.id.aminitiescheckbox);
            holder.mCheckBox.setText(mAminities[position]);
            holder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        mAminitiesValue[position] = holder.mCheckBox.getText().toString();
                        isBoolAminities = true;


                    } else {
                        if (isBoolAminities) {
                            List<String> list = new ArrayList<String>(Arrays.asList(mAminitiesValue));
                            list.remove(mAminitiesValue[position]);
                            mAminitiesValue = list.toArray(new String[0]);

                        }
                    }

                    // TODO Auto-generated method stub

                }
            });

            mAminitiesAdapter.setAnimitiesAdapterValue(mAminitiesValue, isFlatAminities);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();


        }

        // TODO Auto-generated method stub
        return convertView;
    }

    public class ViewHolder {
        CheckBox mCheckBox;
        TextView tv;
        EditText mEditText;

    }

}
