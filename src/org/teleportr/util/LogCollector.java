package org.teleportr.util;

import java.util.List;

import org.teleportr.R;
import org.teleportr.R.string;
import org.teleportr.Teleporter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

public class LogCollector {

    public static void feedback(final Context ctx, final String mail) {
        
        final PackageManager packageManager = ctx.getPackageManager();
        final Intent intent = new Intent("com.xtralogic.logcollector.intent.action.SEND_LOG");
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        final boolean isInstalled = list.size() > 0;

        if (!isInstalled){
        	Toast.makeText(ctx, "install LogCollector and try again", Toast.LENGTH_LONG);
        	
        	Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.xtralogic.android.logcollector"));
        	marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	ctx.startActivity(marketIntent); 
               
        } else {
        	Toast.makeText(ctx, "collecting logs..", Toast.LENGTH_LONG);
        	
        	String[] filterSpecs = new String[7];
        	filterSpecs[0] = "*:S";
        	filterSpecs[1] = "HereAmI"+ ":V";
        	filterSpecs[2] = "AndroidRuntime:E";
        	filterSpecs[3] = "BahnDePlugIn" + ":V";
        	filterSpecs[4] = Teleporter.TAG + ":V";
        	filterSpecs[5] = "PlaceProvider" + ":V";
        	filterSpecs[6] = "Autocompletion" + ":V";
        	intent.putExtra("com.xtralogic.logcollector.intent.extra.EXTRA_FILTER_SPECS", filterSpecs);
        	
        	intent.putExtra(Intent.EXTRA_SUBJECT, "feedback "+ctx.getString(R.string.app_name));
        	intent.putExtra("com.xtralogic.logcollector.intent.extra.SEND_INTENT_ACTION", Intent.ACTION_SENDTO);
        	intent.putExtra("com.xtralogic.logcollector.intent.extra.DATA", Uri.parse("mailto:"+mail));
        	intent.putExtra("com.xtralogic.logcollector.intent.extra.FORMAT", "time");
			try {
				PackageInfo info = packageManager.getPackageInfo("org.teleportr", PackageManager.GET_META_DATA);
				intent.putExtra("com.xtralogic.logcollector.intent.extra.ADDITIONAL_INFO", "version: "+info.versionName+" ("+info.versionCode+") \n");
			} catch (NameNotFoundException e) {}
        	
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	ctx.startActivity(intent);
        }
    }
    
}
