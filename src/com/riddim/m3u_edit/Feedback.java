package com.riddim.m3u_edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Feedback extends Base {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		
		
		Button mail = (Button) findViewById(R.id.mail);
		mail.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto","luukgrefte@gmail.com", null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback/Bugs M3U Edit");
			startActivity(Intent.createChooser(emailIntent, "Send me a mail.."));
			}

		});
		
		
	}
}
