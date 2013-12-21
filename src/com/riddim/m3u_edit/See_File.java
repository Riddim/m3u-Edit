package com.riddim.m3u_edit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.riddim.m3u_edit.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class See_File extends Fragment{
	 
   

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    	  View rootView = inflater.inflate(R.layout.see_file, container, false);     

    	  
        return rootView;
        
    }

	public void updateArticleView(String encoded) {
		TextView content =(TextView) getView().findViewById(R.id.content2);
		content.setText(encoded);
	}

	
}
