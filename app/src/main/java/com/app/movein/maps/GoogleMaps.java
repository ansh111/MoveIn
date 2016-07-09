package com.app.movein.maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.app.movein.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMaps extends FragmentActivity implements
		OnMarkerDragListener {

	// private GoogleMap map;
	private GoogleMap googleMap;
	protected MarkerOptions markerOptions, markerOptions1;
	protected AutoCompleteTextView atvPlaces;
	protected PlacesTask placesTask;
	protected ParserTask parserTask;
	protected LatLng latLng, latLng1;
	protected List<Address> addresses;
	private String mAddress,mLocality,mSubLocality;
	private static int GOOGLE_MAPS=101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);
		Bundle b = getIntent().getExtras();
		
		double latitude = b.getDouble("latitude", 0);
		double longitude = b.getDouble("longitude", 0);
		mLocality=b.getString("locality");
		mSubLocality=b.getString("sub_locality");
		String current_address = b.getString("current_address");
		Log.i("FK", "lat =" + latitude + "long =" + longitude);

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		googleMap.setMyLocationEnabled(true);

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude)).zoom(10).build();
		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.setOnMarkerDragListener(this);
		LatLng latLng = new LatLng(latitude, longitude);
		googleMap.addMarker(new MarkerOptions().position(latLng)
				.draggable(true).title(current_address));
		// new GeocoderTask().execute(location);
		
		Button btnDone = (Button) findViewById(R.id.buttonLocationdone);
		Button btnConfirm = (Button) findViewById(R.id.buttonLocationconfirm);
		// AutoComplete Text
		atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
		atvPlaces.setThreshold(1);
		atvPlaces.setText(current_address);

		atvPlaces.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				placesTask = new PlacesTask();
				placesTask.execute(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				// TODO Auto-generated method stub
			}
		});
		// AutoComplete TextView

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String location = atvPlaces.getText().toString();
				if (location != null && !location.equals("")) {
					new GeocoderTask().execute(location);

				}

				// TODO Auto-generated method stub

			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mAddress = atvPlaces.getText().toString();
				
				Intent i = new Intent(GoogleMaps.this,
						com.app.movein.postad.PostAdActivity.class);
				i.putExtra("address", mAddress);
				i.putExtra("locality", mLocality);
				i.putExtra("sub_locality", mSubLocality);
				startActivityForResult(i, GOOGLE_MAPS);
				//GoogleMaps.this.finish();
				// TODO Auto-generated method stub

			}
		});

	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches all places from GooglePlaces AutoComplete Web Service
	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyBWfHoXXZkoQKwdnjY1Dp1gkQaD8dO0ewc";

			String input = "";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";

			// Sensor enabled
			String sensor = "sensor=false";

			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;

			// Output format
			String output = "json";

			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;
			Log.i("FK", "url =" + url);

			try {
				// Fetching the data from we service
				data = downloadUrl(url);
				Log.i("FK", "data =" + data);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// Creating ParserTask
			parserTask = new ParserTask();

			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);
				Log.i("FK", "places =" + places);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			Log.i("FK", "from =" + from);
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the AutoCompleteTextView
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			atvPlaces.setAdapter(adapter);
		}
	}

	// End AutoCompleteTextView
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
			}

			// Clears all the existing markers on the map
			googleMap.clear();

			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {

				Address address = addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());
				mLocality=address.getLocality();
				mSubLocality=address.getSubLocality();
				//Log.i("MI","address get locality"+address.getLocality() +"address.getAdminArea() "+address.getAdminArea()+ "address.getSubAdminArea() "+address.getSubAdminArea()+"address.getSubLocality()"+address.getSubLocality()+"address.getThoroughfare()"+address.getThoroughfare()+"address.getSubThoroughfare()"+address.getSubThoroughfare());
				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				googleMap.addMarker(markerOptions).setDraggable(true);
				atvPlaces.setText(addressText);

				// Locate the first location
				if (i == 0)
					googleMap.animateCamera(CameraUpdateFactory
							.newLatLng(latLng));
			}
		}
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		LatLng latLong = marker.getPosition();
		try {
			Geocoder mGC = new Geocoder(this, Locale.ENGLISH);
			addresses = mGC.getFromLocation(latLong.latitude,
					latLong.longitude, 1);
			if (addresses != null) {
				Address currentAddr = addresses.get(0);
				// String city = addresses.get(0).getLocality();

				StringBuilder mSB = new StringBuilder();
				for (int i = 0; i < currentAddr.getMaxAddressLineIndex(); i++) {
					mSB.append(currentAddr.getAddressLine(i)).append("\n");

				}
				Log.i("FK", "mSB =" + mSB.toString());
				// mSB.append(country);
				/*
				 * Intent i=new Intent(this,Woeid.class); i.putExtra("city",city
				 * ); startActivity(i);
				 */
				String mAddress = mSB.toString();
				// tv.setText(mSB.toString());
				googleMap.clear();
				markerOptions1 = new MarkerOptions();
				markerOptions1.position(latLong);
				markerOptions1.title(mAddress);
				googleMap.addMarker(markerOptions1).setDraggable(true);
				atvPlaces.setText(mAddress);
				/*
				 * final Intent intent = new
				 * Intent(Intent.ACTION_VIEW,Uri.parse(
				 * "http://maps.google.com/maps?" + "saddr="+ latLng1.latitude +
				 * "," + latLng1.longitude + "&daddr=" + latLong.latitude + ","
				 * + latLong.longitude));
				 * intent.setClassName("com.google.android.apps.maps"
				 * ,"com.google.android.maps.MapsActivity");
				 * startActivity(intent);
				 */
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		/*
		 * latLng1=marker.getPosition();
		 * Log.i("FK"," On marker dragstart  lat =" +latLng1.latitude +
		 * "long ="+ latLng1.longitude);
		 */
		// TODO Auto-generated method stub

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==GOOGLE_MAPS)
		{
			String mAddress=data.getStringExtra("area");
			atvPlaces.setText(mAddress);
			
			
		}
		// TODO Auto-generated method stub
		
	}
}
