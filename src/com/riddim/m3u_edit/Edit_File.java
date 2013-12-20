package com.riddim.m3u_edit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.riddim.m3u_edit.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.riddim.m3u_edit.Base;

public class Edit_File extends Fragment{
	String Filename;
	String lineChange;
	String replace;
	View  view;
	String path;
	String encoded = "";
	
	OnHeadlineSelectedListener mCallback;

	MainActivity main = new MainActivity();
	
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
		path = "/storage/sdcard1/Music";
		defaultloc.setText(path);

		Button ReadWebPage = (Button)view.findViewById(R.id.letgo);
		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.defaultcheck);
		if (checkBox.isChecked()) {
			checkBox.setChecked(false);
		};

	    
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				String sdcard = Environment.getExternalStorageDirectory().toString();

				TextView defaultloc =(TextView) view.findViewById(R.id.defaultloc);
				if (checkBox.isChecked()) {

					defaultloc.setText(sdcard);
					path = sdcard;
				} else
				{
					path = "/storage/sdcard1/Music";
					defaultloc.setText(path);
				}
			}
		});

		Button fullScreen = (Button) view.findViewById(R.id.fullscreen);
		fullScreen.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), tempSee_File.class);
				intent.putExtra("text", encoded);
				startActivity(intent);
				
			}
			
		});
		
		ReadWebPage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Boolean access = isExternalStorageWritable();
				//	TextView Show = (TextView) view.findViewById(R.id.show);

				encoded = "";

				if(access){
					String strFilePath;
					TextView FilePath = (TextView) view.findViewById(R.id.locFile);
					strFilePath = FilePath.getText().toString();

					File myDir = new File(path);    
					myDir.mkdirs();

				//	strFilePath = "playlistorgr.m3u";

					File f = new File (myDir, strFilePath);

					StringBuffer sb = new StringBuffer();

					DefaultProg();

					//	Show.setText(strFilePath + "\n" + lineChange + "\n" + replace + "\n" + access);			

					try {
						BufferedReader br = new BufferedReader(new FileReader(f));
						String line;

						while(true)
						{
							line=br.readLine();
							if(line==null)
								break;
							sb.append(line + "\n");				 					    

							int cnt1 = sb.indexOf(lineChange);
							if (cnt1 >= 0){
								sb.replace(cnt1,cnt1+lineChange.length(), replace);
							}
						}
						br.close();
					}
					catch (IOException e) {
						strFilePath ="";
						((MainActivity)getActivity()).createDialog("Error no such file or directory, try a different name or path", "Dismiss", "Error", true);
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
							}
						}
						finally {
							reader.close();
						}
						System.out.print(encoded);

						TextView filetext =(TextView) view.findViewById(R.id.filetext);
						filetext.setText(encoded);

						
						
						
						mCallback.onArticleSelected(encoded);

						
					} catch (IOException e) {
				
						e.printStackTrace();
					}
				}
				else{
					//	Show.setText("Error no access to sd card");		
				
					main.createDialog("Error no access to sd card", "Ok", "Error", true);
				}
			}
		});
		return view;
	}

	public String DefaultProg(){	
		TextView Change =(TextView) view.findViewById(R.id.replace);
		lineChange = Change.getText().toString();

		TextView Repl =(TextView) view.findViewById(R.id.replacewith);
		replace = Repl.getText().toString();

		return lineChange + replace;
	}

	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

}
