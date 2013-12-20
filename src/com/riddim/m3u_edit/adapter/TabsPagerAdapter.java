package com.riddim.m3u_edit.adapter;

import com.riddim.m3u_edit.Edit_File;
import com.riddim.m3u_edit.See_File;
import com.riddim.m3u_edit.Help_Me;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter{


	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new Help_Me();
		case 1:
			// Games fragment activity
			return new Edit_File();
		case 2:
			// Movies fragment activity
			return new See_File();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}



