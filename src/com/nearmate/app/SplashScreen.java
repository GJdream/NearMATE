package com.nearmate.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashScreen extends Activity {
	PackageInfo packageInfo;
	SharedPreferences settings;
	

	private final int SPLASH_TIME = 2000;
	private final int STOP_SPLASH = 1;
	
	/**
	 * Handler for stop the splash screen.
	 */
	private Handler splashHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOP_SPLASH:
				
				
				
				 
				 Log.i("In Splash", "SharedPreferences intialised");
						Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
						
						startActivity(intent);
						
					

				SplashScreen.this.finish();
				break;
					
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		splashHandler.sendEmptyMessageDelayed(STOP_SPLASH, SPLASH_TIME);
		
		
	}
}