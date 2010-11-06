package org.teleportr.activity;

import org.teleportr.R;
import org.teleportr.model.Ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class RideDetails extends Activity {


	private Ride ride;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ride);
	    
	    ride = (Ride) getIntent().getParcelableExtra("ride");
//	    ((RideView)findViewById(R.id.ride)).setRide(ride);
	    
	    switch (ride.mode) {
	    
		case Ride.MODE_SK8:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_sk8);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_sk8);
			break;
		case Ride.MODE_MFG:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_mfg);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_mfg);
			break;
		case Ride.MODE_BIKE:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_bike);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_bike);
			break;
		case Ride.MODE_TAXI:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_taxi);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_taxi);
			findViewById(R.id.home).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent());
				}
			});
			break;
		case Ride.MODE_DRIVE:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_drive);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_car);
			break;
		case Ride.MODE_WALK:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_walk);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_walk);
			break;
		case Ride.MODE_FLIGHT:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_flight);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_ufo);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");

			break;
		case Ride.MODE_TRAIN:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_train);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_train);
			break;
		case Ride.MODE_TRANSIT:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_transit);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_transit);
			break;
		case Ride.MODE_TELEPORTER:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_teleporter);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_teleporter);
			break;

		default:
			break;
		}
	}

	
	
	public void share(View v) {
		
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "teleportR");
		intent.putExtra(Intent.EXTRA_TEXT, "#beer @niederlassung");

		startActivity(Intent.createChooser(intent, "TWEET this to unlock your FREE beer @niederlassung!"));
	}



}
