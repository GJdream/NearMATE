package com.nearmate.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.nearmate.app.SearchedProfileAbout;
import com.nearmate.app.SearchedProfileDetails;

public class SearchedPeopleDetailAdapter extends FragmentPagerAdapter {

	public SearchedPeopleDetailAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:

			return new SearchedProfileAbout();
		case 1:
			return new SearchedProfileDetails();
		
		}
		
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}