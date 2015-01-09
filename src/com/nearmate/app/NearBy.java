package com.nearmate.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.google.android.gms.location.LocationClient;
import com.nearmate.app.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NearBy extends Fragment  {

	private static final String TAG = "NearByTest";
	
	public  ProgressDialog pd;
	LocationClient mLocationClient;
	

	private boolean taskExecuted = false;
	
	
	
	// public static String [] user_name_List;
	 private  ArrayList<String> user_search_name = new ArrayList<String>();
	 

	 private  ArrayList<String> user_search_image = new ArrayList<String>();
	 
	 private ArrayList<String> user_search_location = new ArrayList<String>();
	 
	 private ArrayList<Double> searched_user_latitude = new ArrayList<Double>();
	 
	 private ArrayList<Double> searched_user_longitude = new ArrayList<Double>();
	 
	 
	 
	 
	private CardContainer mCardContainer;
	private Button reload_btn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.nearbytest, container, false);
		
		mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
		reload_btn = (Button)rootView.findViewById(R.id.btn_reload);
		
		reload_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				user_search_image.clear();
				user_search_location.clear();
				user_search_name.clear();
				searched_user_latitude.clear();
				searched_user_longitude.clear();
				
				new FetchingcurrentCityResults().execute();
				//searchNearByPeople();
			}
		});

		return rootView;
	}
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);

	    if (isVisibleToUser) {
	    	if(taskExecuted)
	    	{
	    	}else {
	    		Log.e(TAG, ""+NearMateApp.taskExecuted);
	    	
	    		FetchingcurrentCityResults obj_fetch = new FetchingcurrentCityResults();

	    	if(obj_fetch.getStatus() == AsyncTask.Status.PENDING){
	    	    // My AsyncTask has not started yet
	    		new FetchingcurrentCityResults().execute();
	    		Log.e(TAG, "My AsyncTask has not started yet");
	    		//searchNearByPeople();
	    	}

	    	if(obj_fetch.getStatus() == AsyncTask.Status.RUNNING){
	    	    // My AsyncTask is currently doing work in doInBackground()
	    		Log.e(TAG, "My AsyncTask is currently doing work in doInBackground()");
	    	}

	    	if(obj_fetch.getStatus() == AsyncTask.Status.FINISHED){
	    	    // My AsyncTask is done and onPostExecute was called
	    		Log.e(TAG, "My AsyncTask is done and onPostExecute was called");
    	}
      
     
			}
	    }
	}
	
	
	/*public double CalculationByDistance(double initialLat, double initialLong, double finalLat, double finalLong){
	    PRE: All the input values are in radians!

	    double latDiff = finalLat - initialLat;
	    double longDiff = finalLong - initialLong;
	    double earthRadius = 6371; //In Km if you want the distance in km

	    double distance = 2*earthRadius*Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff/2.0),2)+Math.cos(initialLat)*Math.cos(finalLat)*Math.pow(Math.sin(longDiff/2),2)));

	    return distance;
	    }*/
	 
	
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

	    return Math.round(dist * 6378100);
	}
	
	//private class SearchNearByPeople extends AsyncTask<Void, String, Void>
	 
	   
	   /**
		 * Fetching cuurent adddress
		 */
		
		private class FetchingcurrentCityResults extends AsyncTask<Void, String, Void> {
			private ProgressDialog progressDialog;
			private String status;
			
			@Override
			protected void onPreExecute() {
			    progressDialog = new ProgressDialog(getActivity());
			    progressDialog.setMessage("Searching People near around you...");
			    progressDialog.show();
			    Log.e(TAG,   " In FetchingcurrentAddress");
			    
			}
			@Override
			protected Void doInBackground(Void... params) {
				
		    	
			     try {
					 ParseQuery<ParseUser> query = ParseUser.getQuery();
					 query.whereEqualTo("PresentLocation", NearMateApp.completelocation);
					 query.findInBackground(new FindCallback<ParseUser>() {
					   public void done(List<ParseUser> objects, ParseException e) {
						   if (e == null) 
						     {
						           Log.e(TAG,"@@@@Retrieved " + objects.size());
						           for(int i=0;i<objects.size();i++)
						           {
							             try {
							            	 //For User Image
											 user_search_image.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("facebookId"));
											 
											 //for User Name
											user_search_name.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("name"));
											
											//for user location
											user_search_location.add(i, objects.get(i).getString("PresentLocation"));
							             
											searched_user_latitude.add(i, objects.get(i).getDouble("Lattitude"));
											searched_user_longitude.add(i, objects.get(i).getDouble("Longitude"));
							             
							             } catch (JSONException e1) {
											e1.printStackTrace();
											}
							          }//for loop ends here
						         //deleting fetched current user image from array list
						           for(int i=0;i<user_search_image.size();i++){
						        	   if(NearMateApp.fb_user_id.equals(user_search_image.get(i))){
						        		   user_search_image.remove(i);
									   }
						           }
						           //deleting fetched current user name from array list
						           for(int i =0;i<user_search_name.size();i++){
						        	   if(NearMateApp.fb_user_name.equals(user_search_name.get(i))){
						        		  user_search_name.remove(i);
						                }
						           }
						           showTinderUI();
						      } else {
						           Log.e(TAG, "@@@Error: " + e.getMessage());
						        }
					    }
					 });
			     } catch (Exception e) {
						e.printStackTrace();
					}
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				 
				 SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
					for(int i=0;i<user_search_image.size();i++){
						adapter.add(new CardModel(user_search_name.get(i), user_search_location.get(i), user_search_image.get(i)));
						Log.e(TAG, user_search_image.get(i));
					}
					
					mCardContainer.setAdapter(adapter);
					taskExecuted = true;
					
					progressDialog.dismiss();
				
				super.onPostExecute(result);
			}
		}//Asynchronus task ends here
		
		
	    
	    /**
	     * Search Near By
	     */
		public void searchNearByPeople(){
	    	
	    	//Log.e("check","searchNearByPeople is invoked");
			    	
		     try {
				 ParseQuery<ParseUser> query = ParseUser.getQuery();
				 query.whereEqualTo("PresentLocation", NearMateApp.completelocation);
				 query.findInBackground(new FindCallback<ParseUser>() {
				   public void done(List<ParseUser> objects, ParseException e) {
					   if (e == null) 
					     {
					           Log.e(TAG,"@@@@Retrieved " + objects.size());
					           for(int i=0;i<objects.size();i++)
					           {
						             try {
						            	 //For User Image
										Log.e(TAG + i, ""+objects.get(i).getJSONObject("Sliderprofile").getString("facebookId"));
										 user_search_image.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("facebookId"));
										 
										 //for User Name
										Log.e(TAG + i, ""+objects.get(i).getJSONObject("Sliderprofile").getString("name"));
										user_search_name.add(i, objects.get(i).getJSONObject("Sliderprofile").getString("name"));
										
										//for user location
										
										Log.e(TAG + i, ""+objects.get(i).getString("PresentLocation"));
										user_search_location.add(i, objects.get(i).getString("PresentLocation"));
						             
										searched_user_latitude.add(i, objects.get(i).getDouble("Lattitude"));
										searched_user_longitude.add(i, objects.get(i).getDouble("Longitude"));
										
										Log.e(TAG+ "Lat and Long", objects.get(i).getDouble("Lattitude") +" "+objects.get(i).getDouble("Longitude"));
						             
						             
						             } catch (JSONException e1) {
										e1.printStackTrace();
										}
						          }//for loop ends here
					         //deleting fetched current user image from array list
					           for(int i=0;i<user_search_image.size();i++){
					        	   if(NearMateApp.fb_user_id.equals(user_search_image.get(i))){
					        		   user_search_image.remove(i);
								   }
					           }
					           //deleting fetched current user name from array list
					           for(int i =0;i<user_search_name.size();i++){
					        	   if(NearMateApp.fb_user_name.equals(user_search_name.get(i))){
					        		  user_search_name.remove(i);
					                }
					           }
					           
					           
					           for(int j=0;j<searched_user_latitude.size();j++){
					        	   double tosearch_lat = searched_user_latitude.get(j);
					        	   double tosearch_lon = searched_user_longitude.get(j);
					        	  // double Resultant_distance =  CalculationByDistance(NearMateApp.Lat, NearMateApp.Lon, tosearch_lat, tosearch_lon);
					        	   double Resultant_distance = getDistanceMeters(NearMateApp.Lat, NearMateApp.Lon, tosearch_lat, tosearch_lon);
					        	   
					        	   Log.e(TAG + " Result of Distance", ""+Resultant_distance);
					           }
					          
					           
					           
					    
					          
					           //setting Adapter to grid view 
					          // main_grid_view.setAdapter(new CustomAdapterforGridView(getActivity(), user_search_name,user_search_image));
					           showTinderUI();
					      } else {
					           Log.e(TAG, "@@@Error: " + e.getMessage());
					        }
				    }
				 });
		     } catch (Exception e) {
					e.printStackTrace();
				}
		}
		
		
		
		/**
		 * Tinder like UI
		 */
		public void showTinderUI()
		{
			SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
			for(int i=0;i<user_search_image.size();i++){
				adapter.add(new CardModel(user_search_name.get(i), user_search_location.get(i), user_search_image.get(i)));
				Log.e(TAG, user_search_image.get(i));
			}
			
			/*CardModel carmodel = new CardModel("mtittle", "mDescription", user_search_image.get(0));
			adapter.add(carmodel);*/
			
			mCardContainer.setAdapter(adapter);
			
		}




		
		// 22.730504, 75.908062  Ankit Sir
		// 22.752422, 75.889339 Nitish Agrawal
		// 22.711855, 75.842652 Arpit Sir.
		// 22.694030, 75.845715 Ayush Rawal
		
//		11-05 19:21:38.351: E/NearByTest Result of Distance(5820): 39.81041639214083
//		11-05 19:21:38.352: E/NearByTest Result of Distance(5820): 115.05553305311612
//		11-05 19:21:38.353: E/NearByTest Result of Distance(5820): 408.9941013522281
//		11-05 19:21:38.354: E/NearByTest Result of Distance(5820): 446.6450374307119

		
		//22.752395, 75.890152
		
		
		
		
		
}
