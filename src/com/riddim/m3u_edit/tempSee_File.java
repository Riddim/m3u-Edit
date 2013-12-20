package com.riddim.m3u_edit;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class tempSee_File extends Base {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.see_file);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		TextView content = (TextView) findViewById(R.id.content2);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String value = extras.getString("text");
		    content.setText(value);
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	
	
	/*
	
	public void updateArticleView(String encoded) {
		TextView content = (TextView) findViewById(R.id.content2);
		Intent intent = getIntent();
		encoded = intent.getExtras().getString("text");
		
		content.setText(encoded);
	}
*/


}
