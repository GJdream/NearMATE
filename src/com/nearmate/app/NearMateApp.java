package com.nearmate.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;

import com.kii.cloud.analytics.KiiAnalytics;
import com.kii.cloud.storage.Kii;
import com.nearmate.app.model.MenuItem;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class NearMateApp extends Application {

	public static final String TAG = "NearMateApp";
	
	public static boolean taskExecuted = false;
	public static boolean reaload_clicked = false;
	
	public static String completelocation;
	public static double Lat;
	public static double Lon;
	
	public static String city;
	public static String state;
	public static String country;
	
	public static String fb_user_id;
	public static String fb_user_name;
	public static String to_chat_username;
	

	/**
	 * List of Menus shown in Slider
	 */
	public static List<MenuItem> menuItems = null;
	
	public static final String PREF_USER_IMAGE = "username_image";
	public static final String PREF_USER_NAME = "user_name";
	
	public static final String PREF_SETTINGS_SAVED = "settings_status";
	public static final String PREF_INTEREST_INITIAL = "interest_status";
	
	
	/**
	 * Font Type Face
	 */
	public static Typeface LIGHT_TYPEFACE = null;
	public static Typeface REGULAR_TYPEFACE = null;
	public static Typeface BOLD_TYPEFACE = null;
	public static boolean criteria_saved = false;
	
	/*
	 *Profile Details Strings 
	 */
	public static String user_edu;
	public static String user_job;
	public static String user_income;
	public static String user_zodiac;
	public static String user_lang;
	public static String user_hobby;
	
	private String check_interests_status;
	
	
	/**
	 * Called when the application is starting, before any activity, service, 
	 * or receiver objects (excluding content providers) have been created. 
	 * Implementations should be as quick as possible (for example using lazy initialization of state) 
	 * since the time spent in this function directly impacts the performance of starting the first activity, 
	 * service, or receiver in a process. If you override this method, be sure to call super.onCreate().
	 */

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "LNHqxT62tD5r9pshJL2Cihp8iypQqConTdlUrlvs",
				"WZKMWoB5BMFzpf16XgY7U6glSF0aLTZJ0rjjwbmt");
		
		ParseACL defaultACL = new ParseACL();
		 defaultACL.setPublicReadAccess(true);
		 defaultACL.setPublicWriteAccess(true);
		 ParseACL.setDefaultACL(defaultACL, true);

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));
		
		Kii.initialize(ApplicationConst.APP_ID, ApplicationConst.APP_KEY, Kii.Site.JP);
		KiiAnalytics.initialize(getBaseContext(), ApplicationConst.APP_ID, ApplicationConst.APP_KEY, KiiAnalytics.Site.US);
		
		
try {
			
			initializeMenuItems();
			//checkInterests();
			
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Initialize the List of Menu Items
	 */
	public void initializeMenuItems(){
		
		//Add the menu and Its icon
		menuItems = new ArrayList<MenuItem>();
		
		menuItems.add(new MenuItem(getResources().getString(R.string.matches),R.drawable.heartt));
		menuItems.add(new MenuItem(getResources().getString(R.string.discovery_pref), R.drawable.search));
		menuItems.add(new MenuItem(getResources().getString(R.string.app_setting), R.drawable.settingg));
		menuItems.add(new MenuItem(getResources().getString(R.string.contact_nearmate), R.drawable.contact_uss));
		menuItems.add(new MenuItem(getResources().getString(R.string.share_nearmate), R.drawable.share));
		menuItems.add(new MenuItem(getResources().getString(R.string.signout), R.drawable.signoutt));
		
	}
	

}
