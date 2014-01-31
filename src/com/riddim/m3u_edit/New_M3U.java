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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class New_M3U  extends Fragment  implements OnCreateContextMenuListener {
	Boolean settingsread = false ;
	String musicpath = "";
	View view;
	String path ="", settings = "";

	private ArrayList<String> item = null;
	private ArrayList<String> mp3 = new ArrayList<String>();
	private ArrayList<String> all = new ArrayList<String>();
	private ArrayList<Boolean> status = new ArrayList<Boolean>();
	private List<String> pathlist = null;
	String fileName = "";
	String encoded = "";
	String newfile = "";
	boolean show = true;
	boolean stop = true;
	boolean checkall = false;
	int sort = 0;
	int memsort = 0;
	boolean make = false;

	ProgressDialog progressDialog;

	boolean loadingb = false;

	File f = new File(musicpath);
	File[] files = f.listFiles();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.new_m3u, container, false);


		//Read File internal file

		FileInputStream fis;
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
			settings = "";
			while (fis2.read(input) != -1) {}
			settings += new String(input);
			settingsread = true;

			path = settings;
			//	defaultloc.setText(path);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			((MainActivity)getActivity()).createDialog("No SettingsFile found", "Dismiss", "Error", true);
			path = "/storage/sdcard1/Music";
			//	defaultloc.setText(path);

		} catch (IOException e) {
			e.printStackTrace(); 
		}  


		final Thread thread = new Thread()
		{
			@Override
			public void run() {
				if(show){
					item = new ArrayList<String>();
					all = new ArrayList<String>();
					pathlist = new ArrayList<String>();
					if(!musicpath.equals("")){

						File f = new File(musicpath);
						File[] files = f.listFiles();
						
						
						
						//read files
						FileInputStream fis2;

						try {
							fis2 = ((MainActivity)getActivity()).openFileInput("sort");
							byte[] input = new byte[fis2.available()];
							sort = 0;
							while (fis2.read(input) != -1) {}
						
							sort += Integer.parseInt(new String(input));
							
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						files = sort(sort);
						
						makePreList make = new makePreList(files);
						make.execute();
					}

					//read extra's

					Bundle extras = ((MainActivity)getActivity()).getIntent().getExtras();
					if (extras != null) {
						fileName = extras.getString("playpath");
						Boolean edit = extras.getBoolean("editm3u");
						final EditText name = (EditText) view.findViewById(R.id.make_filename);
						if(edit){
							((MainActivity)getActivity()).runOnUiThread(new Runnable() {
								public void run() {
									name.setText(fileName);							
								}
							});
						}
						if(extras.getString("filepath") != null){
							path = extras.getString("filepath");
						}	

						if(edit){
							mp3.removeAll(mp3);
							Edit(false);
						}
					}
					show = false;
				}

				((MainActivity)getActivity()).runOnUiThread(new Runnable() {
					public void run() {
						//	populateListview();					
					}
				});
			}
		}; thread.start();


		Button butbrowse = (Button) view.findViewById(R.id.make_butbrowse);
		butbrowse.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), Explorer.class);
				intent.putExtra("the path", path);
				intent.putExtra("editm3u", true);
				startActivity(intent);
				show = true;


			}

		});


		Button butmake = (Button) view.findViewById(R.id.make_butmake);
		butmake.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				MakeFile();
			}

		});

		Button uncheck = (Button) view.findViewById(R.id.make_uncheckall);
		uncheck.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				for(int i = 0; i < status.size(); i++){
					status.set(i, false);
				}
				mp3.removeAll(mp3);
				populateListview();
			}
		});

		Button check = (Button) view.findViewById(R.id.make_checkall);
		check.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {

				for(int i = 0; i < status.size(); i++){
					status.set(i, true);
				}
				checkall = true;
				mp3 = all;
				populateListview();
			}

		});

		final Button sort = (Button) view.findViewById(R.id.make_sort);
		sort.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.sort, menu);
				
				  
			}
			
		});
		sort.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Toast toast = Toast.makeText(getActivity(), "use a long press to bring up a sort menu:)", Toast.LENGTH_LONG);
				toast.show();
				
			}
			
		});
		
		check.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.checked, menu);
			}
		});

	//update listview after 10s    // update view:)
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
			makePreList make = new makePreList(null);
			if(!item.isEmpty()){
			make.execute();
			}
			}
		}, 10);

		return view;
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		String Stringsort;
		Intent intent = new Intent(getActivity(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		
		switch (item.getItemId()) {
		case R.id.Uncheck:
			for(int i = 0; i < status.size(); i++){
				status.set(i, false);
			}
			mp3.removeAll(mp3);
			populateListview();
			return true;
		case R.id.Check:
			for(int i = 0; i < status.size(); i++){
				status.set(i, true);
			}
			checkall = true;
			mp3 = all;
			populateListview();
			return true;
		
		case R.id.A_Z:
			Stringsort = "0";
			sort = 0;
			makesort(Stringsort);
			intent.putExtra("sort", Stringsort);
			startActivity(intent);
			return true;
		case R.id.Z_A:
			Stringsort = "1";
			sort = 1;
			makesort(Stringsort);
			intent.putExtra("sort", Stringsort);
			startActivity(intent);
			
			return true;
			
		case R.id.Last_Modified:
			 Stringsort = "2";
			 sort = 2;
			 makesort(Stringsort);
			intent.putExtra("sort", Stringsort);
			startActivity(intent);
			return true;
			
		case R.id.First_Modified:
			 Stringsort = "3";
			 sort = 3;
			 makesort(Stringsort);
			intent.putExtra("sort", Stringsort);
			startActivity(intent);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	
	public void makesort(String sort){
		try {
			FileOutputStream outputStream;
			outputStream = ((Context)getActivity()).openFileOutput("sort", Context.MODE_PRIVATE);
			outputStream.write(sort.getBytes());
			outputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void populateListview() {
		ArrayAdapter<String> adapter = new MyListAdapter();
		ListView list = (ListView) view.findViewById(R.id.make_list);
		list.setAdapter(adapter);
	}

	private class MyListAdapter extends ArrayAdapter<String> {
		public MyListAdapter(){
			super(getActivity(), R.layout.checkbox_row, item);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null){
				itemView = ((MainActivity)getActivity()).getLayoutInflater().inflate(R.layout.checkbox_row, parent, false);
			}

			final CheckBox music = (CheckBox) itemView.findViewById(R.id.make_check);

			music.setTag(position);
			TextView txt = (TextView) itemView.findViewById(R.id.make_listname);
			txt.setText(item.get(position));

			music.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					int position = (Integer) music.getTag();
					if(isChecked){

						status.set(position, true);
						if(!mp3.contains(item.get(position)) && !mp3.contains(musicpath + "/" + item.get(position))){
							mp3.add(item.get(position));
						}
					} else {
						status.set(position, false);
						mp3.remove(item.get(position));
						mp3.remove(musicpath + "/" + item.get(position));
					}
				}
			});

			music.setChecked(status.get(position));

			System.out.print(position + " is " + status.get(position) + "\n");
			return itemView;

		}
	}

	Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			progressDialog.dismiss();
		}
	};


	public void Edit(boolean auto){

		Thread thread = new Thread()
		{
			@Override
			public void run() {
				((MainActivity)getActivity()).runOnUiThread(new Runnable() {
					public void run() {
						progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");		
					}
				});		
				encoded ="\n";
				mp3.removeAll(mp3);
				File myDir = new File(path);    
				myDir.mkdirs();
				File f = new File (myDir, fileName);
				StringBuffer sb = new StringBuffer();
				try {
					BufferedReader br = new BufferedReader(new FileReader(f));
					String line;
					while(true)
					{
						line=br.readLine();
						if(line==null)
							break;
						sb.append(line + "\n");				 					    
					}
					br.close();
				}
				catch (IOException e) {
					fileName ="";
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
						while ((line = reader.readLine()) != null) {

							encoded += (line + "\n");
							if(!line.equals("#EXTM3U")){
								mp3.add(line);
							}
						}

						for(int i=0; i < item.size(); i++){
							for(int j = 0; j < mp3.size(); j++){
								if(mp3.get(j).contains(item.get(i))){
									status.set(i, true);
									break;
								}
							}
						}
					}
					finally {
						reader.close();

						((MainActivity)getActivity()).runOnUiThread(new Runnable() {
							public void run() {
								populateListview();		
								((MainActivity)getActivity()).createDialog("Check the music that was found in the playlist, you can now edit it by unchecking or checking music", "Dismiss", "Succes", true);
								mHandler.sendEmptyMessage(0);
							}
						});

					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		};
		thread.start();

	}


	public void MakeFile(){
		Thread thread = new Thread()
		{
			@Override
			public void run() {
				((MainActivity)getActivity()).runOnUiThread(new Runnable() {
					public void run() {
						progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");					
					}
				});

				File myDir = new File(path);    
				myDir.mkdirs();
				final EditText name = (EditText) view.findViewById(R.id.make_filename);
				fileName = name.getText().toString();
				if(fileName.equals("")){

					((MainActivity)getActivity()).runOnUiThread(new Runnable() {
						public void run() {
							((MainActivity)getActivity()).createDialog("Type a fileName pls", "Dismiss", "Error", true);
						}
					});
					mHandler.sendEmptyMessage(0);
					return;
				}
				if(!fileName.endsWith(".m3u")){
					fileName = (fileName + ".m3u");
				}
				try{
					File file = new File(myDir, fileName);
					if(file.exists())  {
						file.delete();
					}

				}catch (Exception e){
					((MainActivity)getActivity()).runOnUiThread(new Runnable() {
						public void run() {
							((MainActivity)getActivity()).createDialog("Something went wrong horibly wrong!!!", "Dismiss", "Error", true);
						}
					});

					System.err.println("Error: " + e.getMessage());
				}
				File f = new File (myDir, fileName);

				try{
					FileWriter fstream = new FileWriter(f);
					BufferedWriter outobj = new BufferedWriter(fstream);
					newfile += "#EXTM3U\n"; 
					for(String s : mp3){
						if(s.endsWith(".mp3")){
							if(s.contains(musicpath)){
								newfile += (s + "\n");
							} else{
								newfile += (musicpath + "/" + s + "\n");
							}
						} else
							newfile += (s + "\n");
					}
					outobj.write(newfile);
					outobj.close();

					((MainActivity)getActivity()).runOnUiThread(new Runnable() {
						public void run() {
							((MainActivity)getActivity()).createDialog(getString(R.string.playlistmade), "Dismiss", "Succes", true);
						}
					});

				}catch (Exception e){
					((MainActivity)getActivity()).runOnUiThread(new Runnable() {
						public void run() {
							((MainActivity)getActivity()).createDialog("Something went wrong when making the file!!!", "Dismiss", "Error", true);
						}
					});
					System.err.println("Error: " + e.getMessage());
				}
				finally {
					newfile = "";
					mp3.removeAll(mp3);

					for(int i = 0; i < status.size(); i++){
						status.set(i, false);
					}
					mHandler.sendEmptyMessage(0);

					MediaScannerConnection.scanFile(getActivity(), new String[] {path + fileName}, null, null);
					((MainActivity)getActivity()).runOnUiThread(new Runnable() {
						public void run() {
							name.setText("");
							populateListview();		
						}
					});

				}
			}

		};

		thread.start();
	}

	public File[] sort(int sort){
		File f = new File(musicpath);
		File[] files = f.listFiles();
		
		switch (sort){
		case 0: //A-Z
			Arrays.sort(files);
		break;
		case 1: //Z-A
			Arrays.sort(files,Collections.reverseOrder());
			break;
		case 2:
			//Last modified
			Arrays.sort(files, new Comparator<File>(){
				public int compare(File f2, File f1)
				{
					return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				} });
			break;
		case 3:
			//First modified
			Arrays.sort(files, new Comparator<File>(){
				public int compare(File f2, File f1)
				{
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				} });
		default:
			Arrays.sort(files);
			break;
		}
		
		
		return files;
		
	}
	
	
	
	private class makePreList extends AsyncTask<File, Integer, Long> {

		public makePreList(File[] files) {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			files = sort(sort);
		}

		@Override
		protected Long doInBackground(File... params) {
			//File f = new File(musicpath);
			//File[] files = f.listFiles();
			//File[] files = sort(sort);
		
			for(int i=0; i < files.length; i++)
			{
				File file = files[i];
				if(!file.isHidden() && file.canRead() && (file.getName().endsWith(".mp3") )){
					pathlist.add(file.getPath());
					item.add(file.getName());
					all.add(file.getName());
				}
			}
			
			for (int i = 0; i < item.size(); i++) {
				status.add(false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			populateListview();
		}
	}
}






