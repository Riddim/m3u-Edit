package com.riddim.m3u_edit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Explorer extends ListActivity {

	private List<String> item = null;
	private List<String> path = null;
	private String root;
	private TextView myPath;
	String stPath;
	private String pathNow = "";
	String playpath;
	String filepath;
	boolean edit =false;
	boolean m3u, pls, mp3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explorer);
		myPath = (TextView) findViewById(R.id.path);

		
		// set checkbox 
		final CheckBox checkm3u = (CheckBox) findViewById(R.id.checkm3u);
		if (!checkm3u.isChecked()) {
			checkm3u.setChecked(true);
		};

		final CheckBox checkpls = (CheckBox) findViewById(R.id.checkpls);
		if (checkpls.isChecked()) {
			checkpls.setChecked(false);
		};

		final CheckBox checkmp3 = (CheckBox) findViewById(R.id.checkmp3);
		if (checkmp3.isChecked()) {
			checkmp3.setChecked(false);
		};

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		root = Environment.getExternalStorageDirectory().getPath();

		//read extra's
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			stPath = extras.getString("the path");
			edit = extras.getBoolean("editm3u");
			myPath.setText(stPath);
		}


		if(pathNow.equals("")){
			pathNow = stPath;
		}
		
		m3u = true; pls = false; mp3 = false;

		getDir(stPath, m3u, pls, mp3);

		// checkbox settings
		checkm3u.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(checkm3u.isChecked()){
					m3u = true;
				} else{
					m3u = false;	
				}
				getDir(pathNow, m3u, pls, mp3);
			}
		});

		// checkbox settings
		checkpls.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(checkpls.isChecked()){
					pls = true;
				} else{
					pls = false;
				}
				getDir(pathNow, m3u, pls, mp3);
			}
		});

		// checkbox settings
		checkmp3.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if(checkmp3.isChecked()){
					mp3 = true;
				} else{
					mp3 = false;
				}
				getDir(pathNow, m3u, pls, mp3);
			}
		});
	}

	public void getDir(String stPath, boolean m3u, boolean pls, boolean mp3)
	{
		myPath.setText("Location: " + stPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(stPath);
		File[] files = f.listFiles();

		item.add("../");
		path.add(f.getParent()); 

		if(!stPath.equals(pathNow)){
			pathNow = stPath;
		}
		
		if(!stPath.equals(root))
		{
			item.add(root);
			path.add(root);
		}

		for(int i=0; i < files.length; i++)
		{
			File file = files[i];

			if(!file.isHidden() && file.canRead() && file.isDirectory() ){
				path.add(file.getPath());
				item.add(file.getName() + "/");
			}

			if(m3u){
				if(!file.isHidden() && file.canRead() && (file.getName().endsWith(".m3u") )){

					path.add(file.getPath());
					item.add(file.getName());
				}
			}
			if(pls){
				if(!file.isHidden() && file.canRead() && (file.getName().endsWith(".pls")  )){
					path.add(file.getPath());
					item.add(file.getName());
				}
			}
			if(mp3){
				if(!file.isHidden() && file.canRead() && (file.getName().endsWith(".mp3") )){
					path.add(file.getPath());
					item.add(file.getName());
				}
			}

		}

		ArrayAdapter<String> fileList =
				new ArrayAdapter<String>(this, R.layout.row, item);
		setListAdapter(fileList); 
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		final File file = new File(path.get(position));

		if (file.isDirectory())
		{
			if(file.canRead()){
				getDir(path.get(position), m3u, pls, mp3);
			}else{
				new AlertDialog.Builder(this)
				.setIcon(R.drawable.ic_launcher)
				.setTitle("[" + file.getName() + "] folder can't be read!")
				.setPositiveButton("OK", null).show(); 
			} 
		}else {	
			AlertDialog.Builder renameDialog = new AlertDialog.Builder(this);
			renameDialog.setIcon(R.drawable.ic_launcher);
			renameDialog.setTitle(file.getName());
			renameDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					playpath = file.getName();
					filepath = file.getParent();
					launchIntent();
				}
			});
			renameDialog.setNegativeButton("Cancel",null);

			renameDialog.show();
		}
	}

	private void launchIntent() {
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("playpath", playpath);
		i.putExtra("filepath", filepath);
		i.putExtra("editm3u", edit);
		startActivity(i);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			//NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
