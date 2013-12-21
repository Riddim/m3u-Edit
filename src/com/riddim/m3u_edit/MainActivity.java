package com.riddim.m3u_edit;

import com.riddim.m3u_edit.adapter.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends Base implements ActionBar.TabListener, Edit_File.OnHeadlineSelectedListener{
 
	
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Help Me", "Edit File", "See File" };
			
	Fragment see = new See_File();
	
	public void onArticleSelected(String encoded) {
		
		Intent i = new Intent(this, tempSee_File.class);
		i.putExtra("text", encoded);
		//startActivity(i);
		
		
		
		
		//See_File seeFrag = (See_File) getSupportFragmentManager().findFragmentById(R.id.);
		 // seeFrag.updateArticleView(encoded);

	}
        
        
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
		
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
	
			}
			@Override
			public void onPageSelected(int position) {
			       actionBar.setSelectedNavigationItem(position);
			}
		});
		viewPager.setCurrentItem(1);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
}
