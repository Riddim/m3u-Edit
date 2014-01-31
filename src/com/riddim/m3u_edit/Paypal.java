package com.riddim.m3u_edit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class Paypal extends Base{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paypal);
	
	
	ImageView donate = (ImageView) findViewById(R.id.donate_image);
	donate.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=Luuk_Grefte%40hotmail%2ecom&lc=NL&item_name=M3u%2dEdit&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted"));
              startActivity(browserIntent);
		}

	});
	}	
}