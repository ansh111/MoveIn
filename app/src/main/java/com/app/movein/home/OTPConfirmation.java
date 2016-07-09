package com.app.movein.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.movein.R;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Random;

public class OTPConfirmation extends Activity {
    protected AsyncHttpClient client;
    int otpGen;
    EditText ed;
    Button btn;
    Gson gson;
    String URL;
    private String userName, password;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        client = new AsyncHttpClient();
        //client.setTimeout(60000);
        //mContext = context;
        gson = new Gson();
        otpGen = gen();
        Bundle i = getIntent().getExtras();
        String mPhnNo = i.getString("PhoneNo");
        userName = i.getString("UserName");
        password = i.getString("Password");
        URL = "http://smshorizon.co.in/api/sendsms.php?user=rishabh.gu&apikey=aepRImckBmOZk6XcL8Pk&mobile=" + mPhnNo + "&message=" + otpGen + "&senderid=xxyy&type=txt";
        URL = URL.replaceAll(" ", "%20");
        client.get(URL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
                System.out.println("onStart");
            }

            @Override
            public void onSuccess(int arg0,
                                  org.apache.http.Header[] arg1, byte[] arg2) {

                // TODO Auto-generated method stub
                System.out.println("onSuccess");
                System.out.println(arg0);
                System.out.println(arg2.toString());

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                System.out.println("onRetry");
            }


            @Override
            public void onFailure(int arg0,
                                  org.apache.http.Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub

            }

        });

        setContentView(R.layout.otp);
        ed = (EditText) findViewById(R.id.otpEditText);
        btn = (Button) findViewById(R.id.otpButton);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String otpEnter = ed.getText().toString();
                if (otpEnter == null) {
                    Toast.makeText(getBaseContext(), "Enter otp", Toast.LENGTH_SHORT).show();
                } else {
                    int otpFinal = Integer.parseInt(otpEnter);
                    Log.i("FK", "otpgen =" + otpGen + "otpFinal =" + otpFinal);
                    if (otpFinal == otpGen) {

                        Toast.makeText(getBaseContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();

						/*Intent i=new Intent(OTPConfirmation.this,com.FlatKhoj.NDrawerPost.PostActivity.class);
						startActivity(i);*/
                    } else {
                        Toast.makeText(getBaseContext(), "Please enter correct OTP", Toast.LENGTH_SHORT).show();
                    }
                }
                // TODO Auto-generated method stub

            }
        });
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    public int gen() {
        Random r = new Random(System.currentTimeMillis());
        return (1 + r.nextInt(2)) * 10000 + r.nextInt(10000);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

}
