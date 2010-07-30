package org.teleportr.activity;

import org.teleportr.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

public class About extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.about);
	    
	    
	    findViewById(R.id.logo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, 
							Uri.parse("http://teleportr.org")));
			}
		});
	    findViewById(R.id.about).setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		startActivity(new Intent(Intent.ACTION_VIEW, 
	    				Uri.parse("http://teleportr.org")));
	    	}
	    });
	    
	    findViewById(R.id.openstreetmap).setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		startActivity(new Intent(Intent.ACTION_VIEW, 
	    				Uri.parse("http://wiki.openstreetmap.org")));
	    	}
	    });
	    findViewById(R.id.creativecommons).setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		startActivity(new Intent(Intent.ACTION_VIEW, 
	    				Uri.parse("http://creativecommons.org/licenses/by-sa/2.0")));
	    	}
	    });
	    
	    findViewById(R.id.andlabs).setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		startActivity(new Intent(Intent.ACTION_VIEW, 
	    				Uri.parse("http://andlabs.de")));
	    	}
	    });
	    findViewById(R.id.subphisticated).setOnClickListener(new OnClickListener() {
	    	@Override
	    	public void onClick(View v) {
	    		startActivity(new Intent(Intent.ACTION_VIEW, 
	    				Uri.parse("http://subphisticated.com")));
	    	}
	    });
	    
	}

}
