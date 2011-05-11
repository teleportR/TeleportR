package org.teleportr.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.teleportr.R;
import org.teleportr.model.Place;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PlaceDetails extends Activity implements OnEditorActionListener {


	private Place place;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView(R.layout.place);
	    
	    if (getIntent().hasExtra("place"))
	    	place = (Place) getIntent().getParcelableExtra("place");
	    if (place == null)
	    	place = new Place();

//	    if (place.name != null)
	    	((TextView)findViewById(R.id.name)).setText(place.name);
	    	((TextView)findViewById(R.id.name)).setOnEditorActionListener(this);
//	    if (place.city != null)
	    	((TextView)findViewById(R.id.city)).setText(place.city);
	    	((TextView)findViewById(R.id.city)).setOnEditorActionListener(this);
	    if (place.icon != 0)
	    	((ImageView)findViewById(R.id.icon)).setImageResource(place.icon);
	    else
	    	((ImageView)findViewById(R.id.icon)).setImageResource(R.drawable.icon);
	    if (place.address != null) {
	    	Matcher address = Pattern.compile("(.*)\\s+(\\d+)").matcher(place.address);
	    	if (address.find()) {
	    		((TextView)findViewById(R.id.street)).setText(address.group(1));
	    		((TextView)findViewById(R.id.nmb)).setText(address.group(2));
	    	} else {
	    		((TextView)findViewById(R.id.street)).setText(place.address);
	    	}
	    }
	    ((TextView)findViewById(R.id.street)).setOnEditorActionListener(this);
	    
//	    ((TextView)findViewById(R.id.latlon)).setText(place.lat+"\n"+place.lon);

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		finish();
		return true;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		place.address = ((EditText)findViewById(R.id.street)).getText().toString()+
		" "+((EditText)findViewById(R.id.nmb)).getText().toString();
		place.name = ((EditText)findViewById(R.id.name)).getText().toString();
		place.city = ((EditText)findViewById(R.id.city)).getText().toString();
		if (place.name.equals(""))
			place.name = place.address;
		if (place.name.equals(""))
			place.name = "Start";
		setResult(RESULT_OK, getIntent().putExtra("place", place));
		super.finish();
	}
	
	
}
