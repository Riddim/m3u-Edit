package com.riddim.m3u_edit;


import com.riddim.m3u_edit.R;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Base extends FragmentActivity {
	
	final Context context = this;
	Button dialogButton;
	
	public Button createDialog(CharSequence String, CharSequence ButText, CharSequence Title, Boolean dismiss){
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.screen_popup);
		dialog.setTitle(Title);
		dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = (TextView) dialog.findViewById(R.id.txtView);
    	text.setText(String);
    	dialogButton = (Button) dialog.findViewById(R.id.btn_DialogClose);
		dialogButton.setText(ButText);
    	dialog.show();
		if(dismiss){
		dialogButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				dialog.dismiss();
			}
		});
		}
		return dialogButton;
	}
}
