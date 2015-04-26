package com.app.movein.home;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.app.movein.R;

public class MainActivity extends Activity implements LocationListener {
	Button btnSignIn;
	// maps button

	Button btnPost;

	boolean canGetLocation = false;
	Location location;
	double latitude;
	double longitude;
	private String mAddress,mLocality,mSubLocality;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	protected LocationManager locationManager;
	private boolean doubleBackToExitPressedOnce=false;

	// maps button
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnSignIn = (Button) findViewById(R.id.btnsignin);
		btnPost = (Button) findViewById(R.id.btnpost);
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, TabView.class);
				startActivity(i);
				// TODO Auto-generated method stub

			}
		});
		LocationManager locationManager = (LocationManager) this
				.getSystemService(this.LOCATION_SERVICE);

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
		btnPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// new LoadStuff().execute();
				Intent i = new Intent(MainActivity.this,
						com.app.movein.maps.GoogleMaps.class);
				
				i.putExtra("latitude", latitude);
				i.putExtra("longitude", longitude);
				i.putExtra("current_address", mAddress);
				i.putExtra("locality", mLocality);
				i.putExtra("sub_locality",mSubLocality );
				startActivity(i);
				// TODO Auto-generated method stub

			}
		});
		

		// maps or location

	}

	@Override
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		Thread t1=new Thread(){
			public void run() {
				mAddress=getAddress(latitude,longitude);
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
	private String getAddress(final double latitude,final double longitude){
		List<Address> addresses;
		String result=null;
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
				mLocality=currentAddr.getLocality();
				mSubLocality=currentAddr.getSubLocality();
				
				result=mSB.toString();
				
				
				
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
	

}
