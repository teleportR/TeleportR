package org.teleportr.activity;

import org.teleportr.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Help extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setTitle("Tipp");
	    TextView tv = new TextView(this);
	    tv.setText(getIntent().getAction());
	    tv.setGravity(Gravity.CENTER);
	    tv.setPadding(3, 0, 3, 23);
	    setContentView(tv);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}
	
}
