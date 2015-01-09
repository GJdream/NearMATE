package com.nearmate.app;

import java.util.List;

import org.json.JSONObject;

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
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchedProfileDetails extends Fragment{

	private TextView tv_education;
	private TextView tv_job;
	private TextView tv_income;
	private TextView tv_hobbies;
	private TextView tv_language;
	
	private String Fb_id_rec;
	private String Fb_name_rec;
	
	private String mZodiac;
	private String mJob;
	private String mIncome;
	private String mLanguage;
	private String mEducation;
	private TextView tv_zodiac;
	private String mHobbies;
	private boolean taskExecuted = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.searched_profile_detail, container, false);
		
		tv_education = (TextView)rootView.findViewById(R.id.tv_education);
		tv_job = (TextView)rootView.findViewById(R.id.tv_job);
		tv_income = (TextView)rootView.findViewById(R.id.tv_Income);
		tv_language = (TextView)rootView.findViewById(R.id.tv_language);
		tv_hobbies = (TextView)rootView.findViewById(R.id.tv_Hobby);
		tv_zodiac = (TextView)rootView.findViewById(R.id.tv_horoscope);
		
		/**
		 * Getting values from Intent
		 */
		 Fb_id_rec = getActivity().getIntent().getExtras().getString("fb_user_id");
		 Fb_name_rec = getActivity().getIntent().getExtras().getString("fb_user_name");
		Log.e("check in rec new", Fb_id_rec+" "+Fb_name_rec);
		
		//fetchAboutMe();
		
		/*test_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new FetchMyDetail().execute();
			}
		});*/
		
		
		
		
		return rootView;
	
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);

	    if (isVisibleToUser) {

			    	if(taskExecuted)
			    	{
			    		// Do nothing
				    	}else {
				    	
				    	FetchMyDetail obj_fetch = new FetchMyDetail();
			
				    	if(obj_fetch.getStatus() == AsyncTask.Status.PENDING){
				    	    // My AsyncTask has not started yet
				    		new FetchMyDetail().execute();
				    		
				    		//Log.e("test_mine", "My AsyncTask has not started yet");
				    	}
			
				    	if(obj_fetch.getStatus() == AsyncTask.Status.RUNNING){
				    	    // My AsyncTask is currently doing work in doInBackground()
				    		Log.e("test_mine", "My AsyncTask is currently doing work in doInBackground()");
				    	}
			
				    	if(obj_fetch.getStatus() == AsyncTask.Status.FINISHED){
				    	    // My AsyncTask is done and onPostExecute was called
				    		Log.e("test_mine", "My AsyncTask is done and onPostExecute was called");
				    		
				    	}
			    }
	    }
	}
	
	/**
	 * Fetching the user details of Interest from parse
	 */
	
	
	
	/*
	public void fetchAboutMe(){
		

		  try {
				 ParseQuery<ParseUser> query = ParseUser.getQuery();
				 query.whereEqualTo("FacebookId", Fb_id_rec);
				 query.findInBackground(new FindCallback<ParseUser>() {
				   public void done(List<ParseUser> objects, ParseException e) {
					   if (e == null) 
					     {
					           Log.e("with location ","@@@@Retrieved " + objects.size());
					        	   Log.e("check_searched", objects.toString());
					        	   try{
					        		   for(int i=0;i<objects.size();i++){
					        			    //check_about =  objects.get(i).getString("TestAboutMe");
					        			   JSONObject check_about = objects.get(i).getJSONObject("Education");
					        			   Log.e("check_about", ""+check_about);
					        			   String fin = check_about.getString("user_education");
					        			   Log.e("check_about", fin);
					        		   }
					        		  
					        	   }catch(Exception e1){
					        		   e1.printStackTrace();
					        	   }
					     }
				    }
				 });
		     } catch (Exception e) {
					e.printStackTrace();
				}
	}*/
	
	
	
	/**
	 * FetchMyDetail class will fetch the user Details from the parse
	 * @author Ankur
	 *
	 */
	private class FetchMyDetail extends AsyncTask<String, Void, Void> {
		ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(String... params) {

			  try {
							 ParseQuery<ParseUser> query = ParseUser.getQuery();
							 query.whereEqualTo("FacebookId", Fb_id_rec);
							 query.findInBackground(new FindCallback<ParseUser>() {

								public void done(List<ParseUser> objects, ParseException e) {
							     if (e == null) 
							     {
									   try{
									        for(int i=0;i<objects.size();i++)
									        {
									          //Education
									           JSONObject json_edu = objects.get(i).getJSONObject("Education");
									           //if(objects.get(i).getJSONObject("Education") != null){
									           if(json_edu != null){
									           mEducation = json_edu.getString("user_education");
									           }else{
									            mEducation ="Not found";  
									           }
									           //Zodiac
									           JSONObject json_Zodiac = objects.get(i).getJSONObject("Zodiac");
									           if(json_Zodiac != null){
									        	   mZodiac = json_Zodiac.getString("user_zodiac");
									           }else{
									        	   mZodiac ="Not found";
									           }
									           //Job
									           JSONObject json_Job = objects.get(i).getJSONObject("JobDesignation");
									            if(json_Job !=null){
									            	mJob = json_Job.getString("user_job"); 
									            }else {
													mJob = "Not found";
												}
									           //Income
									           JSONObject json_income = objects.get(i).getJSONObject("Income");
									            if(json_income !=null){
									            	 mIncome = json_income.getString("user_income"); 
									            }else {
													mIncome = "Not found";
												}
									           //Language
									           JSONObject json_language = objects.get(i).getJSONObject("Language");
									           if(json_language !=null){
									        	   mLanguage = json_language.getString("user_language"); 
									           }else {
									        	   mLanguage = "Not found";
											   }
									           //Hobbies
									           JSONObject json_hobbies = objects.get(i).getJSONObject("Hobbies");
									           if(json_hobbies !=null){
									        	   mHobbies = json_hobbies.getString("user_hobbies"); 
									           }else {
												   mHobbies = "Not found";
											    }
									         }
									       }catch(Exception e1)
									         {
									          e1.printStackTrace();
									         }
							     }
							  }
					      });
							 try{
								 Thread.sleep(2000);
							 }catch(Exception e){
								 e.printStackTrace();
							 }	 
							 
							 
			     } catch (Exception e) {
						e.printStackTrace();
					}
			  return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			super.onPostExecute(unused);			
			/*try{
				 Thread.sleep(2000);
			 }catch(Exception e){
				 e.printStackTrace();
			 }*/
			tv_education.setText(mEducation);
			tv_job.setText(mJob);
			tv_income.setText(mIncome);
			tv_zodiac.setText(mZodiac);
			tv_language.setText(mLanguage);
			tv_hobbies.setText(mHobbies);
			
			
			pDialog.dismiss();
			
			taskExecuted = true;
			
		}						
	}
	
	
	
	
}
