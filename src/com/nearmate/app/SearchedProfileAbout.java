package com.nearmate.app;

import java.util.List;

import com.facebook.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchedProfileAbout extends Fragment{
	
	private static final String TAG = "SearchedProfileAbout";
	
	private ProfilePictureView user_image;
	private TextView userNameView;
	private TextView userLocationView;
	private TextView userGenderView;
	private TextView tv_about_me;
	private  String Fb_id_rec;
	private String Fb_name_rec;
	String check_about;
	private String Fb_location_rec;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.searched_profile_about, container, false);
		user_image = (ProfilePictureView)rootView.findViewById(R.id.user_image);
		userNameView = (TextView)rootView.findViewById(R.id.userName);
		userLocationView = (TextView)rootView.findViewById(R.id.tv_location);
		userGenderView = (TextView) rootView.findViewById(R.id.tv_gender_and_age);
		tv_about_me = (TextView)rootView.findViewById(R.id.tv_about_me);
	
		 Fb_id_rec = getActivity().getIntent().getExtras().getString("fb_user_id");
		 Fb_name_rec = getActivity().getIntent().getExtras().getString("fb_user_name");
		 Fb_location_rec = getActivity().getIntent().getExtras().getString("fb_user_location");
		
		 Log.e(TAG, Fb_id_rec+" "+Fb_name_rec);
		
		 user_image.setProfileId(Fb_id_rec);
		userNameView.setText(Fb_name_rec);
		userLocationView.setText(Fb_location_rec);
		
		getAboutme();
		
		return rootView;
	}

	public void getAboutme(){
		  try {
				 ParseQuery<ParseUser> query = ParseUser.getQuery();
				 query.whereEqualTo("FacebookId", Fb_id_rec);
				 query.findInBackground(new FindCallback<ParseUser>() {
				   public void done(List<ParseUser> objects, ParseException e) {
					   if (e == null) 
					     {
					           Log.e(TAG, "@@@@Retrieved " + objects.size());
					        	   Log.e(TAG, objects.toString());
					        	   try{
					        		   for(int i=0;i<objects.size();i++){
					        			    check_about =  objects.get(i).getString("TestAboutMe");
					        			   Log.e(TAG, check_about);
					        		   }
					        		   
					        		   tv_about_me.setText(check_about);
					        		 /* String check_about =  objects.get(0).getString("TestAboutMe");
					        		  Log.e("check_about", check_about);*/
					        	   }catch(Exception e1){
					        		   e1.printStackTrace();
					        	   }
					     }
				    }
				 });
		     } catch (Exception e) {
					e.printStackTrace();
				}
	}
}
