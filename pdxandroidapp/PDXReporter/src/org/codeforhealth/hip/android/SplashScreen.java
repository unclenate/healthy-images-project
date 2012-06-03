package org.codeforhealth.hip.android;

import org.codeforhealth.hip.android.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class SplashScreen extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 1000; 
	
	protected Handler _exitHandler = null;
	protected Runnable _exitRunnable = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.custom_splash_dialog);

	    try {
		    Intent intent = getIntent();
		    Bundle extras = intent.getExtras();
		        
		    String action = intent.getAction();
		 
		    if (Intent.ACTION_SEND.equals(action)) {
		    	if (extras.containsKey(Intent.EXTRA_STREAM)) {
		    		Uri uri = (Uri)extras.getParcelable(Intent.EXTRA_STREAM);
		    		ApplicationState.setIntentShareUri(uri);
		        } else if (extras.containsKey(Intent.EXTRA_TEXT)) {
		            //Do nothing for now;
		        }
		    } 
	    } catch (Exception ex) {
	    	//Do nothing for now;
	    }
	    
	    _exitRunnable = new Runnable() {
	    	public void run() {
	    		exitSplash();
	    	}
	    };
	    
	    _exitHandler = new Handler();
	    _exitHandler.postDelayed(_exitRunnable, _splashTime);

	}

	private void exitSplash()
	{
		finish();
		
		Intent intent = new Intent(); 
        intent.setClass(SplashScreen.this, Report.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        
	}
	
}




