package com.app.movein.home;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.movein.R;
import com.google.android.gms.common.SignInButton;

import java.io.InputStream;

public class SignInFragment extends FragmentActivity {


    /**
     *
     */

    Button btnSignIn, btnSignUp;
    EditText editTextUserName, editTextPassword;
    Activity mActivity;
    private SignInButton btnSignInG;
    private Button btnSignOutG, btnRevokeAccess;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    //fb login
    private TextView username;

    //fb login
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signinfragment);
        // TODO Auto-generated method stub

        editTextUserName = (EditText) findViewById(R.id.editTextUserNameToLogin);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordToLogin);
        btnSignIn = (Button) findViewById(R.id.buttonSignIn);


        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

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

    @Override
    public void onResume() {
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //signOutFromGplus();

    }

    /**
     * Background Async task to load user profile picture from url
     */
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


}
