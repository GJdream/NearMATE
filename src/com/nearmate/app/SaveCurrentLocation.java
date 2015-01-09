package com.nearmate.app;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.parse.ParseUser;

public class SaveCurrentLocation implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	
	private static final String TAG = "SaveCurrentLocation";
	
	static LocationClient mLocationClient;
	static Context context;
	
	
	
	
	 /**
	    * Fetching current location
	    */
	   
	   public static JSONObject getLocationInfo(double lat, double lng) {
		   
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
	   
	   
	   /**
	    * Fetching current location Lat Long
	    * 
	    */
	   
	   public static String getCurrentLocationViaJSON(double lat, double lng) {
		  
	        JSONObject jsonObj = getLocationInfo(lat, lng);
	        Log.i(TAG, jsonObj.toString());
	        
	        
	       
	        String City = "";
	        String State = "";
	        String Country = "";
	        String currentLocation = "";
	 
	        try {
	            String status = jsonObj.getString("status").toString();
	            Log.i(TAG, status);
	 
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
	                currentLocation = City + ","
	                        + State + "," + Country;
	                
	            }
	        } catch (Exception e) {
	 
	        }
	      //  FetchingcurrentAddress();
	        return currentLocation;
	        
	       
	        
	    }
	   
	   
	   /**
		 * Fetching cuurent adddress
		 */
		
		public  String FetchingcurrentAddress () {
			
			 // Create the LocationRequest object
		    mLocationClient = new LocationClient(context, this, this);
		    mLocationClient.connect();
			
			double Lat;
			double Lon;
			String currentLocation = "";
			
				
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD  && Geocoder.isPresent())
			      {
						   // Get the current location's latitude & longitude
						      Location mlocation = mLocationClient.getLastLocation();
						      // Display the current location in the UI
						   // To display the current address in the UI
						       Lat = mlocation.getLatitude();
						       Lon = mlocation.getLongitude();
						      
						       
						       JSONObject jsonObj = getLocationInfo(Lat, Lon);
						       
						        String City = "";
						        String State = "";
						        String Country = "";
								try {
							            String status = jsonObj.getString("status").toString();
							            Log.i(TAG, status);
							 
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
							                currentLocation = City + ","
							                        + State + "," + Country;
							                
							            }
							        } catch (Exception e) {
							 
							        }
							       
			      }else {
					
					
			      }
			
	
		
			    if(currentLocation!=null){
			    	  String[] finalAddress = currentLocation.split(","); 
						
			    }
			    
			 saveCurrentLocationToParse();
			 
			 try{
				 Thread.sleep(1000);
			 }catch(Exception e){
				 e.printStackTrace();
			 }
			return currentLocation;
			 
			 //NearMateApp.taskExecuted = true;
			
				
			
		}//Asynchronus task ends here
		
		
		
		/**
		 * Saving current location to parse
		 */
	    public static void  saveCurrentLocationToParse()
	    {
	    	
	    	try {
				ParseUser currentUser = ParseUser.getCurrentUser();
				//NearMateApp.completelocation = City_current+","+State_current+","+Country_current; 
				//currentUser.put("PresentLocation", NearMateApp.completelocation);
				currentUser.saveInBackground();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    
	

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
