package com.app.movein.home;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.movein.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class SignUpFragment extends FragmentActivity implements OnClickListener,
ConnectionCallbacks, OnConnectionFailedListener {
	//GOOGLE+ LOGIN
	private static final String TAG = "MainFragment";
	// Create, automatically open (if applicable), save, and restore the 
	// Active Session in a way that is similar to Android UI lifecycles. 
	 private static final int RC_SIGN_IN = 0;
	    // Logcat tag
	 //   private static final String TAG = "MainActivity";
	 
	    // Profile pic image size in pixels
	
	    private static final int PROFILE_PIC_SIZE = 400;
	    private boolean mIntentInProgress;
	    
	    private boolean mSignInClicked;
	 
	    private ConnectionResult mConnectionResult;
	   
	    private SignInButton btnSignIn;
	   // private Button btnSignOut, btnRevokeAccess;
	 
	    // Google client to interact with Google API
	    private GoogleApiClient mGoogleApiClient;
	//GOOGLE+ LOGIN
	    private UiLifecycleHelper uiHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//FB LOGIN
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		//FB LOGIN
		setContentView(R.layout.signupfragment);
		LoginButton loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
	 
	        // Button click listeners
	        btnSignIn.setOnClickListener((OnClickListener) this);
	     
	        // Initializing google plus api client
	        mGoogleApiClient = new GoogleApiClient.Builder(this)
	                .addConnectionCallbacks(this)
	                .addOnConnectionFailedListener(this).addApi(Plus.API)
	                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
		//FB LOGIN
	        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
			loginBtn.setReadPermissions(Arrays.asList("email"));
		
			
			loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
				@Override
				public void onUserInfoFetched(GraphUser user) {
					Log.i("FK", "onUserInfoFetched");
					if (user != null) {
						String email=user.asMap().get("email").toString() ;
						String gender=user.asMap().get("gender").toString();
						Intent i=new Intent(SignUpFragment.this,SignUPActivity.class);
						i.putExtra("Name", user.getName());
						
						i.putExtra("Gender", gender);
						i.putExtra("Email", email);
						
						startActivity(i);
						finish();
						
						Log.i("FK", " " +user.getName() +user.getBirthday());
						
						
					//	username.setText("You are currently logged in as " + user.getName());
					} else {
						Toast.makeText(getBaseContext(),"You are not logged in",0).show();
						//username.setText("You are not logged in.");
					}
				}
			});
		//FB LOGIN
	}
	//FB LOGIN
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			
			if (state.isOpened()) {
			
				Log.i("FK", "onUserInfoFetched");
				Log.d("MainActivity", "Facebook session opened.");
			} else if (state.isClosed()) {
			
				Log.i("FK", "onUserInfoFetched");
				Log.d("MainActivity", "Facebook session closed.");
			}
		}
	};
	//FB LOGIN
	 public void onStart() {
	        super.onStart();
	        mGoogleApiClient.connect();
	    }
	 
	    public void onStop() {
	        super.onStop();
	        if (mGoogleApiClient.isConnected()) {
	            mGoogleApiClient.disconnect();
	        }
	        signOutFromGplus();
	    }
	    private void resolveSignInError() {
	        if (mConnectionResult.hasResolution()) {
	            try {
	                mIntentInProgress = true;
	                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
	            } catch (SendIntentException e) {
	                mIntentInProgress = false;
	                mGoogleApiClient.connect();
	            }
	        }
	    }
	

		 @Override
		public void onActivityResult(int requestCode, int responseCode,
		            Intent intent) {
		        if (requestCode == RC_SIGN_IN) {
		            if (responseCode != Activity.RESULT_OK) {
		                mSignInClicked = false;
		            }
		 
		            mIntentInProgress = false;
		 
		            if (!mGoogleApiClient.isConnecting()) {
		                mGoogleApiClient.connect();
		            }
		        }
		      uiHelper.onActivityResult(requestCode, responseCode, intent);
		    }
		 
		@Override
		public void onResume() {
			super.onResume();
			uiHelper.onResume();
		}

		@Override
		public void onPause() {
			super.onPause();
			signOutFromGplus();
			uiHelper.onPause();
					
			
		}
		@Override
		public void onDestroy() {
			super.onDestroy();
			uiHelper.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			uiHelper.onSaveInstanceState(outState);
			
		}
		@Override
		public void onConnectionFailed(ConnectionResult result) {
			 if (!result.hasResolution()) {
		            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
		                    0).show();
		            return;
		        }
		 
		        if (!mIntentInProgress) {
		            // Store the ConnectionResult for later usage
		            mConnectionResult = result;
		 
		            if (mSignInClicked) {
		                // The user has already clicked 'sign-in' so we attempt to
		                // resolve all
		                // errors until the user is signed in, or they cancel.
		                resolveSignInError();
		            }
		        }
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onConnected(Bundle connectionHint) {
			 mSignInClicked = false;
		        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		 
		        // Get user's information
		        getProfileInformation();
		 
		        // Update the UI after signin
		     //   updateUI(true);
			// TODO Auto-generated method stub
			
		}
		
		 private void getProfileInformation() {
		        try {
		            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
		                Person currentPerson = Plus.PeopleApi
		                        .getCurrentPerson(mGoogleApiClient);
		                String personName = currentPerson.getDisplayName();
		                //  String personPhotoUrl = currentPerson.getImage().getUrl();
		                  int gender=currentPerson.getGender();
		                  String gen;
		                  Log.i("FK","gender=" + gender);
		                  switch(gender)
		                  {
		                  case 0:
		                  	gen="Male";
		                  	break;
		                  case 1:
		                  	gen="Female";
		                  	break;
		                  default:
		                  	gen="Others";
		                  	break;
		                  }
		                  String personGooglePlusProfile = currentPerson.getUrl();
		                  String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
		                 // String email=user.asMap().get("email").toString() ;
		  				//String gender=personName.asMap().get("gender").toString();
		  				Intent i=new Intent(this,SignUPActivity.class);
		  				i.putExtra("Name", personName);
		  				
		  				i.putExtra("Gender", gen);
		  				i.putExtra("Email", email);
		  				
		  				startActivity(i);
		  				finish();
		 
		              
		 
		            } else {
		                Toast.makeText(this,
		                        "Person information is null", Toast.LENGTH_LONG).show();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		@Override
		public void onConnectionSuspended(int cause) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onClick(View v) {
			 switch (v.getId()) {
		        case R.id.btn_sign_in:
		            // Signin button clicked
		            signInWithGplus();
		            break;
		       
		        }
			// TODO Auto-generated method stub
			
		}
		 private void signInWithGplus() {
		        if (!mGoogleApiClient.isConnecting()) {
		            mSignInClicked = true;
		            resolveSignInError();
		        }
		    }
		 
		    /**
		     * Sign-out from google
		     * */
		    private void signOutFromGplus() {
		        if (mGoogleApiClient.isConnected()) {
		            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
		            mGoogleApiClient.disconnect();
		            mGoogleApiClient.connect();
		          
		        }
		    }
		 
		      
}

