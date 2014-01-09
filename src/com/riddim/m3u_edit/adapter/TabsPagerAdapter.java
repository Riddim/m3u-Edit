package com.riddim.m3u_edit.adapter;

import com.riddim.m3u_edit.Edit_File;
import com.riddim.m3u_edit.New_M3U;
import com.riddim.m3u_edit.Settings;
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
			
			return new Help_Me();
		case 1:
			
			return new Edit_File();
		case 2:
			return new New_M3U();
		
		case 3:
			
			return new Settings();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}



