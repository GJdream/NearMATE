package com.nearmate.app;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	//private static final String TAG = "LoginActivity";
	
	private Button loginButton;
	private Dialog progressDialog;
	public List<String> permissions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});
		

		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user Detail activity
			//showUserDetailsActivity();
			showMainActivity();
			
		}
		
	}

	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		
		
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);
		/**
		 * Permissions old, which are changed
		 *
		 */
		
		/*List<String> permissions = Arrays.asList("basic_info", "user_about_me",
				"user_relationships", "user_birthday", "user_location");*/
		
		
		//currently in use working
		/*List<String> permissions = Arrays.asList("user_about_me", "user_interests",
				"user_birthday", "user_location", "email","user_likes","user_friends");*/
		
		permissions = Arrays.asList(
				ParseFacebookUtils.Permissions.User.ABOUT_ME,
				ParseFacebookUtils.Permissions.User.BIRTHDAY,
				ParseFacebookUtils.Permissions.User.INTERESTS,
				ParseFacebookUtils.Permissions.User.STATUS,
				ParseFacebookUtils.Permissions.User.ACTIVITIES,
				ParseFacebookUtils.Permissions.User.CHECKINS,
				ParseFacebookUtils.Permissions.User.GROUPS,
				ParseFacebookUtils.Permissions.User.LIKES,
				ParseFacebookUtils.Permissions.User.ONLINE_PRESENCE,
				ParseFacebookUtils.Permissions.User.PHOTOS,
				ParseFacebookUtils.Permissions.User.QUESTIONS,
				ParseFacebookUtils.Permissions.User.LOCATION);
	
		
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(NearMateApp.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(NearMateApp.TAG,
							"User signed up and logged in through Facebook!");
					//showUserDetailsActivity();
					showMainActivity();
				} else {
					Log.d(NearMateApp.TAG,
							"User logged in through Facebook!");
					//showUserDetailsActivity();
					showMainActivity();
				}
			}
		});
	}
	
	private void showMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
		
	}
}
