package com.nearmate.app.adapter;


import com.nearmate.app.ProfileAbout;
import com.nearmate.app.ProfileDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProfileTabPageAdapter extends FragmentPagerAdapter {

	public ProfileTabPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ProfileAbout();
		case 1:
			// Games fragment activity
			return new ProfileDetails();
		
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}