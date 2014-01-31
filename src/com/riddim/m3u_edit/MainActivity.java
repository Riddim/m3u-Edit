package com.riddim.m3u_edit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.riddim.m3u_edit.adapter.TabsPagerAdapter;

public class MainActivity extends Base implements ActionBar.TabListener, Edit_File.OnHeadlineSelectedListener{
 
	
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Help Me", "Pc Playlist","Edit/Make","Settings" };
		String startscreen = "0";	
	Fragment see = new Settings();
	
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

		//Read File for start screen

				FileInputStream fis;
				try {
					startscreen = "";
					fis = openFileInput("number");
					byte[] input = new byte[fis.available()];
					while (fis.read(input) != -1) {}
					startscreen += new String(input);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					startscreen = "0";
				} catch (IOException e) {
					e.printStackTrace(); 
					startscreen = "0";
				}  
		
		
		
		
		
		
		
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
		viewPager.setCurrentItem(Integer.parseInt(startscreen));
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String value = extras.getString("playpath");
			if(value == null){
				viewPager.setCurrentItem(3);
			} 
			String value2 = extras.getString("filepath");
			if(value2 != null){
				Boolean edit = extras.getBoolean("editm3u");
				if(edit){
					viewPager.setCurrentItem(2);
				} else {
				viewPager.setCurrentItem(1);
				}
			}
			if(extras.getString("sort")!=null){
				viewPager.setCurrentItem(2);
			}
	
		} else {
			viewPager.setCurrentItem(Integer.parseInt(startscreen));
		}
		
		
		
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

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	
	
	// set menu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		    // Inflate the menu items for use in the action bar
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.main, menu);
		    return true;
		}   
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case R.id.action_settings:
			//NavUtils.navigateUpFromSameTask(this);
			viewPager.setCurrentItem(3);
			return true;	
		case R.id.donate:
			Intent i = new Intent(this, Paypal.class);
			startActivity(i);
			return true;
		case R.id.give_feedback:
		Intent in = new Intent(this, Feedback.class);
		startActivity(in);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
