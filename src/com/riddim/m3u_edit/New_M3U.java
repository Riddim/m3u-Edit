package com.riddim.m3u_edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class New_M3U  extends Fragment {
	Boolean settingsread = false ;
	String musicpath = "";
	View view;
	String path ="", settings = "";

	private ArrayList<String> item = null;
	private List<String> pathlist = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.new_m3u, container, false);





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
			//	defaultloc.setText(path);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			((MainActivity)getActivity()).createDialog("No SettingsFile found", "Dismiss", "Error", true);
			path = "/storage/sdcard1/Music";
			//	defaultloc.setText(path);

		} catch (IOException e) {
			e.printStackTrace(); 
		}  


		item = new ArrayList<String>();
		pathlist = new ArrayList<String>();
		File f = new File(musicpath);
		File[] files = f.listFiles();
		for(int i=0; i < files.length; i++)
		{
			File file = files[i];
			if(!file.isHidden() && file.canRead() && (file.getName().endsWith(".mp3") )){
				pathlist.add(file.getPath());
				item.add(file.getName());
			}
		}

		populateListview();



		Button butbrowse = (Button) view.findViewById(R.id.make_butbrowse);
		butbrowse.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), Explorer.class);
				intent.putExtra("the path", path);
				intent.putExtra("editm3u", true);
				startActivity(intent);



			}

		});


		Button butmake = (Button) view.findViewById(R.id.make_butmake);
		butmake.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {




			}

		});



		return view;
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
			String current = item.get(position);

			
			
			TextView txt = (TextView) itemView.findViewById(R.id.make_listname);
			txt.setText(item.get(position));

			return itemView;
		}

	}

}


