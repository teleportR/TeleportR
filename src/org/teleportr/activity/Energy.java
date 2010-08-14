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
import org.teleportr.R.drawable;
import org.teleportr.R.id;
import org.teleportr.R.layout;
import org.teleportr.R.raw;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.ImageView;

public class Energy extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int DIALOG_ERROR = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	private AnimationDrawable mLogoAnimationDrawable;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);

		this.setContentView(R.layout.splash);

		final ImageView ivSplashLogo = (ImageView)this.findViewById(R.id.iv_splash_logo);

		MediaPlayer.create(this, R.raw.teleport).start();
		ivSplashLogo.setBackgroundResource(R.drawable.logo_animation);
		this.mLogoAnimationDrawable = (AnimationDrawable) ivSplashLogo.getBackground();


		// Receive the VibratorManagerService

		final Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		// Do a short (100ms) vibration.

		vibrator.vibrate(new long[]{150, 50,150, 50,150, 50,150, 50,150, 50,150, 50,150, 50,150, 50,150, 50,150, 50}, -1);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Energy.this.showDialog(DIALOG_ERROR);
			}
		}, 3000);
	}

	@Override
	protected void onResume() {
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Energy.this.mLogoAnimationDrawable.start();
			}
		}, 100);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Dialog onCreateDialog(final int pDialogID) {
		switch(pDialogID) {
			case DIALOG_ERROR:
				return new AlertDialog.Builder(this)
				.setTitle("Teleporter-Error")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage("Sorry, Energy level too low...\n\n$ < 1 + x$ = ;)")
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(final DialogInterface pDialog, final int pWhich) {
						Energy.this.finish();
					}
				})
				.create();
			default:
				return super.onCreateDialog(pDialogID);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
