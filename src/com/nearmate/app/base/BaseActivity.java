package com.nearmate.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import com.nearmate.app.FilterSearch;
import com.nearmate.app.NearMateApp;
import com.nearmate.app.R;
import com.nearmate.app.slider.SlidingFragmentActivity;
import com.nearmate.app.slider.SlidingLeftMenuFragment;
import com.nearmate.app.slider.SlidingMenu;

/**
 * This is the Base Activity for implementing Slider 
 * in every Activity.
 * @author asus i5
 *
 */
public  abstract class BaseActivity extends SlidingFragmentActivity  {
	 
	 
	private SlidingMenu sm = null;
	/**
	 * OnCreate is used to initialize the view.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		try {

			// set the Behind View - Left
			setBehindContentView(R.layout.menu_frame);
			Fragment mFrag = null;
			if (savedInstanceState == null) {
				FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
				mFrag = new SlidingLeftMenuFragment();
				t.replace(R.id.menu_frame, mFrag);
				t.commit();
			} else {
				mFrag = (ListFragment)BaseActivity.this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
			}

			// customize the SlidingMenu
			sm = getSlidingMenu();
			sm.setMode(SlidingMenu.LEFT);
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setShadowDrawable(R.drawable.shadow);
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			sm.setFadeDegree(0.35f);
			sm.setShadowWidth(15);
			//sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set The content View
	 */
	@Override
	public void setContentView(int id) {
		super.setContentView(id);
		
		
	}
	/**
	 * Handle the click event of Menu
	 * @param view
	 */
	public void showSlidingMenu(View view){
		sm.showMenu();
	}
	
	/**
	 * Handle the click event of Filter Button
	 * @param view
	 */
	public void executeFilter(View view){
		
		//Toast.makeText(getApplicationContext(), "Filter", 1000).show();
		Intent intent = new Intent(this,FilterSearch.class);
		startActivity(intent);
	}
	
	/**
	 * Handle the click event of Refresh Button
	 * @param view
	 */
	public void executeRefresh(View view){
		
		//Toast.makeText(getApplicationContext(), "Refresh", 1000).show();
		/*NearByTest objNearBy = new NearByTest();
		objNearBy.searchNearByPeople();*/
		
		NearMateApp.reaload_clicked = true;
	}
	
	
}
