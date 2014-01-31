package com.riddim.m3u_edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.riddim.m3u_edit.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Settings extends Fragment{

	private List<String> item = null;
	private List<String> path = null;
	private String root;
	private TextView myPath;
	String newPath;

	String tempfile = "settings";
	FileOutputStream outputStream;

	String browsepath;
	String browse = "0";
	String numberset = "";
	
	boolean show = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.settings, container, false);  

		myPath = (TextView)view.findViewById(R.id.path);
		root = Environment.getExternalStorageDirectory().getPath();
		root = "/storage";
	//	getDir(root);

		//read files
				FileInputStream fis3;

				try {
					fis3 = ((MainActivity)getActivity()).openFileInput("number");
					byte[] input = new byte[fis3.available()];
					numberset = "";
					while (fis3.read(input) != -1) {}
				
					numberset += new String(input);
					
					TextView number = (TextView) view.findViewById(R.id.numberScreen);
					number.setText(numberset);
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					numberset = "";

				} catch (IOException e) {
					e.printStackTrace(); 
					numberset = "";
				}  
			 
		
		
		//read files
		FileInputStream fis2;

		try {
			fis2 = ((MainActivity)getActivity()).openFileInput("musicpath");
			byte[] input = new byte[fis2.available()];
			browsepath = "";
			while (fis2.read(input) != -1) {}
		
			browsepath += new String(input);

			TextView musicpath = (TextView) view.findViewById(R.id.musicpath);
			musicpath.setText(browsepath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if(!show){
			//((MainActivity)getActivity()).createDialog("No SettingsFile found", "Dismiss", "Error", true);
			show = true;
			}
		} catch (IOException e) {
			e.printStackTrace(); 
		}  
	 
		//read files
		FileInputStream fis;

		try {
			fis = ((MainActivity)getActivity()).openFileInput("playpath");
			byte[] input = new byte[fis.available()];
			browsepath = "";
			while (fis.read(input) != -1) {}
		
			browsepath += new String(input);

			TextView playlistpath = (TextView) view.findViewById(R.id.playlistpath);
			playlistpath.setText(browsepath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if(!show){
				//((MainActivity)getActivity()).createDialog("No SettingsFile found", "Dismiss", "Error", true);
				show = true;
			}
		} catch (IOException e) {
			e.printStackTrace(); 
		}  
		

		
		
		
		//read extra's
				Bundle extras = getActivity().getIntent().getExtras();
				if (extras != null) {
					browse = extras.getString("browse");
					browsepath = extras.getString("path");
					if(browse == null || browsepath == null){
						browse = "0";
						browsepath = "";
					}
				}						
				
		if(browse.matches("1")){
			TextView musicpath = (TextView) view.findViewById(R.id.musicpath);
			musicpath.setText(browsepath);
			
			if(browse.equals("1")){
				try {
					outputStream = ((Context)getActivity()).openFileOutput("musicpath", Context.MODE_PRIVATE);
					outputStream.write(browsepath.getBytes());
					outputStream.close();
					if(!show){
					((MainActivity)getActivity()).createDialog("settingsFile(playlistpath) created", "Proceed", "Succes", true);
					show = true;
					}
					
				} catch (Exception e) {
					if(!show){
					((MainActivity)getActivity()).createDialog("No SettingsFile created", "Dismiss", "Error", true);
					show = true;
					}
					e.printStackTrace();
				}
			}
			
		}
		
		if(browse.matches("2")){
			TextView playlistpath = (TextView) view.findViewById(R.id.playlistpath);
			playlistpath.setText(browsepath);
		
			if ( browse.equals("2")){
				try {
					outputStream = ((Context)getActivity()).openFileOutput("playpath", Context.MODE_PRIVATE);
					outputStream.write(browsepath.getBytes());
					outputStream.close();
					if(!show){
					((MainActivity)getActivity()).createDialog("settingsFile(playlistpath) created", "Proceed", "Succes", true);
					show = true;
					}
					
				} catch (Exception e) {
					if(!show){
						((MainActivity)getActivity()).createDialog("No SettingsFile created", "Dismiss", "Error", true);
						show = true;
						}
					e.printStackTrace();
				}
			}
			
		}
			
		
		Button musicbrowse = (Button) view.findViewById(R.id.browsemusic);
		musicbrowse.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), FolderExplorer.class);
				show = false;
				intent.putExtra("browse", "1");
				startActivity(intent);
			
			}
		});


		Button musicplay = (Button) view.findViewById(R.id.browseplay);
		musicplay.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), FolderExplorer.class);
				show = false;
				intent.putExtra("browse", "2");
				startActivity(intent);
			
			}
		});
		
		
		
		Button startscreen = (Button) view.findViewById(R.id.applyscreen);
		
		startscreen.setOnClickListener(new OnClickListener(){
			String strnumber;
			@Override
			public void onClick(View v) {
			TextView number = (TextView) view.findViewById(R.id.numberScreen);
			try {
				 
			int intnumber = Integer.parseInt(number.getText().toString());
			if(intnumber > 3) {
				((MainActivity)getActivity()).createDialog("-_- Serious, trying to find bugs huh. Well u failed, startscreen is Settings(3)", "Dismiss", "-_-", true);
				number.setText("3");
			} 
			} catch (Exception e) {
				((MainActivity)getActivity()).createDialog("No number!!!", "Dismiss", "Error", true);
				e.printStackTrace();
				strnumber = "0";
			}
			
			strnumber = number.getText().toString();
			try {
				outputStream = ((Context)getActivity()).openFileOutput("number", Context.MODE_PRIVATE);
				outputStream.write(strnumber.getBytes());
				outputStream.close();
				((MainActivity)getActivity()).createDialog("Yahoo, new startscreen!", "Proceed", "Succes", true);
		
			} catch (Exception e) {
				if(!show){
					((MainActivity)getActivity()).createDialog("No SettingsFile created", "Dismiss", "Error", true);
					show = true;
					}
				e.printStackTrace();
			}
			
			}
			
		});
		
		/*Button apply = (Button) view.findViewById(R.id.applysettings);
		apply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TextView newpath = (TextView) view.findViewById(R.id.musicpath);
				String path;
				if(newpath.getText().toString().isEmpty()){

					path = newPath;

				} else {
					path = newpath.getText().toString();
				}


				//make temp file



			}

		});
*/

		/*Button reset = (Button) view.findViewById(R.id.resetsettings);
		reset.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				String rpath = "/storage/sdcard1/Music";

				try {
					outputStream = ((Context)getActivity()).openFileOutput(tempfile, Context.MODE_PRIVATE);
					outputStream.write(rpath.getBytes());
					outputStream.close();
					((MainActivity)getActivity()).createDialog("Default settingsFile created", "Proceed", "Succes", false);
					
					((MainActivity)getActivity()).dialogButton.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
				
							Intent i = new Intent(getActivity(), MainActivity.class);
							startActivity(i);
						}
					});	
					
				} catch (Exception e) {
					((MainActivity)getActivity()).createDialog("No SettingsFile created", "Dismiss", "Error", true);
					e.printStackTrace();
				}

			}

		});*/



		return view;

	}

}
	
	/*private void getDir(String dirPath)
	{
		myPath.setText("Location: " + dirPath);
		item = new ArrayList<String>();
		path = new ArrayList<String>();
		File f = new File(dirPath);
		File[] files = f.listFiles();

		if(!dirPath.equals(root))
		{
			item.add(root);
			path.add(root);
			item.add("../");
			path.add(f.getParent()); 

			newPath = root;
		}

		for(int i=0; i < files.length; i++)
		{
			File file = files[i];

			if(!file.isHidden() && file.canRead()){
				path.add(file.getPath());
				if(file.isDirectory()){
					item.add(file.getName() + "/");

					newPath = (file.getParent());
				}else{
					item.add(file.getName());
				}
			} 
		}

		ArrayAdapter<String> fileList =
				new ArrayAdapter<String>(getActivity(), R.layout.row, item);
		setListAdapter(fileList); 
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		File file = new File(path.get(position));

		if (file.isDirectory())
		{
			if(file.canRead()){
				getDir(path.get(position));
			}else{
				new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_launcher)
				.setTitle("[" + file.getName() + "] folder can't be read!")
				.setPositiveButton("OK", null).show(); 
			} 
		}else {
			new AlertDialog.Builder(getActivity())
			.setIcon(R.drawable.ic_launcher)
			.setTitle("[" + file.getName() + "]")
			.setPositiveButton("OK", null).show();

		}
	}

}
*/






/*
	public void updateArticleView(String encoded) {
		TextView content =(TextView) getView().findViewById(R.id.content2);
		content.setText(encoded);
	}
 */


