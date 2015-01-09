package com.nearmate.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class AllSearch extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

		 private static final String TAG = "AllSearch";
		 private static  String match_Gender;
		 private static String match_item_selected;
		 private static String match_distance_selected;
		 private static String match_interest_selected;
		 private CardContainer mCardContainer;
		 private Button reload_btn;
		 private ProgressDialog progressbar;
		 private LocationClient mLocationClient;
		 private  ArrayList<String> user_search_name = new ArrayList<String>();
		 private  ArrayList<String> user_search_image = new ArrayList<String>();
		 private ArrayList<String> user_search_location = new ArrayList<String>();
		 
		 /**
		  *Array list for location results
		  */
		 private ArrayList<String> image_to_show_loc = new ArrayList<String>();
		 private ArrayList<String> name_to_show_loc = new ArrayList<String>();
		 private ArrayList<String> location_to_show_loc = new ArrayList<String>();
		 
		 /**
		  * Array list for interests selected
		  */
		 private ArrayList<String> image_to_show_interests = new ArrayList<String>();
		 private ArrayList<String> name_to_show_interests = new ArrayList<String>();
		 private ArrayList<String> location_to_show_interests = new ArrayList<String>();
		 private ArrayList<String> searched_user_interests = new ArrayList<String>();
		 private ArrayList<Double> searched_user_latitude = new ArrayList<Double>();
		 private ArrayList<Double> searched_user_longitude = new ArrayList<Double>();
		 private String City_current;
		 private String State_current;
		 private String Country_current;
	 
		private String my_search_criteria;
		private String check_filter_status;
		private String specific_interest_parse;
		private String search_interest_query_keyword;
		private Boolean case3Ready = false;
		private String check_interests_status;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		 // Create the LocationRequest object
		    mLocationClient = new LocationClient(getActivity(), this, this);
		    
		    SharedPreferences settings = getActivity().getSharedPreferences(NearMateApp.PREF_SETTINGS_SAVED, 0);
			check_filter_status = settings.getString(NearMateApp.PREF_SETTINGS_SAVED, "").toString();
			Log.e(TAG+ "Check filter", check_filter_status);
			
			View rootView = inflater.inflate(R.layout.all_search, container, false);
			
			mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
			reload_btn = (Button)rootView.findViewById(R.id.btn_reload);
		
		reload_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SharedPreferences settings = getActivity().getSharedPreferences(NearMateApp.PREF_SETTINGS_SAVED, 0);
				check_filter_status = settings.getString(NearMateApp.PREF_SETTINGS_SAVED, "").toString();
				Log.e(TAG+ "Check filter", check_filter_status);
				
				
				user_search_image.clear();
				user_search_location.clear();
				user_search_name.clear();
				image_to_show_loc.clear();
				name_to_show_loc.clear();
				location_to_show_loc.clear();
				searched_user_latitude.clear();
				searched_user_longitude.clear();
				searched_user_interests.clear();
				image_to_show_interests.clear();
				name_to_show_interests.clear();
				location_to_show_interests.clear();
				
				
				if(check_filter_status.equals("saved")){
					getMySearchCriteria();
				}else {
					Toast.makeText(getActivity(), "Please set your search criteria by pressing Filter on the top", Toast.LENGTH_LONG).show();
				}			
			}
		});
		
		SharedPreferences intrests = getActivity().getSharedPreferences(NearMateApp.PREF_INTEREST_INITIAL, 0);
		check_interests_status = intrests.getString(NearMateApp.PREF_INTEREST_INITIAL, "").toString();
		
		if(check_interests_status.equals("intially_saved")){
			
		}else {
			saveInitialInterest();
		}
		
		//
		if(check_filter_status.equals("saved")){
			//Start searching the people according to Interests
			getMySearchCriteria();
			
		}else {
			Toast.makeText(getActivity(), "Please set your search criteria by pressing Filter on the top", Toast.LENGTH_LONG).show();
		}
		
		
		return rootView;
	}
	
	
	/**
	 * Tinder like UI
	 */
	public void showTinderUI()
	{
		SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
		for(int i=0;i<user_search_image.size();i++){
			adapter.add(new CardModel(user_search_name.get(i), user_search_location.get(i), user_search_image.get(i)));
			//Log.e(TAG, user_search_image.get(i));
		}
		mCardContainer.setAdapter(adapter);
	}
	
	
	
	/**
	 * Tinder like UI for Near By Location Results
	 */
	public void showTinderUIForLocation(){
		SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
		for(int i=0;i<image_to_show_loc.size();i++){
			adapter.add(new CardModel(name_to_show_loc.get(i), location_to_show_loc.get(i), image_to_show_loc.get(i)));
			//Log.e(TAG, user_search_image.get(i));
		}
		mCardContainer.setAdapter(adapter);
	}
	
	
	/**
	 * Tinder like UI for My Interests Results
	 */
	public void showTinderUIForInterests(){
		SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
		for(int i=0;i<image_to_show_interests.size();i++){
			adapter.add(new CardModel(name_to_show_interests.get(i), location_to_show_interests.get(i), image_to_show_interests.get(i)));
			//Log.e(TAG, user_search_image.get(i));
		}
		mCardContainer.setAdapter(adapter);
	}
	
	
	/**
	 * Search Criteria
	 */
	private void getMySearchCriteria()
	
	{
		
		progressbar = new ProgressDialog(getActivity());
		progressbar.setMessage("Searching People near around you...");
		progressbar.show();
		try{
			ParseUser currentUser = ParseUser.getCurrentUser();
			 my_search_criteria = currentUser.getString("SearchCriteria");
			 
			 String[] finalCriteria = my_search_criteria.split(",");
			
			 match_item_selected = finalCriteria[0]; 
			
			 if(match_item_selected.equals("Located near me")){
				 match_distance_selected = finalCriteria[1].replace("km", "").trim(); 
			 }else if(match_item_selected.equals("My Interests")){
				 match_interest_selected = finalCriteria[1]; 
				 getMyInterset();
				
			 }
			   match_Gender = finalCriteria[2]; 
			 
			 Log.e(TAG+ "Search criteria", match_item_selected+" "+match_distance_selected+" "+match_interest_selected+" "+ match_Gender);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Case 1.
		if((match_Gender!="All") && match_item_selected.equals("Located anywhere")){
			case1();
			//new Case1Task().execute();
		}else if ((match_Gender!="All") && match_item_selected.equals("Located near me")) {
			case2();
		}else if ((match_Gender!="All") && match_item_selected.equals("My Interests")) {
			case3();
		}
		
		 if(progressbar.isShowing()){
			 progressbar.dismiss();
		}
	}
	
	
	/**
	 * set initial interests to parse
	 */
	
	private void saveInitialInterest(){
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		

		try{
			JSONObject userEducation = new JSONObject(); 
			userEducation.put("user_Education", "Not found");
			currentUser.put("Education", userEducation);
			currentUser.saveInBackground();
			
			JSONObject userZodiac = new JSONObject();
			userZodiac.put("user_Zodiac",  "Not found");
			currentUser.put("Zodiac", userZodiac);
			currentUser.saveInBackground();
			
			JSONObject userJob = new JSONObject();
			userJob.put("user_JobDesignation", "Not found");
			currentUser.put("JobDesignation", userJob);
			currentUser.saveInBackground();
			
			JSONObject userIncome = new JSONObject();
			userIncome.put("user_Income", "Not found");
			currentUser.put("Income", userIncome);
			currentUser.saveInBackground();
			
			JSONObject userLanguage = new JSONObject();
			userLanguage.put("user_Language", "Not found");
			currentUser.put("Language", userLanguage);
			currentUser.saveInBackground();
			
			JSONObject userHobby = new JSONObject();
			userHobby.put("user_Hobbies", "Not found");
			currentUser.put("Hobbies", userHobby);
			currentUser.saveInBackground();
			
			
			//currentUser.saveInBackground();
			
			SharedPreferences pref_interest_status = getActivity().getSharedPreferences(NearMateApp.PREF_INTEREST_INITIAL, 0);
			SharedPreferences.Editor edt_id = pref_interest_status.edit();
			edt_id.putString(NearMateApp.PREF_INTEREST_INITIAL,"intially_saved");
			edt_id.commit();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	 /**
	    * Fetching current location
	    */
	   
	   public static  JSONObject getLocationInfo(double lat, double lng) {
	        if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                    .permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	            HttpGet httpGet = new HttpGet(
	                    "http://maps.googleapis.com/maps/api/geocode/json?latlng="
	                            + lat + "," + lng + "&sensor=true");
	            HttpClient client = new DefaultHttpClient();
	            HttpResponse response;
	            StringBuilder stringBuilder = new StringBuilder();
	            try {
	                response = client.execute(httpGet);
	                HttpEntity entity = response.getEntity();
	                InputStream stream = entity.getContent();
	                int b;
	                while ((b = stream.read()) != -1) {
	                    stringBuilder.append((char) b);
	                }
	            } catch (ClientProtocolException e) {
	            } catch (IOException e) {
	            }
	            JSONObject jsonObject = new JSONObject();
	            try {
	                jsonObject = new JSONObject(stringBuilder.toString());
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	 
	            return jsonObject;
	        }
	        return null;
	    }

	   
	   
	@Override
	public void onStart() {
		super.onStart();
		 // Connect the client.
		mLocationClient.connect();
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		// Disconnect the client.
				mLocationClient.disconnect();
				super.onStop();
	}
	

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		
	}


	@Override
	public void onConnected(Bundle arg0) {
		 Log.e("here",  "Connected");
			
			/*double Lat;
			double Lon;*/
			//String currentLocation = "";
			Location mlocation = null;
				progressbar = new ProgressDialog(getActivity());
				progressbar.setMessage("Searching People near around you...");
				progressbar.show();
			
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD  && Geocoder.isPresent())
			      {
					try{
							if(mLocationClient != null && mLocationClient.isConnected()){
							   // Get the current location's latitude & longitude
							       mlocation = mLocationClient.getLastLocation();
							}else {
								mLocationClient.connect();
							}
							      // Display the current location in the UI
							   		// To display the current address in the UI
							       NearMateApp.Lat = mlocation.getLatitude();
							       NearMateApp.Lon = mlocation.getLongitude();
							      
							       JSONObject jsonObj = getLocationInfo(NearMateApp.Lat, NearMateApp.Lon);
							        String City = "";
							        String State = "";
							        String Country = "";
									try {
								            String status = jsonObj.getString("status").toString();
								            Log.i("status", status);
								            if (status.equalsIgnoreCase("OK")) {
								                JSONArray Results = jsonObj.getJSONArray("results");
								                JSONObject zero = Results.getJSONObject(0);
								                JSONArray address_components = zero
								                        .getJSONArray("address_components");
								 
								                for (int i = 0; i < address_components.length(); i++) {
								                    JSONObject zero2 = address_components.getJSONObject(i);
								                    String long_name = zero2.getString("long_name");
								                    JSONArray mtypes = zero2.getJSONArray("types");
								                    String Type = mtypes.getString(0);
								 
								                   if (Type.equalsIgnoreCase("locality")) {
								                        // Address2 = Address2 + long_name + ", ";
								                        City = long_name;
								                    } else if (Type
								                            .equalsIgnoreCase("administrative_area_level_1")) {
								                        State = long_name;
								                    } else if (Type.equalsIgnoreCase("country")) {
								                        Country = long_name;
								                    } 
								                }
								                NearMateApp.completelocation = City + ","
								                        + State + "," + Country;
								                NearMateApp.city = City;
								                NearMateApp.state = State;
								                NearMateApp.country = Country;
								                
								                Log.e("new test", NearMateApp.completelocation + NearMateApp.Lat +" "+ NearMateApp.Lon);
								                
								                //Saving the cureent lat and long to Parse
								                ParseUser currentUser = ParseUser.getCurrentUser();
								    			currentUser.put("PresentLocation", NearMateApp.completelocation);
								    			currentUser.put("Lattitude", NearMateApp.Lat);
								    			currentUser.put("Longitude", NearMateApp.Lon);
								    			currentUser.saveInBackground();
								            }
								        } catch (Exception e) {
								 
								        }
								       
				      
					}catch (IllegalStateException ex) {
						ex.printStackTrace();
				        // This will catch the exception, handle as needed
				    }
					
			      }else {
					Toast.makeText(getActivity(),
					"Unable to fetch your Address Your Device doesn't supports Geocoder", Toast.LENGTH_SHORT).show();
			      }

				 if(NearMateApp.completelocation!=null){
			    	  String[] finalAddress = NearMateApp.completelocation.split(",");
					    City_current = finalAddress[0]; 
					    State_current = finalAddress[1]; 
					    Country_current = finalAddress[2]; 
					    Log.e("testing", City_current+State_current+Country_current);
			    }
				 if(progressbar.isShowing()){
					 progressbar.dismiss();
				}
			   
	}

	@Override
	public void onDisconnected() {
	}
	
	/**
	 * Calculating the distance 
	 */
	private static double toRadians(double x)
    {
	 double PIx = 3.141592653589793;
        return x * PIx / 180;
    }

	private static long getDistanceMeters(double lat1, double lng1, double lat2, double lng2) {

    double l1 = toRadians(lat1);
    double l2 = toRadians(lat2);
    double g1 = toRadians(lng1);
    double g2 = toRadians(lng2);

    double dist = Math.acos(Math.sin(l1) * Math.sin(l2) + Math.cos(l1) * Math.cos(l2) * Math.cos(g1 - g2));
    if(dist < 0) {
        dist = dist + Math.PI;
    }

    return Math.round(dist * 6378100)/1000;
}
	
	/**
	 * Converting long to integer 
	 * @param long
	 * @return integer 
	 */
	private static int safeLongToInt(long l) {
	    if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
	        throw new IllegalArgumentException
	            (l + " cannot be cast to int without changing its value.");
	    }
	    return (int) l;
	}
	
	/**
	 * Get the Interest from the parse
	 */
	private void getMyInterset(){
		if(!progressbar.isShowing()){
			progressbar.show();
		}
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		if(match_interest_selected.equals("Education")){
			try {
				specific_interest_parse = currentUser.getJSONObject("Education").getString("user_Education");
				search_interest_query_keyword = "Education";
				Log.e(TAG + "interest fetched from parse", specific_interest_parse +" "+search_interest_query_keyword);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}else if (match_interest_selected.equals("Job")) {
			try {
				specific_interest_parse = currentUser.getJSONObject("JobDesignation").getString("user_JobDesignation");
				search_interest_query_keyword = "JobDesignation";
				Log.e(TAG + "interest fetched from parse", specific_interest_parse+" "+search_interest_query_keyword);
			} catch (JSONException e) {
					e.printStackTrace();
				}
		}else if (match_interest_selected.equals("Income")) {
			try {
				specific_interest_parse = currentUser.getJSONObject("Income").getString("user_Income");
				search_interest_query_keyword = "Income";
				Log.e(TAG + "interest fetched from parse", specific_interest_parse+" "+search_interest_query_keyword);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}else if (match_interest_selected.equals("Zodiac")) {
			try {
				specific_interest_parse = currentUser.getJSONObject("Zodiac").getString("user_Zodiac");
				search_interest_query_keyword = "Zodiac";
				Log.e(TAG + "interest fetched from parse", specific_interest_parse+" "+search_interest_query_keyword);	
			} catch (JSONException e) {
					e.printStackTrace();
				}
		}else if (match_interest_selected.equals("Language")) {
			try {
				specific_interest_parse = currentUser.getJSONObject("Language").getString("user_Language");
				search_interest_query_keyword = "Language";
				Log.e(TAG + "interest fetched from parse", specific_interest_parse+" "+search_interest_query_keyword);	
			} catch (JSONException e) {
					e.printStackTrace();
				}
		}else if (match_interest_selected.equals("Hobby")) {
			try {
				specific_interest_parse = currentUser.getJSONObject("Hobbies").getString("user_Hobbies");
				search_interest_query_keyword = "Hobbies";
				Log.e(TAG + "interest fetched from parse", specific_interest_parse+" "+search_interest_query_keyword);	
			} catch (JSONException e) {
					e.printStackTrace();
				}
		}else {
			Toast.makeText(getActivity(), "Internal error occured", Toast.LENGTH_SHORT).show();
		}
		
		if(specific_interest_parse!=null || specific_interest_parse.length()>0 || specific_interest_parse.equals("Not found")){
			case3Ready = true;
		}else {
			case3Ready = false;
		}
		
		if(progressbar.isShowing()){
			progressbar.dismiss();
		}
		
		
	}

	/**
	 * Case 1 is when gender will be Male of Female and Located anywhere
	 */
	private void case1 (){
		
		user_search_image.clear();
		user_search_location.clear();
		user_search_name.clear();
		image_to_show_loc.clear();
		name_to_show_loc.clear();
		location_to_show_loc.clear();
		searched_user_latitude.clear();
		searched_user_longitude.clear();
		searched_user_interests.clear();
		image_to_show_interests.clear();
		name_to_show_interests.clear();
		location_to_show_interests.clear();
		
		 try {
			 // 
			 
			 ParseQuery<ParseUser> query = ParseUser.getQuery();
			 if(my_search_criteria!=null){
				 query.whereEqualTo("Gender",match_Gender );
				// query.whereEqualTo("PresentLocation",  NearMateApp.completelocation);
				 query.findInBackground(new FindCallback<ParseUser>() {
				   public void done(List<ParseUser> objects, ParseException e) {
					   if (e == null) 
					     {
					           Log.e(TAG + "Case1","@@@@Retrieved " + objects.size());
					           for(int i=0;i<objects.size();i++)
					           {
						             try {
						            	     //For User Image
											 user_search_image.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("facebookId"));
										
											 //for user name
											 user_search_name.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("name"));
										
											 //for user locaton
											 user_search_location.add(i, objects.get(i).getString("PresentLocation"));
										
						             } catch (JSONException e1) {
										e1.printStackTrace();
										}
						          }//for loop ends here
					           
					           
					           //Delete the current user details from the arraylist
					           for(int i=0;i<user_search_image.size();i++){
					        	   if(user_search_image.get(i).equals(NearMateApp.fb_user_id)){
					        		   
					        		   //delete the user image
					        		   user_search_image.remove(i);
					        		   user_search_name.remove(i);
					        		   user_search_location.remove(i);
					        	   }
					           }
					        
					           if(user_search_image.size()>0){
					        	   showTinderUI();
					           }else{
					        	   Toast.makeText(getActivity(), "no results found", Toast.LENGTH_SHORT).show();
					           }
					           
					         
					      } else {
					           Log.e(TAG, "@@@Error: " + e.getMessage());
					        }
				    }
				 });
			 }else {
				Toast.makeText(getActivity(), "Set your search criteria", Toast.LENGTH_SHORT).show();
			}
			 
			 if(progressbar !=null && progressbar.isShowing()){
					progressbar.dismiss();
				}
			 
	     } catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	
	/**
	 * Case 2:  when gender will be Male of Female and  Located Near Me
	 */
	
	private void  case2(){
		
		
		user_search_image.clear();
		user_search_location.clear();
		user_search_name.clear();
		image_to_show_loc.clear();
		name_to_show_loc.clear();
		location_to_show_loc.clear();
		searched_user_latitude.clear();
		searched_user_longitude.clear();
		searched_user_interests.clear();
		image_to_show_interests.clear();
		name_to_show_interests.clear();
		location_to_show_interests.clear();
		
		
		 try {
			 ParseQuery<ParseUser> query = ParseUser.getQuery();
			 if(my_search_criteria!=null){
				 query.whereEqualTo("Gender",match_Gender );
				// query.whereEqualTo("PresentLocation",  NearMateApp.completelocation);
				 query.findInBackground(new FindCallback<ParseUser>() {
				   public void done(List<ParseUser> objects, ParseException e) {
					   if (e == null) 
					     {
					           Log.e(TAG +" Case2","@@@@Retrieved " + objects.size());
					           for(int i=0;i<objects.size();i++)
					           {
						             try {
						            	 			//For User Image
													user_search_image.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("facebookId"));
												
													//for user name
													 Log.e(TAG + "user name", objects.get(i).getJSONObject("Sliderprofile").getString("name"));
													 user_search_name.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("name"));
													 
													//for user location
														Log.e("UserPresentLocation" + i, ""+objects.get(i).getString("PresentLocation"));
														user_search_location.add(i, objects.get(i).getString("PresentLocation"));
												 
												 	//for user latitude
													Log.e("User Latitude" + i, ""+objects.get(i).getDouble("Lattitude"));
													searched_user_latitude.add(i, objects.get(i).getDouble("Lattitude"));
													
													//for user longitude
													Log.e("User Longitude" + i, ""+objects.get(i).getDouble("Longitude"));
													searched_user_longitude.add(i, objects.get(i).getDouble("Longitude"));
													
										
						             } catch (JSONException e1) {
										e1.printStackTrace();
										}
						          }//for loop ends here
					           
					           //Deleting the current user details
					           for(int i=0;i<user_search_image.size();i++){
					        	   
					        	   if(user_search_image.get(i).equals(NearMateApp.fb_user_id)){
					        		   
					        		   //delete the user image
					        		   user_search_image.remove(i);
					        		   user_search_name.remove(i);
					        		   user_search_location.remove(i);
					        		   searched_user_latitude.remove(i);
					        		   searched_user_longitude.remove(i);
					        	   }
					           }
					           for(int j=0;j<user_search_image.size();j++){
					        	   double tosearch_lat = searched_user_latitude.get(j);
					        	   double tosearch_lon = searched_user_longitude.get(j);
					        	   
					        	   long Resultant_distance = getDistanceMeters(NearMateApp.Lat, NearMateApp.Lon, tosearch_lat, tosearch_lon);
					        	   Log.e(TAG + " Result of Distance" + " " +j, " "+Resultant_distance );

					        	 int resultantditance = safeLongToInt(Resultant_distance);
					        	 int user_criteria_distance = Integer.parseInt(match_distance_selected);
					        	   
					        	   if(resultantditance <= user_criteria_distance ){
					        		 Log.e(TAG +"what to do", "result in favour"  );
					        		String image_to_show = user_search_image.get(j);
					        		image_to_show_loc.add(user_search_image.get(j));
					        		
					        		String name_to_show = user_search_name.get(j);
					        		name_to_show_loc.add(user_search_name.get(j));
					        		
					        		String location_to_show = user_search_location.get(j);
					        		location_to_show_loc.add(user_search_location.get(j));
					        		
					        		
					        		Log.e(TAG+ "final user to show location", 
					        				"Name:" + name_to_show +" "+ "Image:" + image_to_show+" "+ "location:" + location_to_show);
					        	}
					           }
					           
					           if(progressbar !=null && progressbar.isShowing()){
									progressbar.dismiss();
								}
					           
					           if(image_to_show_loc.size()>0){
					        	   showTinderUIForLocation();
					           }else{
					        	   Toast.makeText(getActivity(), "no results found", Toast.LENGTH_SHORT).show();
					           }
					      } else {
					           Log.e(TAG, "@@@Error: " + e.getMessage());
					   }
				    }
				 });
			 }else {
				Toast.makeText(getActivity(), "Set your search criteria", Toast.LENGTH_SHORT).show();
			}
			 
			 if(progressbar !=null && progressbar.isShowing()){
					progressbar.dismiss();
				}
	     } catch (Exception e) {
				e.printStackTrace();
		}
		
	}
	
	
	
	
	/**
	 * Case 3  when gender will be Male of Female and need to match interest selected by the user
	 */
	
	@SuppressWarnings("deprecation")
	private void case3(){
		SharedPreferences intrests = getActivity().getSharedPreferences(NearMateApp.PREF_INTEREST_INITIAL, 0);
		check_interests_status = intrests.getString(NearMateApp.PREF_INTEREST_INITIAL, "").toString();
		Log.e(TAG+ "Check filter", check_interests_status);
		
		if(case3Ready){
			user_search_image.clear();
			user_search_location.clear();
			user_search_name.clear();
			image_to_show_loc.clear();
			name_to_show_loc.clear();
			location_to_show_loc.clear();
			searched_user_latitude.clear();
			searched_user_longitude.clear();
			searched_user_interests.clear();
			image_to_show_interests.clear();
			name_to_show_interests.clear();
			location_to_show_interests.clear();
			
			 try {
				 final ParseQuery<ParseUser> query = ParseUser.getQuery();
				 if(my_search_criteria!=null){
					
					 
					// query.whereEqualTo("PresentLocation",  NearMateApp.completelocation);
					 query.findInBackground(new FindCallback<ParseUser>() {
					   public void done(List<ParseUser> objects, ParseException e) {
						   if (e == null) 
						     {
							   query.whereEqualTo("Gender",match_Gender );
							   
						           Log.e(TAG +" Case3","@@@@Retrieved " + objects.size());
						           for(int i=0;i<objects.size();i++)
						           {
							             try {
							            	
							            	
							            		
							            			//For User Image
													user_search_image.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("facebookId"));
													
													//for user name
													user_search_name.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("name"));
													
													//for user location
													user_search_location.add(i, objects.get(i).getString("PresentLocation"));
													
													//for user interest
													Log.e(TAG + "intrest to find",search_interest_query_keyword);
							
													searched_user_interests.add(i, objects.get(i).getJSONObject(search_interest_query_keyword).getString("user_"+search_interest_query_keyword));
							            		
							            	
							            	 
							            	 			
											
							             } catch (JSONException e1) {
											e1.printStackTrace();
											}
							          }//for loop ends here
						        
						           //deleting the current user details
						           for(int i=0;i<user_search_image.size();i++){
						        	   
						        	   if(user_search_image.get(i).equals(NearMateApp.fb_user_id)){
						        		   
						        		   user_search_image.remove(i);
						        		   user_search_name.remove(i);
						        		   user_search_location.remove(i);
						        		   searched_user_interests.remove(i);
						        	   }
						           }
						           
						           //deleting the user who have no intrest saved
						           
						           	for(int i=0;i<user_search_image.size();i++){
						        	   
						        	   if(searched_user_interests.get(i).equals("Not found") ){
						        		   
						        		   user_search_image.remove(i);
						        		   user_search_name.remove(i);
						        		   user_search_location.remove(i);
						        		   searched_user_interests.remove(i);
						        	   }
						           }
						           
						           //adding the final selected users.
						           for(int j=0;j<searched_user_interests.size();j++){
						        	   if(searched_user_interests.get(j).equals(specific_interest_parse)){
						        		   Log.e(TAG +" adding", specific_interest_parse);
						        		  
						        		   image_to_show_interests.add(j, user_search_image.get(j));
						        		   name_to_show_interests.add(j, user_search_name.get(j));
						        		   location_to_show_interests.add(j, user_search_location.get(j));
						        	 
						        	   }
						           }
						           
						           if(progressbar !=null && progressbar.isShowing()){
										progressbar.dismiss();
									}
						           
						           if(image_to_show_interests.size()>0){
						        	   showTinderUIForInterests();
						           }else{
						        	   Toast.makeText(getActivity(), "no results found", Toast.LENGTH_SHORT).show();
						           }
						      } else {
						           Log.e(TAG, "@@@Error: " + e.getMessage());
						   }
					    }
					 });
				 }else {
					Toast.makeText(getActivity(), "Set your search criteria", Toast.LENGTH_SHORT).show();
				}
				 
				 if(progressbar !=null && progressbar.isShowing()){
						progressbar.dismiss();
					}
				 
		     } catch (Exception e) {
					e.printStackTrace();
			}
	
		//if case 3 not ready set alert box
		}else {
			AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create(); //Read Update
			alertDialog.setTitle("Alert");
			alertDialog.setMessage("To search user according to your interests, please update your profile details." );
			alertDialog.setCancelable(false);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					}
			});
			alertDialog.show(); 
			 if(progressbar.isShowing()){
				 progressbar.dismiss();
			}
		}
	}
	
}
