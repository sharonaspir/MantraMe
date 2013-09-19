package com.Sharon.MantraMeWidget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import MantraMeClasses.UserProfile;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.Sharon.MantraMeWidget.R;

public class AddNewUser extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewuser);
	}

	public void nextClicked(View view) {	

		EditText editTextName = (EditText) findViewById(R.id.editTextPersonName);
		EditText editTextEmail = (EditText) findViewById(R.id.editTextEmailAddress);

		EditText editTextPassword = (EditText) findViewById(R.id.editTextUserPassword);
		EditText editTextPasswordConfirm = (EditText) findViewById(R.id.editTextUserPasswordConfirm);

		String name = editTextName.getText().toString();
		String email = editTextEmail.getText().toString();

		String password = editTextPassword.getText().toString();
		String passwordConfirm = editTextPasswordConfirm.getText().toString();

		if (name.isEmpty()){
			Toast.makeText(AddNewUser.this, "Please enter user name", Toast.LENGTH_LONG).show();
			return;
		}

		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
		Matcher m = p.matcher(email);
		boolean matchFound = m.matches();
		
		if (!matchFound){
			Toast.makeText(AddNewUser.this, "Please enter valid mail address", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (password.isEmpty() || passwordConfirm.isEmpty()){
			Toast.makeText(AddNewUser.this, "Please enter password and confirm it", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (!password.equals(passwordConfirm)){
			Toast.makeText(AddNewUser.this, "Passwords does not match", Toast.LENGTH_LONG).show();
			editTextPassword.setText("");
			editTextPasswordConfirm.setText("");
			
			return;
		}

		Log.w("AddNewUser" , "All data recived");		

		UserProfile user = new UserProfile(name, email); 
		user.SetPassWord(password);

		UserProfile.setUser(user);

		try
		{
			Intent k = new Intent(this, NewUserRatings.class);
			startActivity(k);
			finish();
		}catch(Exception e){
			Log.wtf("AddNewUser" , "Exception in AddNewUser.nextClicked(). \n " + e);
		}		

	}
}
