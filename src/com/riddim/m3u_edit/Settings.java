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




public class Settings extends ListFragment{
	
	private List<String> item = null;
	private List<String> path = null;
	private String root;
	private TextView myPath;
	
	
	String tempfile = "settings";
	FileOutputStream outputStream;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.settings, container, false);  

		myPath = (TextView)view.findViewById(R.id.path);
        root = Environment.getExternalStorageDirectory().getPath();
        root = "/storage";
        getDir(root);
		
		

		Button apply = (Button) view.findViewById(R.id.applysettings);
		apply.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TextView newpath = (TextView) view.findViewById(R.id.changepath);
				if(newpath.getText().toString().getBytes() == 0)){
			
		}
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

	private void getDir(String dirPath)
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
     }
     
     for(int i=0; i < files.length; i++)
     {
      File file = files[i];
      
      if(!file.isHidden() && file.canRead()){
       path.add(file.getPath());
          if(file.isDirectory()){
           item.add(file.getName() + "/");
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


	
	
	
	
	
	/*
	public void updateArticleView(String encoded) {
		TextView content =(TextView) getView().findViewById(R.id.content2);
		content.setText(encoded);
	}
	 */


