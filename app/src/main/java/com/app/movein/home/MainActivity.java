package com.app.movein.home;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.movein.R;
import com.app.movein.search.FlatSearchActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements LocationListener, View.OnClickListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // maps button
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    protected LocationManager locationManager;
    Button btnSignIn;
    Button btnPost;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;
    private String mAddress, mLocality, mSubLocality;
    private boolean doubleBackToExitPressedOnce = false;
    private Button btnSearch;
    private TextView mAdvanceSearch, mSeekPeopleText, mSeekRentText;
    private LinearLayout mAdvanceOptons;
    private SeekBar mSeekPeople, mSeekRent;
    private int mSeekPeopleValue, mSeekRentValue;

    // maps button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdvanceSearch = (TextView) findViewById(R.id.advance_search);
        mAdvanceOptons = (LinearLayout) findViewById(R.id.advance_search_options_ll);
        mAdvanceOptons.setVisibility(View.GONE);
        mAdvanceSearch.setOnClickListener(this);
        btnSignIn = (Button) findViewById(R.id.btnsignin);
        btnSignIn.setOnClickListener(this);
        btnPost = (Button) findViewById(R.id.btnpost);
        btnSearch = (Button) findViewById(R.id.buttonLocation);
        btnPost.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        LocationManager locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);


        // maps or location

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Thread t1 = new Thread() {
            public void run() {
                mAddress = getAddress(latitude, longitude);
            }

        };
        t1.start();

        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    private String getAddress(final double latitude, final double longitude) {
        List<Address> addresses;
        String result = null;
        try {

            Geocoder mGC = new Geocoder(this, Locale.ENGLISH);
            addresses = mGC.getFromLocation(latitude,
                    longitude, 1);
            if (addresses != null) {
                Address currentAddr = addresses.get(0);
                String city = addresses.get(0).getLocality();

                StringBuilder mSB = new StringBuilder();
                for (int i = 0; i < currentAddr.getMaxAddressLineIndex(); i++) {
                    mSB.append(currentAddr.getAddressLine(i)).append("\n");
                    //mSB.append(country);

                }
                mSB.append(currentAddr.getLocality());
                mLocality = currentAddr.getLocality();
                mSubLocality = currentAddr.getSubLocality();

                result = mSB.toString();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",
                Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsignin:
                Intent intent = new Intent(MainActivity.this, TabView.class);
                startActivity(intent);
                break;
            case R.id.buttonLocation:
                Intent mIntentSearch = new Intent(MainActivity.this, FlatSearchActivity.class);
                startActivity(mIntentSearch);
                break;
            case R.id.btnpost:
                Intent i = new Intent(MainActivity.this,
                        com.app.movein.maps.GoogleMaps.class);

                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("current_address", mAddress);
                i.putExtra("locality", mLocality);
                i.putExtra("sub_locality", mSubLocality);
                startActivity(i);
                break;
            case R.id.advance_search:
                showAdvanceSearch();
                break;
            default:
                break;
        }

    }

    private void showAdvanceSearch() {
        mAdvanceSearch.animate().alpha(0.0f).setDuration(2000).start();
        mAdvanceOptons.setVisibility(View.VISIBLE);
        mAdvanceOptons.setAlpha(0.0f);
        mAdvanceOptons.animate().alpha(1.0f).setDuration(2000).start();
        mSeekPeople = (SeekBar) findViewById(R.id.search_seek_people);
        mSeekPeopleText = (TextView) findViewById(R.id.progress_no);
        mSeekPeople.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekPeopleValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekPeopleText.setText("" + mSeekPeopleValue + "/" + seekBar.getMax());
            }
        });
        mSeekRent = (SeekBar) findViewById(R.id.search_seek_rent);
        mSeekRentText = (TextView) findViewById(R.id.progress_rent);
        mSeekRent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekRentValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekRentText.setText("" + mSeekRentValue + "/" + seekBar.getMax());
            }
        });
        mAdvanceOptons.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mAdvanceSearch.animate().setDuration(2000).alpha(1.0f).start();
                    mAdvanceOptons.animate().setDuration(2000).alpha(0.0f).start();
                }
                return true;
            }
        });


    }
}
