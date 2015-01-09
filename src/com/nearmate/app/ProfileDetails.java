package com.nearmate.app;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class ProfileDetails extends Fragment {
	

	 private String education_selected;
	 private String staus_selected;
	 private String completeEducation;
	 private String zodiac_selected;
	 private String parse_user_education;
	 private String parse_user_Zodiac;
	 private String job_selected;
	 private String parse_user_Job;
	 private String income_selected;
	 private String parse_user_income;
	 private String language_selected;
	 private String parse_user_language;
	 private String parse_user_hobbies;
	 private boolean profilecomplete;
	 
	 
	 String[] status = new String[] {
		        "(leave this blank)",
		        "intern in",
		        "Graduated from",
		        "Working on",
		        "Dropped out of"
		    };
	 
	 String[] education = new String[] {
		        "(leave this blank)",
		        "company",
		        "high school",
		        "two year college",
		        "university",
		        "masters program",
		        "law school",
		        "med school",
		        "Ph.D program"		        
		    };
	 
	 String[] zodiac = new String[] {
			 "Aries",
			 "Taurus",
			 "Gemini",
			 "Cancer",
			 "Leo",
			 "Virgo",
			 "Libra",
			 "Scorpio",
			 "Sagittarius",
			 "Capricorn",
			 "Aquarius",
			 "Pisces"
	 };
	 
	 String[] jobs = new String[] {
			"(leave this blank)",
			"Student",
			"Art / Music / Writing",
			"Banking / Finance",
			"Administration",
			"Technology",
			"Construction",
			"Education",
			"Entertainment / Media",
			"Management",
			"Hospitality",
			"Law",
			"Medicine",
			"Military",
			"Politics",
			"Government",
			"Sales / Marketing",
			"Science / Engineering",
			"Transportation",
			"Unemployed",
			"other",
			"Rather not say",
			"Retired"
	 };
	 
	 String[] income = new String[] {
			 "(leave this blank)",
			 "Less than $20,000",
			 "$20,000 - $30,000",
			 "$30,000 - $40,000",
			 "$40,000 - $50,000",
			 "$50,000 - $60,000",
			 "$60,000 - $70,000",
			 "$70,000 - $80,000",
			 "$80,000 - $100,000",
			 "$100,000 - $150,000",
			 "$150,000 - $250,000",
			 "$250,000 - $500,000",
			 "$500,000 - $1,000,000",
			 "More than $1,000,000",
			 "Rather not say"
	 };
	 
	 String[] language = new String[] {
			 "(leave this blank)",
				"Mandarin",
				"Spanish",
				"English",
				"Hindi",
				"Arabic",
				"Portuguese",
				"Bengali",
				"Russian",
				"Japanese",
				"Punjabi",
				"German",
				"Malay/Indonesian",
				"Telugu",
				"French",
				"Marathi",
				"Turkish",
				"Italian",
				"Cantonese",
				"Persian",
				"Thai",
				"Gujarati",
				"Kannada",
				"Xiang",
				"Malayalam",
				"Sundanese",
				"Hausa",
				"Oriya",
				"Burmese",
				"Hakka",
				"Tagalog",
				"Yoruba",
				"Swahili",
				"Kiswahili",
				"Uzbek",
				"Sindhi",
				"Amharic",
				"Fula",
				"Romanian",
				"Oromo",
				"Cebuano",
				"Dutch",
				"Kurdish",
				"Lao",
				"Serbo-Croatian",
				"Malagasy",
				"Malagasy",
				"Saraiki",
				"Nepali",
				"Sinhalese",
				"Chittagonian",
				"Madurese",
				"Madhura",
				"Somali",
				"Kazakh",
				"Min Bei",
				"Sylheti",
				"Zulu",
				"Czech",
				"Kinyarwanda",
				"Haitian Creole	",
				"Min Dong",
				"Ilokano",
				"Quechua",
				"Kirundi",
				"Swedish",
				"Hmong",
				"Shona",
				"Uyghur",
				"Hiligaynon",
				"Mossi",
				"Xhosa",
				"Belarusian"
	 };
	 
	 String [] hobbies = new String[] {
			 "(leave this blank)",
			 "Reading",
			 "Watching TV",
			 "Family Time",
			 "Going to Movies",
			 "Fishing",
			 "Computer",
			 "Gardening",
			 "Renting Movies",
			 "Walking",
			 "Exercise",
			 "Listening to Music",
			 "Entertaining",
			 "Hunting",
			 "Team Sports",
			 "Shopping",
			 "Traveling",
			 "Sleeping",
			 "Socializing",
			 "Sewing",
			 "Golf",
			 "Church Activities",
			 "Relaxing",
			 "Playing Music",
			 "Housework",
			 "Crafts",
			 "Watching Sports",
			 "Bicycling",
			 "Playing Cards",
			 "Hiking",
			 "Cooking",
			 "Eating Out",
			 "Dating Online",
			 "Swimming",
			 "Camping",
			 "Skiing",
			 "Working on Cars",
			 "Writing",
			 "Boating",
			 "Motorcycling",
			 "Animal Care",
			 "Bowling",
			 "Painting",
			 "Running",
			 "Dancing",
			 "Horseback Riding",
			 "Tennis",
			 "Theater",
			 "Billiards",
			 "Beach",
			 "Volunteer Work"
	 
	 };
	 

	private ImageView edit_education , edit_job , edit_income , edit_horoscope , edit_language , edit_hobby;
	private TextView tv_education;
	private TextView tv_horoscope;
	private TextView tv_job;
	private TextView tv_income;
	private TextView tv_language;
	
	boolean[] itemsChecked = new boolean[hobbies.length];
	private TextView tv_hobbies;
	ParseUser currentUser = ParseUser.getCurrentUser();
	
	
	//Json objects
	JSONObject userEducation = new JSONObject(); 
	JSONObject userZodiac = new JSONObject();
	JSONObject userJob = new JSONObject();
	JSONObject userIncome = new JSONObject();
	JSONObject userLanguage = new JSONObject();
	JSONObject userHobby = new JSONObject();

	
	
	
	
	/**
	 * this array list contains selected hobbies
	 */
	private  ArrayList<String> selected_hobbies = new ArrayList<String>();
	
	private String final_hobbies;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.profile_deatils, container, false);
		edit_education = (ImageView)rootView.findViewById(R.id.edit_about_me);
		edit_job = (ImageView)rootView.findViewById(R.id.edit_job);
		edit_income = (ImageView)rootView.findViewById(R.id.edit_Income);
		edit_horoscope = (ImageView)rootView.findViewById(R.id.edit_horoscope);
		edit_language = (ImageView)rootView.findViewById(R.id.edit_language);
		edit_hobby = (ImageView)rootView.findViewById(R.id.edit_Hobby);
		tv_education = (TextView)rootView.findViewById(R.id.tv_about_me);
		tv_horoscope = (TextView)rootView.findViewById(R.id.tv_horoscope);
		tv_job = (TextView)rootView.findViewById(R.id.tv_job);
		tv_income = (TextView)rootView.findViewById(R.id.tv_Income);
		tv_language = (TextView)rootView.findViewById(R.id.tv_language);
		tv_hobbies = (TextView)rootView.findViewById(R.id.tv_Hobby);
		
		
		
		new FetchUserDataFromParse(getActivity()).execute("fetch userdata");
		
		/**
		 * Edit Education
		 */
		edit_education.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setEducation();
			}
		});
		
		
		/**
		 * Edit Job
		 */
		edit_job.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setJob();	
			}
		});
		
		/**
		 * Edit Income
		 */
		edit_income.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setIncome();
			}
		});
		
		/**
		 * Edit Horoscope
		 */
		
		edit_horoscope.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setHoroscope();
			}
		});
		
		/**
		 * Edit offspring
		 */
		
		edit_language.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setLanguage();
			}
		});
		
		
		/**
		 * Edit Relegion
		 */
				
		edit_hobby.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				setHobby();
			}
		});
		
		
		return rootView;
	}
	

	/**
	 * Set Education
	 */
	public void setEducation()
	
	{
					final Dialog dialog = new Dialog(getActivity());
					dialog.setContentView(R.layout.education);
					dialog.setTitle("Education...");
					dialog.setCancelable(true);
		 
					//-----------------fetching status string---------------------
					final ListView lv_status = (ListView ) dialog.findViewById(R.id.lv_status);
					final ListView lv_education = (ListView)dialog.findViewById(R.id.lv_education);
					
					 ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,status);
				     lv_status.setAdapter(adapter_status);
				     lv_status.setOnItemClickListener(new OnItemClickListener() {
				     	 
						public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
				     	     staus_selected =(lv_status.getItemAtPosition(pos).toString());
				     	   // Toast.makeText(getActivity(), staus_selected, Toast.LENGTH_SHORT).show();
				     	  }                 
				     	});
				     
				   //-----------------fetching education string---------------------
				     ArrayAdapter<String> adapter_education = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,education);
				     lv_education.setAdapter(adapter_education);
				     lv_education.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
				   		education_selected =(lv_education.getItemAtPosition(pos).toString());
				   	    //Toast.makeText(getActivity(), education_selected, Toast.LENGTH_SHORT).show();
				   		
				   	  }                 
				   	});
					//-------------------OK button click listener
					Button dialogButton = (Button) dialog.findViewById(R.id.button1);
					// if button is clicked, close the custom dialog
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(staus_selected == null && education_selected == null)
							{
								completeEducation = "Not found";
								tv_education.setText(completeEducation);
								saveEducationToParse();
							}else {
								if(staus_selected == "(leave this blank)" && education_selected == "(leave this blank)")
								{
									completeEducation = "Not found";
									tv_education.setText(completeEducation);
									saveEducationToParse();
								}else {
									if(staus_selected == null && education_selected == "(leave this blank)"){
										completeEducation = "Not found";
										tv_education.setText(completeEducation);
										saveEducationToParse();
									}
									if(education_selected == null && staus_selected == "(leave this blank)"){
										completeEducation = "Not found";
										tv_education.setText(completeEducation);
										saveEducationToParse();
									}
									if(staus_selected == "(leave this blank)" && education_selected!= "(leave this blank)")
									{
										completeEducation = education_selected;
										tv_education.setText(completeEducation);
										saveEducationToParse();
									}if(education_selected == "(leave this blank)" && staus_selected!= "(leave this blank)")
									{
										completeEducation = staus_selected;
										tv_education.setText(completeEducation);
										saveEducationToParse();
									}
									if(staus_selected!= "(leave this blank)" && staus_selected !=null && education_selected!= "(leave this blank)" && education_selected !=null )
									{
										completeEducation = staus_selected + " "+ education_selected;	
										tv_education.setText(completeEducation);
										saveEducationToParse();
									}
								}
							}
							dialog.dismiss();
						}
					});
					dialog.show();
	}
	
	
	/**
	 * Save Education to parse
	 */
	
	public void saveEducationToParse(){
		
		// Create a JSON object to hold the profile info
		
		try {
			// Populate the JSON object
			//Facebook ID
			userEducation.put("user_Education", completeEducation);
			
			// Save the user profile info in a user property
			currentUser.put("Education", userEducation);
			currentUser.saveInBackground();

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * AlertBox for HOROSCOPE
	 */
	public void setHoroscope(){
		

		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.profile_horoscope);
		dialog.setTitle("Zodiac Sign...");
		dialog.setCancelable(true);

		//-----------------fetching status string---------------------
		
		final ListView lv_status = (ListView ) dialog.findViewById(R.id.lv_horoscore);
		 ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,zodiac);
	     lv_status.setAdapter(adapter_status);
	     lv_status.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
	     	     zodiac_selected =(lv_status.getItemAtPosition(pos).toString());
	     	    
//	     	    Toast.makeText(getActivity(), zodiac_selected, Toast.LENGTH_SHORT).show();
	     	  }                 
	     	});
		//-------------------OK button click listener
		Button dialogButton = (Button) dialog.findViewById(R.id.button1);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveZodiacToParse();
				tv_horoscope.setText(zodiac_selected);
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	

	/**
	 * Save Zodiac to parse
	 */
	
	public void saveZodiacToParse(){
		
		try {
			// Populate the JSON object
			//Facebook ID
			userZodiac.put("user_Zodiac", zodiac_selected);
			
			// Save the user profile info in a user property
			
			currentUser.put("Zodiac", userZodiac);
			currentUser.saveInBackground();

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Set Job for Alert Box
	 */
	
	public void setJob(){

		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.profile_horoscope);
		dialog.setTitle("Job...");
		dialog.setCancelable(true);

		//-----------------fetching status string---------------------
		
		final ListView lv_status = (ListView ) dialog.findViewById(R.id.lv_horoscore);
		 ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,jobs);
	     lv_status.setAdapter(adapter_status);
	     lv_status.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
	     	     job_selected =(lv_status.getItemAtPosition(pos).toString());
	     	    Toast.makeText(getActivity(), job_selected, Toast.LENGTH_SHORT).show();
	     	  }                 
	     	});
		//-------------------OK button click listener
		Button dialogButton = (Button) dialog.findViewById(R.id.button1);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveJobToParse();
				if(job_selected == "(leave this blank)")
				{
					tv_job.setText("Not found");
				}else 
				{
					tv_job.setText(job_selected);
				}
				
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	
	/**
	 * Save Job to parse
	 */
	public void saveJobToParse(){
		
		// Create a JSON object to hold the profile info
		
		try {
			// Populate the JSON object
			userJob.put("user_JobDesignation", job_selected);
			
			// Save the user profile info in a user property
			
			currentUser.put("JobDesignation", userJob);
			currentUser.saveInBackground();

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Set Income for Alert Box
	 */
	
	public void setIncome(){
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.profile_horoscope);
		dialog.setTitle("Income...");
		dialog.setCancelable(true);

		
		final ListView lv_status = (ListView ) dialog.findViewById(R.id.lv_horoscore);
		 ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,income);
	     lv_status.setAdapter(adapter_status);
	     lv_status.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
	     	     income_selected =(lv_status.getItemAtPosition(pos).toString());
	     	    Toast.makeText(getActivity(), income_selected, Toast.LENGTH_SHORT).show();
	     	  }                 
	     	});
		//-------------------OK button click listener
		Button dialogButton = (Button) dialog.findViewById(R.id.button1);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				saveIncomeToParse();
				if(income_selected == "(leave this blank)")
				{
					tv_income.setText("Not found");
				}else 
				{
					tv_income.setText(income_selected);
				}
				
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	/**
	 * Save Income to parse
	 */
	public void saveIncomeToParse(){
		// Create a JSON object to hold the profile info
				
				try {
					// Populate the JSON object
					userIncome.put("user_Income", income_selected);
					
					currentUser.put("Income", userIncome);
					currentUser.saveInBackground();
				} catch (JSONException e) {
					e.printStackTrace();
				}
	}
	
	/**
	 * Set Language
	 */
	
	public void setLanguage(){
		
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.profile_horoscope);
		dialog.setTitle("Language...");
		dialog.setCancelable(true);

		
		final ListView lv_status = (ListView ) dialog.findViewById(R.id.lv_horoscore);
		 ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,language);
	     lv_status.setAdapter(adapter_status);
	     lv_status.setOnItemClickListener(new OnItemClickListener() {
			

			public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
	     	     language_selected =(lv_status.getItemAtPosition(pos).toString());
	     	    Toast.makeText(getActivity(), language_selected, Toast.LENGTH_SHORT).show();
	     	  }                 
	     	});
		//-------------------OK button click listener
		Button dialogButton = (Button) dialog.findViewById(R.id.button1);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				saveLanguageToParse();
				if(language_selected == "(leave this blank)")
				{
					tv_language.setText("Not found");
				}else 
				{
					tv_language.setText(language_selected);
				}
				
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	/**
	 * Save language
	 */
	public void saveLanguageToParse(){
		// Create a JSON object to hold the profile info
		
		try {
			// Populate the JSON object
			userLanguage.put("user_Language", language_selected);
			// Save the user profile info in a user property
			
			currentUser.put("Language", userLanguage);
			currentUser.saveInBackground();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Set Hobbies for Alert Box
	 */
	
	public void setHobby(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Hobbies...(Please select atmost three hobbies)");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	
		        	try{
		    			for(int i=0;i<selected_hobbies.size();i++){
		    				int size = selected_hobbies.size();
		    				if(size>=3)
		    				{
		    					String first_hobby = selected_hobbies.get(0);
		    					String second_hobby = selected_hobbies.get(1);
		    					String third_hobby = selected_hobbies.get(2);
		    					final_hobbies = first_hobby+","+second_hobby+","+third_hobby;
		    					 tv_hobbies.setText(final_hobbies);
		    					 saveHobbyToParse();
		    				}else {
		    					tv_hobbies.setText("Not found");
		    					Toast.makeText(getActivity(), "please select atleast 3 hobbies", Toast.LENGTH_SHORT).show();
		    				}
		    				
		    			}
		    		}catch(Exception e){
		    			e.printStackTrace();
		    		}
		        	
		        	
		        	 selected_hobbies.clear();
		        	
		        } });

		builder.setMultiChoiceItems(hobbies, null,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which, boolean isChecked) {
						if (isChecked)
							/*Toast.makeText(getActivity(),
									hobbies[which], Toast.LENGTH_SHORT)
									.show();*/
						selected_hobbies.add((String) hobbies[which]);
					}
				});
		
		AlertDialog alert = builder.create();
		//alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//alert.getWindow().setBackgroundDrawableResource(R.drawable.bg_drawer);
		
		alert.show();
	}
	
	/**
	 * Save Hobby to parse
	 */
	
	public void saveHobbyToParse(){
		
		// Create a JSON object to hold the profile info
				
				try {
					// Populate the JSON object
					userHobby.put("user_Hobbies", final_hobbies);
					// Save the user profile info in a user property
					
					currentUser.put("Hobbies", userHobby);
					currentUser.saveInBackground();
				} catch (JSONException e) {
					e.printStackTrace();
				}
	}
	
	/**
	 * Fetching User Profile Details from Parse
	 */
	
	private class FetchUserDataFromParse extends AsyncTask<String, String, String> {
		private Context context;
		private ProgressDialog progressDialog;
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		public FetchUserDataFromParse(Context context) {
		    this.context = context;
		}
		@Override
		protected void onPreExecute() {
		    progressDialog = new ProgressDialog(context);
		    progressDialog.setMessage("Loading...");
		    progressDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
		    try {
		    	
		    	//EDUCATION
		    	if (currentUser.get("Education") != null)
		    	{
					JSONObject userProfile = currentUser.getJSONObject("Education");
							if (userProfile.getString("user_Education") != null)
							{
								parse_user_education = userProfile.get("user_Education").toString();
								NearMateApp.user_edu = parse_user_education;
							}
				}else {
						
						parse_user_education = "Not found";
						NearMateApp.user_edu = parse_user_education;
						
				}
		    	
		    // ZODIAC Sign
		    	if (currentUser.get("Zodiac") != null) 
		    	{
					JSONObject userProfile = currentUser.getJSONObject("Zodiac");
							if (userProfile.getString("user_Zodiac") != null)
							{
								 parse_user_Zodiac = userProfile.get("user_Zodiac").toString();
								 NearMateApp.user_zodiac = parse_user_Zodiac;
							}
				}else {
					parse_user_Zodiac = "Not found";
					NearMateApp.user_zodiac = parse_user_Zodiac;
				}
		    	
		    // JOBS	
		    	if(currentUser.get("JobDesignation")!= null)
		    	{
		    		JSONObject userProfile = currentUser.getJSONObject("JobDesignation");
				    		if (userProfile.getString("user_JobDesignation") != null)
							{
								 parse_user_Job = userProfile.get("user_JobDesignation").toString();
								 NearMateApp.user_job = parse_user_Job;
							}
		    	}else {
		    		parse_user_Job = "Not found";
		    		NearMateApp.user_job = parse_user_Job;
				}
		    	
		    //INCOME
		    	if(currentUser.get("Income")!= null)
		    	{
		    		JSONObject userProfile = currentUser.getJSONObject("Income");
				    		if (userProfile.getString("user_Income") != null)
							{
								 parse_user_income = userProfile.get("user_Income").toString();
								 NearMateApp.user_income = parse_user_income;
							}
		    	}else {
		    		parse_user_income = "Not found";
		    		 NearMateApp.user_income = parse_user_income;
				}
		    	
		    //LANGUAGE
		    	if(currentUser.get("Language")!= null)
		    	{
		    		JSONObject userProfile = currentUser.getJSONObject("Language");
				    		if (userProfile.getString("user_Language") != null)
							{
				    			parse_user_language = userProfile.get("user_Language").toString();
				    			NearMateApp.user_lang = parse_user_language;
							}
		    	}else {
		    		parse_user_language = "Not found";
		    		NearMateApp.user_lang = parse_user_language;
				}
		    	
		    //HOBBIES
		    	if(currentUser.get("Hobbies")!= null)
		    	{
		    		JSONObject userProfile = currentUser.getJSONObject("Hobbies");
				    		if (userProfile.getString("user_Hobbies") != null)
							{
				    			parse_user_hobbies = userProfile.get("user_Hobbies").toString();
				    			NearMateApp.user_hobby = parse_user_hobbies;
							}
		    	}else {
		    		parse_user_hobbies = "Not found";
		    		NearMateApp.user_hobby = parse_user_hobbies;
				}
		    	
		    } catch (JSONException e) {
				e.printStackTrace();
			}
		    return "finish";
		}
		
		@Override
		protected void onPostExecute(String result) {
		    progressDialog.dismiss();
		    tv_education.setText(parse_user_education);
		    tv_horoscope.setText(parse_user_Zodiac);
		    tv_job.setText(parse_user_Job);
		    tv_income.setText(parse_user_income);
		    tv_language.setText(parse_user_language);
		    tv_hobbies.setText(parse_user_hobbies);
		}
}
}
