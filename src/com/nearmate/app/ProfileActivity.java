package com.nearmate.app;


import com.nearmate.app.adapter.ProfileTabPageAdapter;
import com.nearmate.app.base.BaseActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class ProfileActivity extends BaseActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private ProfileTabPageAdapter mAdapter;
	public ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "About", "Deatils"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = ProfileActivity.this.getActionBar();
		
		actionBar.setCustomView(R.layout.header_profile);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EE6C49")));
		/*actionBar.setStackedBackgroundDrawable(getResources().getDrawable(
	            R.drawable.tab_bar_try));*/
		
		actionBar.setBackgroundDrawable(getResources().getDrawable(
	            R.drawable.custom_tab_home));
		
		
		mAdapter = new ProfileTabPageAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	
	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);			 
        // Setting Dialog Title
        alertDialog.setTitle("Confirm...");	 
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to Quit");	 
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
             ProfileActivity.this.finish();
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {	           
            dialog.cancel();
            }
        });
        alertDialog.show();	        
	}	

	

}