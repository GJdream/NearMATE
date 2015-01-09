package com.nearmate.app;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gms.location.LocationClient;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileAbout extends Fragment {
	
	private static final String TAG = "ProfileAbout";
	
	LocationClient mLocationClient;
	
	private ProfilePictureView userProfilePictureView;
	private TextView userNameView;
	private TextView userLocationView;
	private TextView userGenderView;
	private ImageView edit_aboutMe;
	String str_about_user;
	private TextView tv_about_me;
	
	private String fb_user_id;
	private String fb_user_name;
	private String fb_user_location;
	private String fb_user_gender;
	private String fb_user_birthday;
	boolean facebook_session_invalidate;
	boolean internal_error_occured;
	private String parse_user_about_me;
	private int fb_user_test;

	private TextView tv_birthday;

	private GridView likes_grid_view;
	
	//User Likes Name
	 private  ArrayList<String> user_likes_name = new ArrayList<String>();
	
	 //User likes image
	 private  ArrayList<String> user_likes_image = new ArrayList<String>();

	private ListView likes_list_view;

	private EditText ed_pass_test;

	 
	 
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_about, container, false);
		
		
	    
		userProfilePictureView = (ProfilePictureView)rootView.findViewById(R.id.user_image);
		userNameView = (TextView)rootView.findViewById(R.id.userName);
		userLocationView = (TextView)rootView.findViewById(R.id.tv_location);
		userGenderView = (TextView) rootView.findViewById(R.id.tv_gender_and_age);
		edit_aboutMe = (ImageView)rootView.findViewById(R.id.edit_about_me);
		tv_about_me = (TextView)rootView.findViewById(R.id.tv_about_me);
		tv_birthday = (TextView)rootView.findViewById(R.id.tv_birthday);
		
		//for test useless
		//ed_pass_test = (EditText)rootView.findViewById(R.id.password);
	
		//mHlvCustomList = (HorizontalListView) rootView.findViewById(R.id.hlvCustomList);
		//likes_grid_view = (GridView)rootView.findViewById(R.id.grid_view_likes);
		//likes_list_view = (ListView)rootView.findViewById(R.id.list_view_likes);
		

		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
		
			makeMeRequest();
			fetchAboutMe();
		}
		
		
		/*ed_pass_test.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		  

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId == R.id.action_sign_in) {
	            // Do sign in
				Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
	            return true;
	        }
	        return false;
		}
		});*/
		
		edit_aboutMe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				showAlertaboutMe();
				
				
			}
		});
		
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			updateViewsWithProfileInfo();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
		}
	}
	
	
	/**
	 * On Click of About Me
	 */

	public void showAlertaboutMe(){

		/* Alert Dialog Code Start*/     
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setMessage("About Me"); //Message here

        // Set an EditText view to get user input 
        final EditText input = new EditText(getActivity());
        
        input.setText(tv_about_me.getText().toString().trim());
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
         str_about_user = input.getEditableText().toString();
         tv_about_me.setText(str_about_user);
         saveAboutMe();
        } // End of onClick(DialogInterface dialog, int whichButton)
    }); //End of alert.setPositiveButton
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
              dialog.cancel();
          }
    }); //End of alert.setNegativeButton
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
   /* Alert Dialog Code End*/    
		
	}
	
	
	/**
	 * Saving About Me text to the parse
	 */
	public void saveAboutMe(){
	
		try {
			
			ParseUser currentUser = ParseUser.getCurrentUser();
			currentUser.put("TestAboutMe", str_about_user);
			currentUser.put("FacebookId", fb_user_id);
			currentUser.saveInBackground();
			Toast.makeText(getActivity(), "Successfully Updated", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "Error occured Update later", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Fetch AboutMe of User
	 */
	public void fetchAboutMe(){
		ParseUser currentUser = ParseUser
				.getCurrentUser();
		String check = currentUser.getString("TestAboutMe");
		if(check == null || check.length()<0 )
		{
			parse_user_about_me = "Not found";
			tv_about_me.setText(parse_user_about_me);
		}
		else {
			parse_user_about_me = currentUser.getString("TestAboutMe");
			tv_about_me.setText(parse_user_about_me);
		}
	}
	
	
	
	/**
	 * Make Request from Graph API of Facebook
	 */

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							try {
								// Populate the JSON object
								//Facebook ID
								fb_user_id = user.getId();
								
								//User Name
								fb_user_name = user.getName();
								
								//User Gender
								//fb_user_gender = (String) user.getProperty("gender");
								fb_user_gender = (String)user.getProperty("gender").toString();
								
								//User birthdAY
								fb_user_birthday = (String)user.getProperty("birthday").toString();
								//Log.e(TAG, fb_user_birthday);
								
								//User LOcation
								fb_user_location = (String) user.getLocation().getProperty("name");
								
								Log.e("Check_All", fb_user_id +" "+fb_user_name+" "+fb_user_location+" "+ fb_user_gender+" "+fb_user_birthday+" "+fb_user_test);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							//fetching likes
							fetchUserLikes();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//fetching interest
							fetchUserInterest();
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//fetching friend list
							fetchUserFriend();
							//save fetched details to parse
							saveToParse();
							
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


	private void updateViewsWithProfileInfo() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.get("profile") != null) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {
				if (userProfile.getString("facebookId") != null) {
					String facebookId = userProfile.get("facebookId")
							.toString();
					userProfilePictureView.setProfileId(facebookId);
					userProfilePictureView.setCropped(true);
					
					SharedPreferences settings = getActivity().getSharedPreferences(NearMateApp.PREF_USER_IMAGE, 0);
					SharedPreferences.Editor edt = settings.edit();
					edt.putString("facbookId", facebookId);
					edt.commit();
					
				} else {
					// Show the default, blank user profile picture
					userProfilePictureView.setProfileId(null);
				}
				if (userProfile.getString("name") != null) {
					userNameView.setText(userProfile.getString("name"));
					
					SharedPreferences usernameSlider = getActivity().getSharedPreferences(NearMateApp.PREF_USER_NAME, 0);
					SharedPreferences.Editor edt_name = usernameSlider.edit();
					edt_name.putString("username", userProfile.getString("name"));
					edt_name.commit();
					
					
				} else {
					userNameView.setText("");
				}
				if (userProfile.getString("gender") != null) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}
				if(userProfile.getString("birthday")!= null) {
					tv_birthday.setText( fb_user_birthday);
					Log.e(TAG, userProfile.getString("birtday"));
				}else {
					  
				}
				
				if (userProfile.getString("location") != null) {
					userLocationView.setText(userProfile.getString("location"));
				} else {
					
						//new  FetchingcurrentAddress().execute();
					
				}
			
//				progressDialogAll.dismiss();
				Log.e(TAG, "progress dialog dismissed");
				
			} catch (JSONException e) {
				Log.d(NearMateApp.TAG,
						"Error parsing saved user data.");
			}
		}
	}

	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		getActivity().finish();

		// Go to the login view
		//startLoginActivity();
	}

	private void startLoginActivity() {
		//UserDetailsActivity.this.finish();
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	public void getAboutMe(){
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.get("profile") != null) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try{
				if(userProfile.getString("aboutme")!= null) {
					tv_about_me.setText(userProfile.getString("aboutme"));
				}else {
					tv_about_me.setText(R.string.Sample_about_me);
				}
			}catch(Exception e){
				e.printStackTrace();
				//tv_about_me.setText(R.string.Sample_about_me);
			}
		}
	}
	
	
	public void saveToParse(){
		
		JSONObject userProfile = new JSONObject();
		try {
			// Populate the JSON object
			//Facebook ID
			userProfile.put("facebookId", fb_user_id);
						
			//User Name
			userProfile.put("name", fb_user_name);
						
			//User LOcation
				userProfile.put("location", (String) fb_user_location);
		
			//User Gender
				userProfile.put("gender",(String) fb_user_gender);
			
			//User Gender
				userProfile.put("birthday",fb_user_birthday);
		
			
			// Save the user profile info in a user property
			ParseUser currentUser = ParseUser.getCurrentUser();
			
			currentUser.put("profile", userProfile);
			
			currentUser.saveInBackground();

			
			updateViewsWithProfileInfo();
			
			// Show the user info
			//updateViewsWithProfileInfo();
		} catch (JSONException e) {
			Log.d(NearMateApp.TAG,
					"Error parsing returned user data.");
		}
	}
	
	public void fetchUserLikes(){

		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
		
			/* make the API call */
			new Request(
			    ParseFacebookUtils.getSession(),
			    "/me/likes",
			    null,
			    HttpMethod.GET,
			    new Request.Callback() {
			        public void onCompleted(Response response) {
			        	 GraphObject graphObject = response.getGraphObject();
			               
			                if (graphObject != null) {
			                    JSONObject jsonObject = graphObject.getInnerJSONObject();
			                    try {
			                     JSONArray array = jsonObject.getJSONArray("data");
			                     for (int i = 0; i < array.length(); i++) {
			                         JSONObject object = (JSONObject) array.get(i);
			                         
			                         Log.d(TAG + "likes", "id = "+object.get("id"));
			                         user_likes_image.add(i, (String) object.get("id"));
			                         Log.d(TAG + "likes", "name = "+object.get("name") );
			                         user_likes_name.add(i, (String) object.get("name"));
			                      }
			                } catch (JSONException e) {

			                 e.printStackTrace();
			                }	
			        
			        }else {
			        	Log.e(TAG + "likes", "graphObject == null");
					}
			        }
			    }
			).executeAsync();
		}else {
			Log.e(TAG, "Sesion closed");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//likes_grid_view.setAdapter(new CustomAdapterforGridView(getActivity(), user_likes_name,user_likes_image));
		
	}
	
	public void fetchUserInterest(){
		
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			
			/* make the API call */
			new Request(
			    session,
			    "/me/interests",
			    null,
			    HttpMethod.GET,
			    new Request.Callback() {
			        public void onCompleted(Response response) {
			          
			        	GraphObject graphObject = response.getGraphObject();
			               
		                if (graphObject != null) {
		                    JSONObject jsonObject = graphObject.getInnerJSONObject();
		                    try {
		                     JSONArray array = jsonObject.getJSONArray("data");
		                     for (int i = 0; i < array.length(); i++) {
		                         JSONObject object = (JSONObject) array.get(i);
		                         Log.d(TAG +"Interests", "id = "+object.get("id"));
		                         Log.d(TAG +"Interests", "name = "+object.get("name") );
		                      }
		                } catch (JSONException e) {

		                 e.printStackTrace();
		                }	
		                }else {
							Log.e(TAG +"Interests", "graphObject == null");
						}
			        	
			        }
			    }
			).executeAsync();
		}else {
			Log.e(TAG, "session == null && session is closed");
		}
	}
	
	
	/**
	 * Fetching Friends
	 */
	 
	public void fetchUserFriend(){
		
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			/* make the API call */
			new Request(
			    session,
			    "/me/friends",
			    null,
			    HttpMethod.GET,
			    new Request.Callback() {
			        public void onCompleted(Response response) {
			            

			        	GraphObject graphObject = response.getGraphObject();
			               
		                if (graphObject != null) {
		                    JSONObject jsonObject = graphObject.getInnerJSONObject();
		                    try {
		                     JSONArray array = jsonObject.getJSONArray("data");
		                     for (int i = 0; i < array.length(); i++) {
		                         JSONObject object = (JSONObject) array.get(i);
		                         Log.d(TAG + "friends", "id = "+object.get("id"));
		                         Log.d(TAG + "friends", "name = "+object.get("name") );
		                      }
		                } catch (JSONException e) {

		                 e.printStackTrace();
		                }	
		                }else {
		                	Log.e(TAG + "friends", "graphObject == null");
						}
			        	
			        }
			    }
			).executeAsync();
			
			
		}else {
			Log.e(TAG, "session == null && session is closed");
		}
		
		
	}
	
}
