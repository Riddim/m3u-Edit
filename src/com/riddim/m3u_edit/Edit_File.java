package com.riddim.m3u_edit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Edit_File extends Fragment{
	View  view;
	String lineChange = "";
	String replace = "";
	String notExist = "";
	String path;
	String musicpath;
	String encoded = "";
	String tempfile = "lastplay";
	FileOutputStream outputStream;
	boolean settingsread = false;
	String settings = "";
	ArrayList<String> mp3String = null;
	OnHeadlineSelectedListener mCallback;
	MainActivity main = new MainActivity();
	int count = 0;
	
	// Container Activity must implement this interface
	public interface OnHeadlineSelectedListener {
		public void onArticleSelected(String encoded);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnHeadlineSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.edit_file, container, false);

		TextView defaultloc =(TextView) view.findViewById(R.id.defaultloc);


		//Read File internal file

		FileInputStream fis;
		String result = "";
		try {
			fis = ((MainActivity)getActivity()).openFileInput("musicpath");
			byte[] input = new byte[fis.available()];
			while (fis.read(input) != -1) {}
			musicpath = "";
			musicpath += new String(input);


		} catch (FileNotFoundException e) {
			((MainActivity)getActivity()).createDialog("No Musicfolder found, go to settings!", "Dismiss", "Error", true);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}  




		//read settings file

		FileInputStream fis2;

		try {
			fis2 = ((MainActivity)getActivity()).openFileInput("playpath");
			byte[] input = new byte[fis2.available()];
			while (fis2.read(input) != -1) {}
			settings += new String(input);
			settingsread = true;

			path = settings;
			defaultloc.setText(path);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			((MainActivity)getActivity()).createDialog("No SettingsFile found", "Dismiss", "Error", true);
			path = "/storage/sdcard1/Music";
			defaultloc.setText(path);

		} catch (IOException e) {
			e.printStackTrace(); 
		}  


		// set checkbox false

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.defaultcheck);
		if (checkBox.isChecked()) {
			checkBox.setChecked(false);
		};

		// checkbox settings
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				String sdcard = Environment.getExternalStorageDirectory().toString();

				TextView defaultloc =(TextView) view.findViewById(R.id.defaultloc);
				if (checkBox.isChecked()) {
					path = sdcard + "/Music";
					defaultloc.setText(path);

				} else
				{
					if (settingsread){
						path = settings;
					} else {
						path = "/storage/sdcard1/Music";	
					}
					defaultloc.setText(path);
				}
			}
		});

		//read extra's

		Bundle extras = ((MainActivity)getActivity()).getIntent().getExtras();
		if (extras != null) {
			String value = extras.getString("playpath");

			EditText loc = (EditText) view.findViewById(R.id.locFile);
			if(value != null){
				loc.setText(value);

				if(extras.getString("filepath") != null){
					path = extras.getString("filepath");
				}

				defaultloc.setText(path);
				Edit(false);
			}

		}

		Button fullScreen = (Button) view.findViewById(R.id.fullscreen);
		fullScreen.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), tempSee_File.class);
				//intent.putStringArrayListExtra("data", mp3String);
				intent.putExtra("text", encoded);
				startActivity(intent);

			}

		});

		Button browse = (Button) view.findViewById(R.id.browseedit);
		browse.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), Explorer.class);
				intent.putExtra("the path", path);
				startActivity(intent);
			}
		});

		/*		Button letgo = (Button) view.findViewById(R.id.letgo);
		letgo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {

				Edit(false);
			}
		});*/

		Button autoedit = (Button) view.findViewById(R.id.autoedit);
		autoedit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {

				Edit(true);
			}
		});
		return view;
	}

	public void Edit(boolean auto){
		Boolean access = isExternalStorageWritable();
		encoded = "";
		notExist = "";
		if(access){
			String strFilePath;
			TextView FilePath = (TextView) view.findViewById(R.id.locFile);
			strFilePath = FilePath.getText().toString();
			File myDir = new File(path);    
			myDir.mkdirs();
			File f = new File (myDir, strFilePath);
			StringBuffer sb = new StringBuffer();
			mp3String = new ArrayList<String>();

			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				String replauto;
				
				replauto = (musicpath + "/");
				while(true)
				{
					line=br.readLine();
					if(line==null)
						break;
					sb.append(line + "\n");				 					    
					if(!auto){
						int cnt1 = sb.indexOf(lineChange);
						if (cnt1 >= 0){
							sb.replace(cnt1,cnt1+lineChange.length(), replace);
						}
					}
					else {
						int last = sb.lastIndexOf("\\");
						int del = line.lastIndexOf("\\");
						if(last >= 0 && del >=0)
						{
							sb.replace(last - del, last + 1 , replauto);
						}
						int last2 = sb.lastIndexOf("/");
						int del2 = line.lastIndexOf("/");
						if(last2 >= 0 && del2 >=0)
						{
							sb.replace(last2 - del2, last2 + 1 , replauto);
						}
					}
				}
				br.close();
				if(auto){
					((MainActivity)getActivity()).createDialog("Auto Edit completed! New path: " + replauto, "Ok", "NICE", true);
				}
			}
			catch (IOException e) {
				strFilePath ="";
				((MainActivity)getActivity()).createDialog("Error no such file or directory, try a different name or path", "Dismiss", "Error", true);
				f.delete();
				e.printStackTrace();
			}
			try{
				FileWriter fstream = new FileWriter(f);
				BufferedWriter outobj = new BufferedWriter(fstream);
				outobj.write(sb.toString());
				outobj.close();

			}catch (Exception e){
				System.err.println("Error: " + e.getMessage());
			}

			try {
				final BufferedReader reader = new BufferedReader(new FileReader(f));


				try {
					String line;
					String mp3block =  "";
					while ((line = reader.readLine()) != null) {
						
						if(line.equals("#EXTM3U")){
							mp3String.add(line);

						} else {
							if(!line.endsWith(".mp3")){
								mp3block += (line + "\n");
							}	
							else {
								File file = new File(line);
								if(file.exists())  {

									mp3block += line;
									mp3String.add(mp3block);
									mp3block = "";
								}
								else {

									mp3block += line;
									mp3String.add(mp3block);
									count++;
									notExist += (mp3block + "\n"); 
									mp3block = "";
								}
							}
						}
						encoded += (line + "\n");
					}
					if(!notExist.equals("")){
						if(count<5){
							
							((MainActivity)getActivity()).createDialog("Did not exist:\n" + notExist, "Dismiss", "Error", true);
						} else {
							((MainActivity)getActivity()).createDialog("A lot of wrong paths, check the crosses XD", "Dismiss", "Error", true);
						}
						count =0;
						
					}
				}
				finally {

					reader.close();
				}
				populateListview();

				

				//	ArrayAdapter<String> listmp3 =
				//			new ArrayAdapter<String>(getActivity(), R.layout.row2, mp3String);
				//	setListAdapter(listmp3); 

				mCallback.onArticleSelected(encoded);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{	

			main.createDialog("Error no access to sd card", "Ok", "Error", true);
		}
	}



	private void populateListview() {
		ArrayAdapter<String> adapter = new MyListAdapter();
		ListView list = (ListView) view.findViewById(R.id.list);
		list.setAdapter(adapter);
	}

	private class MyListAdapter extends ArrayAdapter<String> {
		public MyListAdapter(){
			super(getActivity(), R.layout.row2, mp3String);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null){
				itemView = ((MainActivity)getActivity()).getLayoutInflater().inflate(R.layout.row2, parent, false);
			}
			String current = mp3String.get(position);

			if(position == 0){

				ImageView imageView = (ImageView) itemView.findViewById(R.id.vink);
				imageView.setImageDrawable(null);
			} else {
				if(notExist.contains(current)){
					ImageView imageView = (ImageView) itemView.findViewById(R.id.vink);
					imageView.setImageResource(R.drawable.cross);
				} else {
					ImageView imageView = (ImageView) itemView.findViewById(R.id.vink);
					imageView.setImageResource(R.drawable.vinkje);
				}
			}
			TextView txt = (TextView) itemView.findViewById(R.id.rowtext);
			txt.setText(mp3String.get(position));

			return itemView;
		}
	}

	/*
	// read user input
	public String DefaultProg(){	
		TextView Change =(TextView) view.findViewById(R.id.replace);
		lineChange = Change.getText().toString();

		TextView Repl =(TextView) view.findViewById(R.id.replacewith);
		replace = Repl.getText().toString();

		return lineChange + replace;
	}
	 */



	// check external storage 
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}





}

