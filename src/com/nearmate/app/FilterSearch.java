package com.nearmate.app;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FilterSearch extends Activity{
	
	private static final String TAG = "FilterSearch";
	
	private ImageView backToMainActivity;
	private ListView lv_main;
	private String item_selected;
	private String distance_selected;
	private String interest_selected;
	private ListView lv_another; 
	//private String gender_to_search = "All";
	private String gender_to_search;
	
	private String check_filter_status;
	
	private String mySearchCriteria;
	
	 // Create and populate a List of planet names.  
    String[] location_strings = new String[] { "Located anywhere", "Located near me", "My Interests"};
	String[] location_length = new String[] { "5 km", "10 km", "25 km", "50 km", "100 km", "250 km", "500 km" };
	String [] strings_my_interests = new String[] { "Education", "Job","Income" , "Zodiac", "Language" , "Hobby" };

	
	private RadioGroup radioSexGroup;

	private Button save_Filter;

	private String my_saved_search_criteria;

	private RadioButton btn_female;

	private RadioButton btn_male;


	private TextView tv_sub_heading;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filter_search);
		
		getSavedFilterSetting();
		
		SharedPreferences settings = getSharedPreferences(NearMateApp.PREF_SETTINGS_SAVED, 0);
		check_filter_status = settings.getString(NearMateApp.PREF_SETTINGS_SAVED, "").toString();
		if(check_filter_status.equals("saved")){
			NearMateApp.criteria_saved = false;
		}
		
		Log.e(TAG+ "Check filter", check_filter_status);
		
		backToMainActivity = (ImageView)findViewById(R.id.backToMain);
		tv_sub_heading = (TextView)findViewById(R.id.sub_heading);
		lv_main = (ListView)findViewById(R.id.lv_main);
		lv_another = (ListView)findViewById(R.id.lv_another);
		save_Filter = (Button)findViewById(R.id.save_settings);
		radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
		
		/*RadioButton TheTextIsHere = (RadioButton) findViewById(radioSexGroup.getCheckedRadioButtonId());
		
		Log.e(TAG +"Radio position", ""+TheTextIsHere.getText().toString());*/
		
		
		radioSexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// find which radio button is selected
				
				if(checkedId == R.id.radioMale) {
					gender_to_search = "male";
					Toast.makeText(getApplicationContext(), gender_to_search, 
							Toast.LENGTH_SHORT).show();
					
				} else if(checkedId == R.id.radioFemale) {
					gender_to_search = "female";
					Toast.makeText(getApplicationContext(), gender_to_search, 
							Toast.LENGTH_SHORT).show();
				} 
				
			}
			
			
		});
		
		save_Filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveFilterToParse();
			}
		});
		
		backToMainActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(check_filter_status.equals("saved") || NearMateApp.criteria_saved){
					FilterSearch.this.finish();
				}else {
					Toast.makeText(getApplicationContext(), "Please save the changes", Toast.LENGTH_SHORT).show();
				}
					
			}
		});
		
		ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,location_strings);
	     lv_main.setAdapter(adapter_status);
	     lv_main.setOnItemClickListener(new OnItemClickListener() {
				

				public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
		     	     item_selected =(lv_main.getItemAtPosition(pos).toString());
		     	   		     	    
		     	    if(item_selected.equals("Located near me")){
		     	    	if(interest_selected != null){
		     	    		interest_selected.equals(null);
		     	    	}
		     	    	tv_sub_heading.setVisibility(View.VISIBLE);
		     	    	tv_sub_heading.setText("Select your desired distance");
		     	    	lv_another.setVisibility(View.VISIBLE);
		     	    	onLocationNearMe();
		     	    	
		     	    }else if (item_selected.equals("My Interests")) {
		     	    	if(distance_selected !=null){
		     	    		distance_selected.equals(null);
		     	    	}
		     	    	tv_sub_heading.setVisibility(View.VISIBLE);
		     	    	tv_sub_heading.setText("Select your desired interests");
		     	    	lv_another.setVisibility(View.VISIBLE);
						searchViaMyInterests();
						
					}else {
						lv_another.setVisibility(View.INVISIBLE);
		     	    	distance_selected = null;
		     	    	interest_selected = null;
					}
		     	  }                 
		     	});
	}
	
	 
	
	public void onLocationNearMe(){
		
		ArrayAdapter<String> adapter_length = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,location_length);
	    	lv_another.setAdapter(adapter_length);
	    	
	    	lv_another.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
					distance_selected =(lv_another.getItemAtPosition(pos).toString());
				}        
		     	});
	}
	
	private void searchViaMyInterests(){
		ArrayAdapter<String> adapter_interests = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,strings_my_interests);
		lv_another.setAdapter(adapter_interests);
		lv_another.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> myAdapter, View myView, int pos, long mylng) {
				
				interest_selected = lv_another.getItemAtPosition(pos).toString();
				//Toast.makeText(getApplicationContext(), interest_selected, Toast.LENGTH_SHORT).show();
	     	   
			}        
	     	});
	}
	
	private void saveFilterToParse(){
		if(item_selected!=null){
			try {
 				ParseUser currentUser = ParseUser.getCurrentUser();
 				
 				 if( item_selected.equals("Located near me") && distance_selected!=null ){
					
 					mySearchCriteria = item_selected +","+ distance_selected+","+gender_to_search;
				}else if (item_selected.equals("Located near me") && distance_selected == null) {
					Toast.makeText(getApplicationContext(), "Please select Distance", Toast.LENGTH_SHORT).show();
				}else if (item_selected.equals("My Interests") && interest_selected!=null) {
					mySearchCriteria = item_selected +","+ interest_selected+","+gender_to_search;
				}else if (item_selected.equals("My Interests") && interest_selected == null) {
					Toast.makeText(getApplicationContext(), "Please select Interest", Toast.LENGTH_SHORT).show();
				}else if(item_selected.equals("Located anywhere")){
					mySearchCriteria = item_selected + ","+ "null"+","+gender_to_search;
				}
 				
 				currentUser.put("SearchCriteria",mySearchCriteria);
	   			currentUser.saveInBackground();
 				
 				Toast.makeText(getApplicationContext(), "settings successfully saved", Toast.LENGTH_SHORT).show();
 			
			} catch (Exception e) {
 				e.printStackTrace();
 			}
		}else {
			Toast.makeText(getApplicationContext(), "Please select any one from the below list", Toast.LENGTH_SHORT).show();
		}
	//NearMateApp.criteria_saved = true;
		SharedPreferences pref_settings_status = getSharedPreferences(NearMateApp.PREF_SETTINGS_SAVED, 0);
		SharedPreferences.Editor edt_id = pref_settings_status.edit();
		edt_id.putString(NearMateApp.PREF_SETTINGS_SAVED,"saved");
		edt_id.commit();
		NearMateApp.criteria_saved = true;
	}
	
	
	@Override
	public void onBackPressed()
	{
		if(check_filter_status.equals("saved") || NearMateApp.criteria_saved){
			FilterSearch.this.finish();
		}else {
			Toast.makeText(getApplicationContext(), "Please save the changes", Toast.LENGTH_SHORT).show();
		}       
	}	

	/**
	 * Get my Saved Filter setting from parse
	 */
	
	private void getSavedFilterSetting(){
		
		btn_male = (RadioButton)findViewById(R.id.radioMale);
		btn_female = (RadioButton)findViewById(R.id.radioFemale);
		
		try{
			
			ParseUser currentUser = ParseUser.getCurrentUser();
			
			 my_saved_search_criteria = currentUser.getString("SearchCriteria");
			
			 if(my_saved_search_criteria == null && my_saved_search_criteria.length()<0){
				 Toast.makeText(getApplicationContext(), "First set your Details in your profile", Toast.LENGTH_SHORT).show();
			}else {
				 
				 String[] finalCriteria = my_saved_search_criteria.split(",");
				
				 String gender_fetched = finalCriteria[2]; 
				
				 Log.e(TAG+ "fetched criteria", gender_fetched);
				 if(gender_fetched.equals("male")){
					 gender_to_search = "male";
					 btn_male.setChecked(true);
				 }else if (gender_fetched.equals("female")) {
					 btn_female.setChecked(true);
					 gender_to_search = "female";
				}else {
					Toast.makeText(getApplicationContext(), "Please set your search criteria", Toast.LENGTH_SHORT).show();
				}
			}
			 
			 String[] finalCriteria = my_saved_search_criteria.split(",");
			
			 String gender_fetched = finalCriteria[2]; 
			
			 Log.e(TAG+ "fetched criteria", gender_fetched);
			 if(gender_fetched.equals("male")){
				 gender_to_search = "male";
				 btn_male.setChecked(true);
			 }else if (gender_fetched.equals("female")) {
				 btn_female.setChecked(true);
				 gender_to_search = "female";
			}else {
				Toast.makeText(getApplicationContext(), "Please set your search criteria", Toast.LENGTH_SHORT).show();
			}
			 
			 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
