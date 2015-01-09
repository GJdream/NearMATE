package com.nearmate.app.slider;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.nearmate.app.FilterSearch;
import com.nearmate.app.MainActivity;
import com.nearmate.app.NearMateApp;
import com.nearmate.app.ProfileActivity;
import com.nearmate.app.R;
import com.nearmate.app.model.MenuItem;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * This is the Fragment class used to show the Left Sliding Menu.
 * This will shows all the available options on Dashboard.
 * @author asus i5
 *
 */
public class SlidingLeftMenuFragment extends Fragment {
	
	Context mContext;
	String username_toshow;
	
	private ListView menuList = null;
	
	
	String user_image_from_preference;
	private ProfilePictureView userProfilePictureView;
	private String user_name_from_pref;
	private TextView tv_username;
	private TextView viewProfile;
	private String facebookId;
	private String facebookname;
	private String PhoneModel;
	private String AndroidVersion;
	//Typeface HelType;
	
	private static String CONTACT_REPORT_ISSUES = "nearmate.report@gmail.com";
	private static String CONTACT_SUGGESTIONS = "nearmate.suggest@gmail.com";
	private static String CONTACT_PARTNER = "nearmate.partner@gmail.com";
	/**
	 * Create the view and bind it to activity.
	 */
	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.list, null);
		
		SharedPreferences pref_google_id = getActivity().getSharedPreferences(NearMateApp.PREF_USER_IMAGE, 0);
		user_image_from_preference = pref_google_id.getString("facbookId", "");
		// Log.e("check", user_name_from_pref);
		 
		 SharedPreferences pref_user_name = getActivity().getSharedPreferences(NearMateApp.PREF_USER_NAME, 0);
		 user_name_from_pref = pref_user_name.getString("username", "");
		 Log.e("check", user_name_from_pref);
		 
		 
			
		
		 viewProfile = (TextView)view.findViewById(R.id.view_profile);
		//Terms of Use
			//TextView terms = (TextView) view.findViewById(R.id.terms);
		 viewProfile.setTypeface(NearMateApp.REGULAR_TYPEFACE);
			//For underlining the Text
			SpannableString content = new SpannableString(getResources().getString(R.string.view_profile));
			content.setSpan(new UnderlineSpan(), 0, getResources().getString(R.string.view_profile).length(), 0);
			viewProfile.setText(content);
			viewProfile.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(getActivity(),ProfileActivity.class);
					getActivity().startActivity(intent);
					getActivity().finish();
				}
			});
			
		
		userProfilePictureView = (ProfilePictureView)view.findViewById(R.id.user_image);
		userProfilePictureView.setProfileId(user_image_from_preference);
		userProfilePictureView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),ProfileActivity.class);
				getActivity().startActivity(intent);
				getActivity().finish();				
			}
		});
		
		tv_username = (TextView)view.findViewById(R.id.userName);
		tv_username.setText(user_name_from_pref);
	
		
		menuList = (ListView) view.findViewById(R.id.menuListview);
		menuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				//Matches
				if(id ==0){
					Intent intent = new Intent(getActivity(), MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				
				//Discovery Preference
				if(id == 1){
					Intent intent = new Intent(getActivity(), FilterSearch.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				
				//App settings
				if(id == 2){
					
				}
				
				//Contact us
				if(id== 3){
					contactUs();
				}
				// Share 
				if(id == 4){
					shareNearMate();
				}
				
				//Signout
				if(id == 5){
					onLogoutButtonClicked();
				}
			}
		});	
		return view;
	}

	/**
	 * Called when activity is created
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			makeMeRequest();
		}
		
		 //HelType = Typeface.createFromAsset(getActivity().getAssets(),"AlexandriaFLF.ttf"); 
		
		SharedPreferences pref_google_id = getActivity().getSharedPreferences(NearMateApp.PREF_USER_IMAGE, 0);
		user_image_from_preference = pref_google_id.getString("facbookId", "");
		 Log.e("check", user_name_from_pref);
		 
		 SharedPreferences pref_user_name = getActivity().getSharedPreferences(NearMateApp.PREF_USER_NAME, 0);
		 user_name_from_pref = pref_user_name.getString("username", "");
		 Log.e("check", user_name_from_pref);
		
		if(NearMateApp.menuItems != null){
			MenuAdapter adapter = new MenuAdapter(getActivity());
			for(MenuItem menuItem : NearMateApp.menuItems)
				adapter.add(menuItem);
			menuList.setAdapter(adapter);
		}
	}

	
	/**
	 * Fetching Device Details
	 */
	private void fetchingDeviceDetails(){
		try{
			
			// Device model
			 PhoneModel = android.os.Build.MODEL;

			// Android version
			 AndroidVersion = android.os.Build.VERSION.RELEASE;
			
			
		}catch(Exception e){
			e.printStackTrace();
			}
		}
	
/**
 * FB integration	
 */
		private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								//Facebook ID
								userProfile.put("facebookId", user.getId());
								
								//User Name
								userProfile.put("name", user.getName());
								
								//UserGender
								userProfile.put("gender", (String)user.getProperty("gender").toString());
								

								// Save the user profile info in a user property
								ParseUser currentUser = ParseUser
										.getCurrentUser();
								currentUser.put("Sliderprofile", userProfile);
								currentUser.put("Gender", (String)user.getProperty("gender").toString());
								currentUser.saveInBackground();

								// Show the user info
								updateViewsWithProfileInfo();
								
							} catch (JSONException e) {
								e.printStackTrace();
								Log.d(NearMateApp.TAG,"Error parsing returned user data.");
							}

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(NearMateApp.TAG,
										"The facebook session was invalidated.");
								onLogoutButtonClicked();
							} else {
								Log.d(NearMateApp.TAG,
										"Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
				});
		request.executeAsync();

	}
		
		/**
		 * Populating slider view with fb details
		 */
		private void updateViewsWithProfileInfo() {
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser.get("Sliderprofile") != null) {
				JSONObject userProfile = currentUser.getJSONObject("Sliderprofile");
				try {
					if (userProfile.getString("facebookId") != null) {
						facebookId = userProfile.get("facebookId")
								.toString();
						userProfilePictureView.setProfileId(facebookId);
					
						
					} else {
						// Show the default, blank user profile picture
						userProfilePictureView.setProfileId(null);
					}
					if (userProfile.getString("name") != null) {
						
						facebookname = userProfile.getString("name");
						tv_username.setText(userProfile.getString("name"));
						
					} else {
						tv_username.setText("");
					}
					
				} catch (JSONException e) {
					Log.d(NearMateApp.TAG,
							"Error parsing saved user data.");
				}
			}
			
			//saving user FB id and Name to parse
			saveFBidname();
		}

	/**
	 * This adapter is used to initialize and shoew the list data of Menu.
	 * @author asus i5
	 *
	 */
	public class MenuAdapter extends ArrayAdapter<MenuItem> {


		public MenuAdapter(Context context) {
			super(context, 0);
		}

		@SuppressLint("InflateParams")
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			//title.setTypeface(HelType);
			title.setText(getItem(position).tag);
			
			//convertView.findViewById(R.id.right_icon);
			return convertView;
		}
	}
	
	
	/**
	 * On Logout
	 */
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		getActivity().finish();

		// Go to the login view
		//startLoginActivity();
	}
	
	
	/**
	 * Saving FB details to parse
	 */
	public void saveFBidname(){
		try {
			
			ParseUser currentUser = ParseUser
					.getCurrentUser();
			currentUser.put("FacebookId", facebookId);
			currentUser.put("FacebookName", facebookname);
			
			NearMateApp.fb_user_id = facebookId;
			NearMateApp.fb_user_name = facebookname;
			
			currentUser.saveInBackground();
			//Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			//Toast.makeText(getActivity(), "Error occured Update later", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Share Near Mate
	 */
	
	private void shareNearMate(){
		
		//create the send intent
		Intent shareIntent = 
		 new Intent(android.content.Intent.ACTION_SEND);
		//set the type
		shareIntent.setType("text/plain");
		//add a subject
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
		 "Experience the New NEARMATE");
		String dummylink = "http://mindminesinfotech.com/";
		SpannableString content = new SpannableString(dummylink);
		 content.setSpan(new UnderlineSpan(), 0, dummylink.length(), 0);
		//build the body of the message to be shared				
		String shareMessage = "A new Era to stay connected:  \n  Meet \n  Attach  \n  Treat   \n  Enjoy  \n ";
		//add the message
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
		 shareMessage+ " " + content);
		//start the chooser for sharing
		startActivity(Intent.createChooser(shareIntent, 
		 "Share here"));
		
	}
	
	/**
	 * Contact US
	 */
	private void contactUs(){
		
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle("Contact Nearmate");
		   // builder.setMessage("Help us to make NearMate a better experience for everyone");
		    builder.setItems(new CharSequence[]
		            {"Repot issue", "Suggest us", "Partner with us"},
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                    // The 'which' argument contains the index position
		                    // of the selected item
		                    switch (which) {
		                        case 0:
		                            contactCase0();
		                            break;
		                        case 1:
		                        	contactCase1();
		                            break;
		                        case 2:
		                        	contactCase2();
		                            break;
		                        
		                    }
		                }
		            });
		    builder.create().show();
		
	}
	
	/**
	 * Case 0 for Report Issues
	 */
	private void contactCase0(){
		// get phone specifications
		fetchingDeviceDetails();
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { CONTACT_REPORT_ISSUES });
		
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, facebookname+ "," + " "+ "Reporting an issue of NearMate");

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Model Name:" +" "+PhoneModel +"\n"+ "Android Version"+" "+AndroidVersion+"\n"+"Please Write us here below:"+"\n"+"\n");

		startActivity(emailIntent);
	}
	
	
	/**
	 * Case 1 forSuggestions
	 */
	private void contactCase1(){
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { CONTACT_SUGGESTIONS });
		
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, facebookname+ "," + " "+ "want to suggest NearMate");

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please Write us here below:"+"\n"+"\n");

		startActivity(emailIntent);
	}
	
	
	/**
	 * Case 2 for Partner with us
	 */
	private void contactCase2(){
		
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		emailIntent.setType("plain/text");

		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { CONTACT_PARTNER });
		
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, facebookname+ "," + " "+ "want to be partner with Nearmate");

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please Write us here below:"+"\n"+"\n");

		startActivity(emailIntent);
	}
	

	
}
