package com.riddim.m3u_edit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.riddim.m3u_edit.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



public class Settings extends Fragment{

	String tempfile = "settings";
	FileOutputStream outputStream;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.settings, container, false);  



		Button apply = (Button) view.findViewById(R.id.applysettings);
		apply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TextView newpath = (TextView) view.findViewById(R.id.changepath);
				String path = newpath.getText().toString();

				//make temp file
				try {
					outputStream = ((Context)getActivity()).openFileOutput(tempfile, Context.MODE_PRIVATE);
					outputStream.write(path.getBytes());
					outputStream.close();
					((MainActivity)getActivity()).createDialog("SettingsFile created", "Proceed", "Succes", true);
				} catch (Exception e) {
					((MainActivity)getActivity()).createDialog("No SettingsFile created", "Dismiss", "Error", true);
					e.printStackTrace();
				}


			}

		});


		Button reset = (Button) view.findViewById(R.id.resetsettings);
		reset.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			
				String rpath = "/storage/sdcard1/Music";
				
				try {
					outputStream = ((Context)getActivity()).openFileOutput(tempfile, Context.MODE_PRIVATE);
					outputStream.write(rpath.getBytes());
					outputStream.close();
					((MainActivity)getActivity()).createDialog("Default settingsFile created", "Proceed", "Succes", true);
				} catch (Exception e) {
					((MainActivity)getActivity()).createDialog("No SettingsFile created", "Dismiss", "Error", true);
					e.printStackTrace();
				}
				
			}
			
		});



		return view;

	}




	/*
	public void updateArticleView(String encoded) {
		TextView content =(TextView) getView().findViewById(R.id.content2);
		content.setText(encoded);
	}
	 */

}
