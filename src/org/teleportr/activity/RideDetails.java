package org.teleportr.activity;

import org.teleportr.R;
import org.teleportr.model.Ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
			((TextView)findViewById(R.id.text1)).setText("wouldn't it be great to skate through town, and your phone introduces you to the nicest spots along the way? if you are skate-nerd or nerd-skater feel free to write this plugin. \n\n scotty@teleportR.org ");
			break;
		case Ride.MODE_MFG:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_mfg);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_mfg);
			((TextView)findViewById(R.id.text1)).setText("sharing is great! sharing mobility is also quite cheap and enviremental friendly. why not have a place, where you can find all the different offers at one time? feel free to help us shift the paradigms of mobility. \n\n scotty@teleportR.org");
			break;
		case Ride.MODE_BIKE:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_bike);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_bike);
			((TextView)findViewById(R.id.text1)).setText("slow down and see the landscape; go by bike. just hit the button below. this plugin is not great jet, but somehow it works. ;-) make a tour, or make this plugin better \n\n--> scotty@teleportR.org");
			break;
		case Ride.MODE_TAXI:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_taxi);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_taxi);
			((TextView)findViewById(R.id.text1)).setText("very basic concept of a teleportR taxi-plugin. if you need a taxi, hit the button below, and talk to a munich taxi company. plz. compare the real fare payed to our estimated and tell us the difference. \n\n scotty@teleportR.org");
			break;
		case Ride.MODE_DRIVE:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_drive);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_car);
			((TextView)findViewById(R.id.text1)).setText("basicly this is just a navi. one like nearly everybody got. how could one make this extraordinary and super userfriendly? push the button for testing, and share your ideas. \n\n scotty@teleportR.org");
			break;
		case Ride.MODE_WALK:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_walk);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_walk);
			((TextView)findViewById(R.id.text1)).setText("the walkers guide through urbanity. how can we make this galactic? help us. \n\n scotty@teleportR.org");
			break;
		case Ride.MODE_FLIGHT:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_flight);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_ufo);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");

			break;
		case Ride.MODE_TRAIN:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_train);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_train);
			((TextView)findViewById(R.id.text1)).setText("Congratulations!\nYou found Scotty :-)\nand won a FREE beer.\nHit the SHARE button\nto unlock your beer.\nCheers!");
			break;
		case Ride.MODE_TRANSIT:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_transit);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_transit);
			((TextView)findViewById(R.id.text1)).setText("");
			break;
		case Ride.MODE_TELEPORTER:
			findViewById(R.id.layout).setBackgroundResource(R.drawable.mode_teleporter);
			((Button)findViewById(R.id.home)).setBackgroundResource(R.drawable.btn_teleporter);
			((TextView)findViewById(R.id.text1)).setText("right now teleportation is at a post-experimental stage; we made some substential breakthrouths, but it is not stabel jet. we need more experianced technitians who will join our mission for mobile mobility. Join scotty and the teleportR projekt. beam now! ");
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
