package com.app.movein.postad.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.app.movein.R;

public class RadioAdapter extends BaseAdapter {
    Context mContext;
    String mRadioBuilder[] = {"", "", "", "", "", "", ""};
    IRadioAdapterData mRadioAdapter;
    private String[] mRadioGroup = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private String[] mRadioOptions1 = {"Bachelors", "Veg", "Working", "Allowed", "Allowed", "Allowed", "Allowed"};
    private String[] mRadioOptions2 = {"Family", "Non-Veg", "Student", "Not Allowed", "Not Allowed", "Not Allowed", "Not Allowed"};
    private String[] mRadioOptions3 = {"Any", "No Issue", "Any", "No Issue", "No Issue", "No Issue", "No Issue"};
    private String[] mTextOptions = {"Flat For", "Food Type", "Designation", "Smoking", "Drinking", "Pets", "Guests"};

    public RadioAdapter(Context c, IRadioAdapterData mRadioAdapterData) {
        this.mRadioAdapter = mRadioAdapterData;
        mContext = c;

        // TODO Auto-generated constructor stub
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mRadioGroup.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
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
            if (position == mRadioGroup.length - 1) {
                convertView = mInflater.inflate(R.layout.preferencelayout, parent, false);
                holder.mRadioConfirm = (Button) convertView.findViewById(R.id.radioconfirmbutton);
                holder.mRadioConfirm.setText("Confirm");
                holder.mRadioGroup = (RadioGroup) convertView.findViewById(R.id.radioGrouppreference);
                holder.mRadioGroup.setVisibility(View.GONE);
                holder.mRadioConfirm.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mRadioAdapter.setRadioAdapterValue(mRadioBuilder);
                        // TODO Auto-generated method stub

                    }
                });

            } else if (position == 0) {
                convertView = mInflater.inflate(R.layout.preferencelayout, parent, false);
                holder.tv1 = (TextView) convertView.findViewById(R.id.radiobuttonhint);
                holder.tv1.setText("Select a preference");
                holder.mRadioGroup = (RadioGroup) convertView.findViewById(R.id.radioGrouppreference);
                holder.mRadioGroup.setVisibility(View.GONE);
                holder.mRadioConfirm = (Button) convertView.findViewById(R.id.radioconfirmbutton);
                holder.mRadioConfirm.setVisibility(View.GONE);

            } else {

                convertView = mInflater.inflate(R.layout.preferencelayout, parent, false);
                holder.mRadioConfirm = (Button) convertView.findViewById(R.id.radioconfirmbutton);
                holder.mRadioGroup = (RadioGroup) convertView.findViewById(R.id.radioGrouppreference);
                holder.mRadioGroup.setTag(mRadioGroup[position]);
                holder.tv = (TextView) convertView.findViewById(R.id.radiotext);
                holder.tv.setText(mTextOptions[position]);
                holder.mRadioOption1 = (RadioButton) convertView.findViewById(R.id.radioption1);
                holder.mRadioOption1.setText(mRadioOptions1[position]);
                holder.mRadioOption2 = (RadioButton) convertView.findViewById(R.id.radioption2);
                holder.mRadioOption2.setText(mRadioOptions2[position]);
                holder.mRadioOption3 = (RadioButton) convertView.findViewById(R.id.radioption3);
                holder.mRadioOption3.setText(mRadioOptions3[position]);
                holder.mRadioConfirm.setVisibility(View.GONE);
                holder.tv1 = (TextView) convertView.findViewById(R.id.radiobuttonhint);
                holder.tv1.setVisibility(View.GONE);
                holder.mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.radioption1) {
                            if (holder.mRadioOption1.getText() != null) {    //String a1=String.valueOf(holder.mRadioOption1.getText());
                                mRadioBuilder[position] = holder.mRadioOption1.getText().toString();
                                Log.i("MI", "option 1" + holder.mRadioOption1.getText() + "position=" + position);
                            }

                        } else if (checkedId == R.id.radioption2) {
                            if (holder.mRadioOption2.getText() != null) {
                                //String a2=String.valueOf(holder.mRadioOption2.getText());
                                Log.i("MI", "option 2" + holder.mRadioOption2.getText() + "position=" + position);
                                mRadioBuilder[position] = holder.mRadioOption2.getText().toString();

                            }

                        } else if (checkedId == R.id.radioption3) {
                            if (holder.mRadioOption3.getText() != null) {
                                //String a3=String.valueOf(holder.mRadioOption3.getText());
                                mRadioBuilder[position] = holder.mRadioOption3.getText().toString();
                                Log.i("MI", "option 3" + holder.mRadioOption3.getText() + "position=" + position);
                            }
                        }

                        // TODO Auto-generated method stub

                    }
                });


            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // TODO Auto-generated method stub
        return convertView;
    }

    public class ViewHolder {
        RadioGroup mRadioGroup;
        Button mRadioConfirm;
        TextView tv, tv1;
        RadioButton mRadioOption1, mRadioOption2, mRadioOption3;
    }

}
