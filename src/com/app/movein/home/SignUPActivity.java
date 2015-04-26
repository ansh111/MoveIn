package com.app.movein.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.movein.R;

public class SignUPActivity extends Activity
{
	EditText editTextUserName,editTextPassword,editTextConfirmPassword,editTextPhoneNo,editTextGender,editTextEmail;
	Button btnCreateAccount;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		savedInstanceState=getIntent().getExtras();
		String name=savedInstanceState.getString("Name");

		String gender =savedInstanceState.getString("Gender");
		String email=savedInstanceState.getString("Email");
	
		// get Instance  of Database Adapter
		
		
		// Get Refferences of Views
		editTextUserName=(EditText)findViewById(R.id.editTextUserName);
		editTextUserName.setText(name);
		editTextUserName.setKeyListener(null);
		editTextUserName.setEnabled(false);
		editTextPassword=(EditText)findViewById(R.id.editTextPassword);
		editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
		editTextPhoneNo=(EditText) findViewById(R.id.editTextPhoneNo);
		editTextEmail=(EditText) findViewById(R.id.editTextEmail);
		editTextEmail.setText(email);
		editTextEmail.setKeyListener(null);
		editTextEmail.setEnabled(false);

		editTextGender=(EditText) findViewById(R.id.editTextGender);
		editTextGender.setText(gender);
		editTextGender.setKeyListener(null);
		editTextGender.setEnabled(false);
		btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String userName=editTextUserName.getText().toString();
			String password=editTextPassword.getText().toString();
			String confirmPassword=editTextConfirmPassword.getText().toString();
			String mPhnNo=editTextPhoneNo.getText().toString();
			String gender=editTextGender.getText().toString();
			String email=editTextEmail.getText().toString();
			
			// check if any of the fields are vaccant
			if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
			{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
			}
			// check if both password matches
			if(!password.equals(confirmPassword))
			{
				Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
			  
			
			    Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
			 
			    Intent i=new Intent(SignUPActivity.this,OTPConfirmation.class);
			    i.putExtra("PhoneNo", mPhnNo);
			    i.putExtra("Username", userName);
			    i.putExtra("Pass", password);
			    startActivity(i);
			    
			}
		}
	});
}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		
	}
}
