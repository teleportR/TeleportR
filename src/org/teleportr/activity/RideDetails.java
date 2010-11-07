package org.teleportr.activity;

import org.teleportr.R;
import org.teleportr.Teleporter;
import org.teleportr.model.Ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class RideDetails extends Activity {


	private Ride ride;
	private String shareText = "mobility goes mobile @teleporter #gtugbc #gddde";

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
			((TextView)findViewById(R.id.text1)).setText("wouldn't it be great to go by skateboard through town, and your phone introduces you to the nicest skatespots? if you are skate-nerd or nerd-skater feel free to write this plugin... --> scotty@teleportR.org ");
			break;
		case Ride.MODE_MFG:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_mfg);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_mfg);
			((TextView)findViewById(R.id.text1)).setText("sharing is great! sharing mobility is also quite cheap and enviremental friendly. why not have a place, where you can finde all the different offers at one time? feel free to help us shift the paradimes of mobility...  --> scotty@teleportR.org");
			break;
		case Ride.MODE_BIKE:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_bike);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_bike);
			((TextView)findViewById(R.id.text1)).setText("slow down and see the landscape. go by bike, just hit the button below. this plugin is not great jet, but somehow it works. ;-) make a tour, or make this plugin better --> scotty@teleportR.org");
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
			((TextView)findViewById(R.id.text1)).setText("very basic concept of a teleportR taxi-plugin. if you need a taxi, hit the button below, and talk to a munich taxi company. plz. compare the real fare payed to our estimated and tell us the difference --> scotty@teleportR.org");
			shareText = "#taxi to "+((Teleporter)getApplication()).destination.name+"? #rideshare @teleporter #gtugbc #gddde";
			break;
		case Ride.MODE_DRIVE:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_drive);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_car);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			shareText = "driving to "+((Teleporter)getApplication()).destination.name+" #rideshare @teleporter #gtugbc #gddde";
			break;
		case Ride.MODE_WALK:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_walk);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_walk);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			break;
		case Ride.MODE_FLIGHT:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_flight);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_ufo);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			shareText = "found Scotty @teleporter and unlocked one #beer @niederlassung #gtugbc #gddde";
			break;
		case Ride.MODE_TRAIN:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_train);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_train);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			break;
		case Ride.MODE_TRANSIT:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_transit);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_transit);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			((WebView)findViewById(R.id.web)).loadUrl(ride.uri);
			findViewById(R.id.web).setVisibility(View.VISIBLE);
			shareText = "on my way to "+((Teleporter)getApplication()).destination.name+" @teleporter arriving "+RideView.DATE_FORMAT.format(ride.arr)+" #gtugbc #gddde "+ride.uri;
			break;
		case Ride.MODE_TELEPORTER:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_teleporter);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_teleporter);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			break;

		default:
			break;
		}
	}

	
	
	public void share(View v) {
		
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "teleportR");
		intent.putExtra(Intent.EXTRA_TEXT, shareText);

		startActivity(Intent.createChooser(intent, "TWEET this to unlock your FREE beer @niederlassung!"));
	}



}
