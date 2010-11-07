/**
 * Copyright (c) 2010: andlabs gbr, teleportr.org All rights reserved.
 *	
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version <http://www.gnu.org/licenses/>
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
**/


package org.teleportr.activity;

import org.teleportr.R;
import org.teleportr.util.LogCollector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScottySays extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.sotty_says);
	    
	    final AlphaAnimation fade = new AlphaAnimation(0, 1f);
		fade.setInterpolator(new DecelerateInterpolator());
		fade.setDuration(900);
		findViewById(R.id.scotty_layout).startAnimation(fade);
	    final TranslateAnimation jump = new TranslateAnimation(-70f, 0f, 172f, 0f);
	    jump.setDuration(800);
	    jump.setInterpolator(new OvershootInterpolator(2.3f));
	    findViewById(R.id.scotty).startAnimation(jump);
	    
	    new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				final TextView scottyText = (TextView)findViewById(R.id.scottytext);
				final ImageView scottySay = (ImageView)findViewById(R.id.scottysay);
				final ImageButton mailButton = (ImageButton) findViewById(R.id.mail);

				final AlphaAnimation speak = new AlphaAnimation(0, 1f);
				speak.setInterpolator(new AccelerateInterpolator());
				speak.setDuration(420);
				final TranslateAnimation bounce = new TranslateAnimation(100, 0, 0, 0);
				bounce.setInterpolator(new BounceInterpolator());
				bounce.setDuration(700);

				scottySay.startAnimation(speak);
				scottyText.startAnimation(speak);
				mailButton.startAnimation(bounce);
				scottySay.setVisibility(View.VISIBLE);
				scottyText.setVisibility(View.VISIBLE);
				mailButton.setVisibility(View.VISIBLE);
				scottyText.setText(getIntent().getAction());
			}
		}, 650);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
	
	public void mail(View v) {
		
		new AlertDialog.Builder(this)
        .setTitle("feedback")
        .setIcon(android.R.drawable.ic_dialog_info)
        .setPositiveButton("send logs", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
            	LogCollector.feedback(ScottySays.this, "scotty@teleportr.org, flo@andlabs.de");
            }
        })
        .setNeutralButton("mail scotty", new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:scotty@teleportr.org"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "beam me up! "+getString(R.string.app_name));
				try {
					PackageInfo info = getPackageManager().getPackageInfo("org.teleportr", PackageManager.GET_META_DATA);
					intent.putExtra(Intent.EXTRA_TEXT, "version: "+info.versionName+" ("+info.versionCode+") \n");
				} catch (NameNotFoundException e) {}
                startActivity(intent); 
            }}
        )
    .show();
	}
	
}
