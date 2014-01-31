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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import android.widget.ProgressBar;
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
	Message msg = new Message();
	boolean show = true;

	ProgressDialog progressDialog;


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

		ProgressBar	mProgress = (ProgressBar) view.findViewById(R.id.edit_file_progressBar);
		mProgress.setVisibility(View.GONE);
		TextView loading = (TextView) view.findViewById(R.id.Edit_file_loading);
		loading.setVisibility(View.GONE);


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
			if(!show){
				((MainActivity)getActivity()).createDialog("No Musicfolder found, go to settings!", "Dismiss", "Error", true);
				show = true;
				}
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace(); 
		}  




		//read settings file

		FileInputStream fis2;

		try {
			fis2 = ((MainActivity)getActivity()).openFileInput("playpath");
			byte[] input = new byte[fis2.available()];
			settings = "";
			while (fis2.read(input) != -1) {}
			settings += new String(input);
			settingsread = true;
			path = settings;
			defaultloc.setText(path);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if(!show){
			((MainActivity)getActivity()).createDialog("No SettingsFile found", "Dismiss", "Error", true);
			show = true;
			}
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
			Boolean edit = extras.getBoolean("editm3u");
			String value = extras.getString("playpath");

			EditText loc = (EditText) view.findViewById(R.id.locFile);
			if(value != null){
				if(!edit){
					loc.setText(value);
				}
				if(extras.getString("filepath") != null){
					path = extras.getString("filepath");
				}
				defaultloc.setText(path);

				if(!edit){
					Edit(false);
				}
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
				show = true;
			}
		});

		Button autoedit = (Button) view.findViewById(R.id.autoedit);
		autoedit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				EditPlay edit = new EditPlay();
				edit.execute();
			//	Edit(true);
			}
		});
		

		

		return view;
	}

	
	
	
	Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			 	progressDialog.dismiss();
		}
	};

	public void Edit(final boolean auto){
		final ProgressBar	mProgress = (ProgressBar) view.findViewById(R.id.edit_file_progressBar);
		mProgress.setVisibility(View.VISIBLE);
		final TextView loading = (TextView) view.findViewById(R.id.Edit_file_loading);
		loading.setVisibility(View.VISIBLE);

		Thread thread = new Thread()
		{
			@Override
			public void run() {
	
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
						final String replauto;

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
							((MainActivity)getActivity()).runOnUiThread(new Runnable() {
								public void run() {
									((MainActivity)getActivity()).createDialog("Auto Edit completed! New path: " + replauto, "Ok", "NICE", true);
								}
							});

						}
					}



					catch (IOException e) {
						strFilePath ="";
						((MainActivity)getActivity()).runOnUiThread(new Runnable() {
							public void run() {
								((MainActivity)getActivity()).createDialog("Error no such file or directory, try a different name or path", "Dismiss", "Error", true);
							}
						});


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
								//	mp3String.add(line);

								} else {
									if(!line.endsWith(".mp3")){
										mp3block += (line + "\n");
									}	
									else {
										File file = new File(line);
										if(file.exists())  {

											mp3block += line;
											mp3String.add(line);
											mp3block = "";
										}
										else {

											mp3block += line;
											mp3String.add(line);
											count++;
											notExist += (mp3block + "\n"); 
											mp3block = "";
										}
									}
								}
								encoded += (line + "\n");
							}

							if(!notExist.equals("")){
								((MainActivity)getActivity()).runOnUiThread(new Runnable() {
									public void run() {

										if(show){
											if(count<5){
												((MainActivity)getActivity()).createDialog("Did not exist:\n" + notExist, "Dismiss", "Error", true);

											} else {
												((MainActivity)getActivity()).createDialog("A lot of wrong paths, check the crosses XD", "Dismiss", "Error", true);
											}
											show = false;
										}
										count = 0;
									}
								});	
							}
						}
						finally {

							reader.close();

						}

						((MainActivity)getActivity()).runOnUiThread(new Runnable() {
							public void run() {
								populateListview();
								mProgress.setVisibility(View.GONE);
								loading.setVisibility(View.GONE);
							}
						});

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{	
					//			main.createDialog("Error no access to sd card", "Ok", "Error", true);
				}
			}
		};
		thread.start();

	}

	private class EditPlay extends AsyncTask<Long, Integer, Long> {

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			populateListview();
			mHandler.sendEmptyMessage(0);
			if(notExist.equals("")){
				((MainActivity)getActivity()).createDialog("AutoEdit completed!!! "  , "Okey", "Done", true);
			} else {
				if(show){
					if(count<5){
						((MainActivity)getActivity()).createDialog("AutoEdit completed!!!\n but... did not exist:\n" + notExist, "Dismiss", "Error", true);

					} else {
						((MainActivity)getActivity()).createDialog("A lot of wrong paths, check the crosses XD", "Dismiss", "Error", true);
					}
					show = false;
				}
				count = 0;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			show = true;
			progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");	
		}

		@Override
		protected Long doInBackground(Long... arg0) {
			encoded = "";
			notExist = "";
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
					final String replauto;

					replauto = (musicpath + "/");
					while(true)
					{
						line=br.readLine();
						if(line==null)
							break;
						sb.append(line + "\n");				 					    
				
					
							int last = sb.lastIndexOf("\\");
							int del = line.lastIndexOf("\\");
							if(last >= 0 && del >=0)
							{
								sb.replace(last - del, last + 1 , replauto);
							}
							
							int last2 = sb.lastIndexOf("/");
							int del2 = line.lastIndexOf("/");
							if(last2 >= 0 && del2 >=0) {
								sb.replace(last2 - del2, last2 + 1 , replauto);	
						}
					}
					br.close();
				}

				catch (IOException e) {
					strFilePath ="";
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
							//	mp3String.add(line);

							} else {
								if(!line.endsWith(".mp3")){
									mp3block += (line + "\n");
								}	
								else {
									File file = new File(line);
									if(file.exists())  {

										mp3block += line;
										mp3String.add(line);
										mp3block = "";
									}
									else {
										mp3block += line;
										mp3String.add(line);
										count++;
										notExist += (line + "\n"); 
										mp3block = "";
									}
								}
							}
							encoded += (line + "\n");
						}
					}
					finally {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			return null;
		}
	}


	public class PullTasksThread extends Thread {
		public void run () {
			populateListview();
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

		//	if(position == 0){

			//	ImageView imageView = (ImageView) itemView.findViewById(R.id.vink);
		//		imageView.setImageDrawable(null);
		//	} else {
				if(notExist.contains(current)){
					ImageView imageView = (ImageView) itemView.findViewById(R.id.vink);
					imageView.setImageResource(R.drawable.cross);
				} else {
					ImageView imageView = (ImageView) itemView.findViewById(R.id.vink);
					imageView.setImageResource(R.drawable.vinkje);
				}
		//	}
			TextView txt = (TextView) itemView.findViewById(R.id.rowtext);
			txt.setText(mp3String.get(position));


			txt.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {


				}
			});



			return itemView;
		}
	}

	// check external storage 
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}





}

