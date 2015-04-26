package com.app.movein.home;


import java.io.InputStream;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.movein.R;
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
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

public class SignInFragment extends FragmentActivity 
{

	
 
    private SignInButton btnSignInG;
    private Button btnSignOutG, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
	/**
	 * 
	 */
	
	Button btnSignIn,btnSignUp;
	
	EditText editTextUserName,  editTextPassword;
	Activity mActivity;
	//fb login
	
	
	private TextView username;
	
	//fb login
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     	setContentView(R.layout.signinfragment);
			// TODO Auto-generated method stub
	
			 editTextUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
		     editTextPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
		     btnSignIn=(Button)findViewById(R.id.buttonSignIn); 
		  
			
			
		  
			btnSignIn.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// get The User name and Password
					String userName=editTextUserName.getText().toString();
					String password=editTextPassword.getText().toString();
					
				//DB CODE TO BE WRITTEN
					
				}
			});
		    // Set OnClick Listener on SignUp button 
		  
		
	 
	    
	     
	   
	     
	}
	
	
	
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	    // Close The Database
	
	}
	

	
	
 public void onStart() {
        super.onStart();
       // mGoogleApiClient.connect();
    }
 
    public void onStop() {
        super.onStop();
      
    }
 
    /**
     * Method to resolve any signin errors
     * */
   
 
    
 
   
 
  
 
   
 
 
   
 
  
 
   
 
    /**
     * Revoking access from google
     * */
   
    /**
     * Background Async task to load user profile picture from url
     * */
    @SuppressLint("NewApi")
	private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
 
        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }
 
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
 
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
       
    }
@Override
public void onResume() {
	// Logs 'install' and 'app activate' App Events.
	AppEventsLogger.activateApp(this);
		// TODO Auto-generated method stub
		super.onResume();
	}
	

	@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//signOutFromGplus();
			
		}
	
	
	
}
